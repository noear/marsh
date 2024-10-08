package org.noear.marsh.uapi;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.*;
import org.noear.marsh.uapi.common.Attrs;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class UapiGateway extends Gateway {
    private List<Handler> beforeHandlers = new ArrayList<>();
    private List<Handler> afterHandlers = new ArrayList<>();

    /**
     * 语言
     */
    public Locale lang(Context c) {
        return c.getLocale();
    }

    /**
     * 前置处理
     */
    public void before(Handler handler) {
        beforeHandlers.add(handler);
    }

    /**
     * 后置处理
     */
    public void after(Handler handler) {
        afterHandlers.add(handler);
    }

    @Override
    protected void mainBefores(Context c) throws Throwable {
        for (Handler h : beforeHandlers) {
            h.handle(c);
        }
    }

    @Override
    protected void mainAfters(Context c) throws Throwable {
        for (Handler h : afterHandlers) {
            h.handle(c);
        }
    }

    /**
     * 渲染定制
     */
    @Override
    public void render(Object obj, Context c) throws Throwable {
        if (c.getRendered()) {
            return;
        }

        c.setRendered(true);

        //
        // 有可能根本没数据过来
        //
        if (obj instanceof ModelAndView) {
            //如果有模板，则直接渲染
            //
            c.result = obj;
            c.render(obj);
        } else {
            //如果没有按Result tyle 渲染
            //
            if (obj == null && c.status() == 404) {
                obj = UapiCodes.CODE_4001011;
            }

            Result result = null;
            if (obj instanceof UapiCode) {
                c.attrSet(Attrs.log_level, Level.WARN.toInt());

                //处理标准的状态码
                UapiCode err = (UapiCode) obj;
                String description = UapiCodes.CODE_note(lang(c), err);

                result = Result.failure(err.getCode(), description);
            } else if (obj instanceof Throwable) {
                c.attrSet(Attrs.log_level, Level.WARN.toInt());

                //处理未知异常
                String description = UapiCodes.CODE_note(lang(c), UapiCodes.CODE_400);
                result = Result.failure(Result.FAILURE_CODE, description);
            } else if (obj instanceof ONode) {
                //处理ONode数据（为兼容旧的）
                result = Result.succeed(((ONode) obj).toData());
            } else if (obj instanceof Result) {
                //处理Result结构
                result = (Result) obj;
            } else {
                //处理java bean数据（为扩展新的）
                result = Result.succeed(obj);
            }

            if (Utils.isEmpty(result.getDescription()) && result.getCode() > Result.SUCCEED_CODE) {
                result.setDescription(UapiCodes.CODE_note(lang(c), result.getCode()));
            }

            c.result = result;
        }
    }
}
