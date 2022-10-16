package admindemo1.dso.db;

import admindemo1.Config;
import admindemo1.dso.CacheUtil;
import admindemo1.model.view.TagCountsVo;
import admindemo1.model.data.water_cfg.WhitelistDo;
import org.noear.water.dso.WhitelistApi;
import org.noear.water.utils.TextUtils;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DbWaterCfgApi {

    private static DbContext db() {
        return Config.water;
    }


    //获取白名单表tag
    public static List<TagCountsVo> getWhitelistTags() throws Exception {
        return db().table("water_cfg_whitelist")
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsVo.class);
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
                .selectList("*", WhitelistDo.class);
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
                .selectItem("*", WhitelistDo.class);
    }

    public static List<WhitelistDo> getWhitelistByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                                    .stream()
                                    .map(s->Integer.parseInt(s))
                                    .collect(Collectors.toList());

        return db().table("water_cfg_whitelist")
                .whereIn("row_id", list)
                .selectList("*", WhitelistDo.class);
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
                .selectExists();
    }

}
