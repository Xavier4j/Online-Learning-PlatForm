package club.doyoudo.platform.vo;

import club.doyoudo.platform.entity.Notice;
import club.doyoudo.platform.entity.Profile;
import lombok.Data;

@Data
public class NoticeWithAuthor extends Notice {
    private Profile author;
}
