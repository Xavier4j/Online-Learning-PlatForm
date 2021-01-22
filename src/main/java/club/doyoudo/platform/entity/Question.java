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
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题目id
     */
    private String id;

    /**
     * 题目
     */
    private String question;

    /**
     * 题目创建时间
     */
    private LocalDateTime createTime;

    /**
     * 题目类型：	0，单选题；	1，多选题；	2，判断题；	3，主观题；
     */
    private Integer type;

    /**
     * 该字段仅当题目为判断题时有用，0：false;1:true;
     */
    private Boolean isRight;


}
