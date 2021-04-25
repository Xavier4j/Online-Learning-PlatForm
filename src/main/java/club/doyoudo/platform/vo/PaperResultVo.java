package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Profile;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaperResultVo {
    private Profile writer;
    private int score;
    //是否批改
    private Boolean isGrade;
    private LocalDateTime answerTime;
}
