package club.doyoudo.platform.service.impl;

import club.doyoudo.platform.entity.Profile;
import club.doyoudo.platform.mapper.ProfileMapper;
import club.doyoudo.platform.service.IProfileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xavier4j
 * @since 2021-01-23
 */
@Service
public class ProfileServiceImpl extends ServiceImpl<ProfileMapper, Profile> implements IProfileService {
    @Resource
    ProfileMapper profileMapper;

    @Override
    public boolean insertProfile(Profile profile) {
        return profileMapper.insert(profile) == 1;
    }
}
