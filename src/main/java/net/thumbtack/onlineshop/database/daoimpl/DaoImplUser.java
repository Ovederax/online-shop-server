package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.CommonMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.dto.UserInfo;
import net.thumbtack.onlineshop.dto.response.user.GetClientsInfoResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
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
public class DaoImplUser extends DaoImplBase implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoImplUser.class);

    @Override
    public void registerAdministrator(Administrator admin) throws ServerException {
        LOGGER.debug("UserDAO registerAdministrator");
        try(SqlSession sqlSession = getSession()) {
            try {
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
                getUserMapper(sqlSession).insert(client);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insertAdmin client in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public String login(String login, String password) throws UserException {
        LOGGER.debug("UserDAO loginClient");
        String token = UUID.randomUUID().toString();
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                Client client = userMapper.findClientByLogin(login);
                Administrator admin = userMapper.findAdministratorsByLogin(login);
                if (client != null) {
                    if(client.getPassword().equals(password)) {
                      userMapper.login(login, token);
                    } else {
                        throw new UserException(UserExceptionEnum.BAD_PASSWORD);
                    }
                }else  if (admin != null){
                    if(admin.getPassword().equals(password)) {
                        userMapper.login(login, token);
                    } else {
                        throw new UserException(UserExceptionEnum.BAD_PASSWORD);
                    }
                }else {
                    throw new UserException(UserExceptionEnum.LOGIN_NOT_FOUND_DB);
                }

            } catch (RuntimeException ex) {
                LOGGER.info("Can't user login in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
            return token;
        }
    }

    @Override
    public UserInfo getUserInfo(String javasessionid) throws UserException {
        LOGGER.debug("UserDAO getUserInfo");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                String login = userMapper.findLoginByToken(javasessionid);
                if(login != null) {
                    Client client = userMapper.findClientByLogin(login);
                    Administrator admin = userMapper.findAdministratorsByLogin(login);
                    if(client != null) {
                        return new UserInfo(client);
                    } else if(admin != null) {
                        return new UserInfo(admin);
                    }
                    // тут внутренняя ошибка сервера
                    // тк логин найден а данных по нему нет...
                    throw new RuntimeException();
                } else {
                    throw new UserException(UserExceptionEnum.USER_IS_INACTIVE);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findAllClients in DB ", ex);
                throw ex;
            }
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
            } catch (RuntimeException ex) {
                sqlSession.rollback();
                LOGGER.info("Can't clearData DB", ex);
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void logout(String token) throws UserException {
        LOGGER.debug("UserDAO logout");
        try(SqlSession sqlSession = getSession()) {
            try {
                CommonMapper commonMapper = getCommonMapper(sqlSession);
                UserMapper userMapper = getUserMapper(sqlSession);
                userMapper.logout(token);
                if(commonMapper.selectRowCount() != 1) {
                    throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't user logout DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<GetClientsInfoResponse> getClientsInfo(String token) throws UserException {
        LOGGER.debug("UserDAO getClientsInfo");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                String login = userMapper.findLoginByToken(token);
                if(login != null) {
                    Administrator admin = userMapper.findAdministratorsByLogin(login);
                    if(admin != null) {
                        List<Client> list = userMapper.findAllClients();
                        List<GetClientsInfoResponse> outList = new ArrayList<>();
                        for (Client it : list) {
                            outList.add(new GetClientsInfoResponse(it.getId(), it.getFirstname(), it.getLastname(), it.getPatronymic(), it.getEmail(), it.getAddress(), it.getTelefon()));
                        }
                        return outList;
                    }else {
                        throw new UserException(UserExceptionEnum.USER_IS_NOT_ADMIN);
                    }
                } else {
                    throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
                }
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
    public Administrator findAdministratorByToken(String token) {
        LOGGER.debug("UserDAO findAdministratorByLogin");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                String login = userMapper.findLoginByToken(token);
                if(login != null) {
                    return userMapper.findAdministratorsByLogin(login);
                } else  {
                    return null;
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findAdministratorByLogin in DB ", ex);
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
    public Client findClientByToken(String token) {
        LOGGER.debug("UserDAO findClientByToken");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                String login = userMapper.findLoginByToken(token);
                if(login != null) {
                    return userMapper.findClientByLogin(login);
                } else  {
                    return null;
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findClientByToken in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void editAdministrator(String JAVASESSIONID, Administrator admin, String oldPassword) throws UserException {
        LOGGER.debug("UserDAO editAdministrator");
        try(SqlSession sqlSession = getSession()) {
            try {
                Administrator oldAdmin = findAdministratorByToken(JAVASESSIONID);
                if(!oldAdmin.getPassword().equals(oldPassword)) {
                    throw new UserException(UserExceptionEnum.BAD_PASSWORD);
                }
                admin.setId(oldAdmin.getId());
                UserMapper userMapper = getUserMapper(sqlSession);
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
    public void editClient(String JAVASESSIONID, Client client, String oldPassword) throws UserException {
        LOGGER.debug("UserDAO editClient");
        try(SqlSession sqlSession = getSession()) {
            try {
                Client oldClient = findClientByToken(JAVASESSIONID);
                if(!oldClient.getPassword().equals(oldPassword)) {
                    throw new UserException(UserExceptionEnum.BAD_PASSWORD);
                }
                UserMapper userMapper = getUserMapper(sqlSession);
                client.setId(oldClient.getId());
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
