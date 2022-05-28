package admindemo2.model.data.water_cfg;

import lombok.Getter;

@Getter
public class LoggerDo {
    public int logger_id;
    public String tag;
    public String logger;
    public String source;
    public int keep_days;
    public long row_num;
    public long row_num_today;
    public long row_num_today_error;
    public long row_num_yesterday;
    public long row_num_yesterday_error;
    public long row_num_beforeday;
    public long row_num_beforeday_error;
    public String note;
    public long counts;
    public int is_enabled;
    public int is_alarm;

    public boolean isHighlight() {
        return (row_num_today_error > 0);
    }
}