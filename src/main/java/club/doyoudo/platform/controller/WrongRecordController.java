package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Video;
import club.doyoudo.platform.entity.WrongRecord;
import club.doyoudo.platform.service.ICourseService;
import club.doyoudo.platform.service.IPointService;
import club.doyoudo.platform.service.IVideoService;
import club.doyoudo.platform.service.IWrongRecordService;
import club.doyoudo.platform.vo.ResponseWrapper;
import club.doyoudo.platform.vo.VideoWithCourse;
import club.doyoudo.platform.vo.VideoWithPoint;
import club.doyoudo.platform.vo.WrongRecordWithVideos;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xavier4j
 * @since 2021-02-14
 */
@CrossOrigin
@RestController
@RequestMapping("/wrong-record")
public class WrongRecordController {
    @Resource
    IWrongRecordService wrongRecordService;
    @Resource
    IVideoService videoService;
    @Resource
    ICourseService courseService;
    @Resource
    IPointService pointService;

    @ApiOperation(value = "查询错误记录", notes = "查询错误记录，并为其推荐视频", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/select")
    public ResponseWrapper selectWrongRecord(Long userId) {
        //条件查询wrongRecordList，只要最新的前十个，并且按照知识点错误次数进行排序
        QueryWrapper<WrongRecord> wrongRecordQueryWrapper = new QueryWrapper<>();
        wrongRecordQueryWrapper.eq("user_id", userId);
        wrongRecordQueryWrapper.orderByDesc("update_time");
        List<WrongRecord> wrongRecordList = wrongRecordService.list(wrongRecordQueryWrapper);
        if (wrongRecordList.size() > 10) {
            wrongRecordList.subList(0, 10);
        }
        wrongRecordList.sort(Comparator.comparing(WrongRecord::getNum).reversed());
        //将查询好的wrongRecordList转换成含有推荐视频的List
        List<WrongRecordWithVideos> wrongRecordWithVideosList = new ArrayList<>();
        for (WrongRecord wrongRecord : wrongRecordList) {
            WrongRecordWithVideos wrongRecordWithVideos = JSONObject.parseObject(JSONObject.toJSONString(wrongRecord), WrongRecordWithVideos.class);
            //根据错误记录的知识点id查询视频列表
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("point_id", wrongRecord.getPointId());
            List<Video> videoList = videoService.list(videoQueryWrapper);
            //set知识点
            wrongRecordWithVideos.setPoint(pointService.getById(wrongRecord.getPointId()));
            //set视频列表
            List<VideoWithCourse> videoWithCourseList = new ArrayList<>();
            Map<Long, Integer> courseIdMap = new HashMap<>();
            for (Video video : videoList) {
                VideoWithCourse videoWithCourse = JSONObject.parseObject(JSONObject.toJSONString(video), VideoWithCourse.class);
                videoWithCourse.setCourse(courseService.getById(video.getCourseId()));
                Long courseId = video.getCourseId();
                if (courseIdMap.containsKey(courseId)) {
                    courseIdMap.put(courseId, courseIdMap.get(courseId) + 1);
                } else {
                    courseIdMap.put(courseId, 1);
                }
                Integer count = courseIdMap.get(courseId);
                if (count > 1) {
                    videoWithCourse.setPoint(wrongRecordWithVideos.getPoint().getPoint() + "(" + count + ")");
                    System.out.println(wrongRecordWithVideos.getPoint().getPoint() + "(" + count + ")");
                } else {
                    videoWithCourse.setPoint(wrongRecordWithVideos.getPoint().getPoint());
                }
                videoWithCourseList.add(videoWithCourse);
            }
            wrongRecordWithVideos.setVideoWithCourseList(videoWithCourseList);
            //插入List中
            wrongRecordWithVideosList.add(wrongRecordWithVideos);
        }
        return new ResponseWrapper(true, 200, "查询记录成功!", wrongRecordWithVideosList);
    }
}

