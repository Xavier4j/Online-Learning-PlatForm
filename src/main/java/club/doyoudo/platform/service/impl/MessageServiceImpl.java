package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Message;
import club.doyoudo.platform.mapper.MessageMapper;
import club.doyoudo.platform.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-22
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
