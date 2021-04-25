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
 * @since 2021-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷_题目表id
     */
    private Long id;

    /**
     * 试卷id
     */
    private Long paperId;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目分值
     */
    private Integer score;

    /**
     * 题号
     */
    private Integer sequence;


}
