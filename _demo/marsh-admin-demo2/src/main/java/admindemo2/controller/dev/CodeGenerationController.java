package admindemo2.controller.dev;

import admindemo2.controller.BaseController2;
import admindemo2.dso.db.DbPaaSApi;
import admindemo2.dso.db.DbWaterCfgApi;
import admindemo2.model.view.TagCountsVo;
import admindemo2.model.data.water_cfg.ConfigDo;
import admindemo2.model.data.water_paas.PaasFileDo;
import admindemo2.model.data.water_paas.PaasFileType;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.water.utils.TextUtils;
import org.noear.wood.DbContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Mapping("/dev/code")
public class CodeGenerationController extends BaseController2 {

    private static final int CFG_TYPE_DB = 10;

    private static final String TEMPLATE_TAG = "_code";

    private static String buildSqlGetFields(String table) {
        return "SHOW FULL FIELDS FROM `" + table + "`;";
    }

    @Mapping("")
    public ModelAndView code(String tag_name) throws SQLException {

        List<TagCountsVo> tags = DbWaterCfgApi.getConfigTagsByType(CFG_TYPE_DB);

        viewModel.set("tag_name", TextUtils.isNotEmpty(tag_name) ? tag_name : (tags.size() > 0 ? tags.get(0).tag : null));

        viewModel.set("resp", tags);

        return view("dev/code");

    }

    @Mapping("inner/{tag}")
    public ModelAndView inner(String tag) throws SQLException {

        List<ConfigDo> cfgs = DbWaterCfgApi.getConfigsByType(tag, CFG_TYPE_DB);

        List<PaasFileDo> tmls = DbPaaSApi.getFileList(TEMPLATE_TAG, PaasFileType.tml);

        viewModel.set("cfgs", cfgs);
        viewModel.set("tmls", tmls);

        viewModel.set("tag_name", tag);

        return view("dev/code_inner");
    }

    @Mapping("ajax/tb")
    public Result tb(String tag,
                     String key) throws SQLException {

        ConfigDo cfg = DbWaterCfgApi.getConfigByTagName(tag , key);
        DbContext db = cfg.getDb();

        List<String> tbs = new ArrayList<>();
        db.getMetaData().getTableAll().forEach((tw)->{
            tbs.add(tw.getName());
        });
        tbs.sort(String::compareTo);

        return Result.succeed(tbs);

    }
}
