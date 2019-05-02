package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
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
    public void addCategory(Category category) throws ServerException {
        LOGGER.debug("CategoryDao addCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).insertCategory(category);
            } catch (Exception ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                sqlSession.rollback();
                ErrorCode code = ErrorCode.CANT_ADD_CATEGORY_WITH_NO_UNIQUE_NAME;
                throw new ServerException(code.getErrorCode(), code.getMessage(), code.getField());
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
            } catch (Exception ex) {
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
            } catch (Exception ex) {
                LOGGER.info("Can't updateCategory in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteCategory(int id) {
        LOGGER.debug("CategoryDao deleteCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).deleteCategoryById(id);
            } catch (Exception ex) {
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
            } catch (Exception ex) {
                LOGGER.info("Can't getParentsCategories in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public Category getCategoryById(int id) {
        LOGGER.debug("CategoryDao getCategoryById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).findCategoryById(id);
            } catch (Exception ex) {
                LOGGER.info("Can't getCategoryById in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Category> getCategoriesById(List<Integer> categories) {
        LOGGER.debug("CategoryDao getCategoriesById");
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
            } catch (Exception ex) {
                LOGGER.info("Can't getCategoriesById in DB ", ex);
                throw ex;
            }
        }
    }
}
