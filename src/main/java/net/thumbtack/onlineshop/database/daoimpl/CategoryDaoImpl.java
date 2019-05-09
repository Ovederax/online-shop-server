package net.thumbtack.onlineshop.database.daoimpl;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
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
            } catch (MySQLIntegrityConstraintViolationException ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_CATEGORY_WITH_NO_UNIQUE_NAME);
            } catch (SQLException ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_CATEGORY);
            }
            sqlSession.commit();
        }
    }

    @Override
    public Category getCategory(int id) throws ServerException {
        LOGGER.debug("CategoryDao addCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).findCategoryById(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't addCategory in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CATEGORY);
            }
        }
    }

    @Override
    public void updateCategory(int id, String name, Integer parentId) throws ServerException {
        LOGGER.debug("CategoryDao updateCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).updateCategoryById(id, name, parentId);
            } catch (SQLException ex) {
                LOGGER.info("Can't updateCategory in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_UPDATE_CATEGORY);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteCategory(int id) throws ServerException {
        LOGGER.debug("CategoryDao deleteCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                getCategoryMapper(sqlSession).deleteCategoryById(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't deleteCategory in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_DELETE_CATEGORY);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Category> getParentsCategories() throws ServerException {
        LOGGER.debug("CategoryDao getParentsCategories");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).getParentsCategories();
            } catch (SQLException ex) {
                LOGGER.info("Can't getParentsCategories in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CATEGORY);
            }
        }
    }

    @Override
    public Category getCategoryById(int id) throws ServerException {
        LOGGER.debug("CategoryDao getCategoryById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getCategoryMapper(sqlSession).findCategoryById(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't getCategoryById in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CATEGORY);
            }
        }
    }

    @Override
    public List<Category> getCategoriesById(List<Integer> categories) throws ServerException {
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
            } catch (SQLException ex) {
                LOGGER.info("Can't getCategoriesById in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CATEGORY);
            }
        }
    }
}
