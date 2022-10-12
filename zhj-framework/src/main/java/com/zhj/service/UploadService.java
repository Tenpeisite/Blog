package com.zhj.service;

import com.zhj.domin.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/10 17:47
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
