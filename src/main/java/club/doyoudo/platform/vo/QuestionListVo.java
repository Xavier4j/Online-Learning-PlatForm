package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuestionListVo {
    List<Question> questionList;
}
