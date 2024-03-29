import com.google.common.util.concurrent.AbstractIdleService;
import com.jiagouedu.services.front.account.AccountService;
import com.jiagouedu.services.front.account.bean.Account;
import com.jiagouedu.services.front.order.bean.Order;
import com.jiagouedu.services.front.order.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class TradeBootstrap extends AbstractIdleService implements ServletContextListener {

    private ClassPathXmlApplicationContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeBootstrap.class);

    public static void main(String[] args) {
        TradeBootstrap bootstrap = new TradeBootstrap();
        bootstrap.startAsync();
        try {
            Object lock = new Object();
            synchronized (lock) {
                while (true) {
                    lock.wait();
                }
            }
        } catch (InterruptedException ex) {
            LOGGER.error("ignore interruption", ex);
        }
    }


    @Override
    protected void startUp() throws Exception {
        LOGGER.info("===================shop-goods START ....==========================");
        context = new ClassPathXmlApplicationContext(new String[]{"spring/applicationContext.xml"});
        context.start();
        AccountService accountService = (AccountService) context.getBean("membersService");
        Account account = accountService.selectById("44");
        System.out.println(account.getId());
        context.registerShutdownHook();
        LOGGER.info("shop-goods service started successfully");


    }


    @Override
    protected void shutDown() throws Exception {
        context.stop();
        LOGGER.info("service stopped successfully");
    }


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.info("shop-goods service started ");
        try {
            startUp();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("ignore interruption ");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            shutDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

