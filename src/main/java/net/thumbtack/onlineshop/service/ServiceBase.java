package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.model.entity.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;

import java.util.ArrayList;
import java.util.List;

abstract class ServiceBase {

    void checkAdministratorPrivileges(UserDao userDao, String token) throws ServerException {
        User user = userDao.getUserByToken(token);
        Administrator administrator = userDao.getAdministratorById(user.getId());
        if(administrator == null) {
            throw new ServerException(ErrorCode.YOU_NO_HAVE_THIS_PRIVILEGES);
        }
    }

    Client getClientByToken(UserDao userDao, String token) throws ServerException {
        User user = userDao.getUserByToken(token);
        Client client = userDao.getClientById(user.getId());
        if(client == null) {
            throw new ServerException(ErrorCode.USER_IS_NOT_CLIENT);
        }
        return client;
    }

    List<String> getCategoriesListNames(Product product) {
        List<String> list = new ArrayList<>();
        for(Category it : product.getCategories()) {
            list.add(it.getName());
        }
        return list;
    }
}
