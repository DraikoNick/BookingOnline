package by.draikon.model.fabrics;

import by.draikon.model.exceptions.DaoException;
import by.draikon.model.impl.ServiceImplDaoDB;
import by.draikon.model.interfaces.ServiceDAO;
import java.util.Properties;
import static by.draikon.model.utils.Constants.RES_SERVICES;
import static org.apache.taglibs.standard.functions.Functions.toUpperCase;

public class ServiceDaoFactory {
    private enum ServiceDaoImplKind {
        DATABASE{
            @Override
            ServiceDAO getImpl() {
                return new ServiceImplDaoDB();
            }
        };
        abstract ServiceDAO getImpl();
    }
    public static ServiceDAO getDaoFromFabric(Properties properties) throws DaoException {
        try{
            ServiceDaoFactory.ServiceDaoImplKind serviceDAOImplKind =
                    ServiceDaoFactory.ServiceDaoImplKind.valueOf(toUpperCase(properties.getProperty(RES_SERVICES)));
            return serviceDAOImplKind.getImpl();
        }catch (IllegalArgumentException e){
            throw new DaoException(e.getMessage());
        }
    }
}
