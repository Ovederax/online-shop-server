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
public class UserService extends ServiceBase {
    @Autowired
    private UserDao userDao;

    public void registerAdministrator(AdministratorRegisterRequest req) throws ServerException {
        Administrator administrator = new Administrator(req.getFirstName(), req.getLastName(), req.getPatronymic(), req.getPosition(), req.getLogin(), req.getPassword());
        userDao.registerAdministrator(administrator);
    }

    public void registerClient(ClientRegisterRequest req) throws ServerException {
        Client client = new Client(req.getFirstName(), req.getLastName(), req.getPatronymic(),
                req.getEmail(), req.getAddress(), convertPhoneFormat(req.getPhone()), req.getLogin(), req.getPassword());
        userDao.registerClient(client);
    }

    /** @return token in uuid format */
    public UserLoginResponse login(UserLoginRequest req) throws ServerException {
        User user = userDao.getUserByLogin(req.getLogin());
        if(user == null) {
            throw new ServerException(ErrorCode.LOGIN_NOT_FOUND_DB);
        }
        if(!user.getPassword().equals(req.getPassword())) {
            throw new ServerException(ErrorCode.BAD_PASSWORD);
        }
        UUID token = UUID.randomUUID();
        userDao.login(user, token);
        return new UserLoginResponse(token.toString(), getUserInfoByUserId(user.getId()));
    }

    public void logout(String token) throws ServerException {
        if(userDao.logout(token) != 1) {
            throw new ServerException(ErrorCode.UUID_NOT_FOUND);
        }
    }

    public UserInfoResponse getUserInfo(String token) throws ServerException {
        User user = userDao.getUserByToken(token);
        return getUserInfoByUserId(user.getId());
    }

    public List<ClientInfo> getClientsInfo(String token) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        return userDao.getClientsInfo();
    }

    public AdministratorInfoResponse editAdministrator(AdministratorEditRequest req, String token) throws ServerException {
        User user = userDao.getUserByToken(token);
        Administrator admin = userDao.getAdministratorById(user.getId());
        if(admin == null) {
            throw new ServerException(ErrorCode.EDIT_NOT_YOUR_TYPE_USER);
        }
        if(!admin.getPassword().equals(req.getOldPassword())) {
            throw new ServerException(ErrorCode.BAD_PASSWORD);
        }
        // сомневаюсь, что обновление сущности с вызовом множества методов
        // будет быстрее создание нового объекта через конструктор с нужными полями
        admin.updateEntity(req.getFirstName(), req.getLastName(), req.getPatronymic(), req.getPosition(), req.getNewPassword());
        userDao.editAdministrator(admin);
        return new AdministratorInfoResponse(admin.getId(), admin.getFirstname(), admin.getLastname(), admin.getPatronymic(), admin.getPosition());
    }

    public ClientInfoResponse editClient(ClientEditRequest req, String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        if(!client.getPassword().equals(req.getOldPassword())) {
            throw new ServerException(ErrorCode.BAD_PASSWORD);
        }
        String phone = convertPhoneFormat(req.getPhone());
        client.updateEntity(req.getFirstName(), req.getLastName(), req.getPatronymic(), req.getEmail(), req.getAddress(), phone, req.getNewPassword());
        userDao.editClient(client);
        return new ClientInfoResponse(client.getId(), client.getFirstname(), client.getLastname(), client.getPatronymic(), client.getEmail(), client.getAddress(), phone, client.getDeposit().getMoney());
    }

    public UserInfoResponse addMoneyDeposit(DepositMoneyRequest dto, String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        int newMoneyDeposit = client.getMoney() + Integer.parseInt(dto.getDeposit());
        userDao.setMoneyDeposit(client, newMoneyDeposit);
        return getUserInfo(token);
    }

    public UserInfoResponse getMoneyDeposit(String token) throws ServerException {
        Client client = getClientByToken(userDao, token);
        return new ClientInfoResponse(client.getId(), client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getMoney());
    }

    private UserInfoResponse getUserInfoByUserId(int userId) throws ServerException {
        Administrator administrator = userDao.getAdministratorById(userId);
        if(administrator != null) {
            return new AdministratorInfoResponse(administrator.getId(), administrator.getFirstname(), administrator.getLastname(), administrator.getPatronymic(), administrator.getPosition());
        }
        Client client = userDao.getClientById(userId); // if (client == null) -> internal server error
        return new ClientInfoResponse(client.getId(), client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getMoney());
    }

    private String convertPhoneFormat(String phone) {
        return phone.replaceAll("-", "");
    }
}
