package apidemo1.model.data;

import java.util.Date;
import org.noear.weed.annotation.*;

@Table("water_reg_service")
public class WaterRegServiceDo{
  @PrimaryKey
  public int service_id;
  /** md5(name+‘#’+address) */
  public String key;
  public String name;
  /** 版本号 */
  public String ver;
  public String address;
  /** 源信息 */
  public String meta;
  public String note;
  public String alarm_mobile;
  public String alarm_sign;
  /** 0:待检查；1检查中 */
  public int state;
  public String code_location;
  /** 检查方式（0被检查；1自己签到） */
  public int check_type;
  /** 状态检查地址 */
  public String check_url;
  /** 最后检查时间 */
  public Date check_last_time;
  /** 最后检查状态（0：OK；1：error） */
  public int check_last_state;
  /** 最后检查描述 */
  public String check_last_note;
  /** 检测异常数量 */
  public int check_error_num;
  /** 是否为不稳定的 */
  public int is_unstable;
  /** 是否为已启用 */
  public int is_enabled;

}

        