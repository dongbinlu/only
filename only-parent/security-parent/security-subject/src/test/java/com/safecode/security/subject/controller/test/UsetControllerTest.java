package com.safecode.security.subject.controller.test;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//如何运行这个测试用例，用SpringRunner来运行
@RunWith(SpringRunner.class)
@SpringBootTest
public class UsetControllerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    //perform 执行
    @Test
    public void whenQuerySuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user").param("username", "jojo")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));

    }

    @Test
    public void whenQueryConditionsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user").param("account", "jojo").param("mail", "jojo@qq.com")
                .param("deptId", "1").param("page", "2").param("size", "15").param("sort", "id,desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));

    }

    @Test
    public void whenInfoIntegerSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("tome"));
    }

    @Test
    public void whenInfoStringSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/a").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value("String"));
    }

    @Test
    public void whenJsonViewSuccess() throws Exception {
        String result = mockMvc
                .perform(MockMvcRequestBuilders.get("/user/jsonView").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenCreateSuccess() throws Exception {
        Date date = new Date();
        long time = date.getTime();
        String content = "{\"account\":\"admin\",\"deptId\":\"1\",\"password\":\"   \",\"mail\":\"admin@qq.com\",\"birthday\":"
                + time + "}";
        String result = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/create").content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1)).andReturn().getResponse()
                .getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenUpdateSuccess() throws Exception {
        Date date = new Date(
                LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        long time = date.getTime();
        String content = "{\"id\":\"30\",\"account\":\"admin\",\"deptId\":\"1\",\"password\":null,\"mail\":\"admin@qq.com\",\"birthday\":"
                + time + "}";
        String result = mockMvc
                .perform(MockMvcRequestBuilders.put("/user/update/30").content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(30)).andReturn().getResponse()
                .getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenUploadSuccess() throws UnsupportedEncodingException, Exception {
        String result = mockMvc
                .perform(MockMvcRequestBuilders.fileUpload("/file/upload")
                        .file(new MockMultipartFile("file", "test.txt", "multipart/form-data",
                                "hello upload".getBytes("UTF-8"))))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        logger.info("上传路径：" + result);
    }

}
