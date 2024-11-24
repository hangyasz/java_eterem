package role;

public class User {
    private String username;
    private String password;
    private Role role;
    private Runnable onChangeCallback; // Megfigyelő


    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
        notifyChange();
    }

    public void setRole(Role role) {
        this.role = role;
        notifyChange();
    }

    public void setUsername(String username) {
        this.username = username;
        notifyChange();
    }

    public void setOnChangeCallback(Runnable callback) {
        this.onChangeCallback = callback;
    }

    private void notifyChange() {
        if (onChangeCallback != null) {
            onChangeCallback.run();
        }
    }
}