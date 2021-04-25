package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Message;
import club.doyoudo.platform.service.IMessageService;
import club.doyoudo.platform.service.IProfileService;
import club.doyoudo.platform.vo.CommentWithReply;
import club.doyoudo.platform.vo.MessageWithSenderProfile;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/message")
public class MessageController {
    @Resource
    IMessageService messageService;
    @Resource
    IProfileService profileService;

    @ApiOperation(value = "查询消息数量", notes = "根据用户id查询消息数量", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/count")
    public ResponseWrapper count(Long userId) {
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("receiver", userId);
        messageQueryWrapper.eq("status", 0);
        messageQueryWrapper.eq("is_delete", 0);
        return new ResponseWrapper(true, 200, "查询成功!", messageService.count(messageQueryWrapper));
    }

    @ApiOperation(value = "查询消息列表", notes = "根据用户id查询消息列表", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/select")
    public ResponseWrapper select(Long userId) {
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("receiver", userId);
        messageQueryWrapper.eq("is_delete", 0);
        List<Message> messageList = messageService.list(messageQueryWrapper);
        List<MessageWithSenderProfile> messageWithSenderProfileList = new ArrayList<>();
        for (Message message : messageList) {
            MessageWithSenderProfile messageWithSenderProfile = JSONObject.parseObject(JSONObject.toJSONString(message), MessageWithSenderProfile.class);
            messageWithSenderProfile.setSenderProfile(profileService.selectProfile(message.getSender()));
            messageWithSenderProfileList.add(messageWithSenderProfile);
        }
        return new ResponseWrapper(true, 200, "查询成功!", messageWithSenderProfileList);
    }

    @ApiOperation(value = "修改消息", notes = "修改消息", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/update")
    public ResponseWrapper update(@RequestBody Message message) {
        System.out.println(message);
        messageService.updateById(message);
        return new ResponseWrapper(true, 200, "修改成功!", null);
    }
}

