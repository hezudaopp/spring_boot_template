package com.youmayon.tutorial.web;

import com.youmayon.tutorial.constant.SecurityConstants;
import com.youmayon.tutorial.domain.User;
import com.youmayon.tutorial.enums.Role;
import com.youmayon.tutorial.service.UserService;
import com.youmayon.tutorial.util.ReflectionUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 通过这里配置使下面的映射都在/users下，可去除
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder = new StandardPasswordEncoder(SecurityConstants.PASSWORD_ENCODER_SECRET);

    /**
     * 处理"/users/"的GET请求，用来获取用户列表
     * 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
     * @return
     */
    @ApiOperation(value = "获取用户列表", notes = "全部用户列表 ")
//    @PreAuthorize("#oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUserList(@RequestParam(value = "enabled") boolean enabled) {
        return userService.list(enabled);
    }

    /**
     * 处理"/users/"的POST请求，用来创建User
     * 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
     * @param user
     * @return
     */
    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(method = RequestMethod.POST)
    public User postUser(@Valid @RequestBody User user,
                         Errors errors) {
        // encode password.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedTime(System.currentTimeMillis() / 1000);
        user.setRole(Role.ROLE_USER.name());
        return userService.save(user);
    }

    /**
     * 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
     * url中的id可通过@PathVariable绑定到函数的参数中
     * @param id
     * @return
     */
    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        return userService.get(id);
    }

    /**
     * 处理"/users/{id}"的PUT请求，用来更新User信息
     * @param id
     * @param unsavedUser
     * @return
     */
    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @RequestMapping(value = "/{id}", method=RequestMethod.PATCH)
    public User patchUser(@PathVariable Long id, @RequestBody User unsavedUser) {
        User savedUser = userService.get(id);
        Assert.notNull(savedUser, "User " + id + " not found.");
        if (unsavedUser.getPassword() != null) {
            unsavedUser.setPassword(passwordEncoder.encode(unsavedUser.getPassword()));
        }
        ReflectionUtils.mergeObject(unsavedUser, savedUser);
        savedUser.setModifiedTime(System.currentTimeMillis() / 1000);
        return userService.save(savedUser);
    }

    /**
     * 处理"/users/{id}"的DELETE请求，用来删除User
     * @param id
     */
    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}