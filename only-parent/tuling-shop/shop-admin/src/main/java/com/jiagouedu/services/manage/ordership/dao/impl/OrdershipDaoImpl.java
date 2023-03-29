package com.jiagouedu.services.manage.ordership.dao.impl;import com.jiagouedu.core.dao.BaseDao;import com.jiagouedu.core.dao.page.PagerModel;import com.jiagouedu.services.manage.ordership.bean.Ordership;import com.jiagouedu.services.manage.ordership.dao.OrdershipDao;import org.springframework.stereotype.Repository;import javax.annotation.Resource;import java.util.List;@Repository("ordershipDaoManage")public class OrdershipDaoImpl implements OrdershipDao {    @Resource    private BaseDao dao;    public void setDao(BaseDao dao) {        this.dao = dao;    }    public PagerModel selectPageList(Ordership e) {        return dao.selectPageList("manage.ordership.selectPageList",                "manage.ordership.selectPageCount", e);    }    public List selectList(Ordership e) {        return dao.selectList("manage.ordership.selectList", e);    }    public Ordership selectOne(Ordership e) {        return (Ordership) dao.selectOne("manage.ordership.selectOne", e);    }    public int delete(Ordership e) {        return dao.delete("manage.ordership.delete", e);    }    public int update(Ordership e) {        return dao.update("manage.ordership.update", e);    }    public int deletes(String[] ids) {        Ordership e = new Ordership();        for (int i = 0; i < ids.length; i++) {            e.setId(ids[i]);            delete(e);        }        return 0;    }    public int insert(Ordership e) {        return dao.insert("manage.ordership.insert", e);    }    public int deleteById(int id) {        return dao.delete("manage.ordership.deleteById", id);    }    public Ordership selectById(String id) {        return (Ordership) dao.selectOne("manage.ordership.deleteById", id);    }}