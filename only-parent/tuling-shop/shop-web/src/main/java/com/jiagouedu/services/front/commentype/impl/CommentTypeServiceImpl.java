package com.jiagouedu.services.front.commentype.impl;import com.jiagouedu.core.ServersManager;import com.jiagouedu.services.front.commentype.CommentTypeService;import com.jiagouedu.services.front.commentype.bean.CommentType;import com.jiagouedu.services.front.commentype.dao.CommentTypeDao;import org.springframework.stereotype.Service;import javax.annotation.Resource;@Service("commentTypeServiceFront")public class CommentTypeServiceImpl extends ServersManager<CommentType, CommentTypeDao>        implements CommentTypeService {    @Resource(name = "commentTypeDaoFront")    @Override    public void setDao(CommentTypeDao commentTypeDao) {        this.dao = commentTypeDao;    }}