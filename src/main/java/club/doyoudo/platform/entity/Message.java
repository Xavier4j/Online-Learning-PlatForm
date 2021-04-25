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
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型，
     */
    private Long id;

    /**
     * 消息接收人
     */
    private Long receiver;

    /**
     * 消息发送人
     */
    private Long sender;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息来源，如果是课程，则存储课程id，如果是公告，则存储公告id
     */
    private Long fromId;

    /**
     * 来源，0代表课程，1代表公告
     */
    private Integer flag;

    /**
     * 消息类型,0代表系统消息，1代表评论，2代表回复
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 0代表未读，1代表已读
     */
    private Boolean status;

    /**
     * 0代表正常，1代表逻辑删除
     */
    private Boolean isDelete;


}
