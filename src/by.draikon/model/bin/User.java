package by.draikon.model.bin;

import by.draikon.model.fabrics.UserStatusFabric.Status;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class User {
    private int id;
    private String name;
    private String surname;
    private Date birthday;
    private String phone;
    private String email;
    private String password;
    private Status status;

    public User(){
        this(0,"","","","","","");
    }
    public User(int id, String name, String surname, Date birthday, String phone, String email, String password){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.status = Status.USER;
    }
    public User(int id, String name, String surname, String birthday, String phone, String email, String password){
        this(id, name, surname, new Date(0L), phone,email,password);
        setBirthday(birthday);
    }
    public User(String login, String password){
        this(0, "","", "", login, login, password);
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Date getBirthday() {
        return birthday;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public void setBirthday(String date)  {
        String[] time = date.split(":");
        Date dateSql = new Date(0L);
        if(date != null || date.compareTo("") != 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                int timeZoneMinutes = Integer.parseInt(time[1]);
                dateSql = new Date(sdf.parse(time[0]).getTime() - timeZoneMinutes * 60 * 1000);
            } catch (Exception e) {
                //everything OK
            }
        }
        setBirthday(dateSql);
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public JSONObject getJsonObj(){
        JSONObject json = new JSONObject();
        json.put("id",id)
                .put("name", name)
                .put("surname", surname)
                .put("birthday", birthday)
                .put("phone", phone)
                .put("email", email)
                .put("password", "")
                .put("status", status);
        return json;
    }
}
