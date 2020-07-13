package by.draikon.controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static by.draikon.model.utils.Constants.URL_INDEX_HTML;
import static by.draikon.model.utils.Constants.URL_LOGOUT;

@WebServlet(URL_LOGOUT)
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + URL_INDEX_HTML);
        return;
    }
}
