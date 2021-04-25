package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.*;
import club.doyoudo.platform.service.*;
import club.doyoudo.platform.vo.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@CrossOrigin
@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Resource
    IReplyService replyService;
    @Resource
    IUserService userService;
    @Resource
    IMessageService messageService;
    @Resource
    ICommentService commentService;

    @ApiOperation(value = "发布回复", notes = "发布回复", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/send")
    public ResponseWrapper sendReply(@RequestBody Reply reply) {
        Long reviewerId = reply.getReviewerId();
        User user = userService.getById(reviewerId);
        reply.setFlag(user.getRole());
        reply.setCreateTime(LocalDateTime.now());
        if (replyService.save(reply)) {
            Message message = new Message();
            message.setReceiver(reply.getRecipientId());
            message.setSender(reply.getReviewerId());
            message.setContent(reply.getContent());
            message.setFromId(commentService.getById(reply.getCommentId()).getBelongId());
            message.setType(2);
            message.setCreateTime(LocalDateTime.now());
            messageService.save(message);
            return new ResponseWrapper(true, 200, "回复成功", reply.getId());
        }
        return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
    }
}

