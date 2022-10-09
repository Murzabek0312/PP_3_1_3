package ru.kata.spring.boot_security.demo.service;




import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface ServiceUser {
    public List<User> getAll();
    public User getUserbyId(int id);
    public boolean add(User user);
    public void edit(int id, User userUpdate);
    public void delete(int id);

    public List<Role> listRoles();
}
