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
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色，0代表学生，1代表管理员（老师),2代表系统级用户
     */
    private Integer role;

    /**
     * 昵称 不能重复
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 0男 1 女 2 保密
     */
    private Integer gender;

    /**
     * 个人说明
     */
    private String note;


}
