package org.noear.marsh.uapi.app;

import java.io.IOException;

/**
 * @author noear 2022/4/8 created
 */
public interface IAppFactory {
    IApp getAppById(int appId) throws Exception;
    IApp getAppByKey(String accessKey) throws Exception;
}
