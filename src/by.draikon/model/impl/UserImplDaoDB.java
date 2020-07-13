package by.draikon.model.impl;

import by.draikon.model.bin.User;
import by.draikon.model.db.DBHelper;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.fabrics.UserStatusFabric;
import by.draikon.model.interfaces.UserDAO;
import by.draikon.model.utils.DBaseQueries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserImplDaoDB implements UserDAO {
    @Override
    public User insertUser(User user) throws DaoException {
        try(DBHelper dbHelper = new DBHelper();
            PreparedStatement psInsert = dbHelper.getPreparedStatement(DBaseQueries.SQL_INS_USER);
            PreparedStatement psCheck = dbHelper.getPreparedStatement(DBaseQueries.SQL_CHK_USER);){
            psInsert.setString(1, user.getName());
            psInsert.setString(2, user.getSurname().compareTo("") == 0 ? null : user.getSurname());
            psInsert.setDate(3, user.getBirthday());
            psInsert.setString(4, user.getPhone());
            psInsert.setString(5, user.getEmail().compareTo("") == 0 ? null : user.getEmail());
            psInsert.setString(6, user.getPassword());
            psInsert.setInt(7, user.getStatus().getId());
            psCheck.setString(1, user.getPhone());
            psCheck.setString(2, user.getEmail());
            synchronized (this){
                try (ResultSet rs = psCheck.executeQuery()){
                    if(rs.next()){
                        return null;        //user exist
                    }
                } catch (SQLException e){
                    throw new SQLException(e);
                }
                user.setId(psInsert.executeUpdate());
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage() + e.getStackTrace());
        }
        return user;
    }

    @Override
    public void updateUser(User user) throws DaoException {
        try(DBHelper dbHelper = new DBHelper();
            PreparedStatement psUpdate = dbHelper.getPreparedStatement(DBaseQueries.SQL_UPD_USER)){
            psUpdate.setString(1, user.getName());
            psUpdate.setString(2, user.getSurname());
            psUpdate.setDate(3, user.getBirthday());
            psUpdate.setString(4, user.getPhone());
            psUpdate.setString(5, user.getEmail());
            psUpdate.setString(6, user.getPassword());
            psUpdate.setInt(7, user.getStatus().getId());
            psUpdate.setInt(8, user.getId());
            synchronized (this){
                psUpdate.executeUpdate();
            }
        }catch (SQLException e){
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public User getUser(User user) throws DaoException {
        try (   DBHelper dbHelper = new DBHelper();
                PreparedStatement psSelect = dbHelper.getPreparedStatement(DBaseQueries.SQL_GET_USER)){
            psSelect.setString(1, user.getPhone());
            psSelect.setString(2, user.getEmail());
            psSelect.setString(3, user.getPassword());
            try(ResultSet rs = psSelect.executeQuery()){
                if (rs.next()){
                    User u = new User();
                    user = new User(rs.getInt(1), rs.getString(2),
                            rs.getString(3), rs.getDate(4),
                            rs.getString(5), rs.getString(6), "");
                    user.setStatus(UserStatusFabric.getUserStatus(rs.getInt(7)));
                }
            }
            return user;
        }catch (SQLException e){
            throw new DaoException(e.getMessage());
        }
    }
}
