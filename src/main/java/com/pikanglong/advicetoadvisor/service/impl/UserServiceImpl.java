package com.pikanglong.advicetoadvisor.service.impl;

import com.pikanglong.advicetoadvisor.entity.AdvisorEntity;
import com.pikanglong.advicetoadvisor.entity.AuthorityEntity;
import com.pikanglong.advicetoadvisor.entity.UserEntity;
import com.pikanglong.advicetoadvisor.mapper.AdvisorMapper;
import com.pikanglong.advicetoadvisor.mapper.UserMapper;
import com.pikanglong.advicetoadvisor.service.AdvisorService;
import com.pikanglong.advicetoadvisor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-26 0:24
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdvisorMapper advisorMapper;

    @Autowired
    private AdvisorService advisorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void addAdmin(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setEnabled(true);
        userMapper.insertUser(userEntity);
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUsername("admin");
        authorityEntity.setAuthority("ROLE_ADMIN");
        userMapper.insertAuthority(authorityEntity);
    }

    @Override
    @Transactional
    public void addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCount(0);
        userMapper.insertUser(userEntity);
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUsername(userEntity.getUsername());
        authorityEntity.setAuthority("ROLE_USER");
        userMapper.insertAuthority(authorityEntity);
    }

    @Override
    public String getAuthority(Principal principal) {
        String username = principal.getName();
        AuthorityEntity authorityEntity = userMapper.selectAuthorityByUsername(username);
        String authority = authorityEntity.getAuthority();
        return authority;
    }

    @Override
    public List<UserEntity> getUsersAll() {
        List<UserEntity> userEntities = userMapper.selectUsers();
        return userEntities;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        UserEntity userEntity = userMapper.selectUserByUsername(username);
        return userEntity;
    }

    @Override
    public UserEntity getUserByUsernameFromAll(String username) {
        UserEntity userEntity = userMapper.selectUserByUsernameAll(username);
        return userEntity;
    }

    @Override
    public UserEntity getUserByCollege(String college) {
        UserEntity userEntity = userMapper.selectUserByCollege(college);
        return userEntity;
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userMapper.updateUser(userEntity);
    }

    @Override
    public List<String> getCollegesAll() {
        List<UserEntity> userEntities = userMapper.selectColleges();
        List<String> colleges = new ArrayList<>();
        for(UserEntity u: userEntities) {
            colleges.add(u.getCollege());
        }
        return colleges;
    }

    @Override
    public void deleteUser(String username) {
        UserEntity userEntity = userMapper.selectUserByUsername(username);
        List<AdvisorEntity> advisorEntities = advisorMapper.selectAdvisorsByCollege(userEntity.getCollege());
        for(AdvisorEntity a : advisorEntities) {
            advisorService.deleteAdvisor(a.getId());
        }
        userMapper.deleteAuthority(username);
        userMapper.deleteUser(username);
    }

    @Override
    @Transactional
    public void increaseCollegeCount(String college) {
        userMapper.increaseCollegeCount(college);
    }

    @Override
    public List<UserEntity> getCollegesEntites() {
        List<UserEntity> userEntities = userMapper.selectColleges();
        return userEntities;
    }
}
