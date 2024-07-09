package com.textrans.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  @Resource
  private StudentDao studentDao;


  public List<User> getStudentList(List<Integer> idList) {

    return studentDao.getStudentList(idList);
  }

  public List<User> getStudentList(Integer type) {
    return studentDao.getStudentList(new ArrayList<>(type));
  }

  public User getStudentById(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("id can not be null");
    }
    return studentDao.getStudentById(id);
  }


  public void login(String name, String password) {
    User user = studentDao.findUserByName(name);
    if (user == null) {
      throw new IllegalArgumentException(name + "不存在");
    }
    if (!Objects.equals(user.getPassword(), password)) {
      throw new IllegalArgumentException(name + "密码输入错误");
    }
  }

}
