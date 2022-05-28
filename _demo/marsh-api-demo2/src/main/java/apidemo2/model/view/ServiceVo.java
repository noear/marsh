package apidemo2.model.view;

import lombok.Getter;
import lombok.Setter;
import org.noear.solon.annotation.Get;

import java.io.Serializable;

/**
 * @author noear 2021/2/12 created
 */
@Setter
@Getter
public class ServiceVo implements Serializable {
    private String protocol;
    private String address;
    private String meta;
    private double weight;
}
