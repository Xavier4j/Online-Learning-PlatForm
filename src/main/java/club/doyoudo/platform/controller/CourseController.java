package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.service.ICourseService;
import club.doyoudo.platform.vo.CourseWithVideoList;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/course")
public class CourseController {
    @Resource
    ICourseService courseService;

    @ApiOperation(value = "模糊查询课程列表", notes = "分页查询所有课程列表，按照时间倒序", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/search")
    public ResponseWrapper searchAll(String courseName, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建用户查询条件构造器
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        if (courseName != null) {
            courseQueryWrapper.like("name", courseName);
        }

        Page<Course> coursePage = new Page<>(current, size);
        Page<Course> page = courseService.page(coursePage, courseQueryWrapper);
        return new ResponseWrapper(true, 200, "查询成功", page);
    }

    @ApiOperation(value = "创建课程", notes = "只需填写username与password", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/create")
    public ResponseWrapper create(@RequestBody CourseWithVideoList courseWithVideoList) {
        //判断是创建还是修改
        String msg = courseWithVideoList.getId() == null ? "创建成功！" : "修改成功";
        Long courseId = courseService.saveOrUpdateCourseWithVideoList(courseWithVideoList);
        if (courseId != null) {
            return new ResponseWrapper(true, 200, msg, courseId);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "查询课程", notes = "根据id查询课程内容", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/select")
    public ResponseWrapper select(Long courseId) {
        return new ResponseWrapper(true, 200, "查询成功", courseService.getCourseWithVideoList(courseId));
    }

    @ApiOperation(value = "删除课程", notes = "根据id删除课程内容", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/delete")
    public ResponseWrapper delete(Long courseId) {
        return new ResponseWrapper(true, 200, "删除成功", courseService.removeById(courseId));
    }
}

