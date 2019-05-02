package net.thumbtack.onlineshop.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.category.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.product.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.user.AdministratorRegisterRequest;
import net.thumbtack.onlineshop.dto.request.user.ClientRegisterRequest;
import net.thumbtack.onlineshop.dto.request.user.DepositMoneyRequest;
import net.thumbtack.onlineshop.dto.response.category.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.dto.response.user.AdministratorInfoResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.integration.ServerConstants;
import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class CommonUtils {
    @Autowired private ObjectMapper mapper;
    private static Client testClient = new Client("Иван", "Иванов", null, "user@gmail.com", "address", "8-913-666-88-99", "user", "pass");
    private static Administrator testAdmin = new Administrator("Егор", "Иванов", null,"position","admin", "root");

    //------------------MOCK MVC--------------------
    public Cookie registerTestAdmin(MockMvc mvc) throws Exception {
        return registerAdmin(testAdmin, mvc);
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
        AddCategoryRequest req = new AddCategoryRequest(category.getName(), parentId);
        AddCategoryResponse respExpected = new AddCategoryResponse( 0, category.getName(), 0, null);

        ResultActions res = mvc.perform(post("/api/categories")
                .cookie(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req)));
        res.andExpect(status().isOk());
        MvcResult mvcRes = res.andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        AddCategoryResponse actual = mapper.readValue(content, AddCategoryResponse.class);
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
        return actual.getId();
    }

    public int addProduct(Product product, Cookie token, List<Integer> categories, MockMvc mvc) throws Exception {
        AddProductRequest req = new AddProductRequest(product.getName(), product.getPrice(), product.getCounter(), categories);

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
        return registerClient(testClient, mvc);
    }

    public Cookie registerClient(Client client, MockMvc mvc) throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest(client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getLogin(), client.getPassword());
        ClientInfoResponse respExpected = new ClientInfoResponse(0, client.getFirstname(), client.getLastname(), client.getPatronymic(), client.getEmail(), client.getAddress(), convertPhoneFormat(client.getPhone()), 0);

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

    public void insertTestDataInBD(MockMvc mvc) throws Exception {
        Cookie token = registerTestAdmin(mvc);

        List<Category> categories = asList(
            new Category("phone", null),
            new Category("dress", null),
            new Category("clock", null),
            new Category("calculator", null)
        );
        List<Product> products  = asList(
            new Product("Iphone X666", 66600, 10, singletonList(categories.get(0))),
            new Product("MegaShorts", 700,100, singletonList(categories.get(1))),
            new Product("TC12-90", 300,30, singletonList(categories.get(2))),
            new Product("CALCULUS-92",100,50,singletonList(categories.get(3))
        ));

        for(Category it: categories) {
            it.setId(addCategory(it, token, mvc));
        }

        for(Product it: products) {
            addProduct(it, token, singletonList(it.getCategories().get(0).getId()), mvc);
        }


    }

    public void addDepositMoney(int money, Cookie clientToken, MockMvc mvc) throws Exception {
        DepositMoneyRequest req = new DepositMoneyRequest(String.valueOf(money));

        ResultActions res = mvc.perform(post("/api/deposits")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(req))
                .cookie(clientToken));

        MvcResult mvcRes = res.andExpect(status().isOk()).andReturn();
        String content = mvcRes.getResponse().getContentAsString();
        ClientInfoResponse actual = mapper.readValue(content, ClientInfoResponse.class);

        assertTrue(actual.getDeposit() >= money);
    }

    private String convertPhoneFormat(String phone) {
        return phone.replaceAll("-", "");
    }
}
