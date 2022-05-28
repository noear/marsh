package features;

import org.junit.Test;
import org.noear.water.utils.EncryptUtils;

/**
 * @author noear 2021/3/24 created
 */
public class Md5Test {
    @Test
    public void test(){
        String md5= EncryptUtils.md5("config.get#10305#W3y7mWYLgFVK1q/UyLw9+Q==#null#1616564989369"); //EncryptUtils.md5("Hello_%谢");
        System.out.println(md5);
    }
}
