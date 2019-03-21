package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    //--------------------------TABLE clients----------------------------------------
    @Insert("INSERT INTO clients(firstname, lastname, patronymic, email, address, telefon, login, password) " +
            "VALUES(#{client.firstname}, #{client.lastname}, #{client.patronymic}," +
            " #{client.email}, #{client.address}, #{client.telefon}, #{client.login}, #{client.password})")
    @Options(useGeneratedKeys = true)
    void insert(@Param("client") Client client);

    @Select("SELECT * FROM clients WHERE login=#{findLogin}")
    Client findClientByLogin(String findLogin);

    @Select("SELECT * FROM clients")
    List<Client> findAllClients();

    @Update("UPDATE clients SET firstname=#{firstname}, lastname=#{lastname}, patronymic=#{patronymic}, email=#{email}, " +
            "address=#{address}, telefon=#{telefon}, password=#{password} WHERE id=#{id}")
    void updateClient(Client client);

    @Delete("DELETE FROM clients WHERE login=#{login}")
    void removeClientByLogin(Client client);

    @Delete("DELETE FROM clients")
    void deleteAllClients();

    //-------------------------TABLE administrators-------------------------------------
    @Insert("INSERT INTO administrators(firstname, lastname, patronymic, position, login, password) " +
            "VALUES(#{firstname}, #{lastname}, #{patronymic}, #{position}, #{login}, #{password})")
    @Options(useGeneratedKeys = true)
    void insertAdmin(Administrator admin);

    @Select("SELECT * FROM administrators WHERE login=#{findLogin}")
    Administrator findAdministratorsByLogin(String findLogin);

    @Select("SELECT * FROM administrators")
    List<Administrator> findAllAdministrators();

    @Update("UPDATE administrators SET firstname=#{firstname}, lastname=#{lastname}, patronymic=#{patronymic}, position=#{position}, password=#{password} WHERE id=#{id}")
    void updateAdministrator(Administrator admin);

    @Delete("DELETE FROM administrators WHERE id=#{id}")
    void removeAdministratorByLogin(Administrator admin);

    @Delete("DELETE FROM administrators")
    void deleteAllAdministrators();

    //--------------------------TABLE logined_users---------------------------------------
    @Insert("INSERT INTO logined_users(name, token) VALUES(#{login}, #{token})")
    void login(String login, String token);

    @Delete("DELETE FROM logined_users")
    void logout(String token);

    @Update("UPDATE logined_users SET name=#{login} WHERE token=#{token}")
    void updateLoginRecord(String login, String token);

    @Select("SELECT token FROM logined_users WHERE name=#{login}")
    List<String> findTokenByLogin(String login);

    @Select("SELECT name FROM logined_users WHERE token=#{token}")
    String findLoginByToken(String token);

    @Delete("DELETE FROM logined_users")
    void deleteAllLoginRecords();
    //-------------------------------------------------------------------------------------

}


