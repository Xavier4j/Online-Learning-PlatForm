package club.doyoudo.platform.service;

import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.vo.CourseWithVideoList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
public interface ICourseService extends IService<Course> {
    Long saveOrUpdateCourseWithVideoList(CourseWithVideoList courseWithVideoList);

    CourseWithVideoList getCourseWithVideoList(Long courseId);
}
