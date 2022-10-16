package admindemo2.controller.dev;

import admindemo2.controller.BaseController2;
import admindemo2.dso.db.DbPaaSApi;
import admindemo2.dso.db.DbWaterCfgApi;
import admindemo2.model.view.TagCountsVo;
import admindemo2.model.data.water.FieldDo;
import admindemo2.model.data.water_cfg.ConfigDo;
import admindemo2.model.data.water_paas.PaasFileDo;
import admindemo2.model.data.water_paas.PaasFileType;
import admindemo2.utils.UnderlineCamelUtil;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.solon.view.freemarker.RenderUtil;
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

    @Mapping("ajax/getcode")
    public Result get(String tag,
                         String key,
                         String tb,
                         int tml_id) throws Exception {

        DbContext db = DbWaterCfgApi.getConfigByTagName(tag , key).getDb();

        Map<String, Object> model = new HashMap<>();

        List<FieldDo> fields = db.sql(buildSqlGetFields(tb)).getList(FieldDo.class);

        for (FieldDo f : fields) {

            if ("PRI".equals(f.key)) {
                model.put("pri_key", f.field);
            }

            if (TextUtils.isEmpty(f.type)) {
                continue;
            }

            if (f.type.startsWith("int")||f.type.startsWith("tinyint")) {
                f.type = "int";
                f.def = "0";
            } else if (f.type.startsWith("bigint")) {
                f.type = "long";
                f.def = "0l";
            } else if (f.type.startsWith("float")) {
                f.type = "float";
                f.def = "0F";
            } else if (f.type.startsWith("double")) {
                f.type = "double";
                f.def = "0D";
            } else if (f.type.startsWith("decimal")) {
                f.type = "BigDecimal";
                f.def = "BigDecimal.ZERO";
            } else if (f.type.startsWith("varchar") || f.type.startsWith("char") || f.type.startsWith("text") || f.type.startsWith("longtext") || f.type.startsWith("json")) {
                f.type = "String";
                f.def = "null";
            } else if (f.type.startsWith("datetime") || f.type.startsWith("date") || f.type.startsWith("time")) {
                f.type = "Date";
                f.def = "null";
            }
        }

        PaasFileDo tml = DbPaaSApi.getFile(tml_id);

        model.put("fields", fields);
        model.put("table_camel", UnderlineCamelUtil.underline2Camel(tb, false));
        model.put("tag", tag);
        model.put("key", key);
        model.put("tb", tb);

        String rst = RenderUtil.render(tml.content, model);

        return Result.succeed(rst);
    }
}
