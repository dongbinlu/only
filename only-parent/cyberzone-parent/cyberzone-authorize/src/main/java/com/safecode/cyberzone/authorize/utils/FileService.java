package com.safecode.cyberzone.authorize.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

@Service
public class FileService {

    @Autowired
    private FastDFSClientWrapper dfsClient;

    /**
     * 校验视频后缀
     *
     * @param file
     * @return boolean
     */
    public static boolean videoPostHasErr(MultipartFile file) {
        //校验视频后缀 TODO
        return false;
    }

    public List<String> getFilePaths(List<MultipartFile> files) {
        List<String> paths = Lists.newArrayList();
        if (files != null && !files.isEmpty())
            files.forEach(file -> {
                try {
                    if (!file.isEmpty()) {
                        //File localFile = null;
                        //localFile = FileUtil.saveToLocal(file, filePath);
                        //String path =  StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
                        String path = dfsClient.uploadFile(file);
                        paths.add(path);
                    }
                } catch (IOException e) {
                    paths.forEach(f -> dfsClient.deleteFile(f));
                    throw new RuntimeException(e.getMessage());
                }
            });
        return paths;
    }

    public String getFilePath(MultipartFile file) {
        String path = null;
        try {
            if (file != null && !file.isEmpty()) {
                path = dfsClient.uploadFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return path;
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        try {
            dfsClient.deleteFile(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}