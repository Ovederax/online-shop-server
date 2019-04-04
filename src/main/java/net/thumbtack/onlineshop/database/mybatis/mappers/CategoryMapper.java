package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.database.mybatis.transfer.CategoryDTO;
import net.thumbtack.onlineshop.model.entity.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface CategoryMapper {
    @Insert("INSERT INTO categories(name, parentId) VALUES(#{name}, #{parentId})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertCategory(CategoryDTO dto);

    @Select("SELECT * FROM categories WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "subCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductsByCategoryId", fetchType = FetchType.LAZY))
    })
    Category findCategoryById(int id);

    @Update("<script>update categories "
            + "<set>"
            + "<if test='name!=null'>name=#{name},</if>"
            + "<if test='parentId!=null'>parentId=#{parentId},</if>"
            + "</set> WHERE id=#{id}"
            + "</script>")
    void updateCategoryById(@Param(value = "id") int id, @Param(value = "name") String name, @Param(value = "parentId") Integer parentId);

    @Delete("DELETE FROM categories WHERE id=#{id}")
    void deleteCategoryById(int id);

    @Select("SELECT * FROM categories")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "subCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductsByCategoryId", fetchType = FetchType.LAZY))
    })
    List<Category> getCategories();

    @Delete("DELETE FROM categories")
    void deleteAllCategory();

}
