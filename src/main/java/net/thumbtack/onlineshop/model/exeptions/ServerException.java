package net.thumbtack.onlineshop.model.exeptions;

public abstract class ServerException extends Exception {
    abstract public String getMessage();
}