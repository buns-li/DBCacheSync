package utils;

import core.PlatGlobal;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by buns on 12/27/15.
 */
public class log4Test extends TestCase {
    private static Logger logger = Logger.getLogger(log4Test.class);

    static {
        /**
         * 加载log4配置
         */
        PropertyConfigurator.configure(PlatGlobal.instance().getProjectRootPath() + "/sysProperties/log4.properties");
    }

    public void testDebug() {
        logger.debug("this is a log4 debug method test case!");
        logger.info("this is a log4 info method test case!");
        logger.error("this is a log4 error method test case!");
        logger.fatal("this is a log4 fatal method test case!");
    }
}
