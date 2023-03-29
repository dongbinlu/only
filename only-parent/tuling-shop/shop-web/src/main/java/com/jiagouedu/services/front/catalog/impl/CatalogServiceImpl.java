package com.jiagouedu.services.front.catalog.impl;import com.jiagouedu.core.ServersManager;import com.jiagouedu.services.front.catalog.CatalogService;import com.jiagouedu.services.front.catalog.bean.Catalog;import com.jiagouedu.services.front.catalog.dao.CatalogDao;import org.springframework.stereotype.Service;import javax.annotation.Resource;import java.util.List;@Service("catalogServiceFront")public class CatalogServiceImpl extends ServersManager<Catalog, CatalogDao> implements        CatalogService {    @Resource(name = "catalogDaoFront")    @Override    public void setDao(CatalogDao catalogDao) {        this.dao = catalogDao;    }    // 加载根节点    public List<Catalog> loadRoot(Catalog e) {        List<Catalog> root = dao.selectList(e);        for (int i = 0; i < root.size(); i++) {            Catalog ee = root.get(i);//			SystemManager.catalogMap.put(Integer.valueOf(ee.getId()), Integer.valueOf(ee.getPid()));            loadChildrenByPid(ee);        }//		Catalog top = new Catalog();//		top.setName("-");//		top.setId("0");//		top.setPid("0");//		root.add(0, top);        return root;    }    /**     * 加载指定节点下的全部子节点     *     * @param item     */    private void loadChildrenByPid(Catalog item) {        Catalog e = new Catalog();        e.setPid(item.getId());        item.setChildren(dao.selectList(e));        if (item.getChildren() != null && item.getChildren().size() > 0) {            for (int i = 0; i < item.getChildren().size(); i++) {                Catalog ee = item.getChildren().get(i);//				SystemManager.catalogMap.put(Integer.valueOf(ee.getId()), Integer.valueOf(ee.getPid()));                loadChildrenByPid(ee);            }        }    }}