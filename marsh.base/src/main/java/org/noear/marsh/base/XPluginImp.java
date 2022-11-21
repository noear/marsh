package org.noear.marsh.base;


import org.noear.marsh.base.utils.BehaviorUtils;
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
import org.noear.wood.WoodConfig;
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
    boolean isWoodStyle2;
    boolean isTrackEnable;
    boolean isErrorLogEnable;

    @Override
    public void start(AopContext context) {
        ValidatorManager.setNoRepeatSubmitChecker(new NoRepeatSubmitCheckerNew());
        ValidatorManager.setWhitelistChecker(new WhitelistCheckerNew());

        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");


        isDebugMode = Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode();

        isWoodStyle2 = "sql".equals(GlobalConfig.sqlPrintStyle());
        isTrackEnable = GlobalConfig.sqlTrackEnable(isDebugMode);
        isErrorLogEnable = GlobalConfig.sqlErrorLogEnable();


        initWood();

        Solon.app().filter(Integer.MIN_VALUE, (ctx, chain) -> {
            try {
                chain.doFilter(ctx);
            } finally {
                MDC.clear();
                NamiAttachment.clear();
            }
        });
    }


    /**
     * 初始化Wood监听事件
     */
    protected void initWood() {
        Class<?> gritClz = Utils.loadClass(clzGritClient);

        if (gritClz == null) {
            initWoodForApi();
        } else {
            initWoodForAdmin();
        }


        WoodConfig.onException((cmd, err) -> {
            TagsMDC.tag0("wood");

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

    private void initWoodForApi() {
        //api项目
        WoodConfig.onExecuteAft(cmd -> {
            if (isDebugMode) {
                if (isWoodStyle2) {
                    log.debug(cmd.toSqlString());
                } else {
                    log.debug(cmd.text + "\r\n" + ONode.stringify(cmd.paramMap()));
                }
            }

            BehaviorUtils.trackOfPerformance(Solon.cfg().appName() , cmd, 1000);

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (Utils.isEmpty(tag)) {
                    tag = "sql";
                }

                CloudClient.metric().addMeter(Solon.cfg().appName()  + "_sql", tag, cmd.text, cmd.timespan());
            }
        });
    }

    private void initWoodForAdmin() {
        //admin 项目
        WoodConfig.onExecuteAft((cmd) -> {
            if (isDebugMode) {
                if (isWoodStyle2) {
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

                BehaviorUtils.trackOfBehavior(Solon.cfg().appName(), cmd, ctx.userAgent(), ctx.pathNew(), userId + "." + userDisplayName, ctx.realIp());
            }

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (Utils.isEmpty(tag)) {
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
