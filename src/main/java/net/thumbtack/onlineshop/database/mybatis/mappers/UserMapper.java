package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;




public interface UserMapper {
    //--------------------------TABLE users------------------------------------------
    @Insert("INSERT INTO users(firstname, lastname, patronymic, login, password) VALUES(#{firstname}, #{lastname}, #{patronymic}, #{login}, #{password})")
    @Options(useGeneratedKeys = true)
    void insertUser(User user);

    @Delete("DELETE FROM users")
    void deleteAllUsers();

    @Select("SELECT id FROM users WHERE login=#{login}")
    int findUserIdByLogin(String login);

    @Select("SELECT * FROM users WHERE login=#{login}")
    User findUserByLogin(String login);

    @Update("UPDATE users SET firstname=#{firstname}, lastname=#{lastname}, patronymic=#{patronymic}, password=#{password} WHERE id=#{id}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE id=#{id}")
    void removeUserById(User user);
    //--------------------------TABLE clients----------------------------------------
    @Insert("INSERT INTO clients(user_id, email, address, phone) VALUES(#{id}, #{email}, #{address}, #{phone})")
    @Options(useGeneratedKeys = true)
    void insertClient(Client client);

    @Select("SELECT * FROM users JOIN clients ON id=user_id WHERE login=#{findLogin}")
    Client findClientByLogin(String findLogin);

    @Select("SELECT * FROM users JOIN clients ON id=user_id WHERE id=#{id}")
    Client findClientById(int id);

    @Select("SELECT * FROM users JOIN clients ON id=user_id")
    List<Client> findAllClients();

    @Update("UPDATE clients SET email=#{email}, address=#{address}, phone=#{phone} WHERE user_id=#{id}")
    void updateClient(Client client);

    // возможно стоит добавить касскадное удаление чтобы когда удалялась запись из clients было удаление из users
    @Delete("DELETE FROM clients WHERE user_id=#{id}")
    void removeClientById(Client client);

    @Delete("DELETE FROM clients")
    void deleteAllClients();

    //-------------------------TABLE administrators-------------------------------------
    @Insert("INSERT INTO administrators(user_id, position) VALUES(#{id}, #{position})")
    @Options(useGeneratedKeys = true)
    void insertAdmin(Administrator admin);

    @Select("SELECT * FROM users JOIN administrators ON id=user_id WHERE login=#{findLogin}")
    Administrator findAdministratorsByLogin(String findLogin);

    @Select("SELECT * FROM users JOIN administrators ON id=user_id WHERE id=#{id}")
    Administrator findAdministratorsById(int id);

    @Select("SELECT * FROM users JOIN administrators ON id=user_id")
    List<Administrator> findAllAdministrators();

    @Update("UPDATE administrators SET position=#{position} WHERE user_id=#{id}")
    void updateAdministrator(Administrator admin);

    @Delete("DELETE FROM administrators WHERE user_id=#{id}")
    void removeAdministratorByLogin(Administrator admin);

    @Delete("DELETE FROM administrators")
    void deleteAllAdministrators();

    //--------------------------TABLE logged_users---------------------------------------
    @Insert("INSERT INTO logged_users(user_id, token) VALUES(#{userId}, #{token})")
    void login(@Param(value = "userId") int userId, @Param(value = "token") String token);

    @Delete("DELETE FROM logged_users WHERE token=#{token}")
    int logout(String token);

    @Select("SELECT token FROM logged_users WHERE user_id=#{id}")
    List<String> findTokenByUserId(User user);

    @Select("SELECT * FROM users JOIN logged_users ON id=user_id WHERE token=#{token}")
    User findUserByToken(String token);

    @Delete("DELETE FROM logged_users")
    void deleteAllLoginRecords();



    //-------------------------------------------------------------------------------------

}


