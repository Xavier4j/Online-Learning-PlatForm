package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Notice;
import club.doyoudo.platform.service.INoticeService;
import club.doyoudo.platform.service.IProfileService;
import club.doyoudo.platform.vo.NoticeWithAuthor;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    INoticeService noticeService;
    @Resource
    IProfileService profileService;

    @ApiOperation(value = "模糊查询公告列表", notes = "分页查询所有公告列表，按照时间倒序", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/search")
    public ResponseWrapper searchAll(String noticeName, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建用户查询条件构造器
        QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();
        if (noticeName != null) {
            noticeQueryWrapper.like("name", noticeName);
        }
        Page<Notice> noticePage = new Page<>(current, size);
        Page<Notice> page = noticeService.page(noticePage, noticeQueryWrapper);
        Page<NoticeWithAuthor> noticeWithAuthorPage = new Page<>(current, size);
        noticeWithAuthorPage.setTotal(page.getTotal());
        noticeWithAuthorPage.setPages(page.getPages());
        List<Notice> noticeList = page.getRecords();
        List<NoticeWithAuthor> noticeWithAuthorList = new ArrayList<>();
        noticeList.forEach(notice -> {
            NoticeWithAuthor noticeWithAuthor = JSONObject.parseObject(JSONObject.toJSONString(notice), NoticeWithAuthor.class);
            noticeWithAuthor.setAuthor(profileService.selectProfile(notice.getAuthorId()));
            noticeWithAuthorList.add(noticeWithAuthor);
        });
        noticeWithAuthorPage.setRecords(noticeWithAuthorList);
        return new ResponseWrapper(true, 200, "查询成功", noticeWithAuthorPage);
    }

    @ApiOperation(value = "创建公告", notes = "只需填写username与password", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/create")
    public ResponseWrapper create(@RequestBody Notice notice) {
        //判断是创建还是修改
        String msg = notice.getId() == null ? "创建成功！" : "修改成功";
        if (notice.getId() == null) {
            notice.setCreateTime(LocalDateTime.now());
            notice.setCreateTime(LocalDateTime.now());
        }
        if (noticeService.saveOrUpdate(notice)) {
            return new ResponseWrapper(true, 200, msg, notice.getId());
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "查询公告", notes = "根据id查询公告内容", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/select")
    public ResponseWrapper select(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);
        NoticeWithAuthor noticeWithAuthor = JSONObject.parseObject(JSONObject.toJSONString(notice), NoticeWithAuthor.class);
        noticeWithAuthor.setAuthor(profileService.selectProfile(notice.getAuthorId()));
        return new ResponseWrapper(true, 200, "查询成功", noticeWithAuthor);
    }

    @ApiOperation(value = "删除公告", notes = "根据id删除公告内容", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/delete")
    public ResponseWrapper delete(Long noticeId) {
        return new ResponseWrapper(true, 200, "删除成功", noticeService.removeById(noticeId));
    }
}

