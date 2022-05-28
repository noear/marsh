package apidemo1;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.cloud.annotation.CloudConfig;
import org.noear.solon.data.cache.CacheService;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.solon.plugin.CacheWrap;

import javax.sql.DataSource;

/**
 * @author noear 2021/2/17 created
 */
@Configuration
public class Config {
    /**
     * 配置数据源
     */
    @Bean(value = "water")
    public DataSource db1(@CloudConfig("water") HikariDataSource ds) {
        return ds;
    }

    /**
     * 配置缓存
     * */
    @Bean
    public CacheService cache1() {
        return CacheWrap.wrap(new LocalCache());
    }

}
