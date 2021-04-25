package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Answer;
import club.doyoudo.platform.entity.PaperQuestion;
import club.doyoudo.platform.entity.Question;
import club.doyoudo.platform.entity.WrongRecord;
import club.doyoudo.platform.service.IAnswerService;
import club.doyoudo.platform.service.IPaperQuestionService;
import club.doyoudo.platform.service.IQuestionService;
import club.doyoudo.platform.service.IWrongRecordService;
import club.doyoudo.platform.vo.AnswerVo;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xavier4j
 * @since 2021-03-23
 */
@CrossOrigin
@RestController
@RequestMapping("/answer")
public class AnswerController {
    @Resource
    IAnswerService answerService;
    @Resource
    IQuestionService questionService;
    @Resource
    IPaperQuestionService paperQuestionService;
    @Resource
    IWrongRecordService wrongRecordService;

    @ApiOperation(value = "提交答案", notes = "提交答案List", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/insertBatch")
    public ResponseWrapper select(@RequestBody AnswerVo answerVo) {
        List<Answer> answerList = answerVo.getAnswerList();
        for (Answer answer : answerList) {
            //查询题目
            Question question = questionService.getById(answer.getQuestionId());
            //条件查询当前问题对应该试卷的PaperQuestion对象，目的是拿到score
            QueryWrapper<PaperQuestion> paperQuestionQueryWrapper = new QueryWrapper<>();
            paperQuestionQueryWrapper.eq("paper_id", answer.getPaperId());
            paperQuestionQueryWrapper.eq("question_id", question.getId());
            PaperQuestion paperQuestion = paperQuestionService.getOne(paperQuestionQueryWrapper);
            //进行自动评分
            answer.setScore(getScore(paperQuestion.getScore(), question, answer.getAnswer()));
            //设置时间
            answer.setCreateTime(LocalDateTime.now());
            //当题目非简答题并且题目没有达到满分时记录错题
            if (question.getType() < 4 && answer.getScore() < paperQuestion.getScore()) {
                WrongRecord wrongRecord = new WrongRecord();
                //先set知识点属性,查询
                wrongRecord.setPointId(question.getPointId());
                WrongRecord one = wrongRecordService.getOne(new QueryWrapper<>(wrongRecord));
                if (one != null) {
                    one.setNum(one.getNum() + 1);
                    wrongRecordService.updateById(one);
                } else {
                    wrongRecord.setUserId(answer.getWriterId());
                    wrongRecord.setNum(1);
                    wrongRecord.setCreateTime(LocalDateTime.now());
                    wrongRecord.setUpdateTime(LocalDateTime.now());
                    wrongRecordService.save(wrongRecord);
                }
            }
        }
        return new ResponseWrapper(true, 200, "提交成功", answerService.saveBatch(answerList));
    }

    @ApiOperation(value = "打分", notes = "传入answerId，对该Answer打分", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/grade")
    public ResponseWrapper getResults(Long answerId, int score) {
        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setScore(score);
        answerService.updateById(answer);
        return new ResponseWrapper(true, 200, "打分成功", null);
    }

    public Integer getScore(int score, Question question, String answer) {
        //简答题不自动处理，等待老师手动处理
        switch (question.getType()) {
            case 0:
            case 2: {
                if (question.getRightAnswer().equals(answer))
                    return score;
                else return 0;
            }
            case 1: {
                String[] rightAnswers = question.getRightAnswer().split(",");
                String[] answers = answer.split(",");
                Arrays.sort(rightAnswers);
                Arrays.sort(answers);
                //例如答案是3项，答题人选了2项，直接返回0分
                if (rightAnswers.length != answer.length()) {
                    return 0;
                }
                for (int i = 0; i < rightAnswers.length; i++) {
                    if (rightAnswers[i].equals(answers[i])) {
                        continue;
                    }
                    return 0;
                }
                return score;
            }
            case 3: {
                String[] rightAnswers = question.getRightAnswer().split(",");
                String[] answers = answer.split(",", -1);
                int finalScore = 0;
                for (int i = 0; i < rightAnswers.length; i++) {
                    if (rightAnswers[i].equals(answers[i])) {
                        finalScore += score / answers.length;
                    }
                }
                return finalScore;
            }
            default:
                return null;
        }
    }
}

