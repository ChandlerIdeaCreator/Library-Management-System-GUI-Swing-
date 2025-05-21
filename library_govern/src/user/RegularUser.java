package user;

public class RegularUser extends User { // Extends User from the same package (user)
    public RegularUser(String username, String password) {
        super(username, password, "普通用户");
    }
}