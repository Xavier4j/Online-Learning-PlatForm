package club.doyoudo.platform.controller;


import club.doyoudo.platform.entity.Point;
import club.doyoudo.platform.service.IPointService;
import club.doyoudo.platform.vo.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseWrapper logIn(Point point) {
        //则进行插入操作
        point.setCreateTime(LocalDateTime.now());
        if (pointService.save(point)) {
            return new ResponseWrapper(true, 200, "创建成功", point.getId());
        } else {
            return new ResponseWrapper(false, 500, "系统异常，请稍后重试!", null);
        }
    }
}

