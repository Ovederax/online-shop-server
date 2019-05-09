package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.basket.AddProductToBasketsRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.BuyProductFromBasketRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.basket.BuyProductFromBasketResponse;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BasketControllerTest {
    private final String URL = ServerConstants.URL;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonRestUtils utils;
    static final private HttpHeaders headers = new HttpHeaders();

    @BeforeClass
    public static void beforeClass() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Before
    public void before() throws Exception {
        restTemplate.getForEntity(URL + "/api/debug/clear", String.class);
    }

    @Test
    public void addProductIntoBasket() throws Exception {
        String adminToken   = utils.registerTestAdmin(restTemplate);
        String clientToken  = utils.registerTestClient(restTemplate);
        int catId = utils.addTestCategory(adminToken, restTemplate);
        List<Integer> categoriesList = Collections.singletonList(catId);
        Product product = new Product("name1", 10, 20, null);
        int productId = (utils.addProduct(product,  adminToken, categoriesList, restTemplate));

         AddProductToBasketsRequest req = new AddProductToBasketsRequest(productId, product.getName(), product.getPrice(), 100);
         ProductInBasketResponse respExpected = new ProductInBasketResponse( 0, product.getName(), product.getPrice(), 100);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientToken);

        ResponseEntity<String> res = restTemplate.exchange(URL+"/api/basket", HttpMethod.POST, new HttpEntity<>(req, clientHeaders), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);

        List<ProductInBasketResponse> actual = mapper.readValue(res.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});
        respExpected.setId(actual.get(0).getId());
        assertEquals(respExpected, actual.get(0));
    }

    @Test
    public void deleteProductFromBasket() throws Exception {
        String adminToken   = utils.registerTestAdmin(restTemplate);
        String clientToken  = utils.registerTestClient(restTemplate);
        int catId = utils.addTestCategory(adminToken, restTemplate);
        List<Integer> categoriesList = Collections.singletonList(catId);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientToken);

        List<Product> products = asList(
            new Product("name1", 10, 20, null),
            new Product("name2", 10, 20, null),
            new Product("name3", 10, 20, null)
        );

        List<ProductInBasketResponse> actual = null;
        for(int i=0; i<products.size(); ++i) {
            Product it = products.get(i);
            int id = utils.addProduct(it,  adminToken, categoriesList, restTemplate);

            AddProductToBasketsRequest req = new AddProductToBasketsRequest(id, it.getName(), it.getPrice(), 20);

            ResponseEntity<String> res = restTemplate.exchange(URL+"/api/basket", HttpMethod.POST, new HttpEntity<>(req, clientHeaders), String.class);
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            actual = mapper.readValue(res.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});

            assertEquals(i+1, actual.size());
        }

        List<ProductInBasketResponse> previous = actual;
        ProductInBasketResponse basketResponse = previous.get(1);

        ResponseEntity<String> res = restTemplate.exchange(URL+"/api/basket/"+basketResponse.getId(), HttpMethod.DELETE, new HttpEntity<>("", clientHeaders), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);

        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/basket/", HttpMethod.GET, new HttpEntity<>("", clientHeaders), String.class);
        assertEquals(res2.getStatusCode(), HttpStatus.OK);

        String content = res2.getBody();
        actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});

        assertEquals(products.size()-1, actual.size());
        assertFalse(actual.contains(basketResponse));
    }

    @Test
    public void updateProductCountInBasket() throws Exception {
        String adminToken   = utils.registerTestAdmin(restTemplate);
        String clientToken  = utils.registerTestClient(restTemplate);
        int catId = utils.addTestCategory(adminToken, restTemplate);
        List<Integer> categoriesList = Collections.singletonList(catId);
        Product product = new Product("name1", 10, 20, null);
        int productId = (utils.addProduct(product,  adminToken, categoriesList, restTemplate));

        AddProductToBasketsRequest req = new AddProductToBasketsRequest(productId, product.getName(), product.getPrice(), 100);
        ProductInBasketResponse respExpected = new ProductInBasketResponse( 0, product.getName(), product.getPrice(), 100);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientToken);

        ResponseEntity<String> res = restTemplate.exchange(URL+"/api/basket/", HttpMethod.POST, new HttpEntity<>(req, clientHeaders), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        List<ProductInBasketResponse> actual = mapper.readValue(res.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});
        respExpected.setId(actual.get(0).getId());
        assertEquals(respExpected, actual.get(0));

        BasketUpdateCountProductRequest updReq = new BasketUpdateCountProductRequest(actual.get(0).getId(), product.getName(), product.getPrice(), 70);
        respExpected = new ProductInBasketResponse( 0, product.getName(), product.getPrice(), 70);
        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/basket/", HttpMethod.PUT, new HttpEntity<>(updReq, clientHeaders), String.class);
        assertEquals(res2.getStatusCode(), HttpStatus.OK);
        actual = mapper.readValue(res2.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});
        respExpected.setId(actual.get(0).getId());
        assertEquals(respExpected, actual.get(0));
    }

    @Test
    public void getProductsInBasket() throws Exception {
        String  adminToken   = utils.registerTestAdmin(restTemplate);
        String clientToken  = utils.registerTestClient(restTemplate);
        int catId = utils.addTestCategory(adminToken, restTemplate);
        List<Integer> categoriesList = Collections.singletonList(catId);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientToken);

        List<Product> products = asList(
                new Product("name1", 10, 20, null),
                new Product("name2", 10, 20, null),
                new Product("name3", 10, 20, null)
        );

        List<ProductInBasketResponse> actual = null;
        for(int i=0; i<products.size(); ++i) {
            Product it = products.get(i);
            int id = utils.addProduct(it,  adminToken, categoriesList, restTemplate);

            AddProductToBasketsRequest req = new AddProductToBasketsRequest(id, it.getName(), it.getPrice(), 20);
            ResponseEntity<String> res = restTemplate.exchange(URL+"/api/basket", HttpMethod.POST, new HttpEntity<>(req, clientHeaders), String.class);
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            actual = mapper.readValue(res.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});
            assertEquals(i+1, actual.size());
        }
    }

    @Test
    public void buyProductsFromBasket() throws Exception {
        utils.insertTestDataInBD( utils.registerTestAdmin(restTemplate), restTemplate);
        String clientToken  = utils.registerTestClient(restTemplate);
        utils.addDepositMoney(10000, clientToken, restTemplate);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.set("Cookie", clientToken);

        ResponseEntity<String> res = restTemplate.exchange(URL+"/api/products", HttpMethod.GET, new HttpEntity<>("", clientHeaders), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        List<GetProductResponse> products = mapper.readValue(res.getBody(), new TypeReference<List<GetProductResponse>>(){});

        assertEquals(4, products.size());
        products.sort(Comparator.comparingInt(GetProductResponse::getPrice));

        List<AddProductToBasketsRequest> basketsRequests = asList(
                new AddProductToBasketsRequest(products.get(0).getId(), products.get(0).getName(), products.get(0).getPrice(), 3),
                new AddProductToBasketsRequest(products.get(1).getId(), products.get(1).getName(), products.get(1).getPrice(), 1),
                new AddProductToBasketsRequest(products.get(2).getId(), products.get(2).getName(), products.get(2).getPrice(), 2),
                new AddProductToBasketsRequest(products.get(3).getId(), products.get(3).getName(), products.get(3).getPrice(), 1)
        );

        List<ProductInBasketResponse> actual = null;
        for(int i=0; i<basketsRequests.size(); ++i) {
            AddProductToBasketsRequest it = basketsRequests.get(i);
            ProductInBasketResponse respExpected = new ProductInBasketResponse( 0, it.getName(), it.getPrice(), it.getCount());
            ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/basket", HttpMethod.POST, new HttpEntity<>(it, clientHeaders), String.class);
            assertEquals(res2.getStatusCode(), HttpStatus.OK);

            actual = mapper.readValue(res2.getBody(), new TypeReference<List<ProductInBasketResponse>>() {});
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

        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/purchases/basket", HttpMethod.POST, new HttpEntity<>(list, clientHeaders), String.class);
        assertEquals(res2.getStatusCode(), HttpStatus.OK);
        BuyProductFromBasketResponse response = mapper.readValue(res2.getBody(), BuyProductFromBasketResponse.class);
        assertEquals(3, response.getBought().size());

        assertEquals(actual.get(actual.size()-1), response.getRemaining().get(0));

        ResponseEntity<String> res3 = restTemplate.exchange(URL+"/api/basket/", HttpMethod.GET, new HttpEntity<>(list, clientHeaders), String.class);
        assertEquals(res3.getStatusCode(), HttpStatus.OK);
        actual = mapper.readValue(res3.getBody(), new TypeReference<List<ProductInBasketResponse>>(){});
        assertEquals(response.getRemaining().get(0), actual.get(0));

        ResponseEntity<ClientInfoResponse> res4 = restTemplate.exchange(URL+"/api/deposits", HttpMethod.GET, new HttpEntity<>("", clientHeaders), ClientInfoResponse.class);
        assertEquals(res4.getStatusCode(), HttpStatus.OK);
        assertEquals(8000, res4.getBody().getDeposit());
    }
}