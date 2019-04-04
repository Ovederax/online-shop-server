package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.mybatis.transfer.CategoryDTO;
import net.thumbtack.onlineshop.model.entity.Category;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDaoImpl  extends BaseDaoImpl implements CategoryDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDaoImpl.class);

    @Override
    public int addCategory(String name, String parentId) {
        LOGGER.debug("CategoryDao addCategory");
        CategoryDTO dto = new CategoryDTO(0, name, parentId);
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).insertCategory(dto);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return dto.getId();
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
    public List<Category> getCategories() {
        LOGGER.debug("CategoryDao getCategories");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getCategories();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't getCategories in DB ", ex);
                throw ex;
            }
        }
    }
}
