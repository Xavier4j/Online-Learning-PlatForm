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
 * @since 2021-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户登录名 不允许更改 不允许重复
     */
    private String username;

    /**
     * 用户密码 限制6~16位
     */
    private String password;

    /**
     * 角色，0代表学生，1代表管理员（老师),2代表系统级用户
     */
    private Integer role;


}
