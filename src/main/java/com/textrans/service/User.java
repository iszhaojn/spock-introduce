package com.textrans.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class User {

  private String name;

  /**
   * 1: teacher
   * 2: student
   */
  private Integer type;

  /**
   * 1: male
   * 2: female
   */
  private Integer gender;


  private LocalDate birthday;

  private String password;


  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public Integer getAge() {
    LocalDate now = LocalDate.now();
    if (birthday == null) {
      setBirthday(LocalDate.of(2006, 1, 1));
    }
    return (int) ChronoUnit.YEARS.between(birthday, now);
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
