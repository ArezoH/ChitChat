import java.time.LocalDateTime;

public class Message {
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    
    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Setters with encapsulation
    public void setSender(String sender) { 
        if (sender != null && !sender.trim().isEmpty()) {
            this.sender = sender.trim();
        }
    }
    
    public void setContent(String content) {
        if (content != null && !content.trim().isEmpty()) {
            this.content = content.trim();
        }
    }
}