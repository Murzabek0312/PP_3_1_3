package ru.kata.spring.boot_security.demo.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class UserValidator implements Validator {

    private final UserService userService;
    private final UsersRepository usersRepository;

    @Autowired
    public UserValidator(UserService userService, UsersRepository usersRepository){
        this.userService = userService;
        this.usersRepository = usersRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    User user = (User) target;
    User user1 = usersRepository.findByUsername(user.getName());
    try{
    userService.loadUserByUsername(user.getUsername());
    } catch (UsernameNotFoundException ignored){
     return;}
    if(user.getId()!= user1.getId())
    errors.rejectValue("username","","Человек с таким именем уже существует");
}
}
