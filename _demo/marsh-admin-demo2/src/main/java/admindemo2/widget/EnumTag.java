package admindemo2.widget;

import admindemo2.dso.EnumUtil;
import admindemo2.model.data.water_cfg.EnumDo;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.NvMap;
import org.noear.water.utils.TextUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component("view:enum")
public class EnumTag implements TemplateDirectiveModel {
    private String group;
    private String style; //select,checkbox,radio

    private String id;
    private String name;
    private String value;

    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        build(env,map);
        //如果它是单例的，可以改为：
        //new EnumTag().build(env, map);
    }

    private void build(Environment env, Map map){
        NvMap mapExt = new NvMap(map);
        group = mapExt.get("group");
        style = mapExt.get("style");
        id =  mapExt.get("id");
        name =  mapExt.get("name");
        value =  mapExt.get("value");

        if (TextUtils.isEmpty(group) == false) {
            try {
                String tagHtml = buildHtml();
                env.getOut().write(tagHtml);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private String buildHtml() throws Exception {
        List<EnumDo> enumList = EnumUtil.get(group);

        StringBuffer sb = new StringBuffer();

        if ("select".equals(style)) {
            buildHtmlForSelect(enumList, sb);
        } else if ("checkbox".equals(style)) {
            buildHtmlForCheckbox(enumList, sb);
        } else if ("radio".equals(style)) {
            buildHtmlForRadio(enumList, sb);
        }

        return sb.toString();
    }

    private void buildHtmlForSelect(List<EnumDo> enumList, StringBuffer sb) {
        sb.append("<select ");
        if (TextUtils.isEmpty(id) == false) {
            sb.append("id=\"").append(id).append("\" ");
        }

        if (TextUtils.isEmpty(name) == false) {
            sb.append("name=\"").append(name).append("\" ");
        }

        sb.append(">");

        for (EnumDo m : enumList) {
            sb.append("<option value='").append(m.value).append("'");

            if (String.valueOf(m.value).equals(value)) {
                sb.append(" selected=\"selected\"");
            }

            sb.append(">").append(m.title).append("</option>");
        }

        sb.append("</select>");
    }

    private void buildHtmlForCheckbox(List<EnumDo> enumList, StringBuffer sb) {

        List<String> list = null;
        if (!TextUtils.isEmpty(value)) {
            list = Arrays.asList(value.split(","));
        }

        for (EnumDo m : enumList) {
            sb.append("<label><input type=\"checkbox\" name='").append(name).append("' value='").append(m.value).append("'");

            if (list != null && list.contains(String.valueOf(m.value))) {
                sb.append(" checked ");
            }

            sb.append("><a>").append(m.title).append("</a></label>\n");
        }
    }

    private void buildHtmlForRadio(List<EnumDo> enumList, StringBuffer sb) {

        for (EnumDo m : enumList) {
            sb.append("<label><input type=\"radio\" name='").append(name).append("' value='").append(m.value).append("'");

            if (String.valueOf(m.value).equals(value)) {
                sb.append(" checked ");
            }

            sb.append("><a>").append(m.title).append("</a></label>\n");
        }
    }
}
