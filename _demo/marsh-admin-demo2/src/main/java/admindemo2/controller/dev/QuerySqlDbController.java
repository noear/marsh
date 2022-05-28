package admindemo2.controller.dev;

import admindemo2.controller.BaseController2;
import admindemo2.dso.ConfigType;
import admindemo2.dso.db.DbWaterCfgApi;
import admindemo2.model.data.water_cfg.ConfigDo;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//非单例
@Controller
@Mapping("/dev/query_db")
public class QuerySqlDbController extends BaseController2 {
   @Mapping("")
   public ModelAndView query() throws SQLException {
       List<ConfigDo> cfgs = DbWaterCfgApi.getConfigTagKeyByType(null, ConfigType.db);

       viewModel.put("cfgs", cfgs);

       return view("dev/query_db");
   }

   static Pattern limit_exp = Pattern.compile("\\s+limit\\s+(\\d+,)?(\\d+)");

   @Mapping(value = "ajax/do")
   public String query_do(String code) {

       //1.对不良条件进行过滤
       if (TextUtils.isEmpty(code)) {
           return "没有代码";
       }

       code = code.trim();
       if(code.startsWith("--")){
           code = code.substring(2);
       }

       if (code.indexOf("::") < 0 || code.startsWith("tag/key"))
           return "没有指定数据库";

       code = code.replace("\n"," ");

       String code2 = " " + code.split("::")[1].toLowerCase().trim();

       if (code.indexOf(";") >= 0) //不能有分号
           return "不能有;号";

       boolean is_ddl =false;

       if(code2.indexOf(" show create table")==0){ //因为前面加了空隔，所以从1开始
           if (code2.indexOf(" delete ") >= 0 || code2.indexOf(" update ") >= 0 || code2.indexOf(" insert ") >= 0 || code2.indexOf(" truncate ") >= 0)
               return "只支持select操作";

           is_ddl = true;
       }else {

           if (code2.indexOf(" delete ") >= 0 || code2.indexOf(" update ") >= 0 || code2.indexOf(" insert ") >= 0 || code2.indexOf(" truncate ") >= 0 || code2.indexOf(" table ") >= 0)
               return "只支持select操作";

           if (code2.indexOf(" select ") < 0)
               return "只支持select操作";

           if (code2.indexOf(" limit ") < 0)
               return "需要limit行数";
           else {
               Matcher m  = limit_exp.matcher(code2);

               if(m.find()){
                   if (Integer.parseInt(m.group(2)) > 100) {
                       return "最多不可超过100条";
                   }
               }else{
                   return "需要limit行数";
               }
           }

           if (code2.indexOf(" join ") > 0)
               return "不支持join操作";
       }



       //2.开始查询数据
       try {
           return exec_sql(code,is_ddl);
       } catch (Exception ex) {
           StringBuilder sb = new StringBuilder();
           sb.append("<p>");
           sb.append(Utils.throwableToString(ex));
           sb.append("</p>");
           return sb.toString();
       }
   }

   private String exec_sql(String code, boolean is_ddl) throws SQLException{
       String db = code.split("::")[0];
       String sql = code.split("::")[1];

       if(db.indexOf("water/water")>=0 && sql.toLowerCase().indexOf("water_cfg_properties") > 0){
           return "只支持业务库查询";
       }

       DbContext dbContext =   DbWaterCfgApi.getConfigByTagName(db).getDb(true);

       DataList list = dbContext.sql(sql).log(true).getDataList();

       //3.构建输出内容
       if(list != null && list.getRowCount()>0) {
           if(is_ddl){
               return buildDDL(list);
           }else{
               return buildData(list);
           }
       }

       return "";
   }

   private String buildDDL(DataList list){
       StringBuilder sb = new StringBuilder();
       String ddl = list.getRow(0).getString("Create Table");

       sb.append("<pre id='ddl'>");
       sb.append(ddl);
       sb.append("</pre>\n");

       return sb.toString();
   }

   private String buildData(DataList list){
       StringBuilder sb = new StringBuilder();
       DataItem tmp = list.getRow(0);

       //3.1.对列名进行排序
       Set<String> cols = tmp.keys();

       //Collections.sort(cols);

       //3.2.构建html
       sb.append("<table class=\"list\">");
       sb.append("<thead><tr>");
       for(String col : cols){
           sb.append("<td>").append(col).append("</td>");
       }
       sb.append("</tr></thead>");
       sb.append("<tbody>");

       for(DataItem item : list.getRows()){
           sb.append("<tr>");
           for(String col : cols){
               sb.append("<td>").append(item.get(col)).append("</td>");
           }
           sb.append("</tr>");
       }

       sb.append("</tbody>");

       return sb.toString();
   }



}
