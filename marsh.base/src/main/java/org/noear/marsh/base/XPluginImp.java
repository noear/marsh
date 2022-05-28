package org.noear.marsh.base;


import org.noear.nami.NamiAttachment;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.ValidatorManager;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.marsh.base.validation.NoRepeatSubmitCheckerNew;
import org.noear.marsh.base.validation.WhitelistCheckerNew;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.WeedConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * @author noear 2021/2/13 created
 */
public class XPluginImp implements Plugin {
    static final String clzGritClient = "org.noear.grit.client.GritClient";
    static Logger log = LoggerFactory.getLogger(XPluginImp.class);

    boolean isDebugMode;
    boolean isWeedStyle2;
    boolean isTrackEnable;
    boolean isErrorLogEnable;

    @Override
    public void start(AopContext context) {
        ValidatorManager.setNoRepeatSubmitChecker(new NoRepeatSubmitCheckerNew());
        ValidatorManager.setWhitelistChecker(new WhitelistCheckerNew());

        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");


        isDebugMode = Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode();

        isWeedStyle2 = "sql".equals(GlobalConfig.sqlPrintStyle());
        isTrackEnable = GlobalConfig.sqlTrackEnable(isDebugMode);
        isErrorLogEnable = GlobalConfig.sqlErrorLogEnable();


        initWeed();

        Solon.global().filter(Integer.MIN_VALUE, (ctx, chain) -> {
            try {
                chain.doFilter(ctx);
            } finally {
                MDC.clear();
                NamiAttachment.clear();
            }
        });
    }


    /**
     * 初始化Weed监听事件
     */
    protected void initWeed() {
        Class<?> gritClz = Utils.loadClass(clzGritClient);

        if (gritClz == null) {
            initWeedForApi();
        } else {
            initWeedForAdmin();
        }


        WeedConfig.onException((cmd, err) -> {
            TagsMDC.tag0("weed");

            if (isErrorLogEnable) {
                if (cmd == null) {
                    log.error("::Error= {}", err);
                } else {
                    log.error("::Sql= {}\n::Args= {}\n\n::Error= {}", cmd.text, ONode.stringify(cmd.paramMap()), err);
                }
            } else {
                if (cmd == null) {
                    log.debug("::Error= {}", err);
                } else {
                    log.debug("::Sql= {}\n::Args= {}\n\n::Error= {}", cmd.text, ONode.stringify(cmd.paramMap()), err);
                }
            }
        });
    }

    private void initWeedForApi() {
        //api项目
        WeedConfig.onExecuteAft(cmd -> {
            if (isDebugMode) {
                if (isWeedStyle2) {
                    log.debug(cmd.toSqlString());
                } else {
                    log.debug(cmd.text + "\r\n" + ONode.stringify(cmd.paramMap()));
                }
            }

            WaterClient.Track.trackOfPerformance(Solon.cfg().appName() , cmd, 1000);

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (TextUtils.isEmpty(tag)) {
                    tag = "sql";
                }

                CloudClient.metric().addMeter(Solon.cfg().appName()  + "_sql", tag, cmd.text, cmd.timespan());
            }
        });
    }

    private void initWeedForAdmin() {
        //admin 项目
        WeedConfig.onExecuteAft((cmd) -> {
            if (isDebugMode) {
                if (isWeedStyle2) {
                    log.debug(cmd.toSqlString());
                } else {
                    log.debug(cmd.text + "\r\n" + ONode.stringify(cmd.paramMap()));
                }
            }

            if (cmd.isLog < 0) {
                return;
            }

            Context ctx = Context.current();

            if (ctx == null) {
                return;
            }


            String sqlUp = cmd.text.toUpperCase();

            if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.contains("INSERT INTO ") || sqlUp.contains("UPDATE ") || sqlUp.contains("DELETE ")) {
                String userDisplayName = getUserDisplayName(ctx);
                String userId = getUserId(ctx);

                WaterClient.Track.trackOfBehavior(Solon.cfg().appName(), cmd, ctx.userAgent(), ctx.pathNew(), userId + "." + userDisplayName, ctx.realIp());
            }

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (TextUtils.isEmpty(tag)) {
                    tag = "sql";
                }

                CloudClient.metric().addMeter(Solon.cfg().appName() + "_sql", tag, cmd.text, cmd.timespan());
            }
        });
    }


    //用于作行为记录
    public String getUserId(Context ctx) {
        return ctx.attr("user_id", "0");
    }

    public String getUserDisplayName(Context ctx) {
        if (ctx != null) {
            return ctx.attr("user_display_name", null);
        } else {
            return null;
        }
    }
}
