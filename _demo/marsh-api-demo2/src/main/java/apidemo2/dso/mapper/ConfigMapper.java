package apidemo2.dso.mapper;

import java.sql.SQLException;
import java.util.*;

import apidemo2.model.data.WaterCfgPropertiesDo;
import org.noear.weed.annotation.Db;
import org.noear.weed.xml.Namespace;

@Db("water")
@Namespace("apidemo2.dso.mapper.ConfigMapper")
public interface ConfigMapper{
    //获取配置列表
    List<WaterCfgPropertiesDo> getConfigsByTag(String tag) throws SQLException;

    //获取配置
    WaterCfgPropertiesDo getConfig(String tag, String key) throws SQLException;

    //设置配置
    long addConfig(String tag, String key, String value) throws SQLException;

    //设置配置
    long udpConfig(String tag, String key, String value) throws SQLException;
}
