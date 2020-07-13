package by.draikon.model.impl;

import by.draikon.model.bin.Master;
import by.draikon.model.bin.Service;
import by.draikon.model.db.DBHelper;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.fabrics.UserStatusFabric;
import by.draikon.model.interfaces.MasterDAO;
import by.draikon.model.utils.DBaseQueries;
import org.json.JSONArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MasterImplDaoDB implements MasterDAO {
    @Override
    public void insertMaster(Master master) throws DaoException {
        try (DBHelper dbHelper = new DBHelper();
             PreparedStatement psInsert = dbHelper.getPreparedStatement(DBaseQueries.SQL_UPD_MASTER_SERVICES);){
            psInsert.setInt(1, master.getId());
            psInsert.setString(2, new JSONArray(master.getServicesId()).toString());
            psInsert.executeUpdate();
        }catch (SQLException e){
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Master getMaster(int masterId, List<Service> serviceList) throws DaoException {
        try (DBHelper dbHelper = new DBHelper();
            PreparedStatement psGetMaster = dbHelper.getPreparedStatement(DBaseQueries.SQL_GET_MASTER);) {
            psGetMaster.setInt(1, masterId);
            Master master = new Master();
            JSONArray jsonServiceIds = new JSONArray();
            try (ResultSet rs = psGetMaster.executeQuery()){
                if(rs.next()){
                    master = new Master(rs.getInt(1),
                            rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5),
                            rs.getString(6), "",
                            new ArrayList<Service>());
                    jsonServiceIds = new JSONArray(rs.getString(7));
                }
            }catch (SQLException e){
                throw new SQLException(e);
            }
            master.setServices(getServicesFromJson(jsonServiceIds, serviceList));
            return master;
        }catch (SQLException e){
            throw new DaoException(e.getMessage() + e.getStackTrace());
        }
    }

    @Override
    public List<Master> getMastersList(int serviceId, List<Service> serviceList) throws DaoException {
        String QUERY = (serviceId == 0) ? DBaseQueries.SQL_GET_MASTERS : DBaseQueries.SQL_GET_MASTERS_FOR_SERVICE;
        try (DBHelper dbHelper = new DBHelper();
            PreparedStatement psGet = dbHelper.getPreparedStatement(QUERY);){
            if(serviceId != 0){
                psGet.setString(1, String.valueOf(serviceId));
            }
            psGet.executeQuery();
            ResultSet rs = psGet.getResultSet();
            List<Master> masters = new ArrayList<>();
            while (rs.next()){
                Master master = new Master(rs.getInt(1), rs.getString(2), rs.getString(3),
                        "","","","",new ArrayList<>());
                master.setStatus(UserStatusFabric.getUserStatus(rs.getInt(4)));
                JSONArray jServices = new JSONArray(rs.getString(5));
                master.setServices(getServicesFromJson(jServices, serviceList));
                masters.add(master);
            }
            rs.close();
            return masters;
        }catch (SQLException e){
            throw new DaoException(e.getMessage() + e.getStackTrace());
        }
    }

    private static List<Service> getServicesFromJson(JSONArray ids, List<Service> services){
        List<Integer> masterIds = ids.toList().stream()
                .map(el -> (Integer)el).collect(Collectors.toList());
        List<Service> masterServices = new ArrayList<>();
        for(Service service:services){
            if(masterIds.contains(service.getId())){
                masterServices.add(service);
            }
        }
        return masterServices;
    }
}
