package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.GetClientsInfoResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment =  SpringBootTest.WebEnvironment. DEFINED_PORT)
public class UserControllerTest {
    @Autowired private UserDao userDao;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    private Gson gson = new Gson();

    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userDao.clearData();
    }

    @Test
    public void registerAdministratorTest() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("admName", "admLast", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,"admName", "admLast", null, "pos");

        ResultActions res = mvc.perform(post("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
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
    }

    @Test
    public void registerClientTest() throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest("cName", "cLast", null, "email", "address", "phone", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"cName", "cLast", null, "email", "address", "phone", 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("cName"))
                .andExpect(cookie().exists("JAVASESSIONID"));
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        Cookie cookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(cookie);
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }

    @Test
    public void logoutAndLoginClientTest() throws Exception {
        // register + login + getinfo
        ClientRegisterRequest req = new ClientRegisterRequest("cName", "cLast", null, "email", "address", "phone", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"cName", "cLast", null, "email", "address", "phone", 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("cName"))
                .andExpect(cookie().exists("JAVASESSIONID"));
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        Cookie token = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(token);
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        res = mvc.perform(delete("/api/sessions").cookie(token));
        res.andExpect(status().isOk());
        assertEquals("{}", res.andReturn().getResponse().getContentAsString());

        UserLoginRequest loginRequest = new UserLoginRequest("login", "pass");
        res = mvc.perform(post("/api/sessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(loginRequest)));
        res.andExpect(status().isOk());
        content = res.andReturn().getResponse().getContentAsString();

        token = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(token);
        actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }

    @Test
    public void clientsInfoTest() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("admName", "admLast", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0, "admName", "admLast", null, "pos");

        ResultActions res = mvc.perform(post("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
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

        // check
        res = mvc.perform(MockMvcRequestBuilders.get("/api/clients").cookie(cookie));
        content = res.andReturn().getResponse().getContentAsString();
        List<GetClientsInfoResponse> list = mapper.readValue(content, new TypeReference<List<GetClientsInfoResponse>>() {});
        assertEquals(0, list.size());

        //CLIENT REGISTER
        ClientRegisterRequest clientRegisterRequest = new ClientRegisterRequest("cName", "cLast", null, "email", "address", "phone", "login", "pass");
        ClientInfoResponse clientInfoResponse = new ClientInfoResponse(0, "cName", "cLast", null, "email", "address", "phone", 0);

        res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(clientRegisterRequest)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("cName"))
                .andExpect(cookie().exists("JAVASESSIONID"));
        mvcRes = res.andReturn();
        content = mvcRes.getResponse().getContentAsString();
        Cookie clientCookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(clientCookie);
        ClientInfoResponse actualClient = mapper.readValue(content, ClientInfoResponse.class);
        clientInfoResponse.setId(actualClient.getId());
        assertEquals(clientInfoResponse, actualClient);

        res = mvc.perform(MockMvcRequestBuilders.get("/api/clients").cookie(cookie));
        content = res.andReturn().getResponse().getContentAsString();
        list = mapper.readValue(content, new TypeReference<List<GetClientsInfoResponse>>() {});
        assertEquals(1, list.size());
    }

    @Test
    public void updateAdministratorProfileTest() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("admName", "admLast", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,"admName", "admLast", null, "pos");

        ResultActions res = mvc.perform(post("/api/admins").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
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

        AdministratorEditRequest admEditReq = new AdministratorEditRequest("adm", "adm", "patron", "pos2", "1234", "1");
        respExpected = new AdministratorInfoResponse(0,"adm", "adm", "patron", "pos2");
        res = mvc.perform(put("/api/admins").cookie(cookie).contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(admEditReq)));
        mvcRes = res.andExpect(status().isOk()).andReturn();
        actual = mapper.readValue(mvcRes.getResponse().getContentAsString(), AdministratorInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }

    @Test
    public void updateClientProfileTest() throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest("cName", "cLast", null, "email", "address", "phone", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"cName", "cLast", null, "email", "address", "phone", 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("cName"))
                .andExpect(cookie().exists("JAVASESSIONID"));
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        Cookie cookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(cookie);
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);

        ClientEditRequest admEditReq = new ClientEditRequest("1", "2", "3", "email2", "address2", "phone2", "pass", "newpass");
        respExpected = new ClientInfoResponse(0,"1", "2", "3", "email2", "address2", "phone2", 0);
        res = mvc.perform(put("/api/clients").cookie(cookie).contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(admEditReq)));
        mvcRes = res.andExpect(status().isOk()).andReturn();
        actual = mapper.readValue(mvcRes.getResponse().getContentAsString(), ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }
}