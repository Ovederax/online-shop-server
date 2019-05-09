package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Deposit;
import org.apache.ibatis.annotations.*;

import java.sql.SQLException;

public interface DepositMapper {
    @Insert("INSERT INTO deposits(clientId, money) VALUES(#{clientId}, #{money})")
    @Options(useGeneratedKeys = true)
    void insertDeposit(Deposit deposit) throws SQLException;

    @Select("SELECT * FROM deposits WHERE clientId=#{clientId}")
    Deposit findDepositByClientId(int clientId) throws SQLException;

    @Update("UPDATE deposits SET money=#{money} WHERE id=#{id}")
    void updateDeposit(Deposit deposit) throws SQLException;

//    Скорей всего не нужно, если будет совмесное удаление клиента с его счетами
//    @Delete("DELETE FROM deposits WHERE clientId=#{clientId}")
//    void deleteDeposit(int clientId);

    @Delete("DELETE FROM deposits")
    void clearAllDeposits() throws SQLException;
}
