package net.thumbtack.onlineshop.dto.request.user;

public class ClientRegisterRequest {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String address;
    private String phone;
    private String login;
    private String password;

    public ClientRegisterRequest(String firstName, String lastName, String patronymic, String email, String address, String phone, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public ClientRegisterRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     Требования к логину, паролю клиента - те же,
     что и для администратора.
     E-mail должен соответствовать требованиям,
     предъявляемым к формату e-mail.
     Почтовый адрес записывается в произвольной форме. Он не может быть пустым, другие требования к нему не предъявляются.
     Допустимые телефонные номера - сотовые номера
     любых операторов России.
     Номер может начинаться как с “8”, так и с “+7”.
     Наличие в номере знаков “-” (дефис) ошибкой не является,
     но перед записью в БД все знаки “-” удаляются.
     Номера телефонов стационарной связи указывать не
     разрешается.
     Номер дома - не обязательно число (например, 20A).
     Номер квартиры (если присутствует) - всегда число.
     */
}
