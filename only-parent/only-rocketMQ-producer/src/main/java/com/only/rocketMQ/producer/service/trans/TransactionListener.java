package com.only.rocketMQ.producer.service.trans;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RocketMQTransactionListener(txProducerGroup = "TxPayGroup")
public class TransactionListener implements RocketMQLocalTransactionListener {

    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();


    /**
     * 执行本地事务
     * <p>
     * 提交事务，它允许消费者消费此消息
     * COMMIT_MESSAGE,
     *
     * 回滚事务，它代表该消息将被删除，不允许被消费
     * ROLLBACK_MESSAGE,
     *
     * 中间状态，它代表需要检查消息队列来确定状态(长事务)
     * UNKNOW,
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        // 获取事务ID
        String transId = (String) msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);

        int value = transactionIndex.getAndIncrement();

        int status = value % 3;

        localTrans.put(transId, status);

        // 执行本地事务 业务入库逻辑

        if (status == 0) {
            // 提交事务
            System.out.printf(" # COMMIT # simulating %s related local transaction exec succeedd! %n", msg);
            return RocketMQLocalTransactionState.COMMIT;
        }
        if (status == 1) {
            // 本地事务回滚
            System.out.printf(" # ROLLBACK # simulating %s related local transaction exec failed! %n", msg);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        // 事务状态不确定，待broker发送ack回查本地事务状态
        System.out.printf(" # UNKNOW # simulating %s related local transaction exec UNKNOWN! \n");
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    /**
     * 执行本地事务时可能失败，或者异步提交，导致事务状态暂时不能确认，broker在
     * 一定时间后将会发起重试，broker会向TxPayGroup发起ack回查，
     * 这里producer相当于server端，broker相当于client端，所以可以看出broker&producer-group是双向通信的
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {

        // 业务逻辑，回查数据库看是否存在

        // 获取事务ID
        String transId = (String) msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);

        Integer status = localTrans.get(transId);

        RocketMQLocalTransactionState retState = null;
        if (null != status) {
            switch (status) {
                case 0:
                    retState = RocketMQLocalTransactionState.UNKNOWN;
                    break;
                case 1:
                    retState = RocketMQLocalTransactionState.COMMIT;
                    break;
                case 2:
                    retState = RocketMQLocalTransactionState.ROLLBACK;
                    break;
                default:
                    retState = RocketMQLocalTransactionState.COMMIT;
                    break;
            }
        }

        System.out.printf("checkLocalTransaction is executed once , " + "msgTransactionId=%s , transactionState=%s , status= %s  %s", transId, retState, status);

        return retState;
    }
}
