package by.draikon.model.bin;

import by.draikon.model.exceptions.DaoException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static by.draikon.model.utils.Constants.*;

public class Order {
    private int id;
    private Timestamp dateTime;
    private boolean isBooked;
    private int userId;
    private int serviceId;
    private int masterId;

    public Order(){
        this(0, new Timestamp(0), false, 0,0,0);
    }
    public Order(int id, Timestamp dateTime, boolean isBooked, int userId, int serviceId, int masterId){
        this.id = id;
        this.dateTime = dateTime;
        this.isBooked = isBooked;
        this.userId = userId;
        this.serviceId = serviceId;
        this.masterId = masterId;
    }
    public Order(String date, String time, int userId, int serviceId, int masterId) throws DaoException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date datef = sdf.parse(date + " " + time);
            Timestamp userTime = new Timestamp(datef.getTime());
            this.id = 0;
            this.dateTime = userTime;
            this.isBooked = true;
            this.userId = userId;
            this.serviceId = serviceId;
            this.masterId = masterId;
        } catch (ParseException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public Timestamp getDateTime() {
        return dateTime;
    }
    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(REGEXP_DATE);
        return sdf.format(dateTime);
    }
    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat(REGEXP_TIME);
        return sdf.format(dateTime);
    }
    public boolean isBooked() {
        return isBooked;
    }
    public int getUserId() {
        return userId;
    }
    public int getServiceId() {
        return serviceId;
    }
    public int getMasterId() {
        return masterId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }
    public void setBookingStatus(boolean isBooked) {
        this.isBooked = isBooked;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public JSONObject getJsonObj(){
        JSONObject json = new JSONObject();
        json.put("id",id)
                .put("date", getDate())
                .put("time", getTime())
                .put("isBooked", isBooked)
                .put("userId", userId)
                .put("serviceId", serviceId)
                .put("masterId", masterId);
        return json;
    }
}
