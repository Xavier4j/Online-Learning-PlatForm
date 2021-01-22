package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Point;
import club.doyoudo.platform.mapper.PointMapper;
import club.doyoudo.platform.service.IPointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-22
 */
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements IPointService {

}
