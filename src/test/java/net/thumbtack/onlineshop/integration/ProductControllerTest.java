package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.product.AddProductRequest;
import net.thumbtack.onlineshop.dto.request.product.EditProductRequest;
import net.thumbtack.onlineshop.dto.response.basket.ProductInBasketResponse;
import net.thumbtack.onlineshop.dto.response.category.GetCategoryResponse;
import net.thumbtack.onlineshop.dto.response.product.GetProductResponse;
import net.thumbtack.onlineshop.dto.response.product.ProductResponse;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.utils.CommonRestUtils;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment =  SpringBootTest.WebEnvironment. DEFINED_PORT)
public class ProductControllerTest {
    private final String URL = ServerConstants.URL;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonRestUtils utils;

    @Before
    public void before() throws Exception {
        restTemplate.getForEntity(URL + "/api/debug/clear", String.class);
    }

    @Test
    public void addProductTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        AddProductRequest req = new AddProductRequest("product", 10, 12, null);
        ProductResponse respExpected = new ProductResponse(0,"product", 10, 12, new ArrayList<>());

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.set("Cookie", adminCookie);
        ResponseEntity<ProductResponse> res = restTemplate.exchange(URL+"/api/products", HttpMethod.POST, new HttpEntity<>(req, adminHeaders), ProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ProductResponse actual = res.getBody();
        respExpected.setId(actual.getId());

        assertEquals(respExpected, actual);
    }

    @Test
    public void getProductTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        int categoryId = utils.addTestCategory(adminCookie, restTemplate);

        AddProductRequest req = new AddProductRequest("product", 10, 12, new ArrayList<>(Collections.singletonList(categoryId)));

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.set("Cookie", adminCookie);

