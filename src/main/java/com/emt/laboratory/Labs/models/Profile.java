package com.emt.laboratory.Labs.models;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    private Long profileid;


    private String username;


    private String email;


    private String password;

    private boolean isActive;

    private Role role;

    @OneToOne(fetch = FetchType.LAZY,
             cascade =  CascadeType.ALL,
             mappedBy = "profile")
    private ConfirmationToken confirmationToken;



    @Override
    public String toString() {
        return "Profile{" +
                "id=" + profileid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + isActive +
                ", role=" + role +
                '}';
    }


    public Profile() {
    }



    public Profile(String name, String email, String password) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.isActive = false;
        this.role = Role.User;
    }



    public Long getId() {
        return profileid;
    }

    public void setId(Long id) {
        this.profileid = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        this.isActive = true;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



}
