package admindemo2.dso.db;

import admindemo2.Config;
import admindemo2.model.view.TagCountsVo;
import admindemo2.model.data.water_paas.PaasFileDo;
import admindemo2.model.data.water_paas.PaasFileType;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.wood.DataItem;
import org.noear.wood.DbContext;
import org.noear.wood.DbTableQuery;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DbPaaSApi {
    private static DbContext db() {
        return Config.water_paas;
    }

    //获取logger表tag
    public static List<TagCountsVo> getFileTags(PaasFileType type) throws SQLException {
        return db().table("paas_file").where("file_type=?", type.code)
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsVo.class);
    }

    private static String list_sels = "file_id,tag,label,path,`rank`,note,is_staticize,is_editable,is_disabled,is_exclude,link_to,edit_mode,content_type,update_fulltime,plan_state,plan_begin_time,plan_last_time,plan_last_timespan,plan_interval,plan_max,plan_count";

    public static List<PaasFileDo> getFileList(String tag, PaasFileType type) throws SQLException {
        return getFileList(tag, type, false, null, 0);
    }

    public static List<PaasFileDo> getFileList(String tag, PaasFileType type, boolean disabled, String key, int keyType) throws SQLException {
        DbTableQuery qr = db().table("paas_file").where("1=1");

        if (type.code < PaasFileType.all.code) {
            qr.andEq("is_disabled", (disabled ? 1 : 0));
            qr.andEq("file_type", type.code);
        }

        if (TextUtils.isNotEmpty(key)) {
            if (key.startsWith("@")) {
                key = key.substring(1);
            }

            if (keyType == 12) {
                qr.andLk("path", "%" + key + "%");
            } else if (keyType == 21) {
                if (tag != null) {
                    qr.andEq("tag", tag);
                }
                qr.andLk("content", "%" + key + "%");
            } else if (keyType == 22) {
                qr.andLk("content", "%" + key + "%");
            } else {
                if (tag != null) {
                    qr.andEq("tag", tag);
                }
                qr.andLk("path", "%" + key + "%");
            }
        } else {
            if (tag != null) {
                qr.andEq("tag", tag);
            }
        }

        if (type == PaasFileType.tml) {
            qr.orderByAsc("rank");
        }

        return qr.orderByAsc("path")
                .selectList(list_sels, PaasFileDo.class);
    }

    public static PaasFileDo getFile(int file_id) throws SQLException {
        return db().table("paas_file")
                .where("file_id=?", file_id)
                .selectItem("*", PaasFileDo.class);
    }

    public static int delFile(int file_id) throws SQLException {
        return db().table("paas_file")
                .where("file_id=?", file_id)
                .delete();
    }

    public static int resetFilePlan(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("paas_file").usingExpr(true)
                .set("plan_last_time","$NULL")
                .set("plan_last_timespan",0)
                .whereIn("file_id", list)
                .update();
    }

    public static void setFile(long file_id, DataItem data, PaasFileType type) throws SQLException {

        data.set("file_type", type.code);

        if (file_id > 0) {
            db().table("paas_file").whereEq("file_id", file_id).update(data);

            //更知:缓存更新
            WaterClient.Notice.updateCache("paas:" + file_id);
        } else {
            file_id = db().table("paas_file").insert(data);
        }
    }

    public static List<PaasFileDo> getFilesByIds(PaasFileType type, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("paas_file")
                .whereIn("file_id", list)
                .andEq("file_type", type.code)
                .selectList("*", PaasFileDo.class);
    }

    //批量导入
    public static void impFile(PaasFileType type, String tag, PaasFileDo wm) throws SQLException {
        if (TextUtils.isEmpty(tag) == false) {
            wm.tag = tag;
        }

        if (TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.path) || TextUtils.isEmpty(wm.content)) {
            return;
        }

        if (wm.file_type != type.code) {
            return;
        }

        DataItem item = new DataItem();
        item.setEntity(wm);
        item.remove("file_id");

        //只支持新增导入
        db().table("paas_file")
                .set("file_type", wm.file_type)
                .set("tag", wm.tag)
                .set("label", wm.label)
                .set("note", wm.note)
                .set("path", wm.path)
                .set("rank", wm.rank)
                .set("is_staticize", wm.is_staticize)
                .set("is_editable", wm.is_editable)
                .set("is_disabled", wm.is_disabled)
                .set("link_to", wm.link_to)
                .set("edit_mode", wm.edit_mode)
                .set("content_type", wm.content_type)
                .set("content", wm.content)
                .set("plan_state", wm.plan_state)
                .set("plan_begin_time", wm.plan_begin_time)
                .set("plan_interval", wm.plan_interval)
                .set("plan_max", wm.plan_max)
                .set("create_fulltime", wm.create_fulltime)
                .set("update_fulltime", wm.update_fulltime)
                .set("alarm_sign", wm.alarm_sign)
                .set("alarm_mobile", wm.alarm_mobile)
                .insertBy("path");
    }

    //批量删除
    public static void delFileByIds(PaasFileType type, int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (act == 9) {
            db().table("paas_file")
                    .whereIn("file_id", list)
                    .andEq("file_type", type.code)
                    .delete();
        } else {
            db().table("paas_file")
                    .set("is_disabled", (act == 1 ? 1 : 0))
                    .whereIn("file_id", list)
                    .andEq("file_type", type.code)
                    .update();
        }

        //更知:缓存更新
        for (Object file_id : list) {
            WaterClient.Notice.updateCache("paas:" + file_id);
        }
    }
}