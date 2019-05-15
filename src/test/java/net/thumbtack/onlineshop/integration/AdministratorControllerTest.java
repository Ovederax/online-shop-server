package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.basket.AddProductToBasketsRequest;
import net.thumbtack.onlineshop.dto.request.basket.BuyProductFromBasketRequest;
import net.thumbtack.onlineshop.dto.response.AvailableSettingResponse;
import net.thumbtack.onlineshop.dto.response.basket.BuyProductFromBasketResponse;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.purchase.PurchaseResponse;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByCategory;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByClient;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListByProduct;
import net.thumbtack.onlineshop.dto.response.summary.SummaryListResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.utils.CommonRestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdministratorControllerTest {
    private final String URL = ServerConstants.URL;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonRestUtils utils;
    static final private HttpHeaders headers = new HttpHeaders();

    @Before
    public void before() throws Exception {
        restTemplate.getForEntity(URL + "/api/debug/clear", String.class);
    }

    @Test
    public void getSummaryList() throws Exception {
        Client client = new Client("Иван", "Иванов", null, "user@gmail.com", "address", "89136668899", "user", "pass");
        String adminCookie  = utils.registerTestAdmin(restTemplate);
        String clientCookie = utils.registerClient(client, restTemplate);
        utils.addDepositMoney(10000, clientCookie, restTemplate);

        List<Category> insertCategories = asList(
                new Category("phone", null),
                new Category("dress", null),
                new Category("clock", null),
                new Category("calculator", null)
        );
        List<Product> insertProducts  = asList(
                new Product("Iphone X666", 66600, 10, singletonList(insertCategories.get(0))),
                new Product("MegaShorts", 700,100, singletonList(insertCategories.get(1))),
                new Product("TC12-90", 300,30, singletonList(insertCategories.get(2))),
                new Product("CALCULUS-92",100,50,singletonList(insertCategories.get(3))
                ));

        for(Category it: insertCategories) {
            utils.addCategory(it, adminCookie, restTemplate);
        }

        for(Product it: insertProducts) {
            utils.addProduct(it, adminCookie, singletonList(it.getCategories().get(0).getId()), restTemplate);
        }

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientCookie);
        ResponseEntity<String> res = restTemplate.exchange(URL+"/api/products", HttpMethod.GET, new HttpEntity<>("", clientHeaders), String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<GetProductResponse> products = mapper.readValue(res.getBody(), new TypeReference<List<GetProductResponse>>(){});

        assertEquals(4, products.size());
        products.sort(Comparator.comparingInt(GetProductResponse::getPrice));

        List<AddProductToBasketsRequest> basketsRequests = asList(
                new AddProductToBasketsRequest(products.get(0).getId(), products.get(0).getName(), products.get(0).getPrice(), 3),
                new AddProductToBasketsRequest(products.get(1).getId(), products.get(1).getName(), products.get(1).getPrice(), 1),
                new AddProductToBasketsRequest(products.get(2).getId(), products.get(2).getName(), products.get(2).getPrice(), 2),
                new AddProductToBasketsRequest(products.get(3).getId(), products.get(3).getName(), products.get(3).getPrice(), 1)
        );
        products.sort(Comparator.comparingInt(GetProductResponse::getId));

        List<ProductInBasketResponse> actual = null;
        for(int i=0; i<basketsRequests.size(); ++i) {
            AddProductToBasketsRequest it = basketsRequests.get(i);
            ProductInBasketResponse respExpected = new ProductInBasketResponse( 0, it.getName(), it.getPrice(), it.getCount());

            ResponseEntity<String> response = restTemplate.exchange(URL+"/api/basket", HttpMethod.POST, new HttpEntity<>(it, clientHeaders), String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());;

            actual = mapper.readValue(response.getBody(), new TypeReference<List<ProductInBasketResponse>>() {});
            actual.sort(Comparator.comparingInt(ProductInBasketResponse::getPrice));
            respExpected.setId(actual.get(i).getId());
            assertEquals(actual.get(i), respExpected);
        }

        List<BuyProductFromBasketRequest> list = new ArrayList<>();
        assert actual != null;
        for (int i = 0; i<actual.size()-1; ++i) {
            ProductInBasketResponse it = actual.get(i);
            list.add(new BuyProductFromBasketRequest(it.getId(), it.getName(), it.getPrice(), it.getCount()));
        }

        ResponseEntity<BuyProductFromBasketResponse> response = restTemplate.exchange(URL+"/api/purchases/basket", HttpMethod.POST, new HttpEntity<>(list, clientHeaders), BuyProductFromBasketResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        BuyProductFromBasketResponse buyBasketResponse = response.getBody();
        assertEquals(3, buyBasketResponse.getBought().size());

        res = restTemplate.exchange(URL+"/api/basket/", HttpMethod.GET, new HttpEntity<>("", clientHeaders), String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        actual = mapper.readValue(res.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});
        assertEquals(actual.get(actual.size()-1), buyBasketResponse.getRemaining().get(0));

        ResponseEntity<ClientInfoResponse> res2 = restTemplate.exchange(URL+"/api/deposits", HttpMethod.GET, new HttpEntity<>("", clientHeaders), ClientInfoResponse.class);
        assertEquals(HttpStatus.OK, res2.getStatusCode());
        ClientInfoResponse clientInfo = res2.getBody();
        assertEquals(8000, clientInfo.getDeposit());

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.add("Cookie", adminCookie);

        //----Test one----
        ResponseEntity<String> res3 = restTemplate.exchange(URL+"/api/purchases?allInfo=true&clients="+ client.getId(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(res3.getStatusCode(), HttpStatus.OK);
        SummaryListResponse listResponse = mapper.readValue(res3.getBody(), SummaryListResponse.class);

        SummaryListByClient summaryListByClient = listResponse.getSummaryListByClients().get(0);
        assertEquals(2000, summaryListByClient.getSummaryAmount());
        assertEquals(new ClientInfo(client.getId(), client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone()), summaryListByClient.getClientInfo());

        List<PurchaseResponse> purchases = summaryListByClient.getPurchases();
        purchases.sort(Comparator.comparingInt(PurchaseResponse::getId));

        List<BuyProductResponse> productResponses = buyBasketResponse.getBought();
        productResponses.sort(Comparator.comparingInt(BuyProductResponse::getId));

        assertEquals(productResponses.size(), purchases.size());
        for(int i=0; i<productResponses.size(); ++i) {
            PurchaseResponse it = purchases.get(i);
            BuyProductResponse temp = new BuyProductResponse(it.getId(), it.getName(), it.getBuyPrice(), it.getBuyCount());
            assertEquals(productResponses.get(i), temp);
        }
        //----Test two----
        ResponseEntity<String> res4 = restTemplate.exchange(URL+"/api/purchases?clients="+ client.getId(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(res4.getStatusCode(), HttpStatus.OK);
        listResponse = mapper.readValue(res4.getBody(), SummaryListResponse.class);

        summaryListByClient = listResponse.getSummaryListByClients().get(0);
        assertEquals(2000, summaryListByClient.getSummaryAmount());
        assertEquals(new ClientInfo(client.getId(), client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone()), summaryListByClient.getClientInfo());

        purchases = summaryListByClient.getPurchases();
        assertEquals(null, purchases);

        //----Test three----
        ResponseEntity<String> res5 = restTemplate.exchange(URL+"/api/purchases?allInfo=true&clients="+ client.getId()+"&products="+products.get(1).getId()+","+products.get(2).getId(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(res5.getStatusCode(), HttpStatus.OK);
        listResponse = mapper.readValue(res5.getBody(), SummaryListResponse.class);

        summaryListByClient = listResponse.getSummaryListByClients().get(0);
        assertEquals(2000, summaryListByClient.getSummaryAmount());
        assertEquals(new ClientInfo(client.getId(), client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone()), summaryListByClient.getClientInfo());

        assertNotNull(summaryListByClient.getPurchases());
        assertEquals(productResponses.size(),summaryListByClient.getPurchases().size());
        List<SummaryListByProduct> summaryListByProduct = listResponse.getSummaryListByProducts();
        assertEquals(2, summaryListByProduct.size());
        summaryListByProduct.sort(Comparator.comparingInt(o -> o.getProduct().getId()));
        assertEquals(products.get(1).getName(), summaryListByProduct.get(0).getProduct().getName());
        assertEquals(null, listResponse.getSummaryListByCategories());

        //---Test four-----
        StringJoiner joiner = new StringJoiner(",");
        for (Category it : insertCategories) {
            joiner.add(Integer.toString(it.getId()));
        }
        ResponseEntity<String> res6 = restTemplate.exchange(URL+"/api/purchases?categories="+joiner.toString(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(res6.getStatusCode(), HttpStatus.OK);
        listResponse = mapper.readValue(res6.getBody(), SummaryListResponse.class);
        assertNull(listResponse.getSummaryListByClients());
        assertNull(listResponse.getSummaryListByProducts());

        List<SummaryListByCategory> summaryListByCategory = listResponse.getSummaryListByCategories();
        summaryListByCategory.sort((Comparator.comparing(o -> o.getCategory().getName())));
        assertEquals(4, summaryListByCategory.size());
        assertEquals("calculator",summaryListByCategory.get(0).getCategory().getName());
        assertEquals(1,summaryListByCategory.get(0).getPurchases().size());
        assertEquals("phone",summaryListByCategory.get(3).getCategory().getName());
        assertEquals(0,summaryListByCategory.get(3).getPurchases().size());
        //---Test five---
        ResponseEntity<String> res7 = restTemplate.exchange(URL+"/api/purchases", HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(res7.getStatusCode(), HttpStatus.OK);
        listResponse = mapper.readValue(res7.getBody(), SummaryListResponse.class);
        assertNull(listResponse.getSummaryListByClients());
        assertNull(listResponse.getSummaryListByProducts());
        assertNull(listResponse.getSummaryListByCategories());
    }

    @Test
    public void getSettings() {
        AvailableSettingResponse respExpected = new AvailableSettingResponse(50, 4);
        ResponseEntity<AvailableSettingResponse> res = restTemplate.exchange(URL+"/api/settings", HttpMethod.GET, new HttpEntity<>("", new HttpHeaders()), AvailableSettingResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        AvailableSettingResponse actual = res.getBody();
        assertEquals(respExpected, actual);
    }
}