package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Point;
import club.doyoudo.platform.entity.Video;
import club.doyoudo.platform.entity.WrongRecord;
import lombok.Data;

import java.util.List;

@Data
public class WrongRecordWithVideos extends WrongRecord {
    private Point point;
    private List<VideoWithCourse> videoWithCourseList;
}
