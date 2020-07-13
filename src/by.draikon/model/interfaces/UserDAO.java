package by.draikon.model.interfaces;

import by.draikon.model.bin.User;
import by.draikon.model.exceptions.DaoException;

public interface UserDAO {
    public User insertUser(User user) throws DaoException;
    public void updateUser(User user) throws DaoException;
    public User getUser(User user) throws DaoException;
}
