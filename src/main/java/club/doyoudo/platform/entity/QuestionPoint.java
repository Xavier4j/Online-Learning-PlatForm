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
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题_知识点表id
     */
    private Long id;

    /**
     * 问题id
     */
    private Long questionId;

    /**
     * 知识点id
     */
    private Long pointId;


}
