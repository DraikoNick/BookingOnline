package by.draikon.model.interfaces;

import by.draikon.model.bin.Order;
import by.draikon.model.exceptions.DaoException;
import java.util.List;

public interface BookingDAO {
    public List<Order> getOrdersForMaster(int masterId) throws DaoException;
    public void makeReservation(Order order) throws DaoException;
}
