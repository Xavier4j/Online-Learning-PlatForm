package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Test;
import club.doyoudo.platform.mapper.TestMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-03-22
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements IService<Test> {

}
