package by.draikon.model.interfaces;

import by.draikon.model.bin.Service;
import by.draikon.model.exceptions.DaoException;
import java.util.List;
import java.util.Map;

public interface ServiceDAO {
    public void insertService(Service service) throws DaoException;
    public void updateService(Service service) throws DaoException;
    public List<Service> getServices() throws DaoException;
}
