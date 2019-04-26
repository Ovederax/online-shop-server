package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.model.entity.Category;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class CategoryDaoImpl  extends BaseDaoImpl implements CategoryDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public void addCategory(Category category) {
        LOGGER.debug("CategoryDao addCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).insertCategory(category);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                sqlSession.rollback();
                // REVU throw your own exception everywhere
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public Category getCategory(int id) {
        LOGGER.debug("CategoryDao addCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).findCategoryById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void updateCategory(int id, String name, Integer parentId) {
        LOGGER.debug("CategoryDao updateCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).updateCategoryById(id, name, parentId);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't updateCategory in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteCategory(int id) {
        /**Удаление подкатегории приводит к тому, что все находившиеся
         в нем товары больше к этой подкатегории не принадлежат.
         Сами товары не удаляются.
         Удаление категории приводит к удалению всех ее подкатегорий.
         Все находившиеся в подкатегориях этой категории товары
         больше не принадлежат к этим подкатегориям.
         Если после удаления категории или подкатегории список
         категорий для некоторого товара оказывается пустым, считается,
         что этот товар теперь не принадлежит ни к одной категории или
         подкатегории.*/
        LOGGER.debug("CategoryDao deleteCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).deleteCategoryById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't deleteCategory in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Category> getParentsCategories() {
        LOGGER.debug("CategoryDao getParentsCategories");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getParentsCategories();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't getParentsCategories in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    // REVU getCategoryById
    public Category findCategoryById(int id) {
        LOGGER.debug("CategoryDao findCategoryById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).findCategoryById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findCategoryById in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    // REVU the same
    public List<Category> findCategoriesById(List<Integer> categories) {
        LOGGER.debug("CategoryDao findCategoriesById");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(categories.size() == 0) {
                    return new ArrayList<>();
                }
                StringJoiner joiner = new StringJoiner(",");
                for(Integer it: categories) {
                    joiner.add(it.toString());
                }
                return getCategoryMapper(sqlSession).findCategoriesById(joiner.toString());
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findCategoriesById in DB ", ex);
                throw ex;
            }
        }
    }
}
