package com.jiagouedu.services.manage.order.dao;import com.jiagouedu.core.DaoManager;import com.jiagouedu.services.manage.order.bean.Order;import com.jiagouedu.services.manage.order.bean.OrdersReport;import com.jiagouedu.web.action.manage.report.ReportInfo;import java.util.List;public interface OrderDao extends DaoManager<Order> {    /**     * 查询指定时间范围内的订单的销量情况     *     * @param order     * @return     */    List<ReportInfo> selectOrderSales(Order order);    /**     * 查询指定时间范围内的产品的销量情况     *     * @param order     * @return     */    List<ReportInfo> selectProductSales(Order order);    List<Order> selectCancelList(Order order);    void updatePayMonery(Order e);    OrdersReport loadOrdersReport();}