package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ServiceUserImpl implements ServiceUser {
    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServiceUserImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAll() {
        return usersRepository.findAll();
    }

    @Override
    public User getUserbyId(int id) {
        Optional <User> foundUser = usersRepository.findById(id);
        return foundUser.orElse(null);
    }

    public List<Role> listRoles(){
       return roleRepository.findAll();
    }

    @Transactional
    @Override
    public boolean add(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User userBD = usersRepository.findByUsername(user.getUsername());
        if(userBD!=null){
            return false;
        }
        System.out.println(user.getRoles());
        System.out.println(user.getRoles()==null);
        if(user.getRoles()==null) {
            user.setRoles(roleRepository.findRoleByName("ROLE_USER"));
        }
        user.setPassword(encodedPassword);
        usersRepository.save(user);
        return true;
    }
    @Transactional
    @Override
    public void edit(int id, User userUpdate) {
    userUpdate.setId(id);
    User userFromDb = getUserbyId(id);
        String passwordUserFromDb = userFromDb.getPassword();
        String passwordUserFromForm = userUpdate.getPassword();
        if(!passwordUserFromDb.equals(passwordUserFromForm)){
        String encodedPassword = passwordEncoder.encode(userUpdate.getPassword());
        userUpdate.setPassword(encodedPassword);}
    usersRepository.save(userUpdate);
    }
    @Transactional
    @Override
    public void delete(int id) {
    usersRepository.deleteById(id);
    }

    private static Collection <? extends GrantedAuthority> getAuthorities(User user){
        String[] userRoles = user.getRoles().stream().map((role)->  role.getName()).toArray(String[]::new);
        Collection <GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return  authorities;
    }



}
