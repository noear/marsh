package org.noear.marsh.uadmin.dso;

import org.noear.grit.model.domain.Subject;
import org.noear.grit.solon.SessionBase;

/**
 * @author noear 2014-10-19
 * @since 1.0
 */
public final class Session extends SessionBase {
    private static final Session _current = new Session();
    public static Session current() {
        return _current;
    }


    //////////////////////////////////
    //当前项目的扩展

    @Override
    public void loadSubject(Subject subject) throws Exception {
        if (subject == null || subject.subject_id == null) {
            return;
        }

        setSubjectId(subject.subject_id);
        setLoginName(subject.login_name);
        setDisplayName(subject.display_name);
    }


    public String getValidation() {
        return localGet("validation_string", null);
    }

    public void setValidation(String validation) {
        localSet("validation_string", validation.toLowerCase());
    }
}
