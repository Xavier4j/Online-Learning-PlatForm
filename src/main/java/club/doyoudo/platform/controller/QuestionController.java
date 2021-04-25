package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Question;
import club.doyoudo.platform.service.IPointService;
import club.doyoudo.platform.service.IQuestionService;
import club.doyoudo.platform.vo.QuestionListVo;
import club.doyoudo.platform.vo.QuestionWithPointAndScore;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
@RequestMapping("/question")
public class QuestionController {

    @Resource
    IQuestionService questionService;
    @Resource
    IPointService pointService;

    @ApiOperation(value = "新建创建问题", notes = "创建问题", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/create")
    @Transactional
    public ResponseWrapper create(@RequestBody Question question) {
        question.setCreateTime(LocalDateTime.now());
        String msg = question.getId() == null ? "创建成功！" : "修改成功";
        if (question.getType() == 3) {
            question.setBlankNum(question.getRightAnswer().split(",").length);
        }
        if (questionService.saveOrUpdate(question)) {
            return new ResponseWrapper(true, 200, msg, null);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "批量创建问题", notes = "批量创建问题", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/createBatch")
    @Transactional
    public ResponseWrapper createBatch(@RequestBody QuestionListVo QuestionListVo) {
        List<Question> questionList = QuestionListVo.getQuestionList();
        questionList.forEach(question -> {
            question.setCreateTime(LocalDateTime.now());
        });
        if (questionService.saveBatch(questionList)) {
            return new ResponseWrapper(true, 200, "批量创建成功", null);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "查询所有问题", notes = "分页查询问题列表，默认按照问题名字排序", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/selectAll")
    public ResponseWrapper selectAll(String search, Integer type, Long pointId, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建用户查询条件构造器
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        if (search != null) {
            questionQueryWrapper.like("question", search);
        }
        if (type != null) {
            questionQueryWrapper.eq("type", type);
        }
        if (pointId != null) {
            questionQueryWrapper.eq("point_id", pointId);
        }
        Page<Question> questionPage = new Page<>(current, size);
        Page<Question> page = questionService.page(questionPage, questionQueryWrapper);
        Page<QuestionWithPointAndScore> questionWithPointPage = new Page<>(current, size);
        questionWithPointPage.setTotal(page.getTotal());
        questionWithPointPage.setPages(page.getPages());
        List<Question> questionList = page.getRecords();
        List<QuestionWithPointAndScore> questionWithPointList = new ArrayList<>();
        questionList.forEach(question -> {
            QuestionWithPointAndScore questionWithPoint = JSONObject.parseObject(JSONObject.toJSONString(question), QuestionWithPointAndScore.class);
            questionWithPoint.setPoint(pointService.getById(questionWithPoint.getPointId()));
            questionWithPointList.add(questionWithPoint);
        });
        questionWithPointPage.setRecords(questionWithPointList);
        return new ResponseWrapper(true, 200, "查询成功", questionWithPointPage);
    }

    @ApiOperation(value = "查询问题", notes = "根据id查询问题", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/selectById")
    public ResponseWrapper selectById(Long questionId) {
        //创建用户查询条件构造器
        Question question = questionService.getById(questionId);
        return new ResponseWrapper(true, 200, "查询成功", question);
    }

    @ApiOperation(value = "随机查询问题", notes = "根据条件随机查询一个问题", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/selectOneRandom")
    public ResponseWrapper selectOneRandom(Integer type, Long pointId, String questionIds) {
        //创建用户查询条件构造器
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        if (type != null) {
            questionQueryWrapper.eq("type", type);
        }
        if (pointId != null) {
            questionQueryWrapper.eq("point_id", pointId);
        }
        if (questionIds != null && !questionIds.equals("")) {
            String[] questionIdStrList = questionIds.split(",");
            List<Long> questionIdList = new ArrayList<>();
            for (String idStr : questionIdStrList) {
                questionIdList.add(Long.parseLong(idStr));
            }
            questionQueryWrapper.notIn("id", questionIdList);
        }
        List<Question> questionList = questionService.list(questionQueryWrapper);
        if (questionList.size() == 0) {
            return new ResponseWrapper(true, 601, "没有复合条件的问题，请更改条件！", null);
        }
        Random random = new Random();
        int i = random.nextInt(questionList.size());
        QuestionWithPointAndScore questionWithPoint = JSONObject.parseObject(JSONObject.toJSONString(questionList.get(i)), QuestionWithPointAndScore.class);
        questionWithPoint.setPoint(pointService.getById(questionWithPoint.getPointId()));
        return new ResponseWrapper(true, 200, "查询成功", questionWithPoint);
    }

    @ApiOperation(value = "删除题目", notes = "根据id删除题目", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/delete")
    public ResponseWrapper delete(Long questionId) {
        return new ResponseWrapper(true, 200, "删除成功", questionService.removeById(questionId));
    }
}

