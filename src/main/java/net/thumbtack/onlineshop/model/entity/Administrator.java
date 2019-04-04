package net.thumbtack.onlineshop.model.entity;




public class Administrator extends User{
    private String position;

    public Administrator(String firstname, String lastname, String patronymic, String position, String login, String password) {
        super(0, firstname, lastname, patronymic, login, password);
        this.position = position;
    }

    public Administrator() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
