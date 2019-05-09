package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.utils.CommonRestUtils;
import net.thumbtack.onlineshop.utils.CommonUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment. DEFINED_PORT)
public class ClientControllerTest {
    private final String URL = ServerConstants.URL;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private CommonRestUtils utils;

    @Before
    public void before() throws Exception {
        restTemplate.getForEntity(URL + "/api/debug/clear", String.class);
    }

    @Test
    public void addMoneyDeposit() throws Exception {
        String cookie = utils.registerTestClient(restTemplate);
        DepositMoneyRequest req = new DepositMoneyRequest("123");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Иван", "Иванов", null, "user@gmail.com", "address", "89136668899", 123);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", cookie);
        ResponseEntity<ClientInfoResponse> res = restTemplate.exchange(URL+"/api/deposits", HttpMethod.POST, new HttpEntity<>(req, clientHeaders), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        ClientInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);


        res = restTemplate.exchange(URL+"/api/deposits", HttpMethod.GET, new HttpEntity<>("", clientHeaders), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }

    @Test
    public void buyProduct() throws Exception {
        String adminCookie   = utils.registerTestAdmin(restTemplate);
        String clientCookie  = utils.registerTestClient(restTemplate);

        utils.addDepositMoney(100, clientCookie, restTemplate);

        int catId = utils.addTestCategory(adminCookie, restTemplate);
        List<Integer> categoriesList = Collections.singletonList(catId);
        Product product = new Product("name1", 10, 20, null);
        int productId = (utils.addProduct(product,  adminCookie, categoriesList, restTemplate));


        BuyProductRequest req = new BuyProductRequest(productId, product.getName(), product.getPrice(), null);
        BuyProductResponse respExpected = new BuyProductResponse( 0, product.getName(), product.getPrice(), 1);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientCookie);
        ResponseEntity<BuyProductResponse> res = restTemplate.exchange(URL+"/api/purchases", HttpMethod.POST, new HttpEntity<>(req, clientHeaders), BuyProductResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        BuyProductResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        //-------------CHECK DEPOSIT-------------------------
        ResponseEntity<ClientInfoResponse> res2 = restTemplate.exchange(URL+"/api/deposits", HttpMethod.GET, new HttpEntity<>("", clientHeaders), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(90,  res2.getBody().getDeposit());

        //-------------CHECK PRODUCT COUNT--------------------
        ResponseEntity<GetProductResponse> res3 = restTemplate.exchange(URL+"/api/products/"+productId, HttpMethod.GET, new HttpEntity<>("", clientHeaders), GetProductResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(90,  res2.getBody().getDeposit());
        assertEquals(product.getCounter()-1, res3.getBody().getCount());
    }
}