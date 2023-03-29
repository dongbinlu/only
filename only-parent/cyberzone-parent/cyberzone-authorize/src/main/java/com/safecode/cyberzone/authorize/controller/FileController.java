package com.safecode.cyberzone.authorize.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Preconditions;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.entity.SysLog;
import com.safecode.cyberzone.authorize.properties.SecurityProperties;
import com.safecode.cyberzone.authorize.service.SysLogService;
import com.safecode.cyberzone.authorize.utils.FastDFSClientWrapper;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.base.dto.ResponseData;

@RestController
@RequestMapping("/file")
public class FileController {

    private final static String UserTemplate = "/static/user.xls";

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/upload")
    public ResponseData file(MultipartFile[] files) throws IOException {
        Preconditions.checkNotNull(files, "请选择需要上传的文件");
        List<String> urls = Lists.newArrayList();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

            if (!checkSuffix(suffix)) {
                return new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传文件不支持", null);
            }
            //检查是否是图片  核心校验
            BufferedImage bi = ImageIO.read(file.getInputStream());
            if (bi == null) {
                return new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传文件不支持", null);
            }


            String contentType = file.getContentType();
            if (checkExist(contentType)) {
                addLog("上传文件:" + file.getOriginalFilename(), file.getOriginalFilename(), "/file/upload");
                String path = fastDFSClientWrapper.uploadFile(file);
                urls.add(securityProperties.getOther().getFastdfs_url() + path);
            } else {
                logger.warn("上传文件不支持");
                return new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传文件不支持", null);
            }
        }
        logger.info("文件上传成功");
        return new ResponseData(HttpStatus.OK.value(), "上传成功", urls);
    }

    @GetMapping("/downLoad")
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream input = FileController.class.getResourceAsStream(UserTemplate);

        response.reset();
        response.setContentType("application/x-msdownload;charset=utf-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("用户模板" + ".xls").getBytes("gbk"), "iso-8859-1"));// 下载文件的名称
            addLog("下载文件:" + "用户模板.xls", "", "/file/downLoad");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServletOutputStream sout = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(input);
            bos = new BufferedOutputStream(sout);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
            bos.close();
            bis.close();
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }

    }

    private boolean checkExist(String contentType) {
        if (contentType.equals("image/jpeg") || contentType.equals("image/png")) {
            return true;

        } else {
            return false;
        }
    }

    private boolean checkSuffix(String suffix) {
        if (suffix.equals(".jpeg") || suffix.equals(".png") || suffix.equals(".jpg")) {
            return true;

        } else {
            return false;
        }
    }

    private void addLog(String remark, String requestObject, String requestUrl) {
        sysLogService.save(SysLog.builder().remark(remark).userId(AuthHolder.getCurrentUserId().longValue())
                .projectName("cyberzone-authorize").requestObject(requestObject).requestUrl(requestUrl)
                .createTime(new Date()).userName(AuthHolder.getCurrentUsername()).ip(IpUtil.getRemoteIp(request))
                .logStatus("操作日志").build());
    }
}
