package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Comment;
import club.doyoudo.platform.mapper.CommentMapper;
import club.doyoudo.platform.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
