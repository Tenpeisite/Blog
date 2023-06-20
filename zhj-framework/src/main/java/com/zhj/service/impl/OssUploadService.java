package com.zhj.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.exception.SystemException;
import com.zhj.service.UploadService;
import com.zhj.utils.PathUtils;
import com.zhj.utils.RedisCache;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/10 17:48
 */
@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String website;

    //@Autowired
    //private RedisTemplate redisTemplate;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        // 判断文件类型或者文件大小
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        if (!originalFilename.endsWith(".jpg") && !originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpeg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果判断通过上传文件到oss
        String filePath = PathUtils.generateFilePath(originalFilename);
        //redisTemplate.opsForSet().add(SystemConstants.Article_PIC_RESOURCES, filePath);
        String url = uploadOss(img, filePath);
        return ResponseResult.okResult(url);
    }

    public String uploadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return website + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }

    public void deleteOss(String key) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    //public void getAllOss() {
    //    //构造一个带指定 Region 对象的配置类
    //    Configuration cfg = new Configuration(Region.region2());
    //
    //    Auth auth = Auth.create(accessKey, secretKey);
    //    BucketManager bucketManager = new BucketManager(auth, cfg);
    //
    //    //文件名前缀
    //    String prefix = "";
    //    //每次迭代的长度限制，最大1000，推荐值 1000
    //    int limit = 1000;
    //    //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
    //    String delimiter = "";
    //
    //    //列举空间文件列表
    //    BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
    //    while (fileListIterator.hasNext()) {
    //        //处理获取的file list结果
    //        FileInfo[] items = fileListIterator.next();
    //        for (FileInfo item : items) {
    //            System.out.println(item.key);
    //            System.out.println(item.hash);
    //            System.out.println(item.fsize);
    //            System.out.println(item.mimeType);
    //            System.out.println(item.putTime);
    //            System.out.println(item.endUser);
    //        }
    //    }
    //
    //}
}
