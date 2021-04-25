package club.doyoudo.platform.service;

import club.doyoudo.platform.entity.Reply;
import club.doyoudo.platform.vo.ReplyWithProfile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
public interface IReplyService extends IService<Reply> {
    List<ReplyWithProfile> getReplyWithProfileList(Long commentId);
}
