package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // <--- 添加导入

// Needs User, Administrator, RegularUser from the same package (user)

public class UserManager {
    private List<User> users;
    private User loggedInUser = null;

    public UserManager() {
        users = new ArrayList<>();
        boolean adminCreated = registerUser("administrator12", "admin882865", "管理员");
        if (adminCreated) {
            System.out.println("控制台: Default admin user 'administrator12' with password 'admin882865' created.");
        } else {
            System.err.println("控制台: Failed to create default admin user.");
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public boolean registerUser(String username, String password, String role) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            System.out.println("控制台: 用户名和密码不能为空！");
            return false;
        }
        String trimmedUsername = username.trim();
        String trimmedRole = role != null ? role.trim() : "";

        if (findUser(trimmedUsername).isPresent()) {
            System.out.println("控制台: 用户 " + trimmedUsername + " 已存在！");
            return false;
        }

        User newUser = null;
        if ("管理员".equals(trimmedRole)) {
            boolean isAdminLoggedIn = (loggedInUser != null && loggedInUser.getRole().equals("管理员"));
            boolean noUsersExist = users.isEmpty();

            if (!isAdminLoggedIn && !noUsersExist) {
                System.out.println("控制台: 对不起，只有当前登录的管理员才能注册新的管理员用户 (当系统已有用户时)。");
                return false;
            }
            newUser = new Administrator(trimmedUsername, password);

        } else if ("普通用户".equals(trimmedRole)) {
            newUser = new RegularUser(trimmedUsername, password);
        } else {
            System.out.println("控制台: 角色无效哦，请您注意提示！");
            return false;
        }

        if (newUser != null) {
            users.add(newUser);
            System.out.println("控制台: 恭喜！用户 " + trimmedUsername + " 注册成功！");
            return true;
        }
        return false;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            System.out.println("控制台: 用户名和密码不能为空！");
            return false;
        }
        Optional<User> userOpt = findUser(username.trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.verifyPassword(password)) {
                loggedInUser = user;
                System.out.println("控制台: 登录成功！欢迎 " + username + "(" + loggedInUser.getRole() + ")");
                return true;
            }
        }

        loggedInUser = null;
        System.out.println("控制台: sorry!登录失败,您的用户名或密码有误");
        return false;
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println("控制台: 好的，用户 " + loggedInUser.getUsername() + " 已注销。");
            loggedInUser = null;
        } else {
            System.out.println("控制台: 额...当前无用户登录。");
        }
    }

    public boolean deleteUser(String usernameToDelete) {
        if (usernameToDelete == null || usernameToDelete.trim().isEmpty()) {
            System.out.println("控制台: 删除物品时用户名不能为空！");
            return false;
        }
        String trimmedUsername = usernameToDelete.trim();

        if (loggedInUser == null || !loggedInUser.getRole().equals("管理员")) {
            System.out.println("控制台: 对不起，您的权限不足！只有管理员可以删除用户");
            return false;
        }

        if (loggedInUser.getUsername().equals(trimmedUsername)) {
            System.out.println("控制台: 您不能删除当前登录的账户！");
            return false;
        }

        boolean removed = users.removeIf(user -> user.getUsername().equals(trimmedUsername));

        if (removed) {
            System.out.println("控制台: 恭喜！用户 " + trimmedUsername + " 删除成功！");
        } else {
            System.out.println("控制台: sorry!未找到用户 " + trimmedUsername);
        }
        return removed;
    }

    private Optional<User> findUser(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}