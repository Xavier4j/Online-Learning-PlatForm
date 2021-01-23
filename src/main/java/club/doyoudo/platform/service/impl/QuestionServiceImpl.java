package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Question;
import club.doyoudo.platform.mapper.QuestionMapper;
import club.doyoudo.platform.service.IQuestionService;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

}
