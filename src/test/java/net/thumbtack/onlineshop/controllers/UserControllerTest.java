package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment =  SpringBootTest.WebEnvironment. DEFINED_PORT)
public class UserControllerTest {
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper mapper;
    private MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc.perform(get("/api/debug/clear")).andExpect(status().isOk());
    }

    @Test
    public void registerAdministratorTest() throws Exception {
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
    }

    @Test
    public void registerClientTest() throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("Петр"))
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
        ClientRegisterRequest req = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("Петр"))
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
        res = mvc.perform(post("/api/sessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(loginRequest)));
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

        // check
        res = mvc.perform(MockMvcRequestBuilders.get("/api/clients").cookie(cookie));
        content = res.andReturn().getResponse().getContentAsString();
        List<ClientInfo> list = mapper.readValue(content, new TypeReference<List<ClientInfo>>() {});
        assertEquals(0, list.size());

        //CLIENT REGISTER
        ClientRegisterRequest clientRegisterRequest = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse clientInfoResponse = new ClientInfoResponse(0, "Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(clientRegisterRequest)));
        res.andExpect(status().isOk())
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
        list = mapper.readValue(content, new TypeReference<List<ClientInfo>>() {});
        assertEquals(1, list.size());
    }

    @Test
    public void updateAdministratorProfileTest() throws Exception {
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

        AdministratorEditRequest admEditReq = new AdministratorEditRequest("adm", "adm", "patron", "pos2", "1234", "19999");
        respExpected = new AdministratorInfoResponse(0,"adm", "adm", "patron", "pos2");
        res = mvc.perform(put("/api/admins").cookie(cookie).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(admEditReq)));
        mvcRes = res.andExpect(status().isOk()).andReturn();
        actual = mapper.readValue(mvcRes.getResponse().getContentAsString(), AdministratorInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }

    @Test
    public void updateClientProfileTest() throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("Петр"))
                .andExpect(cookie().exists("JAVASESSIONID"));
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        Cookie cookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(cookie);
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);

        ClientEditRequest admEditReq = new ClientEditRequest("Илья", "Смирнов", "ЁдритьКолотить", "user2@gmail.com", "address2", "+76666666666", "pass", "newpass");
        respExpected = new ClientInfoResponse(0,"Илья", "Смирнов", "ЁдритьКолотить", "user2@gmail.com", "address2", "+76666666666", 0);
        res = mvc.perform(put("/api/clients").cookie(cookie).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(admEditReq)));
        mvcRes = res.andExpect(status().isOk()).andReturn();
        actual = mapper.readValue(mvcRes.getResponse().getContentAsString(), ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }
}