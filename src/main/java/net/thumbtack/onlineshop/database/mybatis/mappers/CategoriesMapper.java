package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Client;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CategoriesMapper {
    //---------------------------------CLIENTS------------------------------------------------------------------
    /*@Insert("INSERT INTO clients(firstname, lastname, patronymic, email, address, telefon, login, password) " +
            "VALUES(#{client.firstname}, #{client.lastname}, #{client.patronymic}," +
            " #{client.email}, #{client.address}, #{client.telefon}, #{client.login}, #{client.password})")
    void insert(@Param("client") Client clientDTO);

    @Update("UPDATE clients SET firstname=#{firstname}, lastname=#{lastname}, lastname=#{lastname}, patronymic=#{patronymic}," +
            "email=#{email}, address=#{address}, telefon=#{telefon}, password=#{password} WHERE login=#{client.login}")
    void updateClient(Client client);

    @Delete("DELETE FROM clients WHERE login=#{client.login}")
    void delete(Client client);


    @Select("SELECT firstname, lastname, patronymic, email, address, telefon, login, password, FROM clients")
    @Results({ @Result(property = "id", column = "id") })
    List<Client> getAll();

    @Delete("DELETE FROM clients")
    void deleteAll();*/

}
