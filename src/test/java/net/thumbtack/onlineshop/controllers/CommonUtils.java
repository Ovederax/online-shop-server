package net.thumbtack.onlineshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.cathegory.CategoryAddRequest;
import net.thumbtack.onlineshop.dto.request.product.ProductAddRequest;
import net.thumbtack.onlineshop.dto.request.user.AdministratorRegisterRequest;
import net.thumbtack.onlineshop.dto.request.user.ClientRegisterRequest;
import net.thumbtack.onlineshop.dto.response.cathegory.CategoryAddResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class CommonUtils {
    @Autowired private ObjectMapper mapper;

    public Cookie registerTestAdmin(MockMvc mvc) throws Exception {
        return registerAdmin(new Administrator(
                "first", "last", null,
                "position","admin", "root"), mvc);
    }

    public int addTestCategory(Cookie token, MockMvc mvc) throws Exception {
        return addCategory(new Category("Phones", null), token, mvc);
    }

    public Cookie registerAdmin(Administrator admin, MockMvc mvc) throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest(admin.getFirstname(),
                admin.getLastname(), admin.getPatronymic(), admin.getPosition(), admin.getLogin(),
                admin.getPassword());

        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,
                admin.getFirstname(), admin.getLastname(), admin.getPatronymic(), admin.getPosition());

        ResultActions res = mvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk())
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

    public int addCategory(Category category, Cookie token, MockMvc mvc) throws Exception {
        Integer parentId = 0;
        if(category.getParent() != null) {
            parentId = category.getParent().getId();
        }
        CategoryAddRequest req = new CategoryAddRequest(category.getName(), parentId);
        CategoryAddResponse respExpected = new CategoryAddResponse( 0, category.getName(), 0, null);

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
        return actual.getId();
    }

    public int addProduct(Product product, Cookie token, List<Integer> categories, MockMvc mvc) throws Exception {
        ProductAddRequest req = new ProductAddRequest(product.getName(), product.getPrice(), product.getCounter(), categories);

        ResultActions res = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(token));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ProductResponse actual = mapper.readValue(content, ProductResponse.class);

        assertEquals( new ProductResponse(actual.getId(), product.getName(), product.getPrice(), product.getCounter(), categories), actual);
        return actual.getId();
    }

    public Cookie registerTestClient(MockMvc mvc) throws Exception {
        return registerClient(new Client("first", "last", null, "email", "address", "phone", "user", "pass"), mvc);
    }

    public Cookie registerClient(Client client, MockMvc mvc) throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest(client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getLogin(), client.getPassword());
        ClientInfoResponse respExpected = new ClientInfoResponse(0, client.getFirstname(), client.getLastname(), client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), 0);

        ResultActions res = mvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"));
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        Cookie cookie = mvcRes.getResponse().getCookie("JAVASESSIONID");
        assertNotNull(cookie);
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
        return  cookie;
    }
}
