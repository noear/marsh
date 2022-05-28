package admindemo2.controller.dev;


import admindemo2.controller.BaseController2;
import admindemo2.dso.ConfigType;
import admindemo2.dso.db.DbWaterCfgApi;
import admindemo2.dso.EsUtil;
import admindemo2.model.data.water_cfg.ConfigDo;
import admindemo2.utils.JsonFormatTool;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

//非单例
@Controller
@Mapping("/dev/query_es")
public class QueryEsController extends BaseController2 {
    @Mapping("")
    public ModelAndView query() throws SQLException {
        List<ConfigDo> list = DbWaterCfgApi.getConfigTagKeyByType(null, ConfigType.elasticsearch);

        viewModel.put("cfgs", list);

        return view("dev/query_es");
    }

    static Pattern limit_exp = Pattern.compile("\\s+limit\\s+(\\d+)");


    @Mapping(value = "ajax/do")
    public String query_do(String code) {
        String tmp = query_exec(code);
        if (tmp != null && tmp.startsWith("{")) {
            return JsonFormatTool.formatJson(tmp);
        } else {
            return tmp;
        }
    }

    public String query_exec(String code) {
        ONode node = new ONode();
        //1.对不良条件进行过滤
        if (TextUtils.isEmpty(code)) {
            return node.val("请输入代码").toJson();
        }

        if (code.indexOf("#") < 0)
            return node.val("请指定配置").toJson();

        String[] ss = code.trim().split("\n");

        if (ss.length < 3) {
            return node.val("请输入有效代码").toJson();
        }

        //1.检查配置
        String cfg_str = ss[0].trim();
        if (cfg_str.startsWith("#") == false || cfg_str.indexOf("/") < 0) {
            return node.val("请输入有效的配置").toJson();
        } else {
            cfg_str = cfg_str.substring(1).trim();
        }

        //2.检查 methon 和 path
        String methodAndPath = ss[1].trim().replaceAll("\\s+", " ");
        String[] ss2 = methodAndPath.split(" ");

        if (ss2.length != 2) {
            return node.val("请输入有效的Method和Path").toJson();
        }

        String method = ss2[0].trim().toUpperCase();
        String path = ss2[1].trim();

        if ((method.equals("GET") && path.endsWith("_search")) == false) {
            return node.val("只支持查询操作").toJson();
        }


        //3.检查 json
        StringBuilder json = new StringBuilder();
        for (int i = 2, len = ss.length; i < len; i++) {
            json.append(ss[i]);
        }

        if( ONode.loadStr(json.toString()).isObject()==false){
            return node.val("请输入有效的Json代码").toJson();
        }

        try {
            ConfigDo cfg = DbWaterCfgApi.getConfigByTagName(cfg_str);

            return EsUtil.search(cfg, method, path, json.toString());
        } catch (Exception ex) {
            return ONode.serialize(ex);
        }
    }
}

