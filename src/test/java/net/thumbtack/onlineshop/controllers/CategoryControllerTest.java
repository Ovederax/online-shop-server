package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryAddRequest;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryEditRequest;
import net.thumbtack.onlineshop.dto.request.user.AdministratorRegisterRequest;
import net.thumbtack.onlineshop.dto.response.ErrorContent;
import net.thumbtack.onlineshop.dto.response.ErrorResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryAddResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryEditResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryGetResponse;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
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
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CategoryControllerTest {
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper mapper;
    private MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(get("/api/debug/clear")).andExpect(status().isOk());
    }

    private Cookie adminRegisterAndLogin_getCookie() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("admName", "admLast", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,"admName", "admLast", null, "pos");

        ResultActions res = mvc.perform(post("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("admName"))
                .andExpect(cookie().exists("JAVASESSIONID"));
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        Cookie cookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(cookie);
        AdministratorInfoResponse actual = mapper.readValue(content, AdministratorInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
        return cookie;
    }

    @Test
    public void addCategoryTest() throws Exception {
        Cookie token = adminRegisterAndLogin_getCookie();
        CategoryAddRequest req = new CategoryAddRequest("Phones", null);
        CategoryAddResponse respExpected = new CategoryAddResponse( 0, "Phones", 0, null);

        ResultActions res = mvc.perform(post("/api/categories")
                .cookie(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }


    @Test
    public void getCategoryTest() throws Exception {
        Cookie token = adminRegisterAndLogin_getCookie();

        ResultActions res = mvc.perform(post("/api/categories")
                .cookie(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CategoryAddRequest("Phones", null))));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);

        assertEquals(new CategoryAddResponse( actual.getId(), "Phones", 0, null), actual);


        int id = actual.getId();
        res = mvc.perform(get("/api/categories/"+id)
                .cookie(token));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        actual = mapper.readValue(content, CategoryAddResponse.class);
        assertEquals(new CategoryAddResponse( id, "Phones", 0, null), actual);

        // + тесты на сбор связанных объектов.

    }

    @Test
    public void updateCategoryTest() throws Exception {
        Cookie token = adminRegisterAndLogin_getCookie();
        ResultActions res = mvc.perform(post("/api/categories")
                .cookie(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CategoryAddRequest("Phones", null))));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);

        assertEquals(new CategoryAddResponse( actual.getId(), "Phones", 0, null), actual);

        int id = actual.getId();

        res = mvc.perform(put("/api/categories/"+id)
                .cookie(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CategoryEditRequest("Telefones", null))));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        assertEquals(new CategoryEditResponse( id, "Telefones", 0, null),  mapper.readValue(content, CategoryEditResponse.class));

        res = mvc.perform(get("/api/categories/"+id).cookie(token));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        assertEquals(new CategoryGetResponse( id, "Telefones", 0, null), mapper.readValue(content, CategoryGetResponse.class));

    }

    @Test
    public void deleteCategoryTest() throws Exception  {
        Cookie token = adminRegisterAndLogin_getCookie();
        ResultActions res = mvc.perform(post("/api/categories")
                .cookie(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new CategoryAddRequest("Phones", null))));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);

        assertEquals(new CategoryAddResponse( actual.getId(), "Phones", 0, null), actual);

        int id = actual.getId();

        res = mvc.perform(delete("/api/categories/"+id)
                .cookie(token));
        res.andExpect(status().isOk());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();
        assertEquals("{}", content);

        res = mvc.perform(get("/api/categories/"+id).cookie(token));
        res.andExpect(status().isBadRequest());
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();

        ErrorCode ex = ErrorCode.CATEGORY_NO_EXISTS;
        assertEquals(new ErrorResponse(Arrays.asList(new ErrorContent(
                ex.getErrorCode(), ex.getField(), ex.getMessage()
        ))), mapper.readValue(content, ErrorResponse.class));
    }

    @Test
    public void getCategoriesTest() throws Exception {
        Cookie token = adminRegisterAndLogin_getCookie();
        List<CategoryAddRequest> list = Arrays.asList(
                new CategoryAddRequest("Phones", null),
                new CategoryAddRequest("Dress", null),
                new CategoryAddRequest("Games", null));
        List<CategoryAddResponse> responseList = new ArrayList<>(3);
        List<CategoryGetResponse> needGetList = new ArrayList<>(3);
        for(int i=0; i<3; ++i) {
            CategoryAddRequest item = list.get(i);
            ResultActions res = mvc.perform(post("/api/categories")
                    .cookie(token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(item)));
            res.andExpect(status().isOk());
            MvcResult mvcRes = res.andReturn();
            String content = mvcRes.getResponse().getContentAsString();
            CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);

            responseList.add(new CategoryAddResponse(actual.getId(), item.getName(), 0, null));
            needGetList.add(new CategoryGetResponse(actual.getId(), item.getName(), 0, null));
            assertEquals(responseList.get(i), actual);
        }

        ResultActions res = mvc.perform(get("/api/categories/").cookie(token));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        needGetList.sort(Comparator.comparing(CategoryGetResponse::getName));
        assertEquals(needGetList, mapper.readValue(content, new TypeReference<List<CategoryGetResponse>>() {}));
    }
}




