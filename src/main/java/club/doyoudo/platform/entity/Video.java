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
 * @since 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频id
     */
    private String id;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 知识点id
     */
    private String pointId;

    /**
     * 视频路径
     */
    private String location;


}
