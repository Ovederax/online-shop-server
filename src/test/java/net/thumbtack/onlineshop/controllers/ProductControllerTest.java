package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductEditRequest;
import net.thumbtack.onlineshop.dto.response.product.ProductGetResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment =  SpringBootTest.WebEnvironment. DEFINED_PORT)
public class ProductControllerTest {
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonUtils utils;
    private MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(get("/api/debug/clear")).andExpect(status().isOk());
    }


    @Test
    public void addProductTest() throws Exception {
        Cookie token = utils.registerTestAdmin(mvc);
        ProductAddRequest req = new ProductAddRequest("product", 10, 12, null);
        ProductResponse respExpected = new ProductResponse(0,"product", 10, 12, new ArrayList<>());

        ResultActions res = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(token));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ProductResponse actual = mapper.readValue(content, ProductResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }

    @Test
    public void getProductTest() throws Exception {
        Cookie token = utils.registerTestAdmin(mvc);
        int categoryId = utils.addTestCategory(token, mvc);

        ProductAddRequest req = new ProductAddRequest("product", 10, 12, new ArrayList<>(Collections.singletonList(categoryId)));

        ResultActions res = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(token));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ProductResponse actual = mapper.readValue(content, ProductResponse.class);

        assertEquals( new ProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList(categoryId))), actual);


        res = mvc.perform(get("/api/products/"+actual.getId())
                .cookie(token));

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();
        ProductGetResponse response = mapper.readValue(content, ProductGetResponse.class);

        assertEquals(response.getId(), actual.getId());
        assertEquals(new ProductGetResponse(response.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList("Phones"))), response);

    }

    @Test
    public void updateProductTest() throws Exception {
        Cookie token = utils.registerTestAdmin(mvc);
        int categoryId = utils.addTestCategory(token, mvc);

        ProductAddRequest req = new ProductAddRequest("product", 10, 12, new ArrayList<>(Collections.singletonList(categoryId)));

        ResultActions res = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(token));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ProductResponse actual = mapper.readValue(content, ProductResponse.class);

        assertEquals( new ProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList(categoryId))), actual);

        res = mvc.perform(put("/api/products/"+actual.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new ProductEditRequest("superProduct", 3,7,null)))
                .cookie(token));

        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();
        ProductResponse response = mapper.readValue(content, ProductResponse.class);
        res.andExpect(status().isOk());

        assertEquals(response.getId(), actual.getId());
        assertEquals(new ProductResponse(response.getId(),"superProduct", 3, 7, new ArrayList<>(Arrays.asList(categoryId))), response);


        res = mvc.perform(get("/api/products/"+actual.getId())
                .cookie(token));

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();
        ProductGetResponse getResponse = mapper.readValue(content, ProductGetResponse.class);

        assertEquals(getResponse.getId(), actual.getId());
        assertEquals(new ProductGetResponse(getResponse.getId(),"superProduct", 3, 7, new ArrayList<>(Arrays.asList("Phones"))), getResponse);
    }

    @Test
    public void deleteProductTest() throws Exception {
        Cookie token = utils.registerTestAdmin(mvc);
        int categoryId = utils.addTestCategory(token, mvc);

        ProductAddRequest req = new ProductAddRequest("product", 10, 12, new ArrayList<>(Collections.singletonList(categoryId)));

        ResultActions res = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(token));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ProductResponse actual = mapper.readValue(content, ProductResponse.class);

        assertEquals( new ProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList(categoryId))), actual);

        res = mvc.perform(delete("/api/products/"+actual.getId()).cookie(token));

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();
        assertEquals("{}", content);

        res = mvc.perform(get("/api/products/"+actual.getId())
                .cookie(token));

        // Не удаляет продукт полностью, а помечает как удаленный

        mvcRes = res.andExpect(status().isOk()).andReturn();
        content = mvcRes.getResponse().getContentAsString();
        ProductGetResponse response = mapper.readValue(content, ProductGetResponse.class);

        assertEquals(response.getId(), actual.getId());
        assertEquals(new ProductGetResponse(response.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList("Phones"))), response);
    }

    @Test
    public void getProductsTest() throws Exception {
        Cookie token = utils.registerTestAdmin(mvc);

        // TODO
        // Учитывая сложную логику в тз для этого запроса, нужен большой тест с множеством данных
        // Тестовые данные будут забиватся в БД и после этого будут тестироваться опции
        // Когда буду писать большие тесты для всей логики приступлю и к написанию этого теста

   }
}