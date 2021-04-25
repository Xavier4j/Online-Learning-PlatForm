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
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知id
     */
    private Long id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 发布人
     */
    private Long authorId;

    /**
     * 通知封面
     */
    private String coverImageUrl;

    /**
     * 浏览数量
     */
    private String readNum;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;


}
