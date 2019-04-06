package net.thumbtack.onlineshop.dto.response.user;

import java.util.Objects;

public class UserLoginResponse {
    private String token;
    private UserInfoResponse userInfo;  // хмм, нормально ли это

    public UserLoginResponse(String token, UserInfoResponse userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoResponse userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLoginResponse)) return false;
        UserLoginResponse that = (UserLoginResponse) o;
        return Objects.equals(getToken(), that.getToken());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getToken());
    }
}
