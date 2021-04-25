package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Point;
import club.doyoudo.platform.service.IPointService;
import club.doyoudo.platform.vo.CourseWithVideoList;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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
@RequestMapping("/point")
public class PointController {

    @Resource
    IPointService pointService;

    @ApiOperation(value = "创建知识点", notes = "只需传入知识点即可", produces = "application/json", httpMethod = "POST")
    @RequestMapping("/create")
    public ResponseWrapper create(@RequestBody
                                          Point point) {
        QueryWrapper<Point> pointQueryWrapper = new QueryWrapper<>();
        pointQueryWrapper.eq("point", point.getPoint());
        if (pointService.getOne(pointQueryWrapper) != null) {
            return new ResponseWrapper(false, 601, "当前知识点已存在!", null);
        }
        //则进行插入操作
        point.setCreateTime(LocalDateTime.now());
        if (pointService.save(point)) {
            return new ResponseWrapper(true, 200, "创建成功", point.getId());
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

    @ApiOperation(value = "查询所有知识点", notes = "分页查询知识点列表，默认按照知识点名字排序", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/selectAll")
    public ResponseWrapper selectAll(@RequestParam(required = false, defaultValue = "1") int current, @RequestParam(required = false, defaultValue = "15") int size) {
        //创建用户查询条件构造器
        QueryWrapper<Point> pointQueryWrapper = new QueryWrapper<>();
        Page<Point> coursePage = new Page<>(current, size);
        pointQueryWrapper.orderByAsc("point");
        Page<Point> page = pointService.page(coursePage, pointQueryWrapper);
        return new ResponseWrapper(true, 200, "查询成功", page);
    }

    @ApiOperation(value = "修改知识点信息", notes = "根据知识点id修改知识点信息", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/update")
    public ResponseWrapper update(@RequestBody Point point) {
        if (pointService.updateById(point)) {
            return new ResponseWrapper(true, 200, "修改成功", null);
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }

}

