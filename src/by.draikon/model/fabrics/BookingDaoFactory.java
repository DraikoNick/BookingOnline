package by.draikon.model.fabrics;

import by.draikon.model.exceptions.DaoException;
import by.draikon.model.impl.BookingImplDaoDB;
import by.draikon.model.interfaces.BookingDAO;
import java.util.Properties;
import static by.draikon.model.utils.Constants.RES_ORDERS;
import static org.apache.taglibs.standard.functions.Functions.toUpperCase;

public class BookingDaoFactory {
    private enum BookingDaoImplKind {
        DATABASE{
            @Override
            BookingDAO getImpl() {
                return new BookingImplDaoDB();
            }
        };
        abstract BookingDAO getImpl();
    }
    public static BookingDAO getDaoFromFabric(Properties properties) throws DaoException {
        try{
            BookingDaoFactory.BookingDaoImplKind bookingDAOImplKind = BookingDaoFactory.BookingDaoImplKind.valueOf(
                    toUpperCase(
                            properties.getProperty(RES_ORDERS)));
            return bookingDAOImplKind.getImpl();
        }catch (IllegalArgumentException e){
            throw new DaoException(e.getMessage());
        }
    }
}
