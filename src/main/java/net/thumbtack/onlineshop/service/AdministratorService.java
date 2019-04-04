package net.thumbtack.onlineshop.service;

import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    public String getSettings(String token) {
        /**
         Параметр “cookie” для этого запроса не является обязательным. Если он передается, то для администратора выдаются доступные
         ему настройки, а для клиента - доступные ему. Если cookie не передается в запросе, возвращается список настроек, доступных до
         выполнения операции “Login”
         */
        return null;
    }

    //Удаляет все записи в БД. Метод предназначен для отладки, в production должен быть отключен.
    public void clearDataBase() {

    }
}
