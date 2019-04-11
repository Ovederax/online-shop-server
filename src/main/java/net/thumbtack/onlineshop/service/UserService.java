package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.user.*;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
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

    public void checkAdministratorPrivileges(String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        Administrator ad = userDao.findAdministratorById(user.getId());
        if(ad == null) {
            throw new ServerException(ErrorCode.YOU_NO_HAVE_THIS_PRIVILEGES);
        }
    }
    public Client getClientByToken(String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        Client c = userDao.findClientById(user.getId());
        if(c == null) {
            throw new ServerException(ErrorCode.USER_IS_NOT_CLIENT);
        }
        return c;
    }

    public void registerAdministrator(AdministratorRegisterRequest r) throws ServerException {
        Administrator ad = new Administrator(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getPosition(), r.getLogin(), r.getPassword());
        userDao.registerAdministrator(ad);
        // как первоначальная идея возврата, тогда не нужно делать на login getUserInfoByUserId()
        // но для POST UserController::login() - нужно все равно возвращать данные, так что ...
        //return new AdministratorInfoResponse(ad.getId(), ad.getFirstname(), ad.getLastname(), ad.getPatronymic(), ad.getPosition());
    }

    public void registerClient(ClientRegisterRequest r) throws ServerException {
        Client c = new Client(r.getFirstName(), r.getLastName(), r.getPatronymic(),
                r.getEmail(), r.getAddress(), r.getPhone(), r.getLogin(), r.getPassword());
        userDao.registerClient(c);
        //return new ClientInfoResponse(c.getId(), c.getFirstname(), c.getLastname(), c.getPatronymic(), c.getEmail(), c.getAddress(), c.getPhone(), c.getDeposit().getMoney());
    }

    /** @return token in uuid format */
    public UserLoginResponse login(UserLoginRequest r) throws ServerException {
        UUID token = UUID.randomUUID();
        User user = userDao.findUserByLogin(r.getLogin());
        if(user == null) {
            throw new ServerException(ErrorCode.LOGIN_NOT_FOUND_DB);
        }
        if(!user.getPassword().equals(r.getPassword())) {
            throw new ServerException(ErrorCode.BAD_PASSWORD);
        }
        userDao.login(user, token);
        return new UserLoginResponse(token.toString(), getUserInfoByUserId(user.getId()));
    }

    public void logout(String token) throws ServerException {
        if(userDao.logout(token) != 1) {
            throw new ServerException(ErrorCode.UUID_NOT_FOUND);
        }
    }

    private UserInfoResponse getUserInfoByUserId(int userId) {
        Administrator ad = userDao.findAdministratorById(userId);
        if(ad != null) {
            return new AdministratorInfoResponse(ad.getId(), ad.getFirstname(), ad.getLastname(), ad.getPatronymic(), ad.getPosition());
        }
        Client c = userDao.findClientById(userId);
        return new ClientInfoResponse(c.getId(), c.getFirstname(), c.getLastname(), c.getPatronymic(), c.getEmail(), c.getAddress(), c.getPhone(), c.getDeposit().getMoney());
    }
    public UserInfoResponse getUserInfo(String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        return getUserInfoByUserId(user.getId());
    }

    public List<ClientInfo> getClientsInfo(String token) throws ServerException {
        checkAdministratorPrivileges(token);
        return userDao.getClientsInfo();
    }

    public AdministratorInfoResponse editAdministrator(AdministratorEditRequest r, String token) throws ServerException {
        User user = userDao.findUserByToken(token);
        Administrator admin = userDao.findAdministratorById(user.getId());
        if(admin == null) {
            throw new ServerException(ErrorCode.EDIT_NOT_YOUR_TYPE_USER);
        }
        if(!admin.getPassword().equals(r.getOldPassword())) {
            throw new ServerException(ErrorCode.BAD_PASSWORD);
        }
        // сомневаюсь, что обновление сущности с вызовом множества методов
        // будет быстрее создание нового объекта через конструктор с нужными полями
        admin.updateEntity(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getPosition(), r.getNewPassword());
        userDao.editAdministrator(admin);
        return new AdministratorInfoResponse(admin.getId(), admin.getFirstname(), admin.getLastname(), admin.getPatronymic(), admin.getPosition());
    }

    public ClientInfoResponse editClient(ClientEditRequest r, String token) throws ServerException {
        Client c = getClientByToken(token);
        if(!c.getPassword().equals(r.getOldPassword())) {
            throw new ServerException(ErrorCode.BAD_PASSWORD);
        }
        c.updateEntity(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getEmail(), r.getAddress(), r.getPhone(), r.getNewPassword());
        userDao.editClient(c);
        return new ClientInfoResponse(c.getId(), c.getFirstname(), c.getLastname(), c.getPatronymic(), c.getEmail(), c.getAddress(), c.getPhone(), c.getDeposit().getMoney());
    }

    public UserInfoResponse addMoneyDeposit(DepositMoneyRequest dto, String token) throws ServerException {
        Client client = getClientByToken(token);
        client.getDeposit().addMoney( Integer.parseInt(dto.getDeposit()) );
        userDao.reloadMoneyDeposit(client);
        return getUserInfo(token);
    }

    public UserInfoResponse getMoneyDeposit(String token) throws ServerException {
        Client c = getClientByToken(token);
        return getUserInfo(token);
    }
}
