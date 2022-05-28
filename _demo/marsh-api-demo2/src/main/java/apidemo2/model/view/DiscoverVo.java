package apidemo2.model.view;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author noear 2021/2/12 created
 */
@Setter
@Getter
public class DiscoverVo implements Serializable {
    private String url;
    private String policy;
    private List<ServiceVo> list;
}
