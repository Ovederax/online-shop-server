package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.config.ServerProperties;
import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.response.AvailableSettingResponse;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.purchase.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByClient;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.model.entity.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdministratorService extends ServiceBase{
    private final ServerProperties properties;
    private CommonDao commonDao;
    private ProductDao productDao;
    private UserDao userDao;
    private CategoryDao categoryDao;

    @Autowired
    public AdministratorService(UserDao userDao, ProductDao productDao, CategoryDao categoryDao, CommonDao commonDao, ServerProperties properties) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.commonDao = commonDao;
        this.properties = properties;
    }

    /**
     Параметр “cookie” для этого запроса не является обязательным.
     Если он передается, то для администратора выдаются доступные
     ему настройки, а для клиента - доступные ему.
     Если cookie не передается в запросе, возвращается список настроек, доступных до
     выполнения операции “Login”
     В настоящее время для всех 3 случаев выдается один и тот же результат.
     Это поведение может быть в дальнейшем изменено.
     */
    public AvailableSettingResponse getSettings(String token) {
        return new AvailableSettingResponse(properties.getMax_name_length(), properties.getMin_password_length());
    }

    //Удаляет все записи в БД. Метод предназначен для отладки, в production должен быть отключен.
    public void clearDataBase() throws ServerException {
        commonDao.clear();
    }


// Ответ  должен  также  содержать  итоговые  значения  по  выборке.  Например,  если  возвращается  список
//    покупок  некоторого  клиента,  в  ответ  надо  включить  их  суммарную  стоимость.  Также  необходимо  предусмотреть  вариант,  когда
//    выдаются только итоговые значения, без подробностей - в тех случаях, когда это имеет смысл.
//    Ввиду того, что данный запрос может возвращать очень много данных, следует предусмотреть пагинацию результатов, введя
//    параметры запроса “offset” (номер строки результата, с которой начать выдачу) и “limit” (количество строк). Итоговые значения при
//    этом приводятся для возвращаемой выборки, а не для всего списка.

    public SummaryListResponse getSummaryList(String token, boolean allInfo, List<Integer> categories, List<Integer> products, List<Integer> clients, int offset, int limit) throws ServerException {
        checkAdministratorPrivileges(userDao, token);
        List<Client> clientList = null;
//        List<Product> productList = null;
//        List<Category> categoryList = null;
        List<SummaryListByClient> summaryListByClients = new ArrayList<>();

        if(clients != null) {
            clientList = userDao.getClientsById(clients);
            if(clientList != null)
            for (Client it : clientList) {
                int summaryAmount = 0;
                List<PurchaseResponse> purchaseResponses = null;
                if(allInfo) {
                    purchaseResponses = new ArrayList<>(it.getPurchases().size());
                    for (Purchase purchase : it.getPurchases()) {
                        summaryAmount += purchase.getBuyCount() * purchase.getBuyPrice();
                        Product product = purchase.getActual();
                        GetProductResponse productResponse = new GetProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCounter(), getCategoriesListNames(product));
                        purchaseResponses.add(new PurchaseResponse(purchase.getId(), productResponse, purchase.getName(), purchase.getBuyPrice(), purchase.getBuyCount()));
                    }
                } else  {
                    for (Purchase purchase : it.getPurchases()) {
                        summaryAmount += purchase.getBuyCount() * purchase.getBuyPrice();
                    }
                }
                ClientInfo clientInfo = new ClientInfo(it.getId(), it.getFirstname(), it.getLastname(), it.getPatronymic(), it.getEmail(), it.getAddress(), it.getPhone());
                summaryListByClients.add(new SummaryListByClient(clientInfo, purchaseResponses, summaryAmount));
            }
        }
        return new SummaryListResponse(new ArrayList<>(), new ArrayList<>(), summaryListByClients);
    }
}
