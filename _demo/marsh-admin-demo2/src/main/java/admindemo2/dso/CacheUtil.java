package admindemo2.dso;

import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

/**
 * @author noear 2021/2/16 created
 */
public class CacheUtil {
    public static ICacheServiceEx local = new LocalCache();
    public static ICacheServiceEx data = local;
}
