package net.thumbtack.onlineshop.service;

import com.google.gson.Gson;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.UserException;
import net.thumbtack.onlineshop.model.exeptions.enums.UserExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired private UserDao userDao;

    /** VALIDATION
    Логин может содержать только латинские и русские буквы и цифры и
    не может быть пустым.
     Пароль может содержать любые символы и тоже
    не может быть пустым.

     Максимальная длина логина, пароля, фамилии ,
    имени и отчества не более max_name_length  символов.

    Минимальная длина пароля min_password_length символов.

    Логин должен храниться так, как он задан, но не является
    case-sensitive при дальнейшей работе.

    Например, если администратор
    регистрировался с логином “Иванов”, он может впоследствии заходить
    на сервер, используя логины “Иванов”, “иванов”, “иВаНоВ” и т.д.

    Пароль является case-sensitive.*/

    public void checkAdministratorPrivileges(String token) throws UserException {
        User user = userDao.findUserByToken(token);
        if(user == null) {
            throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
        }
        Administrator ad = userDao.findAdministratorById(user.getId());
        if(ad == null) {
            throw new UserException(UserExceptionEnum.YOU_NO_HAVE_THIS_PRIVILEGES);
        }
    }
    public Client getClientByToken(String token) throws UserException {
        User user = userDao.findUserByToken(token);
        if(user == null) {
            throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
        }
        Client c = userDao.findClientById(user.getId());
        if(c == null) {
            throw new UserException(UserExceptionEnum.USER_IS_NOT_CLIENT);
        }
        return c;
    }
    public void checkUserExistByToken(String token) throws UserException {
        User user = userDao.findUserByToken(token);
        if(user == null) {
            throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
        }
    }

    public void registerAdministrator(AdministratorRegisterRequest r) throws ServerException {
        userDao.registerAdministrator(new Administrator(r.getFirstName(), r.getLastName(),
                r.getPatronymic(), r.getPosition(), r.getLogin(), r.getPassword()));
    }

    public void registerClient(ClientRegisterRequest r) throws ServerException {
        userDao.registerClient(new Client(r.getFirstName(), r.getLastName(),
                r.getPatronymic(), r.getEmail(), r.getAddress(),
                r.getPhone(), r.getLogin(), r.getPassword()));
    }

    /** @return token in uuid format */
    public String login(UserLoginRequest r) throws ServerException {
        UUID token = UUID.randomUUID();
        User user = userDao.findUserByLogin(r.getLogin());
        if(user == null) {
            throw new UserException(UserExceptionEnum.LOGIN_NOT_FOUND_DB);
        }
        if(!user.getPassword().equals(r.getPassword())) {
            throw new UserException(UserExceptionEnum.BAD_PASSWORD);
        }
        userDao.login(user, token);
        return token.toString();
    }

    public void logout(String token) throws ServerException {
        if(userDao.logout(token) != 1) {
            throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
        }
    }

    public String getUserInfo(String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        Administrator ad = userDao.findAdministratorById(user.getId());
        if(ad != null) {
            AdministratorInfoResponse response = new AdministratorInfoResponse(ad.getId(), ad.getFirstname(), ad.getLastname(), ad.getPatronymic(), ad.getPosition());
            return new Gson().toJson(response);
        }
        Client c = userDao.findClientById(user.getId());
        ClientInfoResponse response = new ClientInfoResponse(c.getId(), c.getFirstname(), c.getLastname(), c.getPatronymic(), c.getEmail(), c.getAddress(), c.getPhone(), c.getDeposit());
        return new Gson().toJson(response);
    }

    public List<ClientInfo> getClientsInfo(String token) throws ServerException {
        checkAdministratorPrivileges(token);
        return userDao.getClientsInfo();
    }

    public void editAdministrator(AdministratorEditRequest r, String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        if(user == null) {
            throw new UserException(UserExceptionEnum.UUID_NOT_FOUND);
        }
        Administrator ad = userDao.findAdministratorById(user.getId());
        if(ad == null) {
            throw new UserException(UserExceptionEnum.EDIT_NOT_YOUR_TYPE_USER);
        }
        if(!ad.getPassword().equals(r.getOldPassword())) {
            throw new UserException(UserExceptionEnum.BAD_PASSWORD);
        }
        Administrator newAdmin = new Administrator(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getPosition(), null, r.getNewPassword());
        newAdmin.setId(user.getId());
        userDao.editAdministrator(newAdmin);
    }

    public void editClient(ClientEditRequest r, String token) throws ServerException {
        Client client = getClientByToken(token);
        if(!client.getPassword().equals(r.getOldPassword())) {
            throw new UserException(UserExceptionEnum.BAD_PASSWORD);
        }
        Client updateClient = new Client(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getEmail(), r.getAddress(), r.getPhone(), null, r.getNewPassword());
        updateClient.setId(client.getId());
        userDao.editClient(updateClient);
    }

    public void addMoneyDeposit(DepositMoneyRequest dto, String token) throws UserException {
        Client c = getClientByToken(token);
        userDao.addMoneyDeposit(c.getId(), dto.getDeposit());
    }

    public String getMoneyDeposit(String token) throws ServerException {
        Client c = getClientByToken(token);
        return getUserInfo(token);
    }
}
