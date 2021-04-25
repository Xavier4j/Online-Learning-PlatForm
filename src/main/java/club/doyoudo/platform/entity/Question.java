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
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题目id
     */
    private Long id;

    /**
     * 题目
     */
    private String question;

    /**
     * 题目类型：	0，单选题；	1，多选题；	2，判断题；	3，填空题；	4，问答题
     */
    private Integer type;

    /**
     * 选项，当题目为选择题时有效，选项标号从前往后，使用Json数组
     */
    private String options;

    /**
     * 填空题空白处数量
     */
    private Integer blankNum;

    /**
     * 题目答案
     */
    private String rightAnswer;

    /**
     * 题目所涉及知识点
     */
    private Long pointId;

    /**
     * 题目创建时间
     */
    private LocalDateTime createTime;


}
