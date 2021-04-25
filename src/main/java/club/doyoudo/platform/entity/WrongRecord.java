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
public class WrongRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误记录id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 知识点id
     */
    private Long pointId;

    /**
     * 错误次数
     */
    private Integer num;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
