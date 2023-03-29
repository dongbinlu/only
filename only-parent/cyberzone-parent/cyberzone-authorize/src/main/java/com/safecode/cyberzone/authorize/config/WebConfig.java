package com.safecode.cyberzone.authorize.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.pagehelper.PageInfo;
import com.safecode.cyberzone.authorize.interceptor.HttpInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private HttpInterceptor httpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return new Jackson2ObjectMapperBuilder().failOnUnknownProperties(false).serializerByType(PageInfo.class,
                new JsonPageSerializer());

    }

    @SuppressWarnings("rawtypes")
    public class JsonPageSerializer extends JsonSerializer<PageInfo> {

        @Override
        public void serialize(PageInfo page, JsonGenerator jsonGen, SerializerProvider serializerProvider)
                throws IOException, JsonProcessingException {
            ObjectMapper om = new ObjectMapper();
            jsonGen.writeStartObject();

            jsonGen.writeFieldName("pageNum");
            jsonGen.writeNumber(page.getPageNum());
            page.getSize();
            jsonGen.writeFieldName("pageSize");
            jsonGen.writeNumber(page.getPageSize());

            jsonGen.writeFieldName("size");
            jsonGen.writeNumber(page.getSize());

            jsonGen.writeFieldName("startRow");
            jsonGen.writeNumber(page.getStartRow());

            jsonGen.writeFieldName("endRow");
            jsonGen.writeNumber(page.getEndRow());

            jsonGen.writeFieldName("total");
            jsonGen.writeNumber(page.getTotal());

            jsonGen.writeFieldName("pages");
            jsonGen.writeNumber(page.getPages());

            jsonGen.writeFieldName("list");
            jsonGen.writeRawValue(
                    om.writerWithView(serializerProvider.getActiveView()).writeValueAsString(page.getList()));

            jsonGen.writeFieldName("firstPage");
            jsonGen.writeNumber(page.getFirstPage());

            jsonGen.writeFieldName("prePage");
            jsonGen.writeNumber(page.getPrePage());

            jsonGen.writeFieldName("nextPage");
            jsonGen.writeNumber(page.getNextPage());

            jsonGen.writeFieldName("lastPage");
            jsonGen.writeNumber(page.getLastPage());

            jsonGen.writeFieldName("isFirstPage");
            jsonGen.writeBoolean(page.isIsFirstPage());

            jsonGen.writeFieldName("isLastPage");
            jsonGen.writeBoolean(page.isIsLastPage());

            jsonGen.writeFieldName("hasPreviousPage");
            jsonGen.writeBoolean(page.isHasPreviousPage());

            jsonGen.writeFieldName("hasNextPage");
            jsonGen.writeBoolean(page.isHasNextPage());

            jsonGen.writeFieldName("navigatePages");
            jsonGen.writeNumber(page.getNavigatePages());

            jsonGen.writeFieldName("navigatepageNums");
            jsonGen.writeRawValue(om.writerWithView(serializerProvider.getActiveView())
                    .writeValueAsString(page.getNavigatepageNums()));

            jsonGen.writeEndObject();
        }

    }
}
