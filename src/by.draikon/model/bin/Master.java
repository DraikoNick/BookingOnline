package by.draikon.model.bin;

import by.draikon.model.fabrics.UserStatusFabric;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Master extends User {
    private List<Service> services;

    public Master(){
        this(0,"","", "","", "","",new ArrayList<>());
    }
    public Master(int id, String name, String surname, String birthday, String phone, String email, String password, List<Service> services){
        super(id,name,surname,birthday,phone, email,password);
        this.setStatus(UserStatusFabric.Status.MASTER);
        this.services = services;
    }
    public Master(int id, String name, List<Service> services){
        this(id,name,"","","", "","",services);
    }

    public List<Service> getServices() {
        return services;
    }
    public Integer[] getServicesId(){
        Integer[] arr = new Integer[services.size()];
        arr = services.stream().map(service -> service.getId()).toArray(Integer[]::new);
        return arr;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public JSONObject getJsonObj() {
        JSONArray jsonServices = new JSONArray();
        for(Service service:services){
            //jsonServices.put(service.getId(), service.getJsonObj());
            jsonServices.put(service.getId());
        }
        JSONObject json = super.getJsonObj();
        json.put("services", jsonServices);
        return json;
    }
}
