package org.noear.marsh.uapi.handler;

import io.jsonwebtoken.Claims;
import org.noear.marsh.base.GlobalConfig;
import org.noear.marsh.base.utils.Timecount;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.sessionstate.jwt.JwtUtils;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.marsh.uapi.Uapi;
import org.noear.marsh.uapi.common.Attrs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * 结束之前的日志拦截器
 * */
public class EndBeforeLogHandler implements Handler {

    Logger logger;
    int inputLimitSize;

    public EndBeforeLogHandler() {
        this(null);
    }

    public EndBeforeLogHandler(String loggerName) {
        if (Utils.isNotEmpty(loggerName)) {
            logger = LoggerFactory.getLogger(loggerName);
        } else {
            logger = LoggerFactory.getLogger(EndBeforeLogHandler.class);
        }

        inputLimitSize = GlobalConfig.logInputLimitSize();
        Solon.cfg().onChange((k, v) -> {
            //自动更新
            if (GlobalConfig.KEY_inputLimitSize.equals(k)) {
                inputLimitSize = Integer.parseInt(v);
            }
        });
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Uapi uapi = (Uapi) ctx.controller();

        if (uapi == null) {
            return;
        }

        /** 获取一下计时器（开始计时的时候设置的） */
        Timecount timecount = ctx.attr(Attrs.timecount);
        long timespan = 0L;
        if (timecount != null) {
            timespan = timecount.stop().milliseconds();
        }

        String orgIp = ctx.realIp();
        String orgOutput = uapi.getOrgOutput();

        if (null != orgOutput) {
            logOutput(ctx, uapi, orgOutput, orgIp, timespan);
        }

        if (null != ctx.errors) {
            logError(ctx, uapi, ctx.errors, orgIp, timespan);
        }
    }

    /**
     * 日志拦截器中使用
     *
     * @param uapi
     * @param orgOutput
     */
    protected void logOutput(Context ctx, Uapi uapi, String orgOutput, String orgIp, long timespan) {
        if (orgOutput == null) {
            return;
        }

        StringBuilder logInput = new StringBuilder();

        //构建输入项
        String orgInput = uapi.getOrgInput();
        if (null == orgInput) {
            orgInput = ONode.stringify(uapi.context().paramMap());
        }

        if (inputLimitSize > 0) {
            if (orgInput.length() > inputLimitSize) {
                orgInput = orgInput.substring(0, inputLimitSize);
            }
        }


        String org_sign = uapi.context().attr(Attrs.org_sign);
        String org_token = uapi.context().header(Attrs.h_token);

        if (Utils.isNotEmpty(org_token)) {
            logInput.append("> Token: ").append(org_token).append("\r\n");
            try {
                Claims tmp = JwtUtils.parseJwt(org_token);
                logInput.append("> Token.Val: ").append(ONode.stringify(tmp)).append("\r\n");
            } catch (Throwable ex) {
            }
        }
        if (Utils.isNotEmpty(org_sign)) {
            logInput.append("> Sign: ").append(org_sign).append("\r\n");
        }
        logInput.append("> Param: ").append(orgInput).append("\r\n");

        if (timespan > 0) {
            logInput.append("T Elapsed time: ").append(timespan).append("ms\r\n");
        }


        //构建输出项
        StringBuilder logOutput = new StringBuilder();
        String out_sign = uapi.context().attr(Attrs.h_sign);
        String out_token = uapi.context().attr(Attrs.h_token);

        if (Utils.isNotEmpty(out_token)) {
            logOutput.append("< Token: ").append(out_token).append("\r\n");
            try {
                Claims tmp = JwtUtils.parseJwt(out_token);
                logOutput.append("< Token.Val: ").append(ONode.stringify(tmp)).append("\r\n");
            } catch (Throwable ex) {

            }
        }

        if (Utils.isNotEmpty(out_sign)) {
            logOutput.append("< Sign: ").append(out_sign).append("\r\n");
        }

        logOutput.append("< Body: ").append(orgOutput);


        long userId = uapi.getUserID();
        String deviceId = ctx.param(Attrs.g_deviceId);
        if (Utils.isEmpty(deviceId)) {
            deviceId = ctx.header(Attrs.h_clientId);
        }
        if (Utils.isEmpty(deviceId)) {
            deviceId = "ip_" + orgIp;
        } else {
            deviceId = "c_" + deviceId;
        }

        TagsMDC.tag0(uapi.name()).tag1("user_" + userId).tag2(deviceId);

        int level = ctx.attrOrDefault(Attrs.log_level, 0);

        if (Level.WARN.toInt() == level) {
            logger.warn("{}\r\n{}", logInput, logOutput);
        } else {
            logger.info("{}\r\n{}", logInput, logOutput);
        }
    }

    /**
     * 日志拦截器中使用
     *
     * @param uapi
     * @param err
     */
    protected void logError(Context ctx, Uapi uapi, Throwable err, String orgIp, long timespan) {
        if (err == null) {
            return;
        }

        StringBuilder logInput = new StringBuilder();

        //构建输入项
        String orgInput = uapi.getOrgInput();
        if (null == orgInput) {
            orgInput = ONode.stringify(uapi.context().paramMap());
        }

        if (inputLimitSize > 0) {
            if (orgInput.length() > inputLimitSize) {
                orgInput = orgInput.substring(0, inputLimitSize);
            }
        }


        String org_sign = uapi.context().attr(Attrs.org_sign);
        String org_token = uapi.context().header(Attrs.h_token);

        if (Utils.isNotEmpty(org_token)) {
            logInput.append("> Token: ").append(org_token).append("\r\n");
            try {
                Claims tmp = JwtUtils.parseJwt(org_token);
                logInput.append("> Token.Val: ").append(ONode.stringify(tmp)).append("\r\n");
            } catch (Throwable ex) {
            }
        }
        if (Utils.isNotEmpty(org_sign)) {
            logInput.append("> Sign: ").append(org_sign).append("\r\n");
        }
        logInput.append("> Param: ").append(orgInput).append("\r\n");

        if (timespan > 0) {
            logInput.append("T Elapsed time: ").append(timespan).append("ms\r\n");
        }


        long userId = uapi.getUserID();
        String deviceId = ctx.param(Attrs.g_deviceId);
        if (Utils.isEmpty(deviceId)) {
            deviceId = ctx.header(Attrs.h_clientId);
        }
        if (Utils.isEmpty(deviceId)) {
            deviceId = "ip_" + orgIp;
        } else {
            deviceId = "c_" + deviceId;
        }

        TagsMDC.tag0(uapi.name()).tag1("user_" + userId).tag2(deviceId);

        logger.error("{}\r\n{}", logInput, err);
    }
}
