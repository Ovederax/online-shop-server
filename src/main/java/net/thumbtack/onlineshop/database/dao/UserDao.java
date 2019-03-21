package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.dto.UserInfo;
import net.thumbtack.onlineshop.dto.response.user.GetClientsInfoResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.UserException;

import java.util.List;

public interface UserDao {
    void registerAdministrator(Administrator admin) throws ServerException;
    void registerClient(Client client) throws ServerException;
    String login(String login, String password) throws ServerException;
    UserInfo getUserInfo(String javasessionid) throws UserException;
    List<Client> findAllClients();
    void clearData();
    void logout(String uuid) throws UserException;
    List<GetClientsInfoResponse> getClientsInfo(String javasessionid) throws UserException;

    Administrator findAdministratorByLogin(String login);
    Administrator findAdministratorByToken(String token);
    Client findClientByLogin(String login);
    Client findClientByToken(String token);

    void editAdministrator(String JAVASESSIONID, Administrator administrator, String oldPassword) throws UserException;

    void editClient(String JAVASESSIONID, Client client, String oldPassword) throws UserException;
}
