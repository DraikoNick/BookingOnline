package by.draikon.controllers;

import by.draikon.model.bin.User;
import by.draikon.model.db.ConnectorDB;
import by.draikon.model.exceptions.DaoException;
import by.draikon.model.fabrics.UserDaoFactory;
import by.draikon.model.interfaces.UserDAO;
import by.draikon.model.utils.Utils;
import com.mysql.cj.x.protobuf.MysqlxResultset;
import org.json.JSONObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Properties;
import static by.draikon.model.utils.Constants.*;

@WebServlet(
        urlPatterns = {URL_LOGIN, URL_REGIN, URL_INDEX},
        initParams = {
                @WebInitParam(name = PROP_KEY, value = PROP_FILE_NAME)
        },
        loadOnStartup = 1
)
public class IndexController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> user = Optional.ofNullable((User) req.getSession().getAttribute(PAR_OBJ_USER));
        PrintWriter out = resp.getWriter();
        JSONObject jsonUser = new User().getJsonObj();      //user session as guest
        if(user.isPresent()){
            jsonUser = user.get().getJsonObj();             //user session exist
        }
        JSONObject json = new JSONObject().put("user", jsonUser);
        out.print(json);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties properties = (Properties) req.getServletContext().getAttribute(PROP_KEY);
        String login = req.getParameter(PAR_USER_LOGIN);
        try{
            UserDAO userDAO = UserDaoFactory.getDaoFromFabric(properties);
            Optional<User> optionalUser;
            User user;
            if(login == null || login == ""){                       //registration
                user = new User(0,
                        req.getParameter(PAR_USER_NAME),
                        req.getParameter(PAR_USER_SURNAME),
                        req.getParameter(PAR_USER_BIRTHDAY) + ":" + req.getParameter("timezone"),
                        req.getParameter(PAR_USER_PHONE),
                        req.getParameter(PAR_USER_EMAIL),
                        req.getParameter(PAR_USER_PASSWORD));
                optionalUser = Optional.ofNullable(userDAO.insertUser(user));
            }else{                                                  //login
                user = new User(login, req.getParameter(PAR_USER_PASSWORD));
                optionalUser = Optional.ofNullable(userDAO.getUser(user));
            }
            if(optionalUser.isPresent()){
                user = optionalUser.get();
                req.getSession().setAttribute(PAR_OBJ_USER, user);
            }
            resp.sendRedirect(req.getContextPath() + URL_INDEX_HTML);
            return;
        }catch(DaoException e){
            System.err.println(e.getMessage());
            e.getStackTrace();
            throw new ServletException(e.getMessage() + e.getStackTrace());
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String propertiesName = config.getInitParameter(PROP_KEY);
        InputStream input = config.getServletContext().getResourceAsStream(PATH_RES + propertiesName);
        Properties properties = new Properties();
        try {
            properties.load(input);
            input.close();
            ConnectorDB.setProperties(properties);
            config.getServletContext().setAttribute(PROP_KEY, properties);
        } catch (IOException e) {
            throw new ServletException(e.getMessage() + e.getStackTrace());
        }
    }
}
