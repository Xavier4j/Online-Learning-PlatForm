package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Video;
import club.doyoudo.platform.service.IPointService;
import club.doyoudo.platform.service.IVideoService;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/video")
public class VideoController {
    @Resource
    IVideoService videoService;
    @Resource
    IPointService pointService;

    @ApiOperation(value = "插入视频到课程", notes = "传入视频urlList，将视频对象存入数据库", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/add-to-course")
    public ResponseWrapper addToCourse(List<Video> videoList) {
        //进行批量插入操作
        if (videoService.saveBatch(videoList)) {
            return new ResponseWrapper(true, 200, "批量插入成功!", null);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "根据课程查询视频列表", notes = "根据课程id或者知识点查询视频列表", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/searchByCourseId")
    public ResponseWrapper searchByCourseId(Long courseId) {
        //创建用户查询条件构造器
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoQueryWrapper.eq("course_id", courseId);
        return new ResponseWrapper(true, 200, "查询成功", videoService.list(videoQueryWrapper));
    }

    @ApiOperation(value = "根据知识点查询视频列表", notes = "根据知识点ids或者知识点查询视频列表,用逗号隔开", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/searchByPointIds")
    public ResponseWrapper searchByCourseId(String pointIds) {
        String[] ids = pointIds.split(",");
        //构造知识点对应视频的Map
        Map<String, Object> map = new HashMap<>();
        for (String pointId : ids) {
            //创建用户查询条件构造器
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("point_id", pointId);
            String point = pointService.getById(pointId).getPoint();
            List<Video> list = videoService.list(videoQueryWrapper);
            map.put(point, list);
        }
        return new ResponseWrapper(true, 200, "查询成功", map);
    }

    @ApiOperation(value = "修改视频到课程", notes = "传入新的videoList，将视频对象存入数据库", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/update-to-course")
    public ResponseWrapper updateToCourse(List<Video> videoList) {
        //进行批量插入操作
        if (videoService.saveBatch(videoList)) {
            return new ResponseWrapper(true, 200, "批量插入成功!", null);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }
}

