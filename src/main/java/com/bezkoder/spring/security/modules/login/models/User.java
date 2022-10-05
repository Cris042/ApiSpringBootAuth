package com.bezkoder.spring.security.modules.login.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table
(
    name = "users",
    uniqueConstraints = 
    {
       @UniqueConstraint(columnNames = "username"),
       @UniqueConstraint(columnNames = "email")
    }
)

public class User 
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 120)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotBlank
  @Size(max = 20)
  private String course;
  
  @NotBlank
  @Size(max = 20)
  private String athletic;

  @NotBlank
  @Size(max = 20)
  private String number;

  @NotBlank
  @Size(max = 20)
  private String insta;

  @NotBlank
  private String profilePicture;
  
  @NotBlank
  private String status;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User() 
  {
  }

  public User
  (
    String username, String email, String password, String name, String athletic, String course, 
    String insta, String number, String status, String profilePicture
  ) 
  {
    this.username = username;
    this.email = email;
    this.password = password;
    this.name = name;
    this.athletic = athletic;
    this.course = course;
    this.insta = insta;
    this.number = number;
    this.status = status;
    this.profilePicture = profilePicture;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  public String getAthletic() {
    return athletic;
  }

  public void setAthletic(String athletic) {
    this.athletic = athletic;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getInsta() {
    return insta;
  }

  public void setInsta(String insta) {
    this.insta = insta;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

}
