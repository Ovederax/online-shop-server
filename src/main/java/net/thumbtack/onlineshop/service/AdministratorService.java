package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.config.ServerProperties;
import net.thumbtack.onlineshop.database.dao.CategoryDao;
import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.response.AvailableSettingResponse;
import net.thumbtack.onlineshop.dto.response.category.GetCategoryResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.purchase.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByCategory;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByClient;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByProduct;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.model.entity.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PushbackInputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdministratorService extends ServiceBase{
    private final ServerProperties properties;
    private CommonDao commonDao;
    private ProductDao productDao;
    private UserDao userDao;

    @Autowired
    public AdministratorService(UserDao userDao, ProductDao productDao, CommonDao commonDao, ServerProperties properties) {
        this.userDao = userDao;
        this.productDao = productDao;
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

    public SummaryListResponse getSummaryList(String token, boolean allInfo, List<Integer> categories, List<Integer> products, List<Integer> clients, int offset, int limit) throws ServerException {
        checkAdministratorPrivileges(userDao, token);

        List<SummaryListByClient> summaryListByClients = null;
        List<SummaryListByCategory> summaryListByCategories = null;
        List<SummaryListByProduct> summaryListByProducts = null;

        if(clients != null) {
            summaryListByClients = new ArrayList<>();
            List<Client> clientList = userDao.getClientsById(clients);
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

        if(products != null) {
            List<Purchase> purchases = productDao.getPurchasesByProductsId(products);
            Map<Product, List<Purchase>> map = new HashMap<>();
            purchases.forEach((purchase -> {
                Product product = purchase.getActual();
                List<Purchase> list = map.computeIfAbsent(product, k -> new ArrayList<>());
                list.add(purchase);
            }));
            summaryListByProducts = map.entrySet().stream().map((entry)->{
                Product product = entry.getKey();
                return new SummaryListByProduct(new GetProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCounter(), null),
                        makePurchasesResponse(entry.getValue()));
            }).collect(Collectors.toList());
        }

        if(categories != null) {
            summaryListByCategories = new ArrayList<>();
            List<Category> categoriesList = productDao.getProductListOrderCategory(categories);
            for (Category category : categoriesList) {
                List<Purchase> purchases = productDao.getPurchasesByProducts(category.getProducts());
                GetCategoryResponse getCategoryResponse;
                if (category.getParent() != null) {
                    getCategoryResponse = new GetCategoryResponse(category.getId(), category.getName(), category.getParent().getId(), category.getParent().getName());
                } else {
                    getCategoryResponse = new GetCategoryResponse(category.getId(), category.getName(), 0, null);
                }
                summaryListByCategories.add(new SummaryListByCategory(getCategoryResponse, makePurchasesResponseWithProductInfo(purchases)));
            }
        }
        return new SummaryListResponse(summaryListByCategories, summaryListByProducts, summaryListByClients);
    }
    private List<PurchaseResponse> makePurchasesResponse(List<Purchase> purchases) {
        return purchases.stream().map((purchase -> new PurchaseResponse(purchase.getId(), purchase.getName(), purchase.getBuyPrice(), purchase.getBuyCount()))).collect(Collectors.toList());
    }
    private List<PurchaseResponse> makePurchasesResponseWithProductInfo(List<Purchase> purchases) {
        List<PurchaseResponse> list = new ArrayList<>();
        for (Purchase purchase : purchases) {
            Product product = purchase.getActual();
            GetProductResponse productResponse = new GetProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCounter(), getCategoriesListNames(product));
            list.add(new PurchaseResponse(purchase.getId(), productResponse, purchase.getName(), purchase.getBuyPrice(), purchase.getBuyCount()));
        }
        return list;
    }
}
