public class User {
    private String username;
    private boolean active;
    
    public User(String username) {
        this.username = username;
        this.active = true;
    }
    
    // Getters and setters with encapsulation
    public String getUsername() { return username; }
    
    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
        }
    }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}