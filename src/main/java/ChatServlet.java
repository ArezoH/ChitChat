import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private List<Message> messages = new ArrayList<>();
    private Logger logger;
    private Database db;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.logger = new FileLogger("chat_log.txt");
        this.db = new Database();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("MySQL JDBC Driver not found", e);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("getMessages".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            try (PrintWriter out = response.getWriter()) {
                out.print(convertMessagesToJson());
            }
        }
    }
    
    private String convertMessagesToJson() {
        StringBuilder jsonBuilder = new StringBuilder("[");
        
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            jsonBuilder.append("{")
                .append("\"sender\":\"").append(escapeJson(message.getSender())).append("\",")
                .append("\"content\":\"").append(escapeJson(message.getContent())).append("\",")
                .append("\"timestamp\":\"").append(message.getTimestamp()).append("\"")
                .append("}");
            
            if (i < messages.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
    
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("sendMessage".equals(action)) {
            String sender = request.getParameter("sender");
            String content = request.getParameter("content");
            
            if (sender == null || sender.trim().isEmpty() || 
                content == null || content.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "Sender and content cannot be empty");
                return;
            }
            
            Message message = new Message(sender, content);
            messages.add(message);
            
            logger.log("New message from " + sender + ": " + content);
            db.insertMessage(sender, content, LocalDateTime.now());
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}