package cse530a.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cse530a.model.User;

public class UserDao {
    public static User createUser(Connection conn, String username, String password) {
        // FIXME
        return null;
    }
    
    public static void updateUser(Connection conn, User user) {
        // FIXME
    }
    
    public static void deleteUser(Connection conn, User user) {
        // FIXME
    }

    public static User retrieveUser(Connection conn, Long id) {
        // FIXME
        return null;
    }
    
    private static final String RETRIEVE_USER = " select user_id, username, password from users where username = ? ";
    
    public static User retrieveUser(Connection conn, String username) throws SQLException {
        User user = null;
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(RETRIEVE_USER);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DatabaseManager.closeResultSet(rs);
            DatabaseManager.closeStatement(stmt);
        }
        
        return user;
    }
    
}
