package admindemo2.controller.cfg;

import admindemo2.controller.BaseController2;
import admindemo2.dso.db.DbWaterCfgApi;
import admindemo2.dso.TagUtil;
import admindemo2.model.view.TagCountsVo;
import admindemo2.model.data.water_cfg.ConfigDo;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.IOUtils;
import org.noear.water.utils.JsondEntity;
import org.noear.water.utils.JsondUtils;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/cfg/prop")
public class PropController extends BaseController2 {
    @Mapping("")
    public ModelAndView index(String tag_name) throws SQLException {
        List<TagCountsVo> tags = DbWaterCfgApi.getConfigTags();


        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);
        return view("cfg/prop");
    }

    @Mapping("inner")
    public ModelAndView innerDo(Context ctx, String tag_name, String key) throws SQLException {
        int state = ctx.paramAsInt("state", 1);

        TagUtil.cookieSet(tag_name);

        List<ConfigDo> list = DbWaterCfgApi.getConfigsByTag(tag_name, key, state);

        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("state", state);
        viewModel.put("key", key);

        return view("cfg/prop_inner");
    }


    //跳转编辑页面。
    @Mapping("edit")
    public ModelAndView editConfig(String tag_name, Integer row_id) throws SQLException {
        if (row_id == null) {
            row_id = 0;
        }

        ConfigDo cfg = DbWaterCfgApi.getConfig(row_id);

        if (cfg.row_id > 0) {
            tag_name = cfg.tag;
        }

        viewModel.put("row_id", row_id);
        viewModel.put("cfg", cfg);
        viewModel.put("tag_name", tag_name);
        return view("cfg/prop_edit");
    }

    //编辑、保存功能。
    @Mapping("edit/ajax/save")
    public Result save(Integer row_id, String tag, String key, Integer type, String value, String edit_mode) throws SQLException {

        boolean result = DbWaterCfgApi.setConfig(row_id, tag, key, type, value, edit_mode);

        if (result) {
            return Result.succeed();
        } else {
            return Result.failure();
        }
    }

    //编辑、保存功能。
    @Mapping("edit/ajax/del")
    public Result del(Integer row_id) throws SQLException {
        DbWaterCfgApi.delConfig(row_id);

        return Result.succeed();
    }


    //批量导出
    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<ConfigDo> list = DbWaterCfgApi.getConfigByIds(ids);

        String jsonD = JsondUtils.encode("water_cfg_properties", list);

        String filename2 = "water_config_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");
        ctx.output(jsonD);
    }


    //批量导入
    @Mapping("ajax/import")
    public Result importDo(Context ctx, String tag, UploadedFile file) throws Exception {
        String jsonD = IOUtils.toString(file.content);
        JsondEntity entity = JsondUtils.decode(jsonD);

        if (entity == null || "water_cfg_properties".equals(entity.table) == false) {
            return Result.failure("数据不对！");
        }

        List<ConfigDo> list = entity.data.toObjectList(ConfigDo.class);

        for (ConfigDo m : list) {
            DbWaterCfgApi.impConfig(tag, m);
        }

        return Result.succeed();
    }

    //批量处理
    @Mapping("ajax/batch")
    public Result batchDo(Context ctx, String tag, Integer act, String ids) throws Exception {
        if (act == null) {
            act = 0;
        }

        DbWaterCfgApi.delConfigByIds(act, ids);

        return Result.succeed();
    }
}
