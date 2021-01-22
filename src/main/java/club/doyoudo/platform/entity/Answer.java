package club.doyoudo.platform.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 答案id
     */
    private String id;

    /**
     * 答案
     */
    private String answer;

    /**
     * 答案是否正确，该字段仅当题目为选择题时有效
     */
    private Boolean isCorrect;


}
