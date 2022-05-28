package apidemo1.model.view;

import lombok.Builder;

/**
 * @author noear 2021/2/11 created
 */
@Builder
public class ConfigVo {
    public String key;
    public String value;
    public long lastModified;
}
