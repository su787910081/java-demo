
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static {
        System.setProperty("log4j.configurationFile", "conf/log4j2.xml");
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("main starting...");
        logger.info("main finished.");
    }
}
