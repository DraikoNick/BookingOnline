package by.draikon.model.impl;

import by.draikon.model.bin.Order;
import by.draikon.model.db.DBHelper;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.interfaces.BookingDAO;
import by.draikon.model.utils.DBaseQueries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingImplDaoDB implements BookingDAO {

    @Override
    public List<Order> getOrdersForMaster(int masterId) throws DaoException {   //TODO isReserved not used yet
        String QUERY = (masterId == 0) ? DBaseQueries.SQL_GET_BOOKINGS : DBaseQueries.SQL_GET_BOOKING_FOR_MASTER;
        try (DBHelper dbHelper = new DBHelper();
             PreparedStatement psGet = dbHelper.getPreparedStatement(QUERY);){
            if(masterId != 0){
                psGet.setString(1, String.valueOf(masterId));
            }
            psGet.executeQuery();
            ResultSet rs = psGet.getResultSet();
            List<Order> orders = new ArrayList<>();
            while (rs.next()){
                Order order = new Order(
                        rs.getInt(1), rs.getTimestamp(2), rs.getBoolean(3),
                        rs.getInt(4), rs.getInt(5), rs.getInt(6));
                orders.add(order);
            }
            rs.close();
            return orders;
        }catch (SQLException e){
            throw new DaoException(e.getMessage() + e.getStackTrace());
        }
    }

    @Override
    public void makeReservation(Order order) throws DaoException {
        //time, isBooked, userId, serviceId, masterId
        try(DBHelper dbHelper = new DBHelper();
            PreparedStatement psInsert = dbHelper.getPreparedStatement(DBaseQueries.SQL_INS_BOOKING);){
            psInsert.setTimestamp(1, order.getDateTime());
            psInsert.setBoolean(2, order.isBooked());
            psInsert.setInt(3, order.getUserId());
            psInsert.setInt(4, order.getServiceId());
            psInsert.setInt(5, order.getMasterId());
            synchronized (this){
                order.setId(psInsert.executeUpdate());
            }
        }catch (SQLException e){
            throw new DaoException(e.getMessage());
        }
    }
}
