package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.User;
import club.doyoudo.platform.service.IUserService;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    @ApiOperation(value = "登录", notes = "只需填写username与password", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/login")
    public ResponseWrapper logIn(@RequestBody User user) {
        QueryWrapper<User> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username", user.getUsername());
        adminQueryWrapper.eq("password", user.getPassword());
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
        user.setId(null);
        //只能注册为学生
        user.setRole(null);
        if (userService.save(user)) {
            System.out.println(user.getId());
            return new ResponseWrapper(true, 201, "注册成功", null);
        } else {
            return new ResponseWrapper(false, 600, "登录失败，账号或者密码有误!", null);
        }
    }
}

