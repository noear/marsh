package admindemo1.controller;

import org.noear.solon.core.handle.ModelAndView;
import org.noear.marsh.uadmin.controller.BaseController;

/**
 * @author noear 2021/2/16 created
 */
public class BaseController2 extends BaseController {
    @Override
    public ModelAndView view(String viewName) {
        viewModel.put("is_admin",1);
        viewModel.put("is_operator",1);

        return super.view(viewName);
    }
}
