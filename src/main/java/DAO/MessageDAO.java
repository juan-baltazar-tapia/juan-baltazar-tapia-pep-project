package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message insertMessage(Message message){
        
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_Message_id = (int) pkeyResultSet.getInt(1);
                return new Message(generated_Message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {

            String sql = "SELECT * FROM message" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch") );
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getAllMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {

            String sql = "SELECT * FROM message WHERE posted_by = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch") );
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message deleteMessage(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM MESSAGE WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            
            if(!rs.next()) {
                return null;
            }

            Message deletedMessage = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));

            String deleteSql = "DELETE FROM MESSAGE WHERE message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql, Statement.RETURN_GENERATED_KEYS);
            deleteStatement.setInt(1, messageId);

            deleteStatement.executeUpdate();

            return deletedMessage;

            

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }
}

// app.delete("/messages/:id", ctx -> {
//     int messageId = ctx.pathParam("id", Integer.class);
    
//     try (Connection conn = dataSource.getConnection()) {
//         // First get the message
//         String selectSql = "SELECT * FROM messages WHERE messageId = ?";
//         PreparedStatement selectStmt = conn.prepareStatement(selectSql);
//         selectStmt.setInt(1, messageId);
//         ResultSet rs = selectStmt.executeQuery();
        
//         if (!rs.next()) {
//             ctx.status(404).result("Message not found");
//             return;
//         }
        
//         // Store message details before deletion
//         Message deletedMessage = new Message(
//             rs.getInt("messageId"),
//             rs.getString("content"),
//             // ... other fields you need
//         );
        
//         // Then delete it
//         String deleteSql = "DELETE FROM messages WHERE messageId = ?";
//         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
//         deleteStmt.setInt(1, messageId);
//         deleteStmt.executeUpdate();
        
//         // Return the deleted message
//         ctx.json(deletedMessage);
//         ctx.status(200);
        
//     } catch (SQLException e) {
//         ctx.status(500).result("Error deleting message");
//     }
// });