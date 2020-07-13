package by.draikon.controllers;

import by.draikon.model.exceptions.DaoException;
import by.draikon.model.fabrics.JsonResponseFactory;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import static by.draikon.model.utils.Constants.PROP_KEY;
import static by.draikon.model.utils.Constants.URL_BOOKING;

@WebServlet(URL_BOOKING)
public class BookingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties properties = (Properties) req.getServletContext().getAttribute(PROP_KEY);
        String kind = req.getParameter("kind");                                             //service, master, date
        Integer choice = Integer.parseInt(req.getParameter("choice"));                      //id
        try {
            JSONObject jsonAnswer = JsonResponseFactory
                    .getSectionDataFromFactory(properties, kind, choice);
            PrintWriter out = resp.getWriter();
            out.print(jsonAnswer);
            out.close();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
