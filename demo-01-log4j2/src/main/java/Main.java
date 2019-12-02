
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Long.MAX_VALUE;

public class Main {
    static {
        System.setProperty("log4j.configurationFile", "conf/log4j2.xml");
        // 对应log4j2.xml 配置文件中使用的 ${sys:log4j2.dir:-demo-log}
        System.setProperty("log4j2.dir", "demo-01");
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("main starting...");
        for (long i = 1; i < MAX_VALUE; i++) {
            logger.info("i: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException", e);
            }
        }

        logger.info("main finished.");
    }
}
