package com.jiagouedu.services.front.questionnaireItem;import com.jiagouedu.core.Services;import com.jiagouedu.services.front.questionnaireItem.bean.QuestionnaireItem;import java.util.List;public interface QuestionnaireItemService extends Services<QuestionnaireItem> {    void insertList(List<QuestionnaireItem> itemList);}