package com.jiagouedu.services.front.attributelink.dao;import com.jiagouedu.core.DaoManager;import com.jiagouedu.services.front.attributelink.bean.AttributeLink;public interface AttributeLinkDao extends DaoManager<AttributeLink> {    /**     * @param e     * @return     */    int deleteByCondition(AttributeLink e);}