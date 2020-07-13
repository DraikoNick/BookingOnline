package by.draikon.model.utils;

public class DBaseQueries {
    public static final String SQL_INS_USER = "INSERT INTO users (name, surname, birthday, phone, email, password, statusId) " +
                                                "VALUES (?,?,?,?,?,?,?);";
    public static final String SQL_GET_USER = "SELECT id, name, surname, birthday, phone, email, statusId " +
                                                "FROM users WHERE (phone = ? OR email = ?) AND password = ?";
    public static final String SQL_UPD_USER = "UPDATE users SET name = ?, surname = ?, birthday = ?, phone = ?, email = ?, password = ?, statusId = ? " +
                                                "WHERE id = ?;";
    public static final String SQL_CHK_USER = "SELECT id FROM users WHERE (phone LIKE ? AND phone > '') OR (email LIKE ? AND email > '')";

    public static final String SQL_UPD_MASTER_SERVICES = "INSERT INTO status (id, status) VALUES (?, ?) ON DUPLICATE KEY UPDATE status = VALUES(status);";
    public static final String SQL_GET_MASTER = "SELECT users.id, users.name, users.surname, users.birthday, users.phone, users.email, masters.services " +
                                                    "FROM users, masters WHERE users.id = ? && users.id = masters.userId;";

    public static final String SQL_GET_MASTERS_FOR_SERVICE =
            "SELECT id, name, surname, statusId, services FROM (SELECT * FROM users, masters) as alias WHERE id = userId && JSON_OVERLAPS(services,CONCAT('[', ? ,']')) > 0;";

    public static final String SQL_GET_MASTERS = "SELECT id, name, surname, statusId, services FROM (SELECT * FROM users, masters) as alias WHERE id = userId";

    public static final String SQL_INS_SERVICE = "INSERT INTO services (name, time, cost, serviceCatId) VALUES (?,?,?,?);";
    public static final String SQL_UPD_SERVICE = "UPDATE services SET name = ?, time = ?, cost = ?, serviceCatId = ? WHERE id = ?;";
    public static final String SQL_GET_SERVICES = "SELECT service_cat.name, services.id, services.name, services.time, services.cost, services.serviceCatId " +
                                                "FROM service_cat, services WHERE service_cat.id = services.serviceCatId;";

    public static final String SQL_INS_BOOKING = "INSERT INTO bookings (time, isBooked, userId, serviceId, masterId) VALUES (?,?,?,?,?);";
    public static final String SQL_GET_BOOKING_FOR_MASTER = "SELECT * FROM bookings WHERE masterId = ? AND time > NOW() AND isBooked = 0;";
    public static final String SQL_GET_BOOKING_FOR_SERVICE_DATE = "SELECT * FROM bookings WHERE time = ? AND isBooked = 0 " +
            "AND masterId = (SELECT userId FROM masters WHERE JSON_OVERLAPS(services,CONCAT('[', ? ,']')) > 0)";
    public static final String SQL_GET_BOOKINGS = "SELECT * FROM bookings WHERE time > NOW() AND isBooked = 0;";
}
