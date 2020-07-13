package by.draikon.model.utils;

public class Constants {
    /*Database*/
    public static final String DB_PROPERTIES = "../web/WEB-INF/res/web.properties";
    public static final String DB_URL = "db.url";
    public static final String DB_USER = "db.user";
    public static final String DB_PASS = "db.password";
    public static final String ERROR_DB = "ERROR DB: ";
    public static final String ERROR_DB_CONNECTION = ERROR_DB + "Can not connect to DataBase.";
    public static final String ERROR_DB_CLOSE_RESOURCE = ERROR_DB + "Can not close resource.";
    public static final String ERROR_DB_QUERY = ERROR_DB + "Can not make query to database.";

    /*Controllers*/
    public static final String URL_INDEX = "/index";
    public static final String URL_INDEX_HTML = "/index.html";
    public static final String URL_LOGIN = "/login";
    public static final String URL_LOGOUT = "/logout";
    public static final String URL_REGIN = "/regin";
    public static final String URL_NEWS = "/news";
    public static final String URL_SERVICES = "/service";
    public static final String URL_BOOKING = "/booking";

    /*Path*/
    public final static String PATH_RES = "/WEB-INF/res/";
    public final static String PATH_RES_IMG = "/WEB-INF/res/images/pics";
    /*Properties*/
    public final static String PROP_FILE_NAME = "web.properties";
    public final static String PROP_KEY = "properties";
    public final static String RES_USERS = "resource.users";
    public final static String RES_MASTERS = "resource.masters";
    public final static String RES_SERVICES = "resource.services";
    public final static String RES_ORDERS = "resource.orders";
    public final static String RES_GENERATE_ORDERS = "generate.orders.for.booking";
    public final static String RES_MIN_RECORD_TIME = "min.record.time";
    /*JSON*/

    /*regexp*/
    public final static String REGEXP_DATE = "dd-MM-yyyy";
    public final static String REGEXP_TIME = "HH:mm";

    /*parameters names from page and servlet*/
    public final static String PAR_MAP_SERVICES = "services";
    public final static String PAR_OBJ_USER = "user";
    public final static String PAR_USER_LOGIN = "login";
    public final static String PAR_USER_NAME = "name";
    public final static String PAR_USER_SURNAME = "surname";
    public final static String PAR_USER_PHONE = "phone";
    public final static String PAR_USER_EMAIL = "email";
    public final static String PAR_USER_BIRTHDAY = "birthday";
    public final static String PAR_USER_PASSWORD = "password";

}
