package com.example.HibernateApp.DTO;

import com.example.HibernateApp.models.Phone;
import com.example.HibernateApp.models.User;
import lombok.Data;

import java.util.List;

@Data
public class UserPhonesDTO {
    private long id;
    private String name;
    private List<Phone> phones;

    public UserPhonesDTO(User user, List<Phone> phones) {
        this.id = user.getId();
        this.name = user.getName();
        this.phones = phones;
    }

    public UserPhonesDTO() {

    }
}
