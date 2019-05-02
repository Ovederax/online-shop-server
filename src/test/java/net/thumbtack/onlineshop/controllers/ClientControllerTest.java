package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.BuyProductRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.product.BuyProductResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment. DEFINED_PORT)
public class ClientControllerTest {
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
    public void addMoneyDeposit() throws Exception {
        Cookie token = utils.registerTestClient(mvc);
        DepositMoneyRequest req = new DepositMoneyRequest("123");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Иван", "Иванов", null, "user@gmail.com", "address", "89136668899", 123);

        ResultActions res = mvc.perform(post("/api/deposits")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(token));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);

        res = mvc.perform(get("/api/deposits")
                .cookie(token));

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();
        actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }

    @Test
    public void buyProduct() throws Exception {
        Cookie adminToken   = utils.registerTestAdmin(mvc);
        Cookie clientToken  = utils.registerTestClient(mvc);

        utils.addDepositMoney(100, clientToken, mvc);

        int catId = utils.addTestCategory(adminToken, mvc);
        List<Integer> categoriesList = Collections.singletonList(catId);
        Product product = new Product("name1", 10, 20, null);
        int productId = (utils.addProduct(product,  adminToken, categoriesList, mvc));

        BuyProductRequest req = new BuyProductRequest(productId, product.getName(), product.getPrice(), null);
        BuyProductResponse respExpected = new BuyProductResponse( 0, product.getName(), product.getPrice(), 1);

        ResultActions res = mvc.perform(post("/api/purchases")
                .cookie(clientToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();

        BuyProductResponse actual = mapper.readValue(content, BuyProductResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);

        //-------------CHECK DEPOSIT-------------------------
        res = mvc.perform(get("/api/deposits")
                .cookie(clientToken));
        mvcRes = res.andExpect(status().isOk()).andReturn();
        assertEquals(90,  mapper.readValue(mvcRes.getResponse().getContentAsString(), ClientInfoResponse.class).getDeposit());

        //-------------CHECK PRODUCT COUNT--------------------
        res = mvc.perform(get("/api/products/"+productId)
                .cookie(clientToken));

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();

        assertEquals(product.getCounter()-1, mapper.readValue(content, GetProductResponse.class).getCount());
    }

}