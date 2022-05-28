package apidemo2.dso.mapper;

import java.sql.SQLException;
import java.util.*;

import apidemo2.model.data.WaterRegServiceDo;
import org.noear.weed.annotation.Db;
import org.noear.weed.xml.Namespace;

@Db("water")
@Namespace("apidemo2.dso.mapper.RegisterMapper")
public interface RegisterMapper{
    //添加服务
    long addService(WaterRegServiceDo model) throws SQLException;

    //更新服务
    long udpService(WaterRegServiceDo model) throws SQLException;

    //删除服务
    long delService(String key) throws SQLException;

    //查找服务
    List<WaterRegServiceDo> getServiceList(String name) throws SQLException;
}
