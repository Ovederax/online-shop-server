package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.User;
import net.thumbtack.onlineshop.model.exeptions.ServerException;

import java.util.List;
import java.util.UUID;

public interface UserDao {
    void registerAdministrator(Administrator admin) throws ServerException;
    void editAdministrator(Administrator administrator) throws ServerException;

    Administrator getAdministratorById(int id);

    void registerClient(Client client) throws ServerException;
    void editClient(Client client) throws ServerException;

    List<ClientInfo> getClientsInfo() throws ServerException;

    Client getClientById(int id);
    List<Client> getAllClients();

    void login(User user, UUID token) throws ServerException;
    int logout(String uuid) throws ServerException;

    User getUserByLogin(String login);
    User getUserByToken(String token) throws ServerException;

    void reloadMoneyDeposit(Client client, int newMoneyDeposit) throws ServerException;

    void clearData();

    List<Client> getClientsById(List<Integer> clients);
}
