package com.example.nanuri.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String oauthId;

    private String name;

    private String email;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String oauthId, String name, String email, String imageUrl, Role role) {
        this(null,oauthId,name,email,imageUrl,role);
    }

    public User update(String name, String email, String imageUrl){
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        return  this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", oauthId='" + oauthId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role=" + role +
                '}';
    }
}
