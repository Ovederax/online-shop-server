package net.thumbtack.onlineshop.database.mybatis.mappers;

import org.apache.ibatis.annotations.Select;

public interface CommonMapper {
    @Select("SELECT ROW_COUNT()")
    int selectRowCount();
}
