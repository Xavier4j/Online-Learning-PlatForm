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
 * @since 2021-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频id
     */
    private Long id;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 知识点id
     */
    private Long pointId;

    /**
     * 视频路径
     */
    private String url;

    /**
     * 视频的次序
     */
    private Integer sequence;


}
