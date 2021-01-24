package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.service.ICourseService;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
@RequestMapping("/course")
public class CourseController {
    @Resource
    ICourseService courseService;

    @ApiOperation(value = "创建课程", notes = "只需填写username与password", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/create")
    public ResponseWrapper logIn(Course course) {
        //则进行插入操作
        course.setCreateTime(LocalDateTime.now());
        if (courseService.save(course)) {
            return new ResponseWrapper(true, 200, "创建成功", course.getId());
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "模糊查询课程列表", notes = "分页查询所有课程列表，按照时间倒序", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/search")
    public ResponseWrapper searchAll(String courseName, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建用户查询条件构造器
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("name", courseName);
        Page<Course> coursePage = new Page<>(current, size);
        Page<Course> page = courseService.page(coursePage, courseQueryWrapper);
        return new ResponseWrapper(true, 200, "查询成功", page);
    }

    @ApiOperation(value = "修改课程信息", notes = "根据课程id修改课程信息", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/update")
    public ResponseWrapper update(Course course) {
        if (courseService.updateById(course)) {
            return new ResponseWrapper(true, 200, "修改成功", null);
        }
        return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
    }
}

