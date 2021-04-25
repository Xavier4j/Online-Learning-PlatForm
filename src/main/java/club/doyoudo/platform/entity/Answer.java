package club.doyoudo.platform.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xavier4j
 * @since 2021-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回答id
     */
    private Long id;

    /**
     * 试卷id
     */
    private Long paperId;

    /**
     * 问题id
     */
    private Long questionId;

    /**
     * 答题人
     */
    private Long writerId;

    /**
     * 答案
     */
    private String answer;

    /**
     * 题目得分
     */
    private Integer score;

    /**
     * 答题时间
     */
    private LocalDateTime createTime;


}
