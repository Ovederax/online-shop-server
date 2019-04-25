package net.thumbtack.onlineshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties
public class ServerProperties {
    private int rest_http_port;
    private int max_name_length;
    private int min_password_length;

    public ServerProperties() {
    }


    public int getRest_http_port() {
        return rest_http_port;
    }

    public void setRest_http_port(int rest_http_port) {
        this.rest_http_port = rest_http_port;
    }

    public int getMax_name_length() {
        return max_name_length;
    }

    public void setMax_name_length(int max_name_length) {
        this.max_name_length = max_name_length;
    }

    public int getMin_password_length() {
        return min_password_length;
    }

    public void setMin_password_length(int min_password_length) {
        this.min_password_length = min_password_length;
    }
}
