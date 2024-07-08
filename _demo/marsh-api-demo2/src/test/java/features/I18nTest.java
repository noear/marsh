package features;

import apidemo2.App;
import org.junit.jupiter.api.Test;
import org.noear.solon.i18n.I18nUtil;
import org.noear.solon.test.SolonTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author noear 2021/2/24 created
 */
@SolonTest(App.class)
public class I18nTest {
    Logger logger = LoggerFactory.getLogger(LogTest1x.class);

    @Test
    public void test0() throws Exception {
        String note = I18nUtil.getMessage(Locale.SIMPLIFIED_CHINESE, "title");

        assert note != null;

        logger.debug(note);
    }

    @Test
    public void test1() throws Exception {
        String note = I18nUtil.getMessage(Locale.SIMPLIFIED_CHINESE, "hello", new Object[]{"noear"});

        assert note != null;
        assert note.indexOf("noear") >= 0;

        logger.debug(note);
    }
}
