package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.entity.Video;
import club.doyoudo.platform.mapper.CourseMapper;
import club.doyoudo.platform.service.ICourseService;
import club.doyoudo.platform.service.IPointService;
import club.doyoudo.platform.service.IVideoService;
import club.doyoudo.platform.vo.CourseWithVideoList;
import club.doyoudo.platform.vo.VideoWithPoint;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    CourseMapper courseMapper;
    @Resource
    IVideoService videoService;
    @Resource
    IPointService pointService;

    @Override
    @Transactional
    public Long saveOrUpdateCourseWithVideoList(CourseWithVideoList courseWithVideoList) {
        List<VideoWithPoint> videoWithPointList = courseWithVideoList.getVideoList();
        if (videoWithPointList == null) {
            return null;
        }
        //进行课程插入操作
        if (courseWithVideoList.getId() == null) {
            courseWithVideoList.setCreateTime(LocalDateTime.now());
            courseMapper.insert(courseWithVideoList);
        } else {
            courseMapper.updateById(courseWithVideoList);
        }

        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < videoWithPointList.size(); i++) {
            VideoWithPoint videoWithPoint = videoWithPointList.get(i);
            videoWithPoint.setCourseId(courseWithVideoList.getId());
            videoWithPoint.setSequence(i + 1);
            videoList.add(videoWithPoint);
        }
        //批量插入课程视频
        videoService.saveOrUpdateBatch(videoList);
        return courseWithVideoList.getId();
    }

    @Override
    public CourseWithVideoList getCourseWithVideoList(Long courseId) {
        Course course = courseMapper.selectById(courseId);
        CourseWithVideoList courseWithVideoList = JSONObject.parseObject(JSONObject.toJSONString(course), CourseWithVideoList.class);
        QueryWrapper<Video> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("course_id", courseId);
        List<Video> videoList = videoService.list(courseQueryWrapper);
        List<VideoWithPoint> videoWithPointList = new ArrayList<>();
        Map<Long, Integer> pointIdMap = new HashMap<>();
        for (int i = 0; i < videoList.size(); i++) {
            VideoWithPoint videoWithPoint = JSONObject.parseObject(JSONObject.toJSONString(videoList.get(i)), VideoWithPoint.class);
            Long pointId = videoWithPoint.getPointId();
            if (pointIdMap.containsKey(pointId)) {
                pointIdMap.put(pointId, pointIdMap.get(pointId) + 1);
            } else {
                pointIdMap.put(pointId, 1);
            }
            Integer count = pointIdMap.get(pointId);
            if (count > 1) {
                videoWithPoint.setPoint(pointService.getById(pointId).getPoint() + "(" + count + ")");
            } else {
                videoWithPoint.setPoint(pointService.getById(pointId).getPoint());
            }
            videoWithPointList.add(videoWithPoint);
        }
        courseWithVideoList.setVideoList(videoWithPointList);
        return courseWithVideoList;
    }
}
