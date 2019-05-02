package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;




public interface UserMapper {
    //--------------------------TABLE users------------------------------------------
    @Insert("INSERT INTO users(firstname, lastname, patronymic, login, password) VALUES(#{firstname}, #{lastname}, #{patronymic}, #{login}, #{password})")
    @Options(useGeneratedKeys = true)
    void insertUser(User user);

    @Delete("DELETE FROM users")
    void deleteAllUsers();

    @Select("SELECT * FROM users WHERE lower(login)=lower(#{login})")
    User findUserByLogin(String login);

    @Update("UPDATE users SET firstname=#{firstname}, lastname=#{lastname}, patronymic=#{patronymic}, password=#{password} WHERE id=#{id}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE id=#{id}")
    void removeUserById(User user);
    //--------------------------TABLE clients----------------------------------------
    @Insert("INSERT INTO clients(userId, email, address, phone) VALUES(#{id}, #{email}, #{address}, #{phone})")
    void insertClient(Client client);

    @Select("SELECT * FROM users JOIN clients ON id=userId WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
            one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.DepositMapper.findDepositByClientId", fetchType = FetchType.EAGER)),
            @Result(property = "purchases", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findPurchasesByClientId", fetchType = FetchType.LAZY))
    })
    Client findClientById(int id);

    @Select("SELECT * FROM users JOIN clients ON id=userId")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
                    one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.DepositMapper.findDepositByClientId", fetchType = FetchType.EAGER)),
            @Result(property = "purchases", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findPurchasesByClientId", fetchType = FetchType.LAZY))
    })
    List<Client> findAllClients();


    @Select("<script>" +
            "SELECT * FROM users JOIN clients ON id=userId WHERE id IN " +
            "<foreach item='item' index='index' collection='clients' open='(' separator=',' close=')'>" +
            "   #{item}" +
            "</foreach>" +
            "</script>")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "deposit", column = "id", javaType = Deposit.class,
                one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.DepositMapper.findDepositByClientId", fetchType = FetchType.EAGER)),
        @Result(property = "purchases", column = "id", javaType = List.class,
                many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findPurchasesByClientId", fetchType = FetchType.EAGER))
    })
    List<Client> getClientsById(@Param("clients") List<Integer> clients);

    @Update("UPDATE clients SET email=#{email}, address=#{address}, phone=#{phone} WHERE userId=#{id}")
    void updateClient(Client client);

    @Delete("DELETE FROM clients WHERE userId=#{id}")
    void removeClientById(Client client);

    @Delete("DELETE FROM clients")
    void deleteAllClients();

    //-------------------------TABLE administrators-------------------------------------
    @Insert("INSERT INTO administrators(userId, position) VALUES(#{id}, #{position})")
    void insertAdmin(Administrator admin);

    @Select("SELECT * FROM users JOIN administrators ON id=userId WHERE lower(login)=lower(#{findLogin})")
    Administrator findAdministratorsByLogin(String findLogin);

    @Select("SELECT * FROM users JOIN administrators ON id=userId WHERE id=#{id}")
    Administrator findAdministratorsById(int id);

    @Select("SELECT * FROM users JOIN administrators ON id=userId")
    List<Administrator> findAllAdministrators();

    @Update("UPDATE administrators SET position=#{position} WHERE userId=#{id}")
    void updateAdministrator(Administrator admin);

    @Delete("DELETE FROM administrators")
    void deleteAllAdministrators();

    //--------------------------TABLE logged_users---------------------------------------
    @Insert("INSERT INTO logged_users(userId, token) VALUES(#{userId}, #{token})")
    void login(@Param(value = "userId") int userId, @Param(value = "token") String token);

    @Delete("DELETE FROM logged_users WHERE token=#{token}")
    int logout(String token);

    @Select("SELECT token FROM logged_users WHERE userId=#{id}")
    List<String> findTokenByUserId(User user);

    @Select("SELECT * FROM users JOIN logged_users ON id=userId WHERE token=#{token}")
    User findUserByToken(String token);

    @Delete("DELETE FROM logged_users")
    void deleteAllLoginRecords();

    @Update("UPDATE deposits SET money=#{newDeposit} WHERE clientId=#{client.id} AND money=#{client.deposit.money}")
    int updateMoneyDeposit(@Param("client") Client client, @Param("newDeposit") int newDeposit);

    //-------------------------------------------------------------------------------------

}


