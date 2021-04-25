package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Video;
import lombok.Data;

@Data
public class VideoWithPoint extends Video {
    private String point;
}
