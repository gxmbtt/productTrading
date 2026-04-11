package com.example.producttrading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.producttrading.entity.Products;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ProductsMapper extends BaseMapper<Products> {
    @Select("SELECT category as name,count(id) as value from r_products GROUP BY category")
    public List<Map<String, Object>> selectCategoriesCount();

    @Update("update r_products set stock = stock - #{quantity} where id = #{id}")
    public int updateStock(@Param("id") Long id, @Param("quantity") long quantity);

}
