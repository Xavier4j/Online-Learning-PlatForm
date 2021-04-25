package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.entity.Video;
import lombok.Data;

@Data
public class VideoWithCourse extends Video {
    private String point;
    private Course course;
}
