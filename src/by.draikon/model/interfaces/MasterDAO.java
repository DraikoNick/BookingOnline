package by.draikon.model.interfaces;

import by.draikon.model.bin.Master;
import by.draikon.model.bin.Service;
import by.draikon.model.exceptions.DaoException;
import java.util.List;

public interface MasterDAO {
    public void insertMaster(Master master) throws DaoException;
    public Master getMaster(int masterId, List<Service> serviceList) throws DaoException;
    public List<Master> getMastersList(int serviceId, List<Service> serviceList) throws DaoException;  //if id == 0, then get all masters
}
