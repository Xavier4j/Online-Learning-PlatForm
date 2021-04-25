package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Answer;
import lombok.Data;

import java.util.List;

@Data
public class AnswerVo {
    private List<Answer> answerList;
}
