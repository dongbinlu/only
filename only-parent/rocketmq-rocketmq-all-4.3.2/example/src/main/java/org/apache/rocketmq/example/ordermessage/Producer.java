/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.example.ordermessage;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class Producer {
    public static void main(String[] args) throws UnsupportedEncodingException {
        try {
            DefaultMQProducer producer = new DefaultMQProducer("blueboy_group_order");
            producer.setNamesrvAddr("localhost:9876");
            producer.setSendMsgTimeout(20000);
            producer.start();

            // 申明5个Tag
            String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};

            // 创建10个订单
            for (int i = 0; i < 10; i++) {
                // 订单编号
                int orderId = i % 10;
                /**
                 * topic:主题，一系列消息的集合
                 * tag:标签，消息分类用
                 * keys:消息查询用
                 */
                Message msg = new Message("blueboy_topic_order", tags[i % tags.length], "KEY" + i, ("Hello RocketMQ blueboy order " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    /**
                     *选择指定的队列存储消息
                     * @param mqs
                     * @param msg
                     * @param arg
                     * `arg`参数是一个用于附加信息的对象。在`select`方法中，它允许你传递额外的自定义参数，并在消息队列选择过程中进行使用。
                     *
                     * `select`方法通常用于选择特定的消息队列，以便将消息发送到该队列。使用`arg`参数可以传递任何类型的对象，以满足具体的业务需求。它提供了一种灵活的方式来处理选择逻辑。
                     *
                     * 你可以根据实际需求在`select`方法的实现中使用`arg`参数，例如根据特定条件选择消息队列、指定优先级、指定路由规则等等。根据具体的应用场景，`arg`参数可以用于传递任何相关的信息。
                     * @return
                     */
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                }, orderId);

                System.out.printf("%s%n", sendResult);
            }

            producer.shutdown();
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
