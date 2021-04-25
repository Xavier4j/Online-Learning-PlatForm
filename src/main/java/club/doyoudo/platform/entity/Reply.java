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
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    private Long id;

    /**
     * 评论id,该回复仅可在该评论下可见
     */
    private Long commentId;

    /**
     * 回复者id
     */
    private Long reviewerId;

    /**
     * 接受者id(被回复者)
     */
    private Long recipientId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 标记变量，0代表学生回复，1代表老师回复，老师回复默认置顶
     */
    private Integer flag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
