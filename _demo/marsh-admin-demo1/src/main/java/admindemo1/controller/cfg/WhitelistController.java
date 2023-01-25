package admindemo1.controller.cfg;

import admindemo1.controller.BaseController2;
import admindemo1.dso.TagUtil;
import admindemo1.dso.db.DbWaterCfgApi;
import admindemo1.model.view.TagCountsVo;
import admindemo1.model.data.water_cfg.WhitelistDo;
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
@Mapping("/cfg/whitelist")
public class WhitelistController extends BaseController2 {

    //IP白名单列表
    @Mapping("")
    public ModelAndView whitelist(String tag_name) throws Exception {
        List<TagCountsVo> tags = DbWaterCfgApi.getWhitelistTags();

        tag_name = TagUtil.build(tag_name,tags);

        viewModel.put("tag_name",tag_name);
        viewModel.put("tags",tags);
        return view("cfg/whitelist");
    }

    @Mapping("inner")
    public ModelAndView innerDo(Context ctx, String tag_name, String key) throws Exception {
        int state = ctx.paramAsInt("state",1);

        List<WhitelistDo> list = DbWaterCfgApi.getWhitelistByTag(tag_name, key, state);

        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        viewModel.put("state", state);
        viewModel.put("key",key);

        return view("cfg/whitelist_inner");
    }

    //跳转ip白名单新增页面
    @Mapping("edit")
    public ModelAndView whitelistAdd(Integer id, String tag_name) throws SQLException {
        WhitelistDo model = null;
        if (id != null) {
            model = DbWaterCfgApi.getWhitelist(id);
            viewModel.put("m", model);
        } else {
            model = new WhitelistDo();
            viewModel.put("m", model);
        }

        if (model.tag != null) {
            tag_name = model.tag;
        }

        viewModel.put("tag_name", tag_name);
        return view("cfg/whitelist_edit");
    }

    //保存ip白名单新增
    @Mapping("edit/ajax/save")
    public Result saveWhitelistAdd(Integer row_id, String tag, String type, String value, String note) throws Exception {
        boolean result = DbWaterCfgApi.setWhitelist(row_id, tag, type, value, note);

        if (result) {
            DbWaterCfgApi.reloadWhitelist();

            return Result.succeed();
        } else {
            return Result.failure("操作失败");
        }
    }



    //删除IP白名单记录
    @Mapping("ajax/del")
    public Result saveWhitelistDel(Integer row_id) throws Exception {
        boolean result = DbWaterCfgApi.delWhitelist(row_id);

        if (result) {
            DbWaterCfgApi.reloadWhitelist();

            return Result.succeed();
        } else {
            return Result.failure("操作失败");
        }
    }



    //批量导出
    @Mapping("ajax/export")
    public void exportDo(Context ctx, String tag, String ids) throws Exception {
        List<WhitelistDo> list = DbWaterCfgApi.getWhitelistByIds(ids);

        String jsonD = JsondUtils.encode("water_cfg_whitelist", list);

        String filename2 = "water_whitelist_" + tag + "_" + Datetime.Now().getDate() + ".jsond";

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename2 + "\"");

        ctx.output(jsonD);
    }


    //批量导入
    @Mapping("ajax/import")
    public Result importDo(Context ctx, String tag, UploadedFile file) throws Exception {

        String jsonD = IOUtils.toString(file.getContent());
        JsondEntity entity = JsondUtils.decode(jsonD);

        if (entity == null || "water_cfg_whitelist".equals(entity.table) == false) {
            return Result.failure("数据不对！");
        }

        List<WhitelistDo> list = entity.data.toObjectList(WhitelistDo.class);

        for (WhitelistDo m : list) {
            DbWaterCfgApi.impWhitelist(tag, m);
        }

        return Result.succeed();
    }

    //批量删除
    @Mapping("ajax/batch")
    public Result batchDo(Context ctx, String tag, Integer act, String ids) throws Exception {
        if(act == null){
            act = 0;
        }

        DbWaterCfgApi.delWhitelistByIds(act, ids);

        return Result.succeed();
    }
}
