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
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考卷id
     */
    private Long id;

    /**
     * 考卷名称
     */
    private String title;

    /**
     * 0代表试卷，1代表测试
     */
    private Boolean isTest;

    /**
     * 只有知识点测试需要知识点id，当类型为测试时有效
     */
    private Long pointId;

    /**
     * 考试开始时间
     */
    private LocalDateTime startTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
