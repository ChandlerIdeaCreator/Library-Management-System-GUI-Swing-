package user;

public class Administrator extends User { // Extends User from the same package (user)
    public Administrator(String username, String password) {
        super(username, password, "管理员");
    }
}