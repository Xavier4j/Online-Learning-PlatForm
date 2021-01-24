package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Profile;
import club.doyoudo.platform.entity.User;
import club.doyoudo.platform.service.IProfileService;
import club.doyoudo.platform.service.IUserService;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;
    @Resource
    IProfileService profileService;

    @ApiOperation(value = "登录", notes = "只需填写username与password", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/login")
    public ResponseWrapper logIn(@RequestBody User user) {
        //创建用户查询条件构造器
        QueryWrapper<User> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", user.getUsername());
        adminQueryWrapper.eq("password", user.getPassword());
        //根据账号密码查询用户
        User user1 = userService.getOne(adminQueryWrapper);
        if (user1 != null) {
            if (user1.getRole() == 0) {
                return new ResponseWrapper(true, 200, "登录成功,角色为学生！", null);
            } else {
                return new ResponseWrapper(true, 201, "登录成功,角色为老师！", null);
            }
        } else {
            return new ResponseWrapper(false, 600, "登录失败，账号或者密码有误!", null);
        }
    }

    @ApiOperation(value = "注册", notes = "只需填写username与password", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/signup")
    public ResponseWrapper signUp(@RequestBody User user) {
        //创建用户查询条件构造器
        QueryWrapper<User> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", user.getUsername());
        //根据账号查询用户,若已经存在,则注册失败
        if (userService.getOne(adminQueryWrapper) != null) {
            return new ResponseWrapper(false, 600, "注册失败，当前用户名已经存在!", null);
        }
        //如果注册账号合法，则进行插入操作
        //只接受username和password,将其他参数
        user.setId(null);
        //只能注册为学生
        user.setRole(0);
        if (userService.save(user)) {
            Profile profile = new Profile();
            profile.setUserId(user.getId());
            profile.setNickName("新人" + user.getUsername());
            profileService.insertProfile(profile);
            return new ResponseWrapper(true, 200, "注册成功", user.getId());
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }
}

