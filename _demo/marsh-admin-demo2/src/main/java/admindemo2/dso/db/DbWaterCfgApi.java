package admindemo2.dso.db;

import admindemo2.Config;
import admindemo2.dso.CacheUtil;
import admindemo2.dso.ConfigType;
import admindemo2.model.view.TagCountsVo;
import admindemo2.model.data.water_cfg.ConfigDo;
import admindemo2.model.data.water_cfg.LoggerDo;
import admindemo2.model.data.water_cfg.WhitelistDo;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.dso.WhitelistApi;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DbWaterCfgApi {

    private static DbContext db() {
        return Config.water;
    }

    public static List<LoggerDo> getLoggerByTag(String tag) throws Exception {
        return db().table("water_cfg_logger")
                .where("is_enabled=1")
                .build(tb -> {
                    if (!TextUtils.isEmpty(tag)) {
                        tb.and("tag = ?",tag);
                    }
                })
                .orderBy("logger asc")
                .select("*")
                .getList(LoggerDo.class);
    }

    //
    public static LoggerDo getLogger(String logger) {
        try {
            return db().table("water_cfg_logger")
                    .where("logger = ?", logger)
                    .limit(1)
                    .select("*")
                    .getItem(LoggerDo.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    //获取白名单表tag
    public static List<TagCountsVo> getWhitelistTags() throws Exception {
        return db().table("water_cfg_whitelist")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsVo.class);
    }

    //获取ip白名单列表
    public static List<WhitelistDo> getWhitelistByTag(String tag_name, String key, int state) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("is_enabled",state==1)
                .build(tb -> {
                    if(tag_name != null){
                        tb.andEq("tag",tag_name);
                    }

                    if (TextUtils.isEmpty(key) == false) {
                        tb.andLk("value", key + "%");
                    }
                })
                .select("*")
                .getList(WhitelistDo.class);
    }

    //新增ip白名单
    public static boolean setWhitelist(Integer row_id, String tag, String type, String value, String note) throws SQLException {
        if (row_id == null) {
            row_id = 0;
        }

        if(value == null){
            return false;
        }

        DbTableQuery qr = db().table("water_cfg_whitelist")
                .set("tag", tag.trim())
                .set("type", type.trim())
                .set("value", value.trim())
                .set("note", note);

        if (row_id > 0) {
            return qr.whereEq("row_id", row_id).update() > 0;
        } else {
            return qr.insert() > 0;
        }
    }

    //批量导入
    public static void impWhitelist(String tag, WhitelistDo wm) throws SQLException {
        if(TextUtils.isEmpty(tag) == false){
            wm.tag = tag;
        }

        if(TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.value)){
            return;
        }

        db().table("water_cfg_whitelist")
                .set("tag", wm.tag)
                .set("type", wm.type)
                .set("value", wm.value)
                .set("note", wm.note)
                .insertBy("tag,type,value");
    }

    //删除
    public static boolean delWhitelist(int row_id) throws SQLException {
        return db().table("water_cfg_whitelist")
                .where("row_id = ?", row_id)
                .delete() > 0;
    }

    //批量删除
    public static void delWhitelistByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if(act == 9) {
            db().table("water_cfg_whitelist")
                    .whereIn("row_id", list)
                    .delete();
        }else {
            db().table("water_cfg_whitelist")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .whereIn("row_id", list)
                    .update();
        }
    }

    public static WhitelistDo getWhitelist(int row_id) throws SQLException {
        return db().table("water_cfg_whitelist")
                .where("row_id = ?", row_id)
                .select("*")
                .getItem(WhitelistDo.class);
    }

    public static List<WhitelistDo> getWhitelistByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                                    .stream()
                                    .map(s->Integer.parseInt(s))
                                    .collect(Collectors.toList());

        return db().table("water_cfg_whitelist")
                .whereIn("row_id", list)
                .select("*")
                .getList(WhitelistDo.class);
    }

    //加载IP白名单到静态缓存里
    public static void reloadWhitelist() throws Exception {
        CacheUtil.data.clear("water_cfg_whitelist");//waterapi那儿，过一分钟就会自动刷新
    }

    public static boolean isWhitelist(String val) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereIn("tag", Arrays.asList(WhitelistApi.tag_client, WhitelistApi.tag_server))
                .andEq("type", "ip")
                .andEq("value", val)
                .caching(CacheUtil.data).usingCache(60)
                .cacheTag("whitelist")
                .exists();
    }

    public static boolean hasGateway(String name) {
        try {
            return db().table("water_cfg_properties")
                    .whereEq("tag", "_gateway")
                    .andEq("key", name)
                    .exists();
        }catch (Exception ex){
            return false;
        }
    }

    public static void addGateway(String tag, String key, String url, String policy, int is_enabled) throws SQLException {
        ONode n = new ONode()
                .set("url", url)
                .set("service",key.trim())
                .set("policy", policy);

        db().table("water_cfg_properties")
                .set("tag", tag.trim())
                .set("key", key.trim())
                .set("is_enabled", is_enabled)
                .set("type", ConfigType.water_gateway)
                .set("value", n.toJson())
                .insert();
    }

    public static void updGateway(String tag, String ori_key, String key, String url, String policy, int is_enabled) throws SQLException {
        ONode n = new ONode()
                .set("url", url)
                .set("service", key.trim())
                .set("policy", policy);

        //由 tag 决定，是否为gateway

        db().table("water_cfg_properties")
                .set("key", key.trim())
                .set("value", n.toJson())
                .set("is_enabled", is_enabled)
                .set("type", ConfigType.water_gateway)
                .whereEq("tag", tag.trim())
                .andEq("key", ori_key.trim())
                .update();

        WaterClient.Notice.updateCache("upstream:" + ori_key);
        if (ori_key.equals(key) == false) {
            WaterClient.Notice.updateCache("upstream:" + key);
        }
    }


    public static List<ConfigDo> getGateways() throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("type", ConfigType.water_gateway)
                .select("tag,key,row_id,type,is_enabled")
                .getList(ConfigDo.class);
    }

    public static void delConfigByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (list.size() == 0) {
            return;
        }

        if (act == 9) {
            db().table("water_cfg_properties")
                    .whereIn("row_id", list)
                    .delete();
        } else {
            db().table("water_cfg_properties")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .whereIn("row_id", list)
                    .update();
        }
    }


    //导入
    public static void impConfig(String tag, ConfigDo wm) throws SQLException {
        if(TextUtils.isEmpty(tag) == false){
            wm.tag = tag;
        }

        if(TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.key) || TextUtils.isEmpty(wm.value)){
            return;
        }

        db().table("water_cfg_properties")
                .set("tag", wm.tag)
                .set("type", wm.type)
                .set("key", wm.key)
                .set("value", wm.value)
                .set("edit_mode", wm.edit_mode)
                .insertBy("tag,key");
    }

    public static List<ConfigDo> getConfigByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s->Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("water_cfg_properties")
                .whereIn("row_id", list)
                .select("*")
                .getList(ConfigDo.class);
    }


    //编辑更新config。
    public static boolean setConfig(Integer row_id, String tag, String key, Integer type, String value, String edit_mode) throws SQLException {
        DbTableQuery db = db().table("water_cfg_properties")
                .set("row_id", row_id)
                .set("tag", tag.trim())
                .set("key", key.trim())
                .set("type", type)
                .set("edit_mode", edit_mode)
                .set("update_fulltime", "$NOW()").usingExpr(true)
                .set("value", value.trim());

        if (row_id > 0) {
            boolean isOk = db.where("row_id = ?", row_id).update() > 0;

            WaterClient.Notice.updateConfig(tag, key);

            return isOk;
        } else {
            return db.insert() > 0;
        }
    }

    public static void delConfig(Integer row_id) throws SQLException {
        if (row_id == null) {
            return;
        }

        db().table("water_cfg_properties")
                .where("row_id=?", row_id)
                .delete();
    }


    //获取标签数组。
    public static List<TagCountsVo> getConfigTags() throws SQLException {
        return db().table("water_cfg_properties")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsVo.class);
    }

    // 获取有特定类型配置的TAG
    public static List<TagCountsVo> getConfigTagsByType(int type) throws SQLException {
        return db().table("water_cfg_properties")
                .where("type = ?", type)
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag, COUNT(*) AS counts")
                .getList(TagCountsVo.class);
    }

    //编辑功能，根据row_id获取config信息。
    public static ConfigDo getConfig(Integer row_id) throws SQLException {
        if(row_id == null || row_id == 0){
            return new ConfigDo();
        }

        return db().table("water_cfg_properties")
                .where("row_id = ?", row_id)
                .select("*")
                .getItem(ConfigDo.class);
    }


    //根据tag列出config。
    public static ConfigDo getConfigByTagName(String tagName) throws SQLException {
        if(TextUtils.isEmpty(tagName)){
            return new ConfigDo();
        }

        String[] ss = tagName.split("/");

        return getConfigByTagName(ss[0], ss[1]);
    }

    //获取配置项目
    public static DbContext getDbContext(String sourceKey, DbContext defDb) throws SQLException {

        if (sourceKey == null || sourceKey.indexOf("/") < 0) {
            return defDb;
        } else {
            String[] ss = sourceKey.split("/");
            ConfigDo cfg = getConfigByTagName(ss[0], ss[1]);

            if (TextUtils.isEmpty(cfg.value)) {
                return defDb;
            } else {
                return cfg.getDb();
            }
        }
    }

    public static ConfigDo getConfigByTagName(String tag, String name) throws SQLException {
        return getConfigByTagName(tag,name,false);
    }

    public static ConfigDo getConfigByTagName(String tag, String name, boolean cache) throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("tag", tag)
                .andEq("key", name)
                .limit(1)
                .select("*")
                .caching(CacheUtil.data).usingCache(cache)
                .getItem(ConfigDo.class);
    }

    public static List<ConfigDo> getConfigsByTag(String tag, String key, int state) throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("tag", tag)
                .andEq("is_enabled",state==1)
                .build(tb -> {
                    if (!TextUtils.isEmpty(key)) {
                        tb.and("`key` like ?", "%" + key + "%");
                    }
                })
                .select("*")
                .getList(ConfigDo.class);
    }

    public static List<ConfigDo> getConfigsByType(String tag, int type) throws SQLException {
        return db().table("water_cfg_properties")
                .where("type = ?", type)
                .build((tb)->{
                    if(TextUtils.isEmpty(tag) == false){
                        tb.and("tag = ?", tag);
                    }
                })
                .select("*")
                .getList(ConfigDo.class);
    }

    public static List<ConfigDo> getConfigTagKeyByType(String tag, int type) throws SQLException {
        return db().table("water_cfg_properties")
                .where("type = ?", type)
                .build((tb)->{
                    if(TextUtils.isEmpty(tag) == false){
                        tb.and("tag = ?", tag);
                    }
                })
                .select("tag,key")
                .getList(ConfigDo.class);
    }

    //====================================================

    //获取type=10的配置（结构化数据库）
    public static List<ConfigDo> getDbConfigs() throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("type", ConfigType.db)
                .orderBy("`tag`,`key`")
                .select("*")
                .getList(ConfigDo.class);
    }

    //获取type=10的配置（结构化数据库）
    public static List<ConfigDo> getLogStoreConfigs() throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("type", ConfigType.db)
                .orEq("type",ConfigType.water_logger)
                .orderBy("`tag`,`key`")
                .select("*")
                .getList(ConfigDo.class);
    }

    //获取type=10,11,12的配置（结构化数据库 + 非结构化数据库）
    public static List<ConfigDo> getDbConfigsEx() throws SQLException {
        return db().table("water_cfg_properties")
                .where("type >=10 AND type<20")
                .select("*")
                .getList(ConfigDo.class);
    }

    //====================================================

    //获取logger表tag
    public static List<TagCountsVo> getLoggerTags() throws Exception {
        return db().table("water_cfg_logger").whereEq("is_enabled",1)
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsVo.class);
    }

    //根据tag获取列表。
    public static List<LoggerDo> getLoggersByTag(String tag_name, int is_enabled, String sort) throws Exception {
        return db().table("water_cfg_logger")
                .where("tag = ?", tag_name)
                .and("is_enabled = ?",is_enabled)
                .build((tb)->{
                    if(TextUtils.isEmpty(sort) == false){
                        tb.orderBy(sort+" DESC");
                    }else{
                        tb.orderBy("logger ASC");
                    }
                })
                .select("*")
                .getList(LoggerDo.class);

    }

    //根据id获取logger。
    public static LoggerDo getLogger(Integer logger_id) throws Exception {
        return db().table("water_cfg_logger")
                .where("logger_id=?", logger_id)
                .limit(1)
                .select("*")
                .getItem(LoggerDo.class);
    }

    //设置logger。
    public static boolean setLogger(Integer logger_id, String tag, String logger, String source, String note, int keep_days, int is_alarm) throws SQLException {
        DbTableQuery db = db().table("water_cfg_logger")
                .set("tag", tag)
                .set("logger", logger)
                .set("keep_days", keep_days)
                .set("source", source)
                .set("is_alarm",is_alarm)
                .set("note", note);
        if (logger_id > 0) {
            return db.where("logger_id = ?", logger_id).update() > 0;
        } else {
            return db.set("is_enabled",1).insert() > 0;
        }
    }

    //设置启用状态
    public static void setLoggerEnabled(int logger_id, int is_enabled) throws SQLException {
        db().table("water_cfg_logger")
                .where("logger_id = ?", logger_id)
                .set("is_enabled", is_enabled)
                .update();
    }

    public static void delLogger(Integer logger_id) throws SQLException {
        if(logger_id == null){
            return;
        }

        db().table("water_cfg_logger")
                .where("logger_id = ?", logger_id)
                .delete();
    }

}
