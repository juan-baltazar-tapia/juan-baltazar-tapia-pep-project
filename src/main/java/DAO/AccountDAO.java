package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {

    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "SELECT * FROM account WHERE username = ? AND password = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
           
            ResultSet rs = preparedStatement.executeQuery();

            if(!rs.next()){
               return null; 
            } else {
                return new Account(rs.getInt("account_id"), account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account register(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "SELECT * FROM account WHERE username = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
           
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next() || account.getPassword().length() < 4 || account.getUsername() == null || account.getUsername().trim().isEmpty()){
               return null; 
            } 
            // Username does not exist
            String registerSql = "INSERT INTO ACCOUNT (username, password) VALUES (?,?)";
            PreparedStatement registerStatement = connection.prepareStatement(registerSql, Statement.RETURN_GENERATED_KEYS);
            registerStatement.setString(1, account.getUsername());
            registerStatement.setString(2, account.getPassword());

            registerStatement.executeUpdate();

            ResultSet pkeyResultSet = registerStatement.getGeneratedKeys();

            if (pkeyResultSet.next()) {
            int generatedAccountId = pkeyResultSet.getInt(1);
      
            return new Account(generatedAccountId, account.getUsername(), account.getPassword());    
            }
           
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
