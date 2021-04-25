package club.doyoudo.platform.controller;

import club.doyoudo.platform.entity.Course;
import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/search")
public class SearchController {

    @ApiOperation(value = "搜索", notes = "搜索所有相关", produces = "application/json", httpMethod = "GET")
    @RequestMapping("/search")
    public ResponseWrapper searchAll(String search) {
        return new ResponseWrapper(true, 200, "查询成功", null);
    }
}
