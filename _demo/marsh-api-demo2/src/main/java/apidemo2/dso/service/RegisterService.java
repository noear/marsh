package apidemo2.dso.service;

import java.sql.SQLException;
import java.util.*;

import apidemo2.model.data.WaterRegServiceDo;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ProxyComponent;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.data.annotation.CacheRemove;

@ProxyComponent
public class RegisterService {
    @Inject
    apidemo2.dso.mapper.RegisterMapper mapper;

    //添加服务（cache::批量的只能移掉）
    @CacheRemove(tags = "services_${name}")
    public long addService(String name, WaterRegServiceDo model) throws SQLException {
        return mapper.addService(model);
    }

    //更新服务（cache::批量的只能移掉）
    @CacheRemove(tags = "services_${name}")
    public long udpService(String name, WaterRegServiceDo model) throws SQLException {
        return mapper.udpService(model);
    }

    //删除服务（cache::批量的只能移掉）
    @CacheRemove(tags = "services_${name}")
    public long delService(String name,String key) throws SQLException {
        return mapper.delService(key);
    }

    //查找服务（cache::此处不太适合用缓存）
    @Cache(tags = "services_${name}")
    public List<WaterRegServiceDo> getServiceList(String name) throws SQLException {
        return mapper.getServiceList(name);
    }
}
