package apidemo2;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.redisx.RedisClient;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.cloud.CloudEventInterceptor;
import org.noear.solon.cloud.CloudJobInterceptor;
import org.noear.solon.cloud.annotation.CloudConfig;
import org.noear.solon.data.cache.CacheService;
import org.noear.solon.data.cache.CacheServiceSupplier;
import org.noear.marsh.base.interceptor.BaseEventInterceptor;
import org.noear.marsh.base.interceptor.BaseJobInterceptor;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author noear 2021/2/17 created
 */
@Configuration
public class Config {
    /**
     * 配置数据源
     */
    @Bean(value = "water")
    public DataSource db1(@CloudConfig(name = "water",group = "water") HikariDataSource ds) {
        return ds;
    }

    @Bean
    public RedisClient redisClient(@CloudConfig(name = "water_redis", group = "water") Properties pops){
        return new RedisClient(pops, 11);
    }

    /**
     * 配置缓存
     * */
    @Bean
    public CacheService cache1(@CloudConfig(name = "water_cache", group = "water") CacheServiceSupplier supplier) {
        return supplier.get();
    }

    /**
     * 任务拦截器（用于记录自己的性能与日志）
     * */
    @Bean
    public CloudJobInterceptor jobInterceptor(){
        return new BaseJobInterceptor();
    }

    /**
     * 事件拦截器（用于记录自己的性能与日志）
     * */
    @Bean
    public CloudEventInterceptor eventInterceptor(){
        return new BaseEventInterceptor();
    }

    /**
     * 使用 Rock 语言包工厂（替换本地语言包）
     * */
//    @Bean
//    public I18nBundleFactory i18nBundleFactory(){
//        return new RockCodeI18nBundleFactory();
//    }

    /**
     * 使用 Rock 应用工厂（提供ak/sk）
     * */
//    @Bean
//    public IAppFactory iAppFactory(){
//        return new RockAppFactoryImpl();
//    }
}
