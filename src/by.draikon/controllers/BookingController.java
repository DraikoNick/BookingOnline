package by.draikon.controllers;

import by.draikon.model.bin.Order;
import by.draikon.model.bin.User;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.fabrics.BookJsonFactory;
import by.draikon.model.fabrics.BookingDaoFactory;
import by.draikon.model.fabrics.UserDaoFactory;
import by.draikon.model.interfaces.BookingDAO;
import by.draikon.model.interfaces.UserDAO;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Properties;
import static by.draikon.model.utils.Constants.*;

@WebServlet(URL_BOOKING)
public class BookingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties properties = (Properties) req.getServletContext().getAttribute(PROP_KEY);
        String kind = req.getParameter("kind");                                             //services, masters, orders
        String choice = req.getParameter("choice");                                         //
        try {
            JSONObject jsonAnswer = BookJsonFactory.getResponseFromFabric(properties, kind, choice);
            PrintWriter out = resp.getWriter();
            out.print(jsonAnswer);
            out.close();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Properties properties = (Properties) req.getServletContext().getAttribute(PROP_KEY);
            Optional<User> optionalUser = Optional.ofNullable((User) req.getSession().getAttribute(PAR_OBJ_USER));
            if(!optionalUser.isPresent()){
                User user = new User(0,
                        req.getParameter(PAR_USER_NAME),
                        "",
                        "",
                        req.getParameter(PAR_USER_PHONE),
                        "",
                        req.getParameter(PAR_USER_PASSWORD));
                UserDAO userDAO = UserDaoFactory.getDaoFromFabric(properties);
                optionalUser = Optional.ofNullable(userDAO.insertUser(user));
            }
            BookingDAO bookingDAO = BookingDaoFactory.getDaoFromFabric(properties);
            String date = req.getParameter("date");
            String time = req.getParameter("time");
            int serviceId = Integer.parseInt(req.getParameter("serviceId"));
            int masterId = Integer.parseInt(req.getParameter("masterId"));
            int userId = (optionalUser.isPresent()) ? optionalUser.get().getId() : 0;
            Order order = new Order(date, time, userId, serviceId, masterId);
            if(userId != 0){
                bookingDAO.makeReservation(order);
                resp.sendRedirect(req.getContextPath() + URL_INDEX_HTML);
                return;
            }
            throw new DaoException("Не получилось Вас записать - проблемы на сервере.");
        } catch (DaoException e) {
            e.printStackTrace();
        }


    }
}
