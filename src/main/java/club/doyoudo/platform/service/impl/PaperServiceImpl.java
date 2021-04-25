package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Answer;
import club.doyoudo.platform.entity.Paper;
import club.doyoudo.platform.entity.PaperQuestion;
import club.doyoudo.platform.entity.Question;
import club.doyoudo.platform.mapper.PaperMapper;
import club.doyoudo.platform.service.*;
import club.doyoudo.platform.vo.PaperResultVo;
import club.doyoudo.platform.vo.PaperWithQuestionList;
import club.doyoudo.platform.vo.QuestionWithPointAndScore;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements IPaperService {
    @Resource
    PaperMapper paperMapper;
    @Resource
    IPaperQuestionService paperQuestionService;
    @Resource
    IQuestionService questionService;
    @Resource
    IPointService pointService;
    @Resource
    IAnswerService answerService;
    @Resource
    IProfileService profileService;

    @Override
    public Long saveOrUpdatePaperWithQuestionList(PaperWithQuestionList paperWithQuestionList) {
        List<QuestionWithPointAndScore> questionWithPointAndScoreList = paperWithQuestionList.getQuestionList();
        if (questionWithPointAndScoreList == null) {
            return null;
        }
        //进行试卷插入操作
        if (paperWithQuestionList.getId() == null) {
            paperWithQuestionList.setCreateTime(LocalDateTime.now());
            paperMapper.insert(paperWithQuestionList);
        } else {
            paperMapper.updateById(paperWithQuestionList);
        }
        //先清空当前paper的paper_question
        QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
        paperQuestionQueryWrapper.eq("paper_id", paperWithQuestionList.getId());
        paperQuestionService.remove(paperQuestionQueryWrapper);
        //再进行批量插入
        List<PaperQuestion> paperQuestionList = new ArrayList<>();
        for (int i = 0; i < questionWithPointAndScoreList.size(); i++) {
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(paperWithQuestionList.getId());
            paperQuestion.setQuestionId(questionWithPointAndScoreList.get(i).getId());
            paperQuestion.setScore(questionWithPointAndScoreList.get(i).getScore());
            paperQuestion.setSequence(i + 1);
            paperQuestionList.add(paperQuestion);
        }
        //批量插入试卷问题
        paperQuestionService.saveOrUpdateBatch(paperQuestionList);
        return paperWithQuestionList.getId();
    }

    @Override
    public PaperWithQuestionList getPaperWithQuestionList(Long paperId) {
        //查询paper
        Paper paper = paperMapper.selectById(paperId);
        //将Paper使用JSON强转成具有问题列表的PaperWithQuestionList
        PaperWithQuestionList paperWithQuestionList = JSONObject.parseObject(JSONObject.toJSONString(paper), PaperWithQuestionList.class);
        //条件查询当前试卷的问题列表
        QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
        paperQuestionQueryWrapper.eq("paper_id", paperId);
        List<PaperQuestion> paperQuestionList = paperQuestionService.list(paperQuestionQueryWrapper);
        //查询每一个问题的总成绩、知识点
        List<QuestionWithPointAndScore> questionWithPointAndScoreList = new ArrayList<>();
        paperQuestionList.forEach(paperQuestion -> {
            Question question = questionService.getById(paperQuestion.getQuestionId());
            QuestionWithPointAndScore questionWithPointAndScore = JSONObject.parseObject(JSONObject.toJSONString(question), QuestionWithPointAndScore.class);
            questionWithPointAndScore.setScore(paperQuestion.getScore());
            questionWithPointAndScore.setRightAnswer(null);
            questionWithPointAndScore.setPoint(pointService.getById(questionWithPointAndScore.getPointId()));
            questionWithPointAndScoreList.add(questionWithPointAndScore);
        });
        paperWithQuestionList.setQuestionList(questionWithPointAndScoreList);
        return paperWithQuestionList;
    }

    @Override
    public PaperWithQuestionList getPaperWithQuestionAndAnswerList(Long paperId, Long userId) {
        Paper paper = paperMapper.selectById(paperId);
        PaperWithQuestionList paperWithQuestionList = JSONObject.parseObject(JSONObject.toJSONString(paper), PaperWithQuestionList.class);
        QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
        paperQuestionQueryWrapper.eq("paper_id", paperId);
        List<PaperQuestion> paperQuestionList = paperQuestionService.list(paperQuestionQueryWrapper);
        //考卷试题列表
        List<QuestionWithPointAndScore> questionWithPointAndScoreList = new ArrayList<>();
        //考生答案列表
        List<Answer> paperAnswerList = new ArrayList<>();
        paperQuestionList.forEach(paperQuestion -> {
            //查询出每个问题的知识点以及总成绩
            Question question = questionService.getById(paperQuestion.getQuestionId());
            QuestionWithPointAndScore questionWithPointAndScore = JSONObject.parseObject(JSONObject.toJSONString(question), QuestionWithPointAndScore.class);
            questionWithPointAndScore.setScore(paperQuestion.getScore());
            questionWithPointAndScore.setPoint(pointService.getById(questionWithPointAndScore.getPointId()));
            questionWithPointAndScoreList.add(questionWithPointAndScore);
            //查询出考生的答案
            QueryWrapper<Answer> paperAnswerQueryWrapper = new QueryWrapper<>();
            paperAnswerQueryWrapper.eq("paper_id", paperId);
            paperAnswerQueryWrapper.eq("question_id", paperQuestion.getQuestionId());
            paperAnswerQueryWrapper.eq("writer_id", userId);
            paperAnswerList.add(answerService.getOne(paperAnswerQueryWrapper));
        });
        paperWithQuestionList.setQuestionList(questionWithPointAndScoreList);
        paperWithQuestionList.setAnswerList(paperAnswerList);
        return paperWithQuestionList;
    }

    @Override
    public Page<PaperResultVo> getPaperResultListByPage(Long paperId, int current, int size) {
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq("paper_id",paperId);
        answerQueryWrapper.groupBy("writer_id");
        List<PaperResultVo> paperResultVoList = new ArrayList<>();
        Page<Answer> paperPage = new Page<>(current, size);
        Page<Answer> answerPage = answerService.page(paperPage, answerQueryWrapper);
        for (Answer answer : answerPage.getRecords()) {
            PaperResultVo paperResultVo = new PaperResultVo();
            paperResultVo.setWriter(profileService.selectProfile(answer.getWriterId()));
            paperResultVo.setScore(this.getScore(paperId,answer.getWriterId()));
            paperResultVo.setAnswerTime(answer.getCreateTime());
            paperResultVo.setIsGrade(this.isGrade(paperId,answer.getWriterId()));
            paperResultVoList.add(paperResultVo);
        }
        Page<PaperResultVo> page = new Page<>(current, size);
        page.setRecords(paperResultVoList);
        page.setTotal(answerPage.getTotal());
        page.setPages(answerPage.getPages());
        return page;
    }

    @Override
    public List<PaperResultVo> getPaperResultList(Long paperId) {
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq("paper_id",paperId);
        answerQueryWrapper.groupBy("writer_id");
        List<PaperResultVo> paperResultVoList = new ArrayList<>();
        List<Answer> answerList = answerService.list(answerQueryWrapper);
        for (Answer answer : answerList) {
            PaperResultVo paperResultVo = new PaperResultVo();
            paperResultVo.setWriter(profileService.selectProfile(answer.getWriterId()));
            paperResultVo.setScore(this.getScore(paperId,answer.getWriterId()));
            paperResultVo.setAnswerTime(answer.getCreateTime());
            paperResultVo.setIsGrade(this.isGrade(paperId,answer.getWriterId()));
            paperResultVoList.add(paperResultVo);
        }
        return paperResultVoList;
    }

    public int getScore(Long paperId,Long writerId) {
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq("paper_id", paperId);
        answerQueryWrapper.eq("writer_id", writerId);
        List<Answer> answerList = answerService.list(answerQueryWrapper);
        int score = 0;
        for (Answer answer : answerList) {
            if (answer.getScore() != null) {
                score += answer.getScore();
            }
        }
        return score;
    }

    public boolean isGrade(Long paperId,Long writerId) {
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.eq("paper_id", paperId);
        answerQueryWrapper.eq("writer_id", writerId);
        answerQueryWrapper.isNull("score");
        List<Answer> answerList = answerService.list(answerQueryWrapper);
        return answerList.size()<=0;
    }
}