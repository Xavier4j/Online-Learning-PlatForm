package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.*;
import club.doyoudo.platform.service.*;
import club.doyoudo.platform.vo.CommentWithReply;
import club.doyoudo.platform.vo.CourseWithVideoList;
import club.doyoudo.platform.vo.ReplyWithProfile;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
@RequestMapping("/comment")
public class CommentController {
    @Resource
    ICommentService commentService;
    @Resource
    IReplyService replyService;
    @Resource
    IProfileService profileService;
    @Resource
    ICourseService courseService;
    @Resource
    INoticeService noticeService;
    @Resource
    IMessageService messageService;

    @ApiOperation(value = "分页查询评论列表", notes = "分页查询评论列表", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/select")
    public ResponseWrapper selectCommentWithReply(Long belongId, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建评论查询条件构造器
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("belong_id", belongId);
        commentQueryWrapper.orderByDesc("create_time");
        Page<Comment> coursePage = new Page<>(current, size);
        Page<Comment> page = commentService.page(coursePage, commentQueryWrapper);
        List<Comment> commentList = page.getRecords();
        List<CommentWithReply> commentWithReplyList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentWithReply commentWithReply = JSONObject.parseObject(JSONObject.toJSONString(comment), CommentWithReply.class);
            commentWithReply.setReviewerProfile(profileService.selectProfile(commentWithReply.getReviewerId()));
            //查询回复列表
            List<ReplyWithProfile> replyWithProfileList = replyService.getReplyWithProfileList(commentWithReply.getId());
            commentWithReplyList.add(commentWithReply);
        }
        //对commentWithReplyList进行排序，将老师的放在最上面
        commentWithReplyList.sort((o1, o2) -> o2.getReviewerProfile().getRole() - o1.getReviewerProfile().getRole());
        //封装成新的
        Page<CommentWithReply> page1 = new Page<>(current, size);
        page1.setPages(page.getPages());
        page1.setTotal(page.getTotal());
        page1.setRecords(commentWithReplyList);
        return new ResponseWrapper(true, 200, "查询成功", page1);
    }

    @ApiOperation(value = "发布评论", notes = "发布评论", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/send")
    public ResponseWrapper sendComment(@RequestBody Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        if (commentService.save(comment)) {
            Message message = new Message();
            //查询这个评论是视频下的还是公告下的，二者一定有且只有一个为null
            Course course = courseService.getById(comment.getBelongId());
            Notice notice = noticeService.getById(comment.getBelongId());

            if (course != null) {
                message.setReceiver(course.getTeacherId());
                message.setFlag(0);
            }
            if (notice != null) {
                message.setReceiver(notice.getAuthorId());
                message.setFlag(1);
            }

            message.setSender(comment.getReviewerId());
            message.setContent(comment.getContent());
            message.setFromId(comment.getBelongId());
            message.setType(1);
            message.setCreateTime(LocalDateTime.now());
            messageService.save(message);

            return new ResponseWrapper(true, 200, "评论成功", comment.getId());
        }
        return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
    }
}

