package com.example.producttrading.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.producttrading.entity.Order;
import com.example.producttrading.entity.Products;
import com.example.producttrading.service.OrderService;
import com.example.producttrading.service.ProductsService;
import com.example.producttrading.service.RecommendationService;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductsService productsService;

    @Override
    public List<Products> recommend(Long userId, int size) {
        try {
            log.info("推荐开始，当前用户ID: {}", userId);
            List<Order> orders = orderService.list();
            log.info("订单总数: {}", orders.size());
            if (orders.isEmpty()) {
                return fallbackRecommend(size);
            }

            // 构建用户-商品购买关系（去重）
            Map<Long, Set<Long>> userProductMap = new HashMap<>();
            for (Order order : orders) {
                userProductMap
                    .computeIfAbsent((long) order.getUserId(), k -> new HashSet<>())
                    .add(order.getProductId());
            }
            log.info("用户数: {}", userProductMap.size());

            if (!userProductMap.containsKey(userId)) {
                return fallbackRecommend(size);
            }

            // 构建Mahout布尔偏好DataModel
            FastByIDMap<FastIDSet> fastMap = new FastByIDMap<>();
            for (Map.Entry<Long, Set<Long>> entry : userProductMap.entrySet()) {
                FastIDSet productSet = new FastIDSet();
                for (Long pid : entry.getValue()) {
                    productSet.add(pid);
                }
                fastMap.put(entry.getKey(), productSet);
            }
            DataModel model = new GenericBooleanPrefDataModel(fastMap);

            Set<Long> boughtProducts = userProductMap.getOrDefault(userId, Collections.emptySet());
            List<Products> result = new ArrayList<>();
            Set<Long> resultIds = new HashSet<>();

            // ===== 第一级：基于物品的协同过滤（主） =====
            try {
                log.info("尝试基于物品的协同过滤...");
                ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
                Recommender itemRecommender = new GenericBooleanPrefItemBasedRecommender(model, itemSimilarity);
                List<RecommendedItem> itemRecs = itemRecommender.recommend(userId, size);
                log.info("基于物品推荐结果数量: {}", itemRecs.size());
                for (RecommendedItem item : itemRecs) {
                    if (boughtProducts.contains(item.getItemID())) continue;
                    Products product = productsService.getById(item.getItemID());
                    if (product != null && "1".equals(product.getStatus())) {
                        result.add(product);
                        resultIds.add(product.getId());
                    }
                }
            } catch (Exception e) {
                log.warn("基于物品推荐异常: {}", e.getMessage());
            }

            // ===== 第二级：基于用户的协同过滤（辅） =====
            if (result.size() < size && userProductMap.size() >= 2) {
                try {
                    log.info("基于物品结果不足({})，尝试基于用户的协同过滤补充...", result.size());
                    UserSimilarity userSimilarity = new LogLikelihoodSimilarity(model);
                    int neighborhoodSize = Math.min(5, userProductMap.size() - 1);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhoodSize, userSimilarity, model);
                    Recommender userRecommender = new GenericBooleanPrefUserBasedRecommender(model, neighborhood, userSimilarity);
                    List<RecommendedItem> userRecs = userRecommender.recommend(userId, size - result.size());
                    log.info("基于用户推荐结果数量: {}", userRecs.size());
                    for (RecommendedItem item : userRecs) {
                        if (result.size() >= size) break;
                        if (boughtProducts.contains(item.getItemID())) continue;
                        if (resultIds.contains(item.getItemID())) continue;
                        Products product = productsService.getById(item.getItemID());
                        if (product != null && "1".equals(product.getStatus())) {
                            result.add(product);
                            resultIds.add(product.getId());
                        }
                    }
                } catch (Exception e) {
                    log.warn("基于用户推荐异常: {}", e.getMessage());
                }
            }

            // ===== 第三级：热门推荐补充 =====
            if (result.size() < size) {
                log.info("协同过滤结果不足({})，用热门推荐补充...", result.size());
                List<Products> hot = hotRecommend(size);
                for (Products p : hot) {
                    if (result.size() >= size) break;
                    if (!resultIds.contains(p.getId()) && !boughtProducts.contains(p.getId())) {
                        result.add(p);
                        resultIds.add(p.getId());
                    }
                }
            }

            // ===== 第四级：最新上架兜底 =====
            if (result.isEmpty()) {
                result = fallbackRecommend(size);
            }

            log.info("最终推荐结果数量: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("推荐系统异常: {}", e.getMessage(), e);
            return fallbackRecommend(size);
        }
    }

    @Override
    public List<Products> hotRecommend(int size) {
        QueryWrapper<Products> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "1");
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("sell_count");
        wrapper.last("LIMIT " + size);
        List<Products> list = productsService.list(wrapper);
        log.info("热门推荐结果数量: {}", list.size());
        return list;
    }

    private List<Products> fallbackRecommend(int size) {
        List<Products> list = hotRecommend(size);
        if (!list.isEmpty()) {
            return list;
        }
        log.info("热门推荐为空，降级为最新上架");
        QueryWrapper<Products> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "1");
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT " + size);
        list = productsService.list(wrapper);
        log.info("最新上架结果数量: {}", list.size());
        return list;
    }
}
