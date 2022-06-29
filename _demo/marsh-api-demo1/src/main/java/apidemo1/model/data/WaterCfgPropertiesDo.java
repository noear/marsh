package apidemo1.model.data;

import java.util.Date;
import org.noear.weed.annotation.*;

@Table("water_cfg_properties")
public class WaterCfgPropertiesDo{
  @PrimaryKey
  public int row_id;
  /** 分组标签 */
  public String tag;
  /** 属性key */
  public String key;
  /** 类型：0:未知，1:数据库；2:Redis；3:MangoDb; 4:Memcached */
  public int type;
  /** 属性值 */
  public String value;
  public String edit_mode;
  /** 是否可编辑 */
  public boolean is_editable;
  /** 是否启用 */
  public int is_enabled;
  /** 更新时间 */
  public Date update_fulltime;

}

        