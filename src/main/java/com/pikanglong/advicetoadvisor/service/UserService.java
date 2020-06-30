package com.pikanglong.advicetoadvisor.service;

import com.pikanglong.advicetoadvisor.entity.UserEntity;

import java.security.Principal;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-26 0:23
 */
public interface UserService {
    void addAdmin(UserEntity userEntity);

    void addUser(UserEntity userEntity);

    String getAuthority(Principal principal);

    List<UserEntity> getUsersAll();

    UserEntity getUserByUsername(String username);

    UserEntity getUserByUsernameFromAll(String username);

    UserEntity getUserByCollege(String college);

    void updateUser(UserEntity userEntity);

    List<String> getCollegesAll();

    void deleteUser(String username);

    void increaseCollegeCount(String college);

    List<UserEntity> getCollegesEntites();
}
