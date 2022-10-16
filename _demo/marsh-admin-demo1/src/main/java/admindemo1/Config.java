package admindemo1;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.cloud.annotation.CloudConfig;
import org.noear.wood.DbContext;

/**
 * @author noear 2021/2/16 created
 */
@Configuration
public class Config {
    public static DbContext water;

    @Bean
    public void ds(@CloudConfig("water") HikariDataSource ds) {
        water = new DbContext(ds);
    }
}
