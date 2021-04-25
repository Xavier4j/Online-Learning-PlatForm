package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Answer;
import club.doyoudo.platform.entity.Paper;
import club.doyoudo.platform.entity.Question;
import club.doyoudo.platform.service.IAnswerService;
import club.doyoudo.platform.service.IPaperService;
import club.doyoudo.platform.service.IQuestionService;
import club.doyoudo.platform.vo.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
@RequestMapping("/paper")
public class PaperController {

    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);

    // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
    public final static String EXPORT_PATH_PREFIX = "/export/";

    @Resource
    IPaperService paperService;
    @Resource
    IAnswerService answerService;
    @Resource
    IQuestionService questionService;

    @ApiOperation(value = "模糊查询试卷列表", notes = "分页查询所有试卷列表，按照时间倒序", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/search")
    public ResponseWrapper searchAll(String paperName, Boolean isTest, Long userId, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建用户查询条件构造器
        QueryWrapper<Paper> paperQueryWrapper = new QueryWrapper<>();
        if (paperName != null) {
            paperQueryWrapper.like("name", paperName);
        }
        if (isTest != null) {
            paperQueryWrapper.eq("is_test", isTest);
        } else {
            paperQueryWrapper.eq("is_test", false);
        }
        paperQueryWrapper.orderByDesc("create_time");
        Page<Paper> paperPage = new Page<>(current, size);
        Page<Paper> page = paperService.page(paperPage, paperQueryWrapper);

        if (userId == null) {   //如果userId为NULL，则为管理员的考试列表调用，无需更多操作
            return new ResponseWrapper(true, 200, "查询成功", page);
        } else {     //如果userId不为NUll，则为用户的考试列表调用，需知道用户是否已经完成考试，需要将Paper封装为PaperWithState
            Page<PaperWithState> paperWithStatePage = new Page<>(current, size);
            paperWithStatePage.setPages(page.getPages());
            paperWithStatePage.setTotal(page.getTotal());
            List<PaperWithState> paperWithStateList = new ArrayList<>();
            page.getRecords().forEach(paper -> {
                PaperWithState paperWithState = JSONObject.parseObject(JSONObject.toJSONString(paper), PaperWithState.class);

                QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
                answerQueryWrapper.eq("paper_id", paper.getId());
                answerQueryWrapper.eq("writer_id", userId);
                paperWithState.setDone(answerService.list(answerQueryWrapper).size() > 0);

                paperWithStateList.add(paperWithState);
            });
            paperWithStatePage.setRecords(paperWithStateList);
            return new ResponseWrapper(true, 200, "查询成功", paperWithStatePage);
        }
    }

    @ApiOperation(value = "创建或者修改试卷或者知识点测试", notes = "传入试卷即可，有id则为修改，否则为创建", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/create")
    public ResponseWrapper create(@RequestBody PaperWithQuestionList paperWithQuestionList) {
        //判断是创建还是修改
        String msg = paperWithQuestionList.getId() == null ? "创建成功！" : "修改成功";
        //如果是知识点测试
        if (paperWithQuestionList.getIsTest() != null && paperWithQuestionList.getIsTest()) {
            //创建用户查询条件构造器
            QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
            questionQueryWrapper.ne("type", 4);
            if (paperWithQuestionList.getPointId() != null) {
                questionQueryWrapper.eq("point_id", paperWithQuestionList.getPointId());
            }
            List<Question> questionList = questionService.list(questionQueryWrapper);
            if (questionList.size() == 0) {
                return new ResponseWrapper(true, 601, "没有复合条件的问题，请更改条件！", null);
            }
            //如果当前知识点的题目小于十条,则全部返回
            List<QuestionWithPointAndScore> questionWithPointAndScoreList = new ArrayList<>();
            if (questionList.size() < 10) {
                for (Question question : questionList) {
                    QuestionWithPointAndScore questionWithPointAndScore = JSONObject.parseObject(JSONObject.toJSONString(question), QuestionWithPointAndScore.class);
                    questionWithPointAndScoreList.add(questionWithPointAndScore);
                }
            } else {
                //从中随机选取十道题
                Random random = new Random();
                for (int j = 0; j < 10; j++) {
                    int i = random.nextInt(questionList.size());
                    QuestionWithPointAndScore questionWithPointAndScore = JSONObject.parseObject(JSONObject.toJSONString(questionList.remove(i)), QuestionWithPointAndScore.class);
                    questionWithPointAndScoreList.add(questionWithPointAndScore);
                }
            }
            paperWithQuestionList.setQuestionList(questionWithPointAndScoreList);
            paperWithQuestionList.setStartTime(LocalDateTime.now());
            paperWithQuestionList.setEndTime(LocalDateTime.now().plusYears(99));
        }
        Long paperId = paperService.saveOrUpdatePaperWithQuestionList(paperWithQuestionList);
        if (paperId != null) {
            return new ResponseWrapper(true, 200, msg, paperId);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "查询试卷", notes = "根据id查询试卷内容", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/select")
    public ResponseWrapper select(Long paperId) {
        return new ResponseWrapper(true, 200, "查询成功", paperService.getPaperWithQuestionList(paperId));
    }

    @ApiOperation(value = "查询答卷结果List", notes = "根据Paper的id查询答卷结果List", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/result-list")
    public ResponseWrapper selectResultList(Long paperId, @RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        Page<PaperResultVo> page = paperService.getPaperResultListByPage(paperId, current, size);
        return new ResponseWrapper(true, 200, "查询成功", page);
    }

    @ApiOperation(value = "查询考试结果", notes = "根据id查询考试结果", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/result")
    public ResponseWrapper selectResult(Long paperId, Long userId) {
        PaperWithQuestionList paperWithQuestionList = paperService.getPaperWithQuestionAndAnswerList(paperId, userId);
        return new ResponseWrapper(true, 200, "查询成功", paperWithQuestionList);
    }

    @ApiOperation(value = "导出考试结果", notes = "根据id导出考试结果为Excel", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/export")
    public ResponseWrapper exportResult(Long paperId, HttpServletRequest request) {
        Paper paper = paperService.getById(paperId);
        List<PaperResultVo> paperResultList = paperService.getPaperResultList(paperId);

        //新建excel报表
        HSSFWorkbook excel = new HSSFWorkbook();
        //添加一个sheet，名字叫"我的POI之旅"
        HSSFSheet hssfSheet = excel.createSheet("成绩");
        //往excel表格创建一行，excel的行号是从0开始的
        HSSFRow hssfRow = hssfSheet.createRow(0);
        //第一行创建第一个单元格
        HSSFCell hssfCell = hssfRow.createCell(0);
        //设置第一个单元格的值
        hssfCell.setCellValue("名称");
        //第一行创建第二个单元格
        hssfCell = hssfRow.createCell(1);
        //设置第二个单元格的值
        hssfCell.setCellValue("成绩");
        hssfCell = hssfRow.createCell(2);
        hssfCell.setCellValue("交卷时间");
        for (int i = 0; i < paperResultList.size(); i++) {
            PaperResultVo paperResultVo = paperResultList.get(i);
            hssfRow = hssfSheet.createRow(1);
            hssfCell = hssfRow.createCell(0);
            hssfCell.setCellValue(paperResultVo.getWriter().getNickName());
            hssfCell = hssfRow.createCell(1);
            hssfCell.setCellValue(paperResultVo.getScore());
            hssfCell = hssfRow.createCell(2);
            hssfCell.setCellValue(paperResultVo.getAnswerTime());
        }
        //导出文件存储
        FileOutputStream fout = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
        //构建文件导出所要保存的"文件夹路径"--这里是相对路径，保存到项目根路径的文件夹下
        String realPath = new String("static" + EXPORT_PATH_PREFIX);
        logger.info("-----------导出文件保存的路径【" + realPath + "】-----------");
        String format = LocalDateTime.now().format(formatter);
        //存放导出文件的文件夹
        File file = new File(realPath + format);
        logger.info("-----------存放导出文件的文件夹【" + file + "】-----------");
        logger.info("-----------输出文件夹绝对路径 -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径【" + file.getAbsolutePath() + "】-----------");
        if (!file.isDirectory()) {
            //递归生成文件夹
            file.mkdirs();
        }
        //文件名字
        String fileName = paper.getTitle() + UUID.randomUUID().toString() + ".xls";
        logger.info("-----------文件要保存后的名字【" + fileName + "】-----------");
        try {
            //用流将其写到指定路径
            logger.info("-----------文件要保存后的新名字【" + fileName + "】-----------");
            fout = new FileOutputStream(file.getAbsolutePath() + File.separator + fileName);
            excel.write(fout);
            fout.close();
            String filePath = request.getScheme() + "://" + request.getServerName() + ":8090" + EXPORT_PATH_PREFIX + format + fileName;
            logger.info("-----------【" + filePath + "】-----------");
            return new ResponseWrapper(true, 200, "导出成功", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseWrapper(false, 400, "导出失败，原因未知", null);
    }

    @ApiOperation(value = "删除试卷", notes = "根据id删除试卷内容", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/delete")
    public ResponseWrapper delete(Long paperId) {
        return new ResponseWrapper(true, 200, "删除成功", paperService.removeById(paperId));
    }
}

