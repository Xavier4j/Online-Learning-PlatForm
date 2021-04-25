package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Comment;
import club.doyoudo.platform.entity.Profile;
import lombok.Data;

import java.util.List;

@Data
public class CommentWithReply extends Comment {
    private Profile reviewerProfile;
    List<ReplyWithProfile> replyList;
}
