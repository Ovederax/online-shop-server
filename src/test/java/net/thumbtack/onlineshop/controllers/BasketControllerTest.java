package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.basket.AddProductToBasketsRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketBuyProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductBuyRequest;
import net.thumbtack.onlineshop.dto.response.basket.BasketBuyProductResponse;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryGetResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductBuyResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.utils.CommonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BasketControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonUtils utils;
    private MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(get("/api/debug/clear")).andExpect(status().isOk());
    }

    @Test
    public void addProductIntoBasket() throws Exception {
        Cookie adminToken   = utils.registerTestAdmin(mvc);
        Cookie clientToken  = utils.registerTestClient(mvc);
        int catId = utils.addTestCategory(adminToken, mvc);
        List<Integer> categoriesList = Collections.singletonList(catId);
        Product product = new Product("name1", 10, 20, null);
        int productId = (utils.addProduct(product,  adminToken, categoriesList, mvc));

         AddProductToBasketsRequest req = new AddProductToBasketsRequest(productId, product.getName(), product.getPrice(), 100);
         ProductInBasketResponse respExpected = new ProductInBasketResponse( 0, product.getName(), product.getPrice(), 100);

        ResultActions res = mvc.perform(post("/api/basket")
                .cookie(clientToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();

        List<ProductInBasketResponse> actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});
        respExpected.setId(actual.get(0).getId());

        assertEquals(respExpected, actual.get(0));
    }

    @Test
    public void deleteProductFromBasket() throws Exception {
        Cookie adminToken   = utils.registerTestAdmin(mvc);
        Cookie clientToken  = utils.registerTestClient(mvc);
        int catId = utils.addTestCategory(adminToken, mvc);
        List<Integer> categoriesList = Collections.singletonList(catId);

        List<Product> products = asList(
            new Product("name1", 10, 20, null),
            new Product("name2", 10, 20, null),
            new Product("name3", 10, 20, null)
        );

        List<ProductInBasketResponse> actual = null;
        for(int i=0; i<products.size(); ++i) {
            Product it = products.get(i);
            int id = utils.addProduct(it,  adminToken, categoriesList, mvc);

            AddProductToBasketsRequest req = new AddProductToBasketsRequest(id, it.getName(), it.getPrice(), 20);
            ResultActions res = mvc.perform(post("/api/basket")
                    .cookie(clientToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(req)));
            res.andExpect(status().isOk());
            MvcResult mvcRes = res.andReturn();
            String content = mvcRes.getResponse().getContentAsString();
            actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});

            assertEquals(i+1, actual.size());
        }

        List<ProductInBasketResponse> previous = actual;
        ProductInBasketResponse basketResponse = previous.get(1);

        ResultActions res = mvc.perform(delete("/api/basket/"+basketResponse.getId())
                .cookie(clientToken));
        res.andExpect(status().isOk());

        res = mvc.perform(get("/api/basket/")
                .cookie(clientToken));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});

        assertEquals(products.size()-1, actual.size());
        assertFalse(actual.contains(basketResponse));
    }

    @Test
    public void updateProductCountInBasket() throws Exception {
        Cookie adminToken   = utils.registerTestAdmin(mvc);
        Cookie clientToken  = utils.registerTestClient(mvc);
        int catId = utils.addTestCategory(adminToken, mvc);
        List<Integer> categoriesList = Collections.singletonList(catId);
        Product product = new Product("name1", 10, 20, null);
        int productId = (utils.addProduct(product,  adminToken, categoriesList, mvc));

        AddProductToBasketsRequest req = new AddProductToBasketsRequest(productId, product.getName(), product.getPrice(), 100);
        ProductInBasketResponse respExpected = new ProductInBasketResponse( 0, product.getName(), product.getPrice(), 100);

        ResultActions res = mvc.perform(post("/api/basket")
                .cookie(clientToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();

        List<ProductInBasketResponse> actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});
        respExpected.setId(actual.get(0).getId());

        assertEquals(respExpected, actual.get(0));

        BasketUpdateCountProductRequest updReq = new BasketUpdateCountProductRequest(actual.get(0).getId(), product.getName(), product.getPrice(), 70);
        respExpected = new ProductInBasketResponse( 0, product.getName(), product.getPrice(), 70);
        res = mvc.perform(put("/api/basket/")
                .cookie(clientToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(updReq)));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});
        respExpected.setId(actual.get(0).getId());

        assertEquals(respExpected, actual.get(0));
    }

    @Test
    public void getProductsInBasket() throws Exception {
        Cookie adminToken   = utils.registerTestAdmin(mvc);
        Cookie clientToken  = utils.registerTestClient(mvc);
        int catId = utils.addTestCategory(adminToken, mvc);
        List<Integer> categoriesList = Collections.singletonList(catId);

        List<Product> products = asList(
                new Product("name1", 10, 20, null),
                new Product("name2", 10, 20, null),
                new Product("name3", 10, 20, null)
        );

        List<ProductInBasketResponse> actual = null;
        for(int i=0; i<products.size(); ++i) {
            Product it = products.get(i);
            int id = utils.addProduct(it,  adminToken, categoriesList, mvc);

            AddProductToBasketsRequest req = new AddProductToBasketsRequest(id, it.getName(), it.getPrice(), 20);
            ResultActions res = mvc.perform(post("/api/basket")
                    .cookie(clientToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(req)));
            res.andExpect(status().isOk());
            MvcResult mvcRes = res.andReturn();
            String content = mvcRes.getResponse().getContentAsString();
            actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});

            assertEquals(i+1, actual.size());
        }

        List<ProductInBasketResponse> previous = actual;
        ProductInBasketResponse basketResponse = previous.get(1);

        ResultActions res = mvc.perform(delete("/api/basket/"+basketResponse.getId())
                .cookie(clientToken));
        res.andExpect(status().isOk());
    }

    @Test
    public void buyProductsFromBasket() throws Exception {
        utils.insertTestDataInBD(mvc);
        Cookie clientToken  = utils.registerTestClient(mvc);
        utils.addDepositMoney(10000, clientToken, mvc);

        ResultActions res = mvc.perform(get("/api/products")
                .cookie(clientToken));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        List<GetProductResponse> products = mapper.readValue(content, new TypeReference<List<GetProductResponse>>(){});

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
            res = mvc.perform(post("/api/basket")
                    .cookie(clientToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(it)));
            mvcRes = res.andExpect(status().isOk()).andReturn();
            content = mvcRes.getResponse().getContentAsString();

            actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>() {});
            actual.sort(Comparator.comparingInt(ProductInBasketResponse::getPrice));
            respExpected.setId(actual.get(i).getId());
            assertEquals(actual.get(i), respExpected);
        }

        List<BasketBuyProductRequest> list = new ArrayList<>();
        assert actual != null;
        for (int i = 0; i<actual.size()-1; ++i) {
            ProductInBasketResponse it = actual.get(i);
            list.add(new BasketBuyProductRequest(it.getId(), it.getName(), it.getPrice(), it.getCount()));
        }
        res = mvc.perform(post("/api/purchases/basket")
                .cookie(clientToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(list)));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        BasketBuyProductResponse response = mapper.readValue(content, BasketBuyProductResponse.class);
        assertEquals(3, response.getBought().size());


        assertEquals(actual.get(actual.size()-1), response.getRemaining().get(0));

        res = mvc.perform(get("/api/basket/")
                .cookie(clientToken));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        actual = mapper.readValue(content, new TypeReference<List<ProductInBasketResponse>>(){});
        assertEquals(response.getRemaining().get(0), actual.get(0));

        res = mvc.perform(get("/api/deposits")
                .cookie(clientToken));

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();
        ClientInfoResponse clientInfo = mapper.readValue(content, ClientInfoResponse.class);

        assertEquals(8000, clientInfo.getDeposit());
    }
}