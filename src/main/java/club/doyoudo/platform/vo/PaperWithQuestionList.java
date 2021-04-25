package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Answer;
import club.doyoudo.platform.entity.Paper;
import lombok.Data;

import java.util.List;

@Data
public class PaperWithQuestionList extends Paper {
    private List<QuestionWithPointAndScore> questionList;
    private List<Answer> answerList;
}
