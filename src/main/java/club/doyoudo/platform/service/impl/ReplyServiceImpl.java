package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Reply;
import club.doyoudo.platform.mapper.ProfileMapper;
import club.doyoudo.platform.mapper.ReplyMapper;
import club.doyoudo.platform.service.IProfileService;
import club.doyoudo.platform.service.IReplyService;
import club.doyoudo.platform.vo.CommentWithReply;
import club.doyoudo.platform.vo.ReplyWithProfile;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements IReplyService {
    @Resource
    ReplyMapper replyMapper;
    @Resource
    IProfileService profileService;

    @Override
    public List<ReplyWithProfile> getReplyWithProfileList(Long commentId) {
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
        replyQueryWrapper.eq("comment_id", commentId);
        List<Reply> replyList = replyMapper.selectList(replyQueryWrapper);
        List<ReplyWithProfile> replyWithProfileList = new ArrayList<>();
        for (Reply reply : replyList) {
            ReplyWithProfile replyWithProfile = JSONObject.parseObject(JSONObject.toJSONString(reply), ReplyWithProfile.class);
            replyWithProfile.setReviewerProfile(profileService.selectProfile(replyWithProfile.getReviewerId()));
            replyWithProfileList.add(replyWithProfile);
        }
        return replyWithProfileList;
    }
}
