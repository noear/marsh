package apidemo2.dso.sso;

import org.noear.redisx.RedisClient;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

/**
 * 单点登录工具
 *
 * @author noear 2021/9/5 created
 */
@Component
public class SsoService {

    @Inject
    RedisClient redisClient;

    /**
     * 更新单点登录标识（用户登录时设置一次）
     */
    public void setUserSsoKeyNew(long userId, String guid) {
        redisClient.open(ru -> {
            ru.key("user_sso_key_" + userId).expire(-2).set(guid); //-2，表示永久有效
        });
    }

    /**
     * 获取单点登录标识（用户每次请求时，检测一下）
     */
    public String getUserSsoKey(long userId) {
        return redisClient.openAndGet(ru -> ru.key("user_sso_key_" + userId).get());
    }
}
