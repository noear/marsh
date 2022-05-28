package apidemo2.model.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author noear 2021/2/11 created
 */
@Setter
@Getter
@Builder
public class ConfigVo implements Serializable {
    private String key;
    private String value;
    private long lastModified;
}
