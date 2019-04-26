// REVU request ? response ? move to appropriate package
package net.thumbtack.onlineshop.dto;

import net.thumbtack.onlineshop.model.entity.Administrator;
import net.thumbtack.onlineshop.model.entity.Client;

public class UserInfo {
    private Administrator admin;
    private Client client;

    public UserInfo(Administrator admin) {
        this.admin = admin;
    }

    public UserInfo(Client clientDTO) {
        this.client = clientDTO;
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
