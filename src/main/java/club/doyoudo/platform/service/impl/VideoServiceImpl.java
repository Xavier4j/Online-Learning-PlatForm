package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Video;
import club.doyoudo.platform.mapper.VideoMapper;
import club.doyoudo.platform.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

}
