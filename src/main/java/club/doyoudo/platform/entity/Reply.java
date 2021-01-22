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
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    private String id;

    /**
     * 评论id,该回复仅可在该评论下可见
     */
    private String commentId;

    /**
     * 回复者id
     */
    private String reviewerId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
