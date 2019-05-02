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

import javax.servlet.http.Cookie;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class CommonRestUtils {
    @Autowired private ObjectMapper mapper;
    String URL = ServerConstants.URL;
    private static Client testClient = new Client("Иван", "Иванов", null, "user@gmail.com", "address", "8-913-666-88-99", "user", "pass");
    private static Administrator testAdmin = new Administrator("Егор", "Иванов", null,"position","admin", "root");

    public String registerTestClient(TestRestTemplate rest) throws Exception {
        return registerClient(testClient, rest);
    }

    public String registerClient(Client client, TestRestTemplate restTemplate) throws Exception {
        ClientRegisterRequest req = new ClientRegisterRequest(client.getFirstname(), client.getLastname(),
                client.getPatronymic(), client.getEmail(), client.getAddress(), client.getPhone(), client.getLogin(), client.getPassword());
        ClientInfoResponse respExpected = new ClientInfoResponse(0, client.getFirstname(), client.getLastname(), client.getPatronymic(), client.getEmail(), client.getAddress(), convertPhoneFormat(client.getPhone()), 0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ClientInfoResponse> res = restTemplate.exchange(URL+"/api/clients", HttpMethod.POST, new HttpEntity<>(req, headers), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        res.getHeaders();
        String cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);
        ClientInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
        client.setId(actual.getId());
        return cookie;
    }

    public String registerTestAdmin(TestRestTemplate rest) throws Exception {
        return registerAdmin(testAdmin, rest);
    }

    public String registerAdmin(Administrator admin, TestRestTemplate rest) throws Exception {
        AdministratorRegisterRequest req = new AdministratorRegisterRequest(admin.getFirstname(),
                admin.getLastname(), admin.getPatronymic(), admin.getPosition(), admin.getLogin(),
                admin.getPassword());

        AdministratorInfoResponse respExpected = new AdministratorInfoResponse(0,
                admin.getFirstname(), admin.getLastname(), admin.getPatronymic(), admin.getPosition());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<AdministratorInfoResponse> res = rest.exchange(URL+"/api/admins", HttpMethod.POST, new HttpEntity<>(req, headers), AdministratorInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        String cookie = res.getHeaders().get("Set-Cookie").get(0);
        assertNotNull(cookie);

        AdministratorInfoResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        return cookie;
    }

    public int addTestCategory(String cookie, TestRestTemplate rest) throws Exception {
        return addCategory(new Category("Phones", null), cookie, rest);
    }

    public int addCategory(Category category, String cookie, TestRestTemplate rest) throws Exception {
        Integer parentId = 0;
        if(category.getParent() != null) {
            parentId = category.getParent().getId();
        }
        AddCategoryRequest req = new AddCategoryRequest(category.getName(), parentId);
        AddCategoryResponse respExpected = new AddCategoryResponse( 0, category.getName(), 0, null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookie);

        ResponseEntity<AddCategoryResponse> res = rest.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(req, headers), AddCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);

        AddCategoryResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
        category.setId(actual.getId());
        return actual.getId();
    }

    public int addProduct(Product product, String cookie, List<Integer> categories, TestRestTemplate rest) throws Exception {
        AddProductRequest req = new AddProductRequest(product.getName(), product.getPrice(), product.getCounter(), categories);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookie);

        ResponseEntity<ProductResponse> res = rest.exchange(URL+"/api/products", HttpMethod.POST, new HttpEntity<>(req, headers), ProductResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        ProductResponse actual = res.getBody();
        assertEquals( new ProductResponse(actual.getId(), product.getName(), product.getPrice(), product.getCounter(), categories), actual);
        product.setId(actual.getId());
        return actual.getId();
    }

    public void insertTestDataInBD(String cookie, TestRestTemplate rest) throws Exception {
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
            it.setId(addCategory(it, cookie, rest));
        }

        for(Product it: products) {
            addProduct(it, cookie, singletonList(it.getCategories().get(0).getId()), rest);
        }
    }

    public void addDepositMoney(int money, String clientCookie, TestRestTemplate rest) throws Exception {
        DepositMoneyRequest req = new DepositMoneyRequest(String.valueOf(money));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", clientCookie);

        ResponseEntity<ClientInfoResponse> res = rest.exchange(URL+"/api/deposits", HttpMethod.POST, new HttpEntity<>(req, headers), ClientInfoResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);

        ClientInfoResponse actual = res.getBody();
        assertTrue(actual.getDeposit() >= money);
    }

    private String convertPhoneFormat(String phone) {
        return phone.replaceAll("-", "");
    }
}
