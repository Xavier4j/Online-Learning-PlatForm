package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Point;
import club.doyoudo.platform.entity.Question;
import lombok.Data;

@Data
public class QuestionWithPointAndScore extends Question {
    private Point point;
    private int score;
}
