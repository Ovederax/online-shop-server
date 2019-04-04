package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.model.entity.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProductControllerTest {
    @Autowired private CommonDao commonDao;
    @Autowired private ProductDao productDao;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper mapper;
    private MockMvc mvc;
    private Gson gson = new Gson();

    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        commonDao.clear();
    }

    @Test
    public void addProductTest() throws Exception {
        ProductAddRequest req = new ProductAddRequest();
//        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0, "admName", "admLast", null, "pos");
//
//        ResultActions res = mvc.perform(post("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
//        res.andExpect(status().isOk())
//                .andExpect(jsonPath("firstName").value("admName"))
//                .andExpect(cookie().exists("JAVASESSIONID"));
//        MvcResult mvcRes = res.andReturn();
//        String content = mvcRes.getResponse().getContentAsString();
//        Cookie cookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
//        assertNotNull(cookie);
//        AdministratorInfoResponse actual = mapper.readValue(content, AdministratorInfoResponse.class);
//        respExpected.setId(actual.getId());
//
//        assertEquals(respExpected, actual);
    }
}