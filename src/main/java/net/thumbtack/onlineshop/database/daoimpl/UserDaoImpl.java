package net.thumbtack.onlineshop.database.daoimpl;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.DepositMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Deposit;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
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
            } catch (MySQLIntegrityConstraintViolationException ex) {
                LOGGER.info("Can't insertAdmin administrator in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS);
            } catch (SQLException ex) {
                LOGGER.info("Can't insertAdmin administrator in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_ADMINISTRATOR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void registerClient(Client client) throws ServerException {
        LOGGER.debug("UserDAO registerClient");
        try(SqlSession sqlSession = getSession()) {
            UserMapper userMapper = getUserMapper(sqlSession);
            DepositMapper depositMapper = getDepositMapper(sqlSession);
            try {
                userMapper.insertUser(client);
                userMapper.insertClient(client);
                depositMapper.insertDeposit(new Deposit(client.getId(), 0));
            } catch (MySQLIntegrityConstraintViolationException ex) {
                LOGGER.info("Can't insertClient client in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS);
            }  catch (SQLException ex) {
                LOGGER.info("Can't insertClient client in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_CLIENT);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void login(User user, UUID token) throws ServerException {
        LOGGER.debug("UserDAO loginClient");

        try(SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).login(user.getId(), token.toString());
            } catch (SQLException ex) {
                LOGGER.info("Can't user login in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.USER_IS_ACTIVE);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Client> getAllClients() throws ServerException {
        LOGGER.debug("UserDAO getAllClients");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findAllClients();
            } catch (SQLException ex) {
                LOGGER.info("Can't getAllClients in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CLIENTS);
            }
        }
    }

    @Override
    public void clearData() throws ServerException {
        LOGGER.debug("UserDAO clearData");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.deleteAllClients();
                userMapper.deleteAllAdministrators();
                userMapper.deleteAllLoginRecords();
                userMapper.deleteAllUsers();
            } catch (SQLException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't clearData DB", ex);
                throw new ServerException(ErrorCode.CANT_CLEAR_DATABASE);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Client> getClientsById(List<Integer> clients) throws ServerException {
        LOGGER.debug("UserDAO getClientsById");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(clients.size() == 0) {
                    return new ArrayList<>();
                }
                return getUserMapper(sqlSession).getClientsById(clients);
            } catch (SQLException ex) {
                LOGGER.info("Can't getClientsById DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CLIENT);
            }
        }
    }

    @Override
    public void setMoneyDeposit(Client client, int newMoneyDeposit) throws ServerException {
        LOGGER.debug("UserDAO setMoneyDeposit");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(getUserMapper(sqlSession).updateMoneyDeposit(client, newMoneyDeposit) != 1) {
                    throw new ServerException(ErrorCode.BAD_UPDATE_DEPOSIT_IT_IS_CHANGE);
                }
            } catch (SQLException ex) {
                LOGGER.info("Can't setMoneyDeposit DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_UPDATE_MONEY_DEPOSIT);
            }
            sqlSession.commit();
        }
    }

    @Override
    public int logout(String token) throws ServerException {
        LOGGER.debug("UserDAO logout");
        int editCount;
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                editCount = userMapper.logout(token);
            } catch (SQLException ex) {
                LOGGER.info("Can't user logout DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_USER_LOGOUT);
            }
            sqlSession.commit();
        }
        return editCount;
    }

    @Override
    public List<ClientInfo> getClientsInfo() throws ServerException {
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
            } catch (SQLException ex) {
                LOGGER.info("Can't getClientsInfo in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CLIENT_INFO);
            }
        }
    }

    @Override
    public Administrator getAdministratorById(int id) throws ServerException {
        LOGGER.debug("UserDAO getAdministratorById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findAdministratorsById(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't getAdministratorById in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_ADMINISTRATOR);
            }
        }
    }

    @Override
    public User getUserByLogin(String login) throws ServerException {
        LOGGER.debug("UserDAO getUserByLogin");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findUserByLogin(login);
            } catch (SQLException ex) {
                LOGGER.info("Can't getUserByLogin in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CLIENT);
            }
        }
    }

    @Override
    public Client getClientById(int id) throws ServerException {
        LOGGER.debug("UserDAO getClientById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).findClientById(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't getClientById in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CLIENT);
            }
        }
    }

    @Override
    public User getUserByToken(String token) throws ServerException {
        LOGGER.debug("UserDAO getUserByToken");
        try(SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).findUserByToken(token);
                if(user == null) {
                    throw new ServerException(ErrorCode.UUID_NOT_FOUND);
                }
                return user;
            } catch (SQLException ex) {
                LOGGER.info("Can't getUserByToken in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_CLIENT);
            }
        }
    }

    @Override
    public void editAdministrator(Administrator admin) throws ServerException {
        LOGGER.debug("UserDAO editAdministrator");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.updateUser(admin);
                userMapper.updateAdministrator(admin);
                sqlSession.commit();
            } catch (SQLException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't editAdministrator in DB ", ex);
                throw new ServerException(ErrorCode.CANT_UPDATE_ADMINISTRATOR);
            }
        }
    }

    @Override
    public void editClient(Client client) throws ServerException {
        LOGGER.debug("UserDAO editClient");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.updateUser(client);
                userMapper.updateClient(client);
                sqlSession.commit();
            } catch (SQLException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't editClient in DB ", ex);
                throw new ServerException(ErrorCode.CANT_UPDATE_CLIENT);
            }
        }
    }
}
