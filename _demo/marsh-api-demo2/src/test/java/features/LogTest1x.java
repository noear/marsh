package features;

import apidemo2.App;
import org.junit.jupiter.api.Test;
import org.noear.solon.test.SolonTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear 2021/2/23 created
 */
@SolonTest(App.class)
public class LogTest1x {
    Logger logger = LoggerFactory.getLogger(LogTest1x.class);

    @Test
    public void test() throws Throwable{
        logger.trace("test");
        logger.debug("test");
        logger.info("test");
        logger.warn("test");
        logger.error("test");

        Thread.sleep(1000);
    }
}
