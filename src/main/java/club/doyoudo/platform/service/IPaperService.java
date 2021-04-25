package club.doyoudo.platform.service;

import club.doyoudo.platform.entity.Paper;
import club.doyoudo.platform.vo.PaperResultVo;
import club.doyoudo.platform.vo.PaperWithQuestionList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
public interface IPaperService extends IService<Paper> {
    Long saveOrUpdatePaperWithQuestionList(PaperWithQuestionList paperWithQuestionList);

    PaperWithQuestionList getPaperWithQuestionList(Long paperId);

    PaperWithQuestionList getPaperWithQuestionAndAnswerList(Long paperId, Long userId);

    Page<PaperResultVo> getPaperResultListByPage(Long paperId, int current, int size);

    List<PaperResultVo> getPaperResultList(Long paperId);
}
