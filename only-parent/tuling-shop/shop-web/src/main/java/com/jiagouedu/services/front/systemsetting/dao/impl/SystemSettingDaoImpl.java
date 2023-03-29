package com.jiagouedu.services.front.systemsetting.dao.impl;import com.jiagouedu.core.dao.BaseDao;import com.jiagouedu.core.dao.page.PagerModel;import com.jiagouedu.services.front.systemsetting.bean.SystemSetting;import com.jiagouedu.services.front.systemsetting.dao.SystemSettingDao;import org.springframework.stereotype.Repository;import javax.annotation.Resource;import java.util.List;@Repository("systemSettingDao")public class SystemSettingDaoImpl implements SystemSettingDao {    @Resource    private BaseDao dao;    public void setDao(BaseDao dao) {        this.dao = dao;    }    public PagerModel selectPageList(SystemSetting e) {        return dao.selectPageList("front.systemSetting.selectPageList",                "front.systemSetting.selectPageCount", e);    }    public List selectList(SystemSetting e) {        return dao.selectList("front.systemSetting.selectList", e);    }    public SystemSetting selectOne(SystemSetting e) {        return (SystemSetting) dao.selectOne("front.systemSetting.selectOne", e);    }    public int delete(SystemSetting e) {        return dao.delete("front.systemSetting.delete", e);    }    public int update(SystemSetting e) {        return dao.update("front.systemSetting.update", e);    }    public int deletes(String[] ids) {        SystemSetting e = new SystemSetting();        for (int i = 0; i < ids.length; i++) {            e.setId(ids[i]);            delete(e);        }        return 0;    }    public int insert(SystemSetting e) {        return dao.insert("front.systemSetting.insert", e);    }    public int deleteById(int id) {        return dao.delete("front.systemSetting.deleteById", id);    }    @Override    public SystemSetting selectById(String id) {        // TODO Auto-generated method stub        return null;    }}