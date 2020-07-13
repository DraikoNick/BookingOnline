package by.draikon.model.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static by.draikon.model.utils.Constants.PAR_USER_BIRTHDAY;

public class Utils {
    public final static Timestamp convertToServerDateFromUserDate(String userDate, String userOffset) throws ParseException {
        int timeZoneMinutes = Integer.parseInt(userOffset);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp date = new Timestamp(sdf.parse(userDate.substring(0,10)).getTime() - timeZoneMinutes * 60 * 1000);
        return date;
    }

}
