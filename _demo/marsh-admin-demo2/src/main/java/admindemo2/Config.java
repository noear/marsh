package admindemo2;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.cloud.annotation.CloudConfig;
import org.noear.weed.DbContext;

/**
 * @author noear 2021/2/16 created
 */
@Configuration
public class Config {
    public static DbContext water;
    public static DbContext water_paas;


    @Bean
    public void waterDb(@CloudConfig("water") HikariDataSource ds) {
        water = new DbContext(ds);
    }

    @Bean
    public void waterPaasDb(@CloudConfig("water_paas") HikariDataSource ds) {
        water_paas = new DbContext(ds);
    }
}
