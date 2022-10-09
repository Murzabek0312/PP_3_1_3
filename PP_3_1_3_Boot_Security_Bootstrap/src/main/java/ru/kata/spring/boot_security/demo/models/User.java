package ru.kata.spring.boot_security.demo.models;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Transient
    private User user;


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2,max = 100,message = "Имя должно быть от 2 до 100 символов")
    @Column(name = "username")
    private String username;
    @NotEmpty(message = "Фамилие не должно быть пустым")
    @Size(min = 2,max = 100,message = "Фамилие должно быть от 2 до 100 символов")
    @Column(name = "surname")
    private String surname;
    @Min(value = 16, message = "Возраст должен быть от 16 лет")
    @Column(name = "age")
    private int age;
    @Column(name = "password")
    private String password;


    @ManyToMany
    @JoinTable(name = "users_roles",
               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))

    private List<Role> roles;

    public User getUser() {
        return user;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "User{" +
                " id=" + id +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public Collection<Role> getRoles() {

        return roles;}

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setRoles(Role role) {
        if(this.roles==null){
             this.roles = new ArrayList<>();
        }
          this.roles.add(role);
    }

    public User(int id, String name, String surname, int age) {
        this.id = id;
        this.username = name;
        this.surname = surname;
        this.age = age;
    }



    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList( new SimpleGrantedAuthority(user.getRoles().toString()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    public User getName(String username) {
        return this.user;
    }

    public String getStringUserAuthorities() {
        StringBuilder s = new StringBuilder();
        for (GrantedAuthority g : roles) {
            s.append(g);
        }
        return s.toString().replace("ROLE_", " ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && age == user.age && Objects.equals(username, user.username) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(password, user.password) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, surname, age, password, roles);
    }
}
