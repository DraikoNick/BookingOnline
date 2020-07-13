package by.draikon.model.fabrics;

import by.draikon.model.exceptions.DaoException;
import by.draikon.model.impl.MasterImplDaoDB;
import by.draikon.model.interfaces.MasterDAO;
import java.util.Properties;
import static by.draikon.model.utils.Constants.RES_MASTERS;
import static org.apache.taglibs.standard.functions.Functions.toUpperCase;

public class MasterDaoFactory {
    private enum MasterDaoImplKind {
        DATABASE{
            @Override
            MasterDAO getImpl() {
                return new MasterImplDaoDB();
            }
        };
        abstract MasterDAO getImpl();
    }
    public static MasterDAO getDaoFromFabric(Properties properties) throws DaoException {
        try{
            MasterDaoFactory.MasterDaoImplKind masterDAOImplKind = MasterDaoFactory.MasterDaoImplKind.valueOf(
                    toUpperCase(
                            properties.getProperty(RES_MASTERS)));
            return masterDAOImplKind.getImpl();
        }catch (IllegalArgumentException e){
            throw new DaoException(e.getMessage());
        }
    }
}
