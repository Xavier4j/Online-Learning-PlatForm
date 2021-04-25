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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id 不允许重复
     */
    private Long id;

    /**
     * 当该评论是视频的评论时，belong_id就是视频id	当该评论时公告的评论时，belong_id就是公告id
     */
    private Long belongId;

    /**
     * 评论者id
     */
    private Long reviewerId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;


}
