package com.example.HibernateApp.controllers;

import com.example.HibernateApp.DTO.UserPhonesDTO;
import com.example.HibernateApp.models.Phone;
import com.example.HibernateApp.models.User;
import com.example.HibernateApp.repositories.PhoneRepository;
import com.example.HibernateApp.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;

    // GET method to fetch all phones
    @GetMapping("/users")
    public List<UserPhonesDTO> getAllUsersWithPhones() {
        List<User> users = userRepository.findAll();
        List<UserPhonesDTO> usersPhones = new ArrayList<>();

        for (User user : users) {
            UserPhonesDTO userPhones = new UserPhonesDTO();
            userPhones.setId(user.getId());
            userPhones.setName(user.getName());
            userPhones.setPhones(user.getPhones());
            usersPhones.add(userPhones);
        }

        return usersPhones;
    }

    // GET method to fetch phone by Id
    @GetMapping("/users/{id}")
    public ResponseEntity<UserPhonesDTO> getUserWithPhones(@PathVariable(value = "id") Long userId)
            throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User " + userId + " not found"));

        List<Phone> phones = user.getPhones();
        UserPhonesDTO userWithPhonesDTO = new UserPhonesDTO(user, phones);

        return ResponseEntity.ok().body(userWithPhonesDTO);
    }

    // POST method to create a phone
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }


    // PUT method to update a phone's details
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") Long userId, @Valid @RequestBody UserPhonesDTO userDetails
    ) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User " + userId + " not found"));

        user.setName(userDetails.getName());

        // Обновление телефонов пользователя
        List<Phone> updatedPhones = new ArrayList<>();
        for (Phone phone : userDetails.getPhones()) {
            if (phone.getId() != 0) {
                Phone existingPhone = phoneRepository.findById(phone.getId())
                        .orElseThrow(() -> new Exception("Phone " + phone.getId() + " not found"));
                existingPhone.setPhoneName(phone.getPhoneName());
                existingPhone.setOs(phone.getOs());
                updatedPhones.add(existingPhone);
            } else {
                phone.setUser(user);
                updatedPhones.add(phoneRepository.save(phone));
            }
        }

        user.setPhones(updatedPhones);

        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }


    // DELETE method to delete a phone
    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value="id") Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User " + userId + " not found"));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}