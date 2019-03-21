package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommonDaoImpl extends DaoImplBase implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Override
    public void clear() {
        LOGGER.debug("DAO clear clients and administrators table DB");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.deleteAllClients();
                //userMapper.deleteAllAdministrators();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't clear all DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
