package admindemo2.model.data.water;

import lombok.Getter;
import org.noear.wood.GetHandlerEx;
import org.noear.wood.IBinder;

@Getter
public class FieldDo implements IBinder {
    public String field;
    public String type;
    public String key;
    public String comment;
    public String def;

    @Override
    public void bind(GetHandlerEx source) {
        field = source.get("Field").value(null);
        type = source.get("Type").value(null);
        key = source.get("Key").value(null);
        comment = source.get("Comment").value(null);
    }

    @Override
    public IBinder clone() {
        return new FieldDo();
    }
}
