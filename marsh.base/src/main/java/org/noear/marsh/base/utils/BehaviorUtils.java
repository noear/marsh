package org.noear.marsh.base.utils;

import org.noear.snack.ONode;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.logging.event.Level;
import org.noear.solon.logging.event.LogEvent;
import org.noear.wood.Command;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear
 * @since 1.2
 */
public class BehaviorUtils {
    static final String logger_water_log_sql_p = "water_log_sql_p"; //性能
    static final String logger_water_log_sql_b = "water_log_sql_b"; // 行为

    /**
     * 跟踪SQL性能
     *
     * @param service        服务名
     * @param thresholdValue 阈值
     */
    public static void trackOfPerformance(String service, Command cmd, long thresholdValue) {
        long timespan = cmd.timespan();

        if (timespan > thresholdValue) {
            track0(service, cmd, null, null, null, Instance.local().address());
        }
    }

    /**
     * 跟踪SQL行为
     *
     * @param service     服务名
     * @param ua          ua
     * @param path        请求路径
     * @param operator    操作人
     * @param operator_ip 操作IP
     */
    public static void trackOfBehavior(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        track0(service, cmd, ua, path, operator, operator_ip);
    }

    /**
     * 跟踪SQL命令性能
     */
    private static void track0(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        long interval = cmd.timespan();
        String trace_id = CloudClient.trace().getTraceId();

        track0Do(service, trace_id, cmd, interval, ua, path, operator, operator_ip);
    }

    private static void track0Do(String service, String trace_id, Command cmd, long interval, String ua, String path, String operator, String operator_ip) {
        int seconds = (int) (interval / 1000);
        String schema = cmd.context.schema();

        String sqlUp = cmd.text.toUpperCase();

        String method = "OTHER";
        if (sqlUp.indexOf("SELECT ") >= 0) {
            method = "SELECT";
        } else if (sqlUp.indexOf("UPDATE ") >= 0) {
            method = "UPDATE";
        } else if (sqlUp.indexOf("DELETE ") >= 0) {
            method = "DELETE";
        } else if (sqlUp.indexOf("INSERT INTO ") >= 0) {
            method = "INSERT";
        }

        String loggerName = null;

        if (path == null) {
            loggerName = logger_water_log_sql_p;
        } else {
            loggerName = logger_water_log_sql_b;
        }

        StringBuilder content = new StringBuilder();

        content.append(schema).append("::").append(cmd.text);
        content.append("<n-l>$$$").append(ONode.stringify(cmd.paramMap())).append("</n-l>");

        Map<String, String> metaInfo = new LinkedHashMap<>();
        metaInfo.put("tag", String.valueOf(seconds));
        metaInfo.put("tag1", path);
        metaInfo.put("tag2", operator);
        metaInfo.put("tag3", method);
        metaInfo.put("tag4", "");
        metaInfo.put("tag5", String.valueOf(interval));
        metaInfo.put("tag6", schema);
        metaInfo.put("tag7", service);

        LogEvent logEvent = new LogEvent(loggerName, Level.TRACE, metaInfo, content.toString(), System.currentTimeMillis(), "", null);

        CloudClient.log().append(logEvent);
    }
}
