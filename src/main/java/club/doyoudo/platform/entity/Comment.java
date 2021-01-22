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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id 不允许重复
     */
    private String id;

    /**
     * 帖子id，该评论仅可在该帖子下可见
     */
    private String postId;

    /**
     * 评论者id
     */
    private String reviewerId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 0代表正常，1代表删除
     */
    private Boolean isDelete;


}
