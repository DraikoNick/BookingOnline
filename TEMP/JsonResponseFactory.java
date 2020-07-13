package by.draikon.model.fabrics;

import by.draikon.model.bin.Master;
import by.draikon.model.bin.Order;
import by.draikon.model.bin.Service;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.interfaces.BookingDAO;
import by.draikon.model.interfaces.MasterDAO;
import by.draikon.model.interfaces.ServiceDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class JsonResponseFactory {
    public enum SectionKind {
        SERVICE{
            @Override
            JSONObject getSectionData(Properties properties, int choice) throws DaoException {
                JSONObject jsonResult = new JSONObject();
                if(choice > 0){        // service-true-id    (2-2 get services for master id)
                    MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                    Master master = masterDAO.getMaster(choice, getListOfServices(properties));
                    JSONArray jsonServices = new JSONArray();
                    jsonServices = master.getJsonObj().getJSONArray("services");
                    jsonResult.put("services", jsonServices);
                }else{                  // service-false    (1-1 all services)  (3-2 all services)
                    ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                    Map<String, List<Service>> map = serviceDAO.getServices();
                    JSONArray jArrCat = new JSONArray();
                    for(Map.Entry entry:map.entrySet()){
                        List<Service> services = (List<Service>) entry.getValue();
                        JSONArray jsonServices = new JSONArray();
                        for(Service service:services){
                            jsonServices.put(service.getJsonObj());
                        }
                        JSONObject joCat = new JSONObject();
                        joCat.put("category", (String) entry.getKey())
                                .put("services", jsonServices);
                        jArrCat.put(joCat);
                    }
                    jsonResult.put("services", jArrCat);
                }
                return jsonResult;
            }
        },
        MASTER{
            @Override
            JSONObject getSectionData(Properties properties, int choice) throws DaoException {
                JSONObject jsonResult = new JSONObject();
                MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                List<Master> masters = masterDAO.getMastersList(choice, getListOfServices(properties));
                JSONArray jsonMasters = new JSONArray();
                for(Master master:masters){
                    jsonMasters.put(master.getJsonObj());
                }
                jsonResult.put("masters", jsonMasters);
                /*if(isSpecified){        // master-true-id   (1-2 get masters by service id)  (3-3 get masters by service id)
                }else{                  // master-false     (2-1 get all masters)
                }*/
                return jsonResult;
            }
        },
        DATE{
            @Override
            JSONObject getSectionData(Properties properties, int choice) throws DaoException {
                BookingDAO bookingDAO = BookingDaoFactory.getDaoFromFabric(properties);
                List<Order> orders = bookingDAO.getOrdersForMaster(choice, false);  //if choice == 0, then all masters
                orders.sort(new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        return o1.getDateTime().compareTo(o2.getDateTime());
                    }
                });
                Map<String, Map<String, Set<Integer>>> mapDates = new HashMap<>();      //Date, Time, List of masters ids
                for(Order order:orders){
                    String date = order.getDate();
                    String time = order.getTime();
                    Map<String, Set<Integer>> mapTimes = new HashMap<>();
                    Set<Integer> mastersIds = new HashSet<>();
                    if(mapDates.containsKey(date)){
                        mapTimes = mapDates.get(date);
                        if(mapTimes.containsKey(time)){
                            mastersIds = mapTimes.get(time);
                        }
                    }
                    mapDates.remove(date);
                    mastersIds.add(order.getMasterId());
                    mapTimes.put(time, mastersIds);
                    mapDates.put(date, mapTimes);
                }
                JSONArray jDates = new JSONArray();
                for(Map.Entry date:mapDates.entrySet()){
                    JSONObject jDateObj = new JSONObject();
                    JSONArray jTimes = new JSONArray();
                    JSONObject jTimeObj = new JSONObject();
                    for(Map.Entry time: ((Map<String, Set<Integer>>)date.getValue()).entrySet()){
                        Set<Integer> masterIds = (Set<Integer>) time.getValue();
                        JSONArray jIds = new JSONArray(masterIds);
                        jTimeObj.put("time", (String)time.getKey())
                                .put("masterIds", jIds);
                    }
                    jTimes.put(jTimeObj);
                    jDateObj.put("date", (String) date.getKey())
                            .put("times", jTimes);
                    jDates.put(jDateObj);
                }
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("dates", jDates);
                /*if(isSpecified){        // date-true-id   (1-3 get dates for master id)  (2-3 get dates for master id)
                }else{                  // date-false     (3-1 get all dates)
                }*/
                return jsonResult;
            }
        };
        abstract JSONObject getSectionData(Properties properties, int choice) throws DaoException;
    }
    public static JSONObject getSectionDataFromFactory(Properties properties, String bookingKind, int choice) throws DaoException{
        SectionKind sectionKind = SectionKind.valueOf(bookingKind.toUpperCase());
        return sectionKind.getSectionData(properties, choice);
    }

    private static List<Service> getListOfServices(Properties properties) throws DaoException {
        ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
        Map<String, List<Service>> serviceMap= serviceDAO.getServices();
        List<Service> services = new ArrayList<>();
        for(Map.Entry entry:serviceMap.entrySet()){
            services.addAll((List<Service>)entry.getValue());
        }
        return services;
    }
}
