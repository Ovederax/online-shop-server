package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.dto.response.AvailableSettingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {
    private CommonDao commonDao;

    @Autowired
    public AdministratorService(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public AvailableSettingResponse getSettings(String token) {
        /**
         Параметр “cookie” для этого запроса не является обязательным.
         Если он передается, то для администратора выдаются доступные
         ему настройки, а для клиента - доступные ему.
         Если cookie не передается в запросе, возвращается список настроек, доступных до
         выполнения операции “Login”
         В настоящее время для всех 3 случаев выдается один и тот же результат.
         Это поведение может быть в дальнейшем изменено.
         */

        return new AvailableSettingResponse(100, 100);
    }

    //Удаляет все записи в БД. Метод предназначен для отладки, в production должен быть отключен.
    public void clearDataBase() {
        commonDao.clear();
    }
}
