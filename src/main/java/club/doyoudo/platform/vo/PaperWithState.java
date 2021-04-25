package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Paper;
import lombok.Data;

@Data
public class PaperWithState extends Paper {
    //isDone代表当前用户是否已经完成过此考卷
    private boolean isDone;
}
