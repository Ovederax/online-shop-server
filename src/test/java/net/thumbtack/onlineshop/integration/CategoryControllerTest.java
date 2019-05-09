package net.thumbtack.onlineshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.dto.request.category.AddCategoryRequest;
import net.thumbtack.onlineshop.dto.request.category.EditCategoryRequest;
import net.thumbtack.onlineshop.dto.response.ErrorContent;
import net.thumbtack.onlineshop.dto.response.ErrorResponse;
import net.thumbtack.onlineshop.dto.response.category.AddCategoryResponse;
import net.thumbtack.onlineshop.dto.response.category.EditCategoryResponse;
import net.thumbtack.onlineshop.dto.response.category.GetCategoryResponse;
import net.thumbtack.onlineshop.dto.response.user.ClientInfoResponse;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CategoryControllerTest {
    private final String URL = ServerConstants.URL;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper mapper;
    @Autowired private CommonRestUtils utils;

    @Before
    public void before() throws Exception {
        restTemplate.getForEntity(URL + "/api/debug/clear", String.class);
    }

    @Test
    public void addCategoryTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        AddCategoryRequest req = new AddCategoryRequest("Phones", null);
        AddCategoryResponse respExpected = new AddCategoryResponse( 0, "Phones", 0, null);

        HttpHeaders adminHeader = new HttpHeaders();
        adminHeader.setContentType(MediaType.APPLICATION_JSON);
        adminHeader.set("Cookie", adminCookie);

        ResponseEntity<AddCategoryResponse> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(req, adminHeader), AddCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        AddCategoryResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
    }


    @Test
    public void getCategoryTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        AddCategoryRequest req = new AddCategoryRequest("Phones", null);
        AddCategoryResponse respExpected = new AddCategoryResponse( 0, "Phones", 0, null);

        HttpHeaders adminHeader = new HttpHeaders();
        adminHeader.setContentType(MediaType.APPLICATION_JSON);
        adminHeader.set("Cookie", adminCookie);

        ResponseEntity<AddCategoryResponse> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(req, adminHeader), AddCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        AddCategoryResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);


        int id = actual.getId();
        ResponseEntity<AddCategoryResponse> res2 = restTemplate.exchange(URL+"/api/categories/"+id, HttpMethod.GET, new HttpEntity<>("", adminHeader), AddCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        actual = res2.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);
        assertEquals(new AddCategoryResponse( id, "Phones", 0, null), actual);
    }

    @Test
    public void updateCategoryTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        AddCategoryRequest req = new AddCategoryRequest("Phones", null);
        AddCategoryResponse respExpected = new AddCategoryResponse( 0, "Phones", 0, null);

        HttpHeaders adminHeader = new HttpHeaders();
        adminHeader.setContentType(MediaType.APPLICATION_JSON);
        adminHeader.set("Cookie", adminCookie);

        ResponseEntity<AddCategoryResponse> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(req, adminHeader), AddCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        AddCategoryResponse actual = res.getBody();
        respExpected.setId(actual.getId());
        assertEquals(respExpected, actual);

        int id = actual.getId();

        ResponseEntity<EditCategoryResponse> res2 = restTemplate.exchange(URL+"/api/categories/"+id, HttpMethod.PUT,
                new HttpEntity<>(new EditCategoryRequest("Telefones", null), adminHeader), EditCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);

        assertEquals(new EditCategoryResponse( id, "Telefones", 0, null),  res2.getBody());

        ResponseEntity<GetCategoryResponse> res3 = restTemplate.exchange(URL+"/api/categories/"+id, HttpMethod.GET, new HttpEntity<>("", adminHeader), GetCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(new GetCategoryResponse( id, "Telefones", 0, null), res3.getBody());
    }

    @Test
    public void deleteCategoryTest() throws Exception  {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        AddCategoryRequest req = new AddCategoryRequest("Phones", null);


        HttpHeaders adminHeader = new HttpHeaders();
        adminHeader.setContentType(MediaType.APPLICATION_JSON);
        adminHeader.set("Cookie", adminCookie);

        ResponseEntity<AddCategoryResponse> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(req, adminHeader), AddCategoryResponse.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        AddCategoryResponse actual = res.getBody();
        assertEquals(new AddCategoryResponse( actual.getId(), "Phones", 0, null), actual);

        int id = actual.getId();

        ResponseEntity<String> res2 = restTemplate.exchange(URL+"/api/categories/"+id, HttpMethod.DELETE, new HttpEntity<>("", adminHeader), String.class);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals("{}", res2.getBody());

        ResponseEntity<ErrorResponse> res3 = restTemplate.exchange(URL+"/api/categories/"+id, HttpMethod.GET, new HttpEntity<>("", adminHeader), ErrorResponse.class);
        assertEquals(res3.getStatusCode(), HttpStatus.BAD_REQUEST);

        ErrorCode ex = ErrorCode.CATEGORY_NO_EXISTS;
        assertEquals(new ErrorResponse(Arrays.asList(new ErrorContent(
                ex.getErrorCode(), ex.getField(), ex.getMessage()))),res3.getBody());
    }

    @Test
    public void getCategoriesTest() throws Exception {
        String adminCookie = utils.registerTestAdmin(restTemplate);
        List<AddCategoryRequest> list = Arrays.asList(
                new AddCategoryRequest("Phones", null),
                new AddCategoryRequest("Dress", null),
                new AddCategoryRequest("Games", null));

        HttpHeaders adminHeader = new HttpHeaders();
        adminHeader.setContentType(MediaType.APPLICATION_JSON);
        adminHeader.set("Cookie", adminCookie);

        List<GetCategoryResponse> need = new ArrayList<>(5);
        for(AddCategoryRequest item : list) {
            ResponseEntity<AddCategoryResponse> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(item, adminHeader), AddCategoryResponse.class);
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            AddCategoryResponse actual = res.getBody();

            assertEquals(new AddCategoryResponse(actual.getId(), item.getName(), 0, null), actual);
            need.add(new GetCategoryResponse(actual.getId(), item.getName(), 0, null));
        }

        List<AddCategoryRequest> subList = Arrays.asList(
                new AddCategoryRequest("Smartphones", need.get(0).getId()),
                new AddCategoryRequest("RPG", need.get(2).getId()));
        List<String> parentNames = Arrays.asList(need.get(0).getName(), need.get(2).getName());

        for(int i=0; i<subList.size(); ++i) {
            AddCategoryRequest item = subList.get(i);
            ResponseEntity<AddCategoryResponse> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.POST, new HttpEntity<>(item, adminHeader), AddCategoryResponse.class);
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            AddCategoryResponse actual = res.getBody();

            assertEquals(new AddCategoryResponse(actual.getId(), item.getName(), item.getParentId(), parentNames.get(i)), actual);
            need.add(new GetCategoryResponse(actual.getId(), item.getName(),  item.getParentId(), parentNames.get(i)));
        }

        ResponseEntity<String> res = restTemplate.exchange(URL+"/api/categories", HttpMethod.GET, new HttpEntity<>("", adminHeader), String.class);
        String content = res.getBody();

        List<GetCategoryResponse> sortedNeedGetList = Arrays.asList(need.get(1), need.get(2), need.get(4), need.get(0), need.get(3));
        assertEquals(sortedNeedGetList, mapper.readValue(content, new TypeReference<List<GetCategoryResponse>>() {}));
    }
}




