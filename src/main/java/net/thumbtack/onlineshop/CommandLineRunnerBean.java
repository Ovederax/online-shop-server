package net.thumbtack.onlineshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CommandLineRunnerBean implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineRunner.class);
    public void run(String... args) {
        String strArgs = Arrays.stream(args).collect(Collectors.joining("|"));
        LOGGER.info("Application started with arguments:" + strArgs);

//        if(args.length == 0) {
//            throw new RuntimeException();
//        }
//        ApplicationContext ctx = new ClassPathXmlApplicationContext(args[0]);

    }
}