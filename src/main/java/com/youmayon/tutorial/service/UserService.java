package com.youmayon.tutorial.service;

import com.youmayon.tutorial.domain.User;

import java.util.List;

public interface UserService {

    /**
     * 新增或者更新一个用户
     * @param user
     */
    User save(User user);

    /**
     * 根据username获取一个用户
     * @param username
     */
    User get(String username);

    /**
     * 根据id获取一个用户
     * @param id
     * @return
     */
    User get(long id);

    /**
     * 获取全部用户列表
     * @return
     */
    List<User> list();

    /**
     * 根据id删除一个用户
     */
    void delete(long id);
}
