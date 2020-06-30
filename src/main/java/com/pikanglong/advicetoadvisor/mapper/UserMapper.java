package com.pikanglong.advicetoadvisor.mapper;

import com.pikanglong.advicetoadvisor.entity.AuthorityEntity;
import com.pikanglong.advicetoadvisor.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-25 23:26
 */
@Mapper
public interface UserMapper {
    void insertUser(UserEntity userEntity);

    void insertAuthority(AuthorityEntity authorityEntity);

    UserEntity selectUserByUsername(String username);

    UserEntity selectUserByUsernameAll(String username);

    AuthorityEntity selectAuthorityByUsername(String username);

    List<UserEntity> selectUsers();

    UserEntity selectUserByCollege(String college);

    void updateUser(UserEntity userEntity);

    List<UserEntity> selectColleges();

    void deleteAuthority(String username);

    void deleteUser(String username);

    void increaseCollegeCount(String college);
}
