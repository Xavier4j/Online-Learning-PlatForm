package club.doyoudo.platform.controller;

import club.doyoudo.platform.vo.ResponseWrapper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@RestController
@CrossOrigin
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
    public final static String UPLOAD_IMAGE_PATH_PREFIX = "/images/";
    public final static String UPLOAD_VIDEO_PATH_PREFIX = "/videos/";
    public final static String UPLOAD_OTHER_PATH_PREFIX = "/others/";

    //所有视频格式
    private final String[] videoTypes = {".ogm",".wmv",".mpg",".webm",".ogv",".mov",".asx",".mpeg",".mp4",".m4v",".avi"};
    //所有图片格式
    private final String[] imageTypes = {".tiff",".pjp",".jfif",".gif",".svg",".bmp",".png",".jpeg",".svgz",".jpg",".webp",".ico",".xbm",".dib",".tif",".pjpeg",".avif"};

    @PostMapping("/upload")
    public ResponseWrapper upload(MultipartFile uploadFile, HttpServletRequest request) {
        if (uploadFile.isEmpty()) {
            //返回选择文件提示
            return new ResponseWrapper(false, 400, "未选择文件", null);
        }
        String uploadPathPrefix;
        String fileSuffix = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
        if (Arrays.asList(videoTypes).contains(fileSuffix)){    //如果是视频
            uploadPathPrefix = UPLOAD_VIDEO_PATH_PREFIX;
        } else if (Arrays.asList(imageTypes).contains(fileSuffix)){ //如果是图片
            uploadPathPrefix = UPLOAD_IMAGE_PATH_PREFIX;
        } else {
            uploadPathPrefix = UPLOAD_OTHER_PATH_PREFIX;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
//        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd/" );
        //构建文件上传所要保存的"文件夹路径"--这里是相对路径，保存到项目根路径的文件夹下
        String realPath = new String("static" + uploadPathPrefix);
        logger.info("-----------上传文件保存的路径【" + realPath + "】-----------");
        String format = LocalDateTime.now().format(formatter);
        //存放上传文件的文件夹
        File file = new File(realPath + format);
        logger.info("-----------存放上传文件的文件夹【" + file + "】-----------");
        logger.info("-----------输出文件夹绝对路径 -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径【" + file.getAbsolutePath() + "】-----------");
        if (!file.isDirectory()) {
            //递归生成文件夹
            file.mkdirs();
        }
        //获取原始的名字  original:最初的，起始的  方法是得到原来的文件名在客户机的文件系统名称
        String oldName = uploadFile.getOriginalFilename();
        logger.info("-----------文件原始的名字【" + oldName + "】-----------");
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        logger.info("-----------文件要保存后的新名字【" + newName + "】-----------");
        try {
            //构建真实的文件路径
            File newFile = new File(file.getAbsolutePath() + File.separator + newName);
            //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
            uploadFile.transferTo(newFile);
            String filePath = request.getScheme() + "://" + request.getServerName() + ":8090" + uploadPathPrefix + format + newName;
            logger.info("-----------【" + filePath + "】-----------");
            return new ResponseWrapper(true, 200, "上传成功", filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseWrapper(false, 400, "上传失败，原因未知", null);
    }


}