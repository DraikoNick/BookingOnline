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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookJsonFactory {
    public enum BookKind{
        SERVICES{
            @Override
            JSONArray getResponseJson(Properties properties,  String choose) throws DaoException {
                ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                List<Service> list = serviceDAO.getServices();
                JSONArray jsonArr = new JSONArray();
                for(Service service : list){
                    jsonArr.put(service.getJsonObj());
                }
                return jsonArr;
            }
        },
        SERVICES2{      //get masters (they can do serviceId) and give times for this masters
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                List<Master> masters = masterDAO
                        .getMastersList(Integer.parseInt(choose), serviceDAO.getServices());
                BookingDAO bookingDAO = BookingDaoFactory.getDaoFromFabric(properties);
                JSONArray jsonArray = new JSONArray();
                for(Master master:masters){
                    List<Order> orders = bookingDAO.getOrdersForMaster(master.getId());
                    for(Order order : orders){
                        jsonArray.put(order.getJsonObj());
                    }
                }
                return jsonArray;
            }
        },
        SERVICES3{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                try {
                    String[] chooseArr = choose.split("_"); //0-date, 1-time, 2-service
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    Date date = sdf.parse(chooseArr[0] + " " + chooseArr[1]);
                    Timestamp userTime = new Timestamp(date.getTime());
                    ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                    List<Service> services = serviceDAO.getServices();
                    MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                    List<Master> masters = masterDAO.getMastersList(Integer.parseInt(chooseArr[2]),services);
                    BookingDAO bookingDAO = BookingDaoFactory.getDaoFromFabric(properties);
                    JSONArray jsonMasters = new JSONArray();
                    for(Master master : masters){
                        List<Order> orders = bookingDAO.getOrdersForMaster(master.getId());
                        for(Order order:orders){
                            if(order.getDateTime().compareTo(userTime) == 0){
                                jsonMasters.put(master.getJsonObj());
                                break;
                            }
                        }
                    }
                    return jsonMasters;
                } catch (ParseException e) {
                    throw new DaoException(e.getMessage());
                }
            }
        },
        MASTERS{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                List<Master> masters = masterDAO.getMastersList(0, serviceDAO.getServices());
                JSONArray jsonMasters = new JSONArray();
                for(Master master:masters){
                    jsonMasters.put(master.getJsonObj());
                }
                return jsonMasters;
            }
        },
        MASTERS2{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                Master master = masterDAO
                        .getMaster(Integer.parseInt(choose), serviceDAO.getServices());
                JSONArray jsonArray = new JSONArray();
                List<Service> services = master.getServices();
                for(Service service: services){
                    jsonArray.put(service.getJsonObj());
                }
                return jsonArray;
            }
        },
        MASTERS3{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                BookingDAO bookingDAO = BookingDaoFactory.getDaoFromFabric(properties);
                List<Order> orders = bookingDAO.getOrdersForMaster(Integer.parseInt(choose));
                JSONArray json = new JSONArray();
                for(Order order:orders){
                    json.put(order.getJsonObj());
                }
                return json;
            }
        },
        ORDERS{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                BookingDAO bookingDAO = BookingDaoFactory.getDaoFromFabric(properties);
                List<Order> orders = bookingDAO.getOrdersForMaster(0);  //if choice == 0, then all masters
                JSONArray jsonArray = new JSONArray();
                for(Order order : orders){
                    jsonArray.put(order.getJsonObj());
                }
                return jsonArray;
            }
        },
        ORDERS2{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                return BookKind.SERVICES.getResponseJson(properties, choose);
            }
        },
        ORDERS3{
            @Override
            JSONArray getResponseJson(Properties properties, String choose) throws DaoException {
                MasterDAO masterDAO = MasterDaoFactory.getDaoFromFabric(properties);
                ServiceDAO serviceDAO = ServiceDaoFactory.getDaoFromFabric(properties);
                List<Master> masters = masterDAO
                        .getMastersList(Integer.parseInt(choose), serviceDAO.getServices());
                JSONArray jsonMasters = new JSONArray();
                for(Master master:masters){
                    jsonMasters.put(master.getJsonObj());
                }
                return jsonMasters;
            }
        };
        abstract JSONArray getResponseJson(Properties properties, String choose) throws DaoException;
    }
    public final static JSONObject getResponseFromFabric(Properties properties, String bookKind, String choose) throws DaoException{
        BookKind book = BookKind.valueOf(bookKind.toUpperCase());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", book.getResponseJson(properties, choose));
        jsonObject.put("type", bookKind);
        return jsonObject;
    }
}
