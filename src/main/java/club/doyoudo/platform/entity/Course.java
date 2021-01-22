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
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private LocalDateTime createTime;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


}
