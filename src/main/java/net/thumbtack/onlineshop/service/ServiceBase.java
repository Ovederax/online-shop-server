package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;

abstract class ServiceBase {

    void checkAdministratorPrivileges(UserDao userDao, String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        Administrator ad = userDao.findAdministratorById(user.getId());
        if(ad == null) {
            throw new ServerException(ErrorCode.YOU_NO_HAVE_THIS_PRIVILEGES);
        }
    }

    Client getClientByToken(UserDao userDao, String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        Client c = userDao.findClientById(user.getId());
        if(c == null) {
            throw new ServerException(ErrorCode.USER_IS_NOT_CLIENT);
        }
        return c;
    }
}
