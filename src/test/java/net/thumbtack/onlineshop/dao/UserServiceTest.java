package net.thumbtack.onlineshop.dao;

import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.dao.UserDao;
import net.thumbtack.onlineshop.database.daoimpl.UserDaoImpl;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.service.UserService;
import org.junit.Before;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UserServiceTest {
    private UserService userService;

    @Before
    public void before() {

    }

    @Test
    public void registerAdministrator() throws ServerException {
        //userDao.registerAdministrator(new Administrator("Ivan", "Petrov", "Ivanovich", "Manager", "proman", "pass"));
    }

    @Test
    public void registerClient() throws ServerException {

        //ClientRegisterRequest req = new ClientRegisterRequest("first", "last", "patron", "email", "address", "phone", "login", "pass");
        //userService.registerClient(req);
        //userDao.registerClient(new ClientDTO("first", "last", "patron", "email", "address", "phone", "login", "pass"));
        /*List<Client> clients = userDao.findAllClients();/**/

    }

    @Test
    public void login() {
        //ClientRegisterRequest req = new ClientRegisterRequest("first", "last", "patron", "email", "address", "phone", "login", "pass");
    }

    @Test
    public void getUserInfo() {
    }

    @Test
    public void getClientInfo() {
    }

    @Test
    public void editAdministratorProfile() {
    }

    @Test
    public void editClientProfile() {
    }
}