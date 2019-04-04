package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.UserException;
import net.thumbtack.onlineshop.model.exeptions.enums.UserExceptionEnum;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void registerAdministrator(Administrator admin) throws ServerException {
        LOGGER.debug("UserDAO registerAdministrator");
        try(SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(admin);
                getUserMapper(sqlSession).insertAdmin(admin);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insertAdmin administrator in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void registerClient(Client client) {
        LOGGER.debug("UserDAO registerClient");
        try(SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insertUser(client);
                getUserMapper(sqlSession).insertClient(client);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insertClient client in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void login(User user, UUID token) throws UserException {
        LOGGER.debug("UserDAO loginClient");

        try(SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).login(user.getId(), token.toString());
            } catch (RuntimeException ex) {
                LOGGER.info("Can't user login in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Client> findAllClients() {
        LOGGER.debug("UserDAO findAllClients");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findAllClients();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findAllClients in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void clearData() {
        LOGGER.debug("UserDAO clearData");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.deleteAllClients();
                userMapper.deleteAllAdministrators();
                userMapper.deleteAllLoginRecords();
                userMapper.deleteAllUsers();
            } catch (RuntimeException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't clearData DB", ex);
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void addMoneyDeposit(int id, String deposit) {

    }

    @Override
    public int logout(String token) throws UserException {
        LOGGER.debug("UserDAO logout");
        int editCount;
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                editCount = userMapper.logout(token);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't user logout DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return editCount;
    }

    @Override
    public List<ClientInfo> getClientsInfo() throws UserException {
        LOGGER.debug("UserDAO getClientsInfo");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                List<Client> list = userMapper.findAllClients();
                List<ClientInfo> outList = new ArrayList<>();
                for (Client it : list) {
                    outList.add(new ClientInfo(it.getId(), it.getFirstname(), it.getLastname(), it.getPatronymic(), it.getEmail(), it.getAddress(), it.getPhone()));
                }
                return outList;
            } catch (RuntimeException ex) {
                LOGGER.info("Can't getClientsInfo in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public Administrator findAdministratorByLogin(String login) {
        LOGGER.debug("UserDAO findAdministratorByLogin");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findAdministratorsByLogin(login);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findAdministratorByLogin in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public Administrator findAdministratorById(int id) {
        LOGGER.debug("UserDAO findAdministratorById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findAdministratorsById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findAdministratorById in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public User findUserByLogin(String login) {
        LOGGER.debug("UserDAO findUserByLogin");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findUserByLogin(login);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findUserByLogin in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public Client findClientByLogin(String login) {
        LOGGER.debug("UserDAO findClientByLogin");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findClientByLogin(login);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findClientByLogin in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public Client findClientById(int id) {
        LOGGER.debug("UserDAO findClientById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findClientById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findClientById in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public User findUserByToken(String token) {
        LOGGER.debug("UserDAO findUserByToken");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findUserByToken(token);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findUserByToken in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void editAdministrator(Administrator admin) throws UserException {
        LOGGER.debug("UserDAO editAdministrator");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.updateUser(admin);
                userMapper.updateAdministrator(admin);
                sqlSession.commit();
            } catch (RuntimeException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't editAdministrator in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void editClient(Client client) throws UserException {
        LOGGER.debug("UserDAO editClient");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.updateUser(client);
                userMapper.updateClient(client);
                sqlSession.commit();
            } catch (RuntimeException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't editClient in DB ", ex);
                throw ex;
            }
        }
    }
}
