package by.draikon.model.fabrics;

public class UserStatusFabric {
    public enum Status {
        USER (1),
        MASTER (2),
        MANAGER (3),
        ADMIN (4);
        private int id;
        Status(int id){
            this.id = id;
        }
        public int getId() {
            return id;
        }
    }
    public static Status getUserStatus(String statusName){
        try{
            return Status.valueOf(statusName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Status.USER;
        }
    }
    public static Status getUserStatus(int statusId){
        try{
            return Status.values()[statusId-1];
        } catch (IllegalArgumentException e) {
            return Status.USER;
        }
    }
}
