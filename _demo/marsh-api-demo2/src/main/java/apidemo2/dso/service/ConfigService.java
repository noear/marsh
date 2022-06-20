package apidemo2.dso.service;

import java.sql.SQLException;
import java.util.*;

import apidemo2.model.data.WaterCfgPropertiesDo;
import org.noear.solon.annotation.Inject;
import org.noear.solon.aspect.annotation.Service;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.data.annotation.CachePut;
import org.noear.solon.data.annotation.CacheRemove;

@Service
public class ConfigService {
    @Inject
    apidemo2.dso.mapper.ConfigMapper mapper;

    //获取配置列表
    @Cache(tags = "config_tag:${tag}")
    public List<WaterCfgPropertiesDo> getConfigsByTag(String tag) throws SQLException {
        return mapper.getConfigsByTag(tag);
    }

    //获取配置
    @Cache(tags = "config_tag_key:${tag}_${key}")
    public WaterCfgPropertiesDo getConfig(String tag, String key) throws SQLException {
        return mapper.getConfig(tag, key);
    }

    //设置配置（cache:列表型的只能删掉，单体的可以直接更新）
    @CacheRemove(tags = "config_tag:${tag}")
    @CachePut(tags = "config_tag_key:${tag}_${key}")
    public WaterCfgPropertiesDo setConfig(String tag, String key, String value) throws SQLException {
        //1.查找配置
        WaterCfgPropertiesDo cfg = mapper.getConfig(tag, key);

        if (cfg.row_id == 0) {
            //2.如果没有，新增配置
            mapper.addConfig(tag, key, value);

            return mapper.getConfig(tag, key);
        } else {
            if (cfg.is_editable == false) {
                return null;
            }

            //3.如果可以修改，则修改值
            mapper.udpConfig(tag, key, value);
            cfg.value = value;
            cfg.update_fulltime = new Date();

            return cfg;
        }
    }
}
