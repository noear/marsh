package org.noear.marsh.uapi;

import org.noear.snack.core.utils.StringUtil;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.event.EventBus;
import org.noear.marsh.uapi.app.IApp;
import org.noear.marsh.uapi.app.IAppFactory;
import org.noear.marsh.uapi.app.IAppImpl;
import org.noear.marsh.uapi.common.Attrs;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.Context;

import java.sql.SQLException;

@Singleton(false)
public class Uapi {
    protected final Context ctx;
    protected final IAppFactory appFactory;

    public Uapi() {
        ctx = Context.current();
        appFactory = Solon.context().getBean(IAppFactory.class);
    }


    public static <T extends Uapi> T current() {
        return (T) Context.current().controller();
    }

    /**
     * 接口名称（不一定会有）
     */
    public String name() {
        if (ctx == null) {
            return null;
        } else {
            return ctx.attr(Attrs.handler_name);
        }
    }

    /**
     * 上下文对象
     */
    public Context context() {
        return ctx;
    }


    private String _outs;
    private String _nouts;

    /**
     * 检查参数是否需要输出
     */
    protected boolean isOut(String key) {
        if (this._outs == null) {
            this._outs = ctx.param("outs");
        }

        if (this._outs == null) {
            return false;
        } else {
            return this._outs.indexOf(key) > -1;
        }
    }

    /**
     * 检查是否不需要输出
     */
    protected boolean isNotout(String key) {
        if (this._nouts == null) {
            this._nouts = ctx.param("nouts");
        }

        if (this._nouts == null) {
            return false;
        } else {
            return this._nouts.indexOf(key) > -1;
        }
    }


    ///////////
    private String _ip;

    public String ip() {
        if (_ip == null) {
            _ip = ctx.realIp();
        }

        return _ip;
    }

    private IApp _app;

    public IApp getApp() {
        if (_app == null) {
            //先赋值，避各种后续异常时重复进入
            _app = new IAppImpl();

            String appStr = ctx.param(Attrs.app_id);

            if (Utils.isNotEmpty(appStr)) {
                try {
                    if (StringUtil.isNumber(appStr)) {
                        _app = getAppById(Integer.parseInt(appStr));
                    } else {
                        _app = getAppByKey(appStr);
                    }
                } catch (Exception e) {
                    EventBus.publishTry(e);
                }
            }
        }

        return _app;
    }

    public IApp getAppById(int appID) throws Exception {
        return appFactory.getAppById(appID);
    }

    public IApp getAppByKey(String appKey) throws Exception {
        return appFactory.getAppByKey(appKey);
    }


    public int getAppId() {
        return getApp().getAppId();
    }

    public int getAgroupId() throws SQLException {
        return getApp().getAppGroupId();
    }

    private int verId = -1;

    public int getVerId() {
        if (verId < 0) {
            verId = ctx.paramAsInt(Attrs.ver_id);
        }

        return verId;
    }

    public long getUserID() {
        return 0;
    }

    /**
     * 原始输入
     */
    public String getOrgInput() {
        return ctx.attr(Attrs.org_input);
    }

    /**
     * 原始输入签名
     */
    public String getOrgInputSign() {
        return ctx.attr(Attrs.org_input_sign);
    }

    /**
     * 原始输入签名
     */
    public String getOrgInputTimestamp() {
        return ctx.attr(Attrs.org_input_timestamp);
    }

    /**
     * 原始输出（即未加密之前）
     */
    public String getOrgOutput() {
        return ctx.attr(Attrs.org_output);
    }
}
