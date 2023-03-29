
package com.safecode.security.subject.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String folder = "D:/WEBSpace/security-parent/security-subject/src/main/java/com/safecode/security/subject/file/";

    @PostMapping("/upload")
    public String upload(MultipartFile file) throws Exception {
        // 判断附件
        if (file != null && file.getSize() > 0) {

            String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
            String baseName = FilenameUtils.getBaseName(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String newName = uuid + "." + suffix;

            logger.info("文件后缀：" + suffix);
            logger.info("baseName 指获取文件的名称，不包含后缀名：" + baseName);
            logger.info("uuid：" + uuid);
            logger.info("新文件名：" + newName);
            logger.info("上传时参数名称：" + file.getName());
            logger.info("文件名称：" + file.getOriginalFilename());
            logger.info("文件大小：" + file.getSize());
            File localFile = new File(folder, new Date().getTime() + ".txt");

            // 第三方文件系统 用流来读取 然后存储
            file.transferTo(localFile);
            // 绝对路径
            return localFile.getAbsolutePath();
        }

        return "文件上传为空";
    }

    @GetMapping("{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 用Response写回去，所以不需要返回值
        // jdk 1.7 新语法 将流写在try()就无需在finally语句块中关闭流，jdk自己就会关闭
        try (InputStream input = new FileInputStream(new File(folder, id + ".txt"));
             OutputStream output = response.getOutputStream();) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");

            // 将byte数组写进输出流里面 fastdfs里面会用到
            // IOUtils.write(byte[], output);
            IOUtils.copy(input, output);
            output.flush();
        }

    }

    /**
     * 获取src/main/resources下的文件 例如读取redis路径下的redis.properties
     *
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @GetMapping("/src/main/resources")
    public String resourcesFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("redis/redis.properties");
        File file = resource.getFile();
        InputStream input = resource.getInputStream();
        String filename = resource.getFilename();
        String path = resource.getPath();
        URI uri = resource.getURI();
        URL url = resource.getURL();
        List<String> lines = FileUtils.readLines(file, "utf-8");
        for (String line : lines) {
            logger.info(line);
        }
        String content = StringUtils.join(lines, "\n");
        logger.info(content);
        return content;
    }

}
