package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.entity.Video;
import lombok.Data;

import java.util.List;

@Data
public class CourseWithVideoList extends Course {
    private List<VideoWithPoint> videoList;
}
