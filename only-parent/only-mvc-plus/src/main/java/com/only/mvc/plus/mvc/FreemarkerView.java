package com.only.mvc.plus.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreemarkerView {

    private String ftlPath;

    private Map<String, Object> models = new HashMap<>();

    public FreemarkerView(String ftlPath) {
        this.ftlPath = ftlPath;
    }

    public void setModel(String key, Object model) {
        models.put(key, model);
    }

    public void removeModel(String key) {
        models.remove(key);
    }


}
