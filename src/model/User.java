package model;

public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String role;
    private boolean isActive;

    public User(String userId, String username, String password, String role) {
        this(userId, username, password, role, true);
    }

    public User(String userId, String username, String password, String role, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    // Concrete method
    public boolean login(String username, String password) {
        if (this.isActive && this.username.equals(username) && this.password.equals(password)) {
            return true;
        }
        return false;
    }

    public void logout() {
        System.out.println(username + " logged out.");
    }

    // Abstract method: every role answers in its own way
    public abstract String[] getPermissions();
}