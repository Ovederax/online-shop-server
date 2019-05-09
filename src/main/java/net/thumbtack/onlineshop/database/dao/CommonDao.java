package net.thumbtack.onlineshop.database.dao;

import net.thumbtack.onlineshop.model.exeptions.ServerException;

public interface CommonDao {
    void clear() throws ServerException;
}
