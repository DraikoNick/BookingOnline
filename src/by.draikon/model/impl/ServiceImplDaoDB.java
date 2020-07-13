package by.draikon.model.impl;

import by.draikon.model.bin.Currency;
import by.draikon.model.bin.Service;
import by.draikon.model.db.DBHelper;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.interfaces.ServiceDAO;
import by.draikon.model.utils.DBaseQueries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceImplDaoDB implements ServiceDAO {
    @Override
    public void insertService(Service service) throws DaoException {

    }

    @Override
    public void updateService(Service service) throws DaoException {

    }

    @Override
    public List<Service> getServices() throws DaoException {
        try (DBHelper dbHelper = new DBHelper();
             PreparedStatement psGet = dbHelper.getPreparedStatement(DBaseQueries.SQL_GET_SERVICES);) {
            ResultSet rs = psGet.executeQuery();
            List<Service> services = new ArrayList<>();
            while(rs.next()){
                Service service = new Service(rs.getInt(2), rs.getString(3),
                        rs.getInt(4), new Currency(rs.getInt(5)));
                service.setType(rs.getString(1));
                services.add(service);
            }
            return services;
        }catch (SQLException e){
            throw new DaoException(e.getMessage());
        }
    }
}
