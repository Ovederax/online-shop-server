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
import java.lang.reflect.Array;
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
    @Autowired private CommonUtils utils;
    private MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(get("/api/debug/clear")).andExpect(status().isOk());
    }

    @Test
    public void addCategoryTest() throws Exception {
        Cookie token = utils.registerTestAdmin(mvc);
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
        Cookie token = utils.registerTestAdmin(mvc);

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
        Cookie token = utils.registerTestAdmin(mvc);
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
        Cookie token = utils.registerTestAdmin(mvc);
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
        Cookie token = utils.registerTestAdmin(mvc);
        List<CategoryAddRequest> list = Arrays.asList(
                new CategoryAddRequest("Phones", null),
                new CategoryAddRequest("Dress", null),
                new CategoryAddRequest("Games", null));

        List<CategoryGetResponse> need = new ArrayList<>(5);
        for(CategoryAddRequest item : list) {
            ResultActions res = mvc.perform(post("/api/categories")
                    .cookie(token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(item)));
            res.andExpect(status().isOk());
            MvcResult mvcRes = res.andReturn();
            String content = mvcRes.getResponse().getContentAsString();
            CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);


            assertEquals(new CategoryAddResponse(actual.getId(), item.getName(), 0, null), actual);
            need.add(new CategoryGetResponse(actual.getId(), item.getName(), 0, null));
        }

        List<CategoryAddRequest> subList = Arrays.asList(
                new CategoryAddRequest("Smartphones", need.get(0).getId()),
                new CategoryAddRequest("RPG", need.get(2).getId()));
        List<String> parentNames = Arrays.asList(need.get(0).getName(), need.get(2).getName());

        for(int i=0; i<subList.size(); ++i) {
            CategoryAddRequest item = subList.get(i);
            ResultActions res = mvc.perform(post("/api/categories")
                    .cookie(token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(item)));
            res.andExpect(status().isOk());
            MvcResult mvcRes = res.andReturn();
            String content = mvcRes.getResponse().getContentAsString();
            CategoryAddResponse actual = mapper.readValue(content, CategoryAddResponse.class);


            assertEquals(new CategoryAddResponse(actual.getId(), item.getName(), item.getParentId(), parentNames.get(i)), actual);
            need.add(new CategoryGetResponse(actual.getId(), item.getName(),  item.getParentId(), parentNames.get(i)));
        }

        ResultActions res = mvc.perform(get("/api/categories/").cookie(token));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();

        List<CategoryGetResponse> sortedNeedGetList = Arrays.asList(need.get(1), need.get(2), need.get(4), need.get(0), need.get(3));
        assertEquals(sortedNeedGetList, mapper.readValue(content, new TypeReference<List<CategoryGetResponse>>() {}));
    }
}




