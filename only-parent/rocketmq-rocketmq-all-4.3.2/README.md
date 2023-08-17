## Apache RocketMQ [![Build Status](https://travis-ci.org/apache/rocketmq.svg?branch=master)](https://travis-ci.org/apache/rocketmq) [![Coverage Status](https://coveralls.io/repos/github/apache/rocketmq/badge.svg?branch=master)](https://coveralls.io/github/apache/rocketmq?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.rocketmq/rocketmq-all/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Corg.apache.rocketmq)
[![GitHub release](https://img.shields.io/badge/release-download-orange.svg)](https://rocketmq.apache.org/dowloading/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


启动方式一：
打开Namesrv服务入口类运行
org.apache.rocketmq.namesrv.NamesrvStartup#main

打开Broker服务入口类运行
org.apache.rocketmq.broker.BrokerStartup#main


不配环境变量，需要修改启动类

#修改Namesrv启动类
org.apache.rocketmq.namesrv.NamesrvStartup#createNamesrvController
//添加本行代码,路径为Rocketmq项目路径下的子项目distribution工程路径
namesrvConfig.setRocketmqHome("D:\\only\\only-parent\\rocketmq-rocketmq-all-4.3.2\\distribution");
if (null == namesrvConfig.getRocketmqHome()) {
    System.out.printf("Please set the %s variable in your environment to match the location of the RocketMQ installation % n", MixAll.ROCKETMQ_HOME_ENV);
    System.exit(-2);
}
#修改Broker启动类
org.apache.rocketmq.broker.BrokerStartup#createBrokerController
//添加本行代码
brokerConfig.setRocketmqHome("D:\\only\\only-parent\\rocketmq-rocketmq-all-4.3.2\\distribution");
if (null == brokerConfig.getRocketmqHome()) {
    System.out.printf("Please set the %s variable in your environment to match the location of the RocketMQ installation", MixAll.ROCKETMQ_HOME_ENV);
    System.exit(-2);
}

修改代码后还需要添加参数来运行服务
参数添加在Program arguments
启动Namesrv
-n localhost:9876
启动Broker
-n localhost:9876 -c D:\\only\\only-parent\\rocketmq-rocketmq-all-4.3.2\\distribution\\conf\\broker.conf


启动方式二（优先选择）
#添加NameSrv环境变量--注意在Environment variables中添加
ROCKETMQ_HOME=D:\\only\\only-parent\\rocketmq-rocketmq-all-4.3.2\\distribution
#添加Broker环境变量
ROCKETMQ_HOME=D:\\only\\only-parent\\rocketmq-rocketmq-all-4.3.2\\distribution

还需要添加参数来运行服务
参数添加在Program arguments
启动Namesrv
-n localhost:9876
启动Broker
-n localhost:9876 -c D:\\only\\only-parent\\rocketmq-rocketmq-all-4.3.2\\distribution\\conf\\broker.conf





**[Apache RocketMQ](https://rocketmq.apache.org) is a distributed messaging and streaming platform with low latency, high performance and reliability, trillion-level capacity and flexible scalability.**

It offers a variety of features:

* Pub/Sub messaging model
* Scheduled message delivery
* Message retroactivity by time or offset
* Log hub for streaming
* Big data integration
* Reliable FIFO and strict ordered messaging in the same queue
* Efficient pull&push consumption model
* Million-level message accumulation capacity in a single queue
* Multiple messaging protocols like JMS and OpenMessaging
* Flexible distributed scale-out deployment architecture
* Lightning-fast batch message exchange system
* Various message filter mechanics such as SQL and Tag
* Docker images for isolated testing and cloud isolated clusters
* Feature-rich administrative dashboard for configuration, metrics and monitoring


----------

## Learn it & Contact us
* Mailing Lists: <https://rocketmq.apache.org/about/contact/>
* Home: <https://rocketmq.apache.org>
* Docs: <https://rocketmq.apache.org/docs/quick-start/>
* Issues: <https://github.com/apache/rocketmq/issues>
* Ask: <https://stackoverflow.com/questions/tagged/rocketmq>
* Slack: <https://rocketmq-invite-automation.herokuapp.com/>
 

----------

## Apache RocketMQ Community
* [RocketMQ Community Projects](https://github.com/apache/rocketmq-externals)

----------

## Contributing
We always welcome new contributions, whether for trivial cleanups, big new features or other material rewards, more details see [here](http://rocketmq.apache.org/docs/how-to-contribute/) 
 
----------
## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation
