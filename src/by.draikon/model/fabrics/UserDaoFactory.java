package by.draikon.model.fabrics;

import by.draikon.model.exceptions.DaoException;
import by.draikon.model.impl.UserImplDaoDB;
import by.draikon.model.interfaces.UserDAO;
import java.util.Properties;
import static by.draikon.model.utils.Constants.RES_USERS;
import static org.apache.taglibs.standard.functions.Functions.toUpperCase;

public class UserDaoFactory {
    private enum UserDaoImplKind {
        DATABASE{
            @Override
            UserDAO getImpl() {
                return new UserImplDaoDB();
            }
        };
        abstract UserDAO getImpl();
    }
    public static UserDAO getDaoFromFabric(Properties properties) throws DaoException {
        try{
            UserDaoImplKind userDAOImplKind = UserDaoImplKind.valueOf(
                    toUpperCase(
                            properties.getProperty(RES_USERS)));
            return userDAOImplKind.getImpl();
        }catch (IllegalArgumentException e){
            throw new DaoException(e.getMessage());
        }
    }
}
