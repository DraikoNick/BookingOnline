package by.draikon.model.bin;

import org.json.JSONObject;

public class Service {
    private int id;
    private String name;
    private int time;   //required time in minutes for this service
    private Currency cost;
    private String type;

    public Service(){
        this(0, "", 0, new Currency());
    }
    public Service(int id, String name, int time, Currency cost){
        this.id = id;
        this.name = name;
        this.time = time;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getTime() {
        return time;
    }
    public Currency getCost() {
        return cost;
    }
    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setCost(Currency cost) {
        this.cost = cost;
    }
    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getJsonObj(){
        JSONObject json = new JSONObject();
        json.put("id",id)
                .put("name", name)
                .put("time", time)
                .put("cost", cost)
                .put("type", type);
        return json;
    }

    @Override
    public String toString() {
        return id + ";" + name + ";" + time + ";" + cost + ";" + type;
    }
}
