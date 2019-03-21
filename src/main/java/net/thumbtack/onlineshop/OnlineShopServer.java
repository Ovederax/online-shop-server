package net.thumbtack.onlineshop;


//Администраторы магазина должны иметь возможность добавлять и
//удалять категории и товары.
//Клиенты должны иметь возможность формировать корзину клиента и
//покупать товары.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineShopServer {
    public static void main(String[] args) {
        SpringApplication.run(OnlineShopServer.class, args);
    }
}
