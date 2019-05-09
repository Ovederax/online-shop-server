package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.user.*;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfo;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
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
    private final String URL = ServerConstants.URL;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonUtils utils;
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
    public void registerAdministratorTest() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("имя", "фамилия", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,"имя", "фамилия", null, "pos");

        ResponseEntity<AdministratorInfoResponse> res = restTemplate.exchange(URL+"/api/admins", HttpMethod.POST, new HttpEntity<>(req, headers), AdministratorInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0).split("JAVASESSIONID=")[1];
        assertNotNull(cookie);

        AdministratorInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }

    @Test
    public void registerClientTest() throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        ResponseEntity<ClientInfoResponse> res = restTemplate.exchange(URL+"/api/clients", HttpMethod.POST, new HttpEntity<>(req, headers), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0).split("JAVASESSIONID=")[1];

        assertNotNull(cookie);
        ClientInfoResponse actual = res.getBody();

        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }

    @Test
    public void logoutAndLoginClientTest() throws Exception {
        // register + login + getinfo
        ClientRegisterRequest req = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);


        ResponseEntity<ClientInfoResponse> res = restTemplate.exchange(URL+"/api/clients", HttpMethod.POST, new HttpEntity<>(req, headers), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);

        ClientInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookie);
        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/sessions", HttpMethod.DELETE, new HttpEntity<>("", headers), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals("{}", res2.getBody());

        UserLoginRequest loginRequest = new UserLoginRequest("login", "pass");
        res = restTemplate.exchange(URL+"/api/sessions", HttpMethod.POST, new HttpEntity<>(loginRequest, headers), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);
        actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }

    @Test
    public void clientsInfoTest() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("имя", "фамилия", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0, "имя", "фамилия", null, "pos");

        ResponseEntity<AdministratorInfoResponse> res = restTemplate.exchange(URL+"/api/admins", HttpMethod.POST, new HttpEntity<>(req, headers), AdministratorInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);

        AdministratorInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.add("Cookie", cookie);
        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/clients", HttpMethod.GET, new HttpEntity<>(req, adminHeaders), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String content = res2.getBody();
        List<ClientInfo> list = mapper.readValue(content, new TypeReference<List<ClientInfo>>() {});
        assertEquals(0, list.size());


        //CLIENT REGISTER
        ClientRegisterRequest clientRegisterRequest = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse clientInfoResponse = new ClientInfoResponse(0, "Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        ResponseEntity<ClientInfoResponse> res3 = restTemplate.exchange(URL+"/api/clients", HttpMethod.POST, new HttpEntity<>(clientRegisterRequest, headers), ClientInfoResponse.class);
        assertEquals(res3.getStatusCode(), HttpStatus.OK);
        String clientCookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(clientCookie);
        ClientInfoResponse actualClient = res3.getBody();
        clientInfoResponse.setId(actualClient.getId());
        assertEquals(clientInfoResponse, actualClient);


        ResponseEntity<String> res4 = restTemplate.exchange(URL+"/api/clients", HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(res4.getStatusCode(), HttpStatus.OK);
        list = mapper.readValue(res4.getBody(), new TypeReference<List<ClientInfo>>() {});
        assertEquals(1, list.size());
    }

    @Test
    public void updateAdministratorProfileTest() throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest("имя", "фамилия", null, "pos", "admin", "1234");
        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,"имя", "фамилия", null, "pos");

        ResponseEntity<AdministratorInfoResponse> res = restTemplate.exchange(URL+"/api/admins", HttpMethod.POST, new HttpEntity<>(req, headers), AdministratorInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);

        AdministratorInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.add("Cookie", cookie);
        AdministratorEditRequest admEditReq = new AdministratorEditRequest("Степан", "Федоров", "иванович", "pos2", "1234", "19999");
        respExpected = new AdministratorInfoResponse(0,"Степан", "Федоров", "иванович", "pos2");
        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/admins", HttpMethod.PUT, new HttpEntity<>(admEditReq, adminHeaders), String.class);
        actual = mapper.readValue(res2.getBody(), AdministratorInfoResponse.class);
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }

    @Test
    public void updateClientProfileTest() throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest("Петр", "Петров", null, "user@gmail.com", "address", "+7-999-4455445", "login", "pass");
        ClientInfoResponse respExpected = new ClientInfoResponse(0,"Петр", "Петров", null, "user@gmail.com", "address", "+79994455445", 0);

        ResponseEntity<ClientInfoResponse> res = restTemplate.exchange(URL+"/api/clients", HttpMethod.POST, new HttpEntity<>(req, headers), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);
        ClientInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        ClientEditRequest admEditReq = new ClientEditRequest("Илья", "Смирнов", "ЁдритьКолотить", "user2@gmail.com", "address2", "+76666666666", "pass", "newpass");
        respExpected = new ClientInfoResponse(0,"Илья", "Смирнов", "ЁдритьКолотить", "user2@gmail.com", "address2", "+76666666666", 0);

        HttpHeaders clientHeaders = new HttpHeaders();
        clientHeaders.setContentType(MediaType.APPLICATION_JSON);
        clientHeaders.add("Cookie", cookie);
        res = restTemplate.exchange(URL+"/api/clients", HttpMethod.PUT, new HttpEntity<>(admEditReq, clientHeaders), ClientInfoResponse.class);
        actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }
}