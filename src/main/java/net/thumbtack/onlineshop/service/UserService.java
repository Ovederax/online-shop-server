package net.thumbtack.onlineshop.service;

import com.google.gson.Gson;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.UserInfo;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.ErrorContent;
import net.thumbtack.onlineshop.dto.response.ErrorResponse;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.GetClientsInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
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
    private Gson gson;

    public UserService() {
        gson = new Gson();
    }

    /**
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

    Пароль является case-sensitive.

     Для зарегистрировавшегося администратора автоматически выполняется
    операция “Login” (п.3.4)
     */
    public String registerAdministrator(AdministratorRegisterRequest r) {
        try {
            List<ErrorContent> errors = r.validate();// валидация будет переделана через аннотации
            // REVU и не стоит дальше в таком виде ее писать и тратить на это время
            if(errors.size() != 0) {
                return gson.toJson(new ErrorResponse(errors));
            }
            userDao.registerAdministrator(new Administrator(r.getFirstName(), r.getLastName(),
                    r.getPatronymic(), r.getPosition(), r.getLogin(), r.getPassword()));
        }catch (ServerException ex) {
            return gson.toJson(new ErrorResponse(null));
        }
        //AdministratorInfoResponse
        // +JAVASESSIONID скорее всего  через spring
        // будет передаваться
        return gson.toJson(null);
    }

    /** Для зарегистрировавшегося клиента автоматически выполняется операция “Login” (п.3.4)*/
    public void registerClient(ClientRegisterRequest r) {
        try {
            userDao.registerClient(new Client(r.getFirstName(), r.getLastName(),
                    r.getPatronymic(), r.getEmail(), r.getAddress(),
                    r.getPhone(), r.getLogin(), r.getPassword()));
        }catch (ServerException ex) {
            //return gson.toJson(new ErrorResponse(null));
        }
    }
    /** @return token in uuid format */
    public String login(UserLoginRequest r) throws ServerException {
        return userDao.login(r.getLogin(), r.getPassword());
    }

    public void logout(String token) throws ServerException {
        userDao.logout(token);
    }

    public String getUserInfo(String JAVASESSIONID) throws ServerException {
        UserInfo userInfo = userDao.getUserInfo(JAVASESSIONID);
        if(userInfo.getAdmin() != null) {
            Administrator ad = userInfo.getAdmin();
            AdministratorInfoResponse response = new AdministratorInfoResponse(ad.getId(), ad.getFirstname(), ad.getLastname(), ad.getPatronymic(), ad.getPosition());
            return new Gson().toJson(response);
        }
        Client c = userInfo.getClient();
        ClientInfoResponse response = new ClientInfoResponse(c.getId(), c.getFirstname(), c.getLastname(), c.getPatronymic(), c.getEmail(), c.getAddress(), c.getTelefon(), c.getDeposit());
        return new Gson().toJson(response);
    }

    public List<GetClientsInfoResponse> getClientsInfo(String JAVASESSIONID) throws ServerException {
        return userDao.getClientsInfo(JAVASESSIONID);
    }

    public void editAdministrator(AdministratorEditRequest r, String JAVASESSIONID) throws ServerException {
        Administrator admin = userDao.findAdministratorByToken(JAVASESSIONID);
        // хмм а какое применение у старого пароля если есть uuid?
        // возможно проверка пароля должна быть в dao
        Administrator updateAdmin = new Administrator(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getPosition(), admin.getLogin(), r.getNewPassword());
        userDao.editAdministrator(JAVASESSIONID, updateAdmin, r.getOldPassword());
    }

    public void editClient(ClientEditRequest r, String JAVASESSIONID) throws ServerException {
        Client updateClient = new Client(r.getFirstName(), r.getLastName(), r.getPatronymic(), r.getEmail(), r.getAddress(), r.getPhone(), null, r.getNewPassword());
        userDao.editClient(JAVASESSIONID, updateClient, r.getOldPassword());
    }
}
