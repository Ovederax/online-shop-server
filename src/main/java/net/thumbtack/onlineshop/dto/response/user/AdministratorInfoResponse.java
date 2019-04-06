package net.thumbtack.onlineshop.dto.response.user;

import java.util.Objects;

public class AdministratorInfoResponse extends UserInfoResponse{
    private String position;

    public AdministratorInfoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        super(id, firstName, lastName, patronymic);
        this.position = position;
    }

    public AdministratorInfoResponse() {
        super();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdministratorInfoResponse that = (AdministratorInfoResponse) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position);
    }
}
