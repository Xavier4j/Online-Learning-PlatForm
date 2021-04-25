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
public class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 知识点id
     */
    private Long id;

    /**
     * 知识点
     */
    private String point;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