        ResponseEntity<ProductResponse> res = restTemplate.exchange(URL+"/api/products", HttpMethod.POST, new HttpEntity<>(req, adminHeaders), ProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ProductResponse actual = res.getBody();
        assertEquals( new ProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList(categoryId))), actual);

        ResponseEntity<GetProductResponse> res2 = restTemplate.exchange(URL+"/api/products/"+actual.getId(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), GetProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        GetProductResponse response = res2.getBody();
        assertEquals(response.getId(), actual.getId());
        assertEquals(new GetProductResponse(response.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList("Phones"))), response);
    }

    @Test
    public void updateProductTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        int categoryId = utils.addTestCategory(adminCookie, restTemplate);

        AddProductRequest req = new AddProductRequest("product", 10, 12, new ArrayList<>(Collections.singletonList(categoryId)));

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.set("Cookie", adminCookie);

        ResponseEntity<ProductResponse> res = restTemplate.exchange(URL+"/api/products", HttpMethod.POST, new HttpEntity<>(req, adminHeaders), ProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ProductResponse actual = res.getBody();
        assertEquals( new ProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList(categoryId))), actual);

        ResponseEntity<ProductResponse> res2 = restTemplate.exchange(URL+"/api/products/"+actual.getId(), HttpMethod.PUT,
                new HttpEntity<>(new EditProductRequest("superProduct", 3,7,null), adminHeaders), ProductResponse.class);
        assertEquals(HttpStatus.OK, res2.getStatusCode());
        ProductResponse response = res2.getBody();

        assertEquals(response.getId(), actual.getId());
        assertEquals(new ProductResponse(response.getId(),"superProduct", 3, 7, new ArrayList<>(Arrays.asList(categoryId))), response);

        ResponseEntity<GetProductResponse> res3 = restTemplate.exchange(URL+"/api/products/"+actual.getId(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), GetProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        GetProductResponse getProductResponse = res3.getBody();
        assertEquals(response.getId(), actual.getId());
        assertEquals(new GetProductResponse(response.getId(),"superProduct", 3, 7, new ArrayList<>(Arrays.asList("Phones"))), getProductResponse);
    }

    @Test
    public void deleteProductTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        int categoryId = utils.addTestCategory(adminCookie, restTemplate);

        AddProductRequest req = new AddProductRequest("product", 10, 12, new ArrayList<>(Collections.singletonList(categoryId)));

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.set("Cookie", adminCookie);

        ResponseEntity<ProductResponse> res = restTemplate.exchange(URL+"/api/products", HttpMethod.POST, new HttpEntity<>(req, adminHeaders), ProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        ProductResponse actual = res.getBody();
        assertEquals( new ProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList(categoryId))), actual);

        // Не удаляет продукт полностью, а помечает как удаленный
        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/products/"+actual.getId(), HttpMethod.DELETE, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(HttpStatus.OK, res2.getStatusCode());
        assertEquals("{}", res2.getBody());

        ResponseEntity<GetProductResponse> res3 = restTemplate.exchange(URL+"/api/products/"+actual.getId(), HttpMethod.GET, new HttpEntity<>("", adminHeaders), GetProductResponse.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        GetProductResponse getProductResponse = res3.getBody();
        assertEquals(new GetProductResponse(actual.getId(),"product", 10, 12, new ArrayList<>(Arrays.asList("Phones"))), getProductResponse);
    }

    @Test
    public void getProductsTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.set("Cookie", adminCookie);

        List<Category> categories = asList(
                new Category("phones", null),
                new Category("computers", null),
                new Category("electronics", null),
                new Category("home appliances", null)
        );

        for(Category it: categories) {
            utils.addCategory(it, adminCookie, restTemplate);
        }

        List<Category> subcategories = asList(
                new Category("mobile phone", categories.get(0)),
                new Category("phone cases", categories.get(0)),

                new Category("office electronics", categories.get(1)),
                new Category("computer components", categories.get(1)),
                new Category("computer peripherals", categories.get(1)),

                new Category("Photo and video cameras", categories.get(2)),

                new Category("kitchen appliances", categories.get(3)),
                new Category("home appliances", categories.get(3)),
                new Category("hygienic kit", categories.get(3))
        );

        for(Category it: subcategories) {
            utils.addCategory(it, adminCookie, restTemplate);
        }

        List<Product> products  = asList(
                new Product("Samsung Galaxy A70", 23_739, 10, singletonList(subcategories.get(0))),
                new Product("Elephone A5 6 GB 128 GB", 12_293,78, singletonList(subcategories.get(0))),
                //--

                new Product("Офис электронный калькулятор солнечный",1656,50,singletonList(subcategories.get(2))),
                //--
                new Product("Gaomon S620 6,5x4 дюйма Аниме",2369,50,singletonList(subcategories.get(4))),

                new Product("Обнаружения движения камера для входной двери",3967,50,singletonList(subcategories.get(5))),

                new Product("TINTON LIFE Вакуумные Упаковочные пакеты",294,50,singletonList(subcategories.get(6))),
                new Product("ANIMORE Портативный электрическая соковыжималка",2087,50, singletonList(subcategories.get(6))),
                new Product("COVACE пульт дистанционного управления ",7998,50,singletonList(subcategories.get(7))),
                new Product("TINTON LIFE Plug-in power профессиональная машинка",618,50, null)
        );

        for(Product it: products) {
            if(it.getCategories() != null) {
                utils.addProduct(it, adminCookie, singletonList(it.getCategories().get(0).getId()), restTemplate);
            } else {
                utils.addProduct(it, adminCookie, null, restTemplate);
            }
        }

        //---------------------Test------------------------
        //--------------Check categories list----------------
        ResponseEntity<String > res = restTemplate.exchange(URL+"/api/categories", HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<GetCategoryResponse> categoryResponse = mapper.readValue(res.getBody(), new TypeReference<List<GetCategoryResponse>>(){});
        categoryResponse.sort(Comparator.comparingInt(GetCategoryResponse::getId));

        assertEquals(13, categoryResponse.size());
        assertEquals("computers", categoryResponse.get(1).getName());
        assertEquals(0, categoryResponse.get(1).getParentId());
        assertEquals("hygienic kit", categoryResponse.get(12).getName());
        assertEquals("home appliances", categoryResponse.get(12).getParentName());

        //-----------------------default order, null categories-----------------------
        ResponseEntity<String > res2 = restTemplate.exchange(URL+"/api/products", HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        List<GetProductResponse> productResponses = mapper.readValue(res2.getBody(), new TypeReference<List<GetProductResponse>>(){});
        assertEquals(9, productResponses.size());
        // продукты отсортированные по имени
        assertEquals(new GetProductResponse(productResponses.get(0).getId(),
                "ANIMORE Портативный электрическая соковыжималка", 2087,50, asList("kitchen appliances")), productResponses.get(0));
        assertEquals(new GetProductResponse(productResponses.get(5).getId(),"TINTON LIFE Plug-in power профессиональная машинка", 618,50, asList()), productResponses.get(5));

        //-----------------------order category, null categories-----------------------
        ResponseEntity<String > res3 = restTemplate.exchange(URL+"/api/products?order=CATEGORY", HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        productResponses = mapper.readValue(res3.getBody(), new TypeReference<List<GetProductResponse>>(){});
        assertEquals(1, productResponses.size());
        assertEquals(new GetProductResponse(productResponses.get(0).getId(),"TINTON LIFE Plug-in power профессиональная машинка", 618,50, asList()), productResponses.get(0));

        //-----------------------product order, categories-----------------------
        String selectedCategories = categories.get(1).getId() + "," + subcategories.get(6).getId();
        ResponseEntity<String > res4 = restTemplate.exchange(URL+"/api/products?order=PRODUCT&categoriesId="+selectedCategories, HttpMethod.GET, new HttpEntity<>("", adminHeaders), String.class);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        productResponses = mapper.readValue(res4.getBody(), new TypeReference<List<GetProductResponse>>(){});
        assertEquals(2, productResponses.size());
        assertEquals(new GetProductResponse(productResponses.get(0).getId(),"ANIMORE Портативный электрическая соковыжималка", 2087,50, asList("kitchen appliances")), productResponses.get(0));
    }
}