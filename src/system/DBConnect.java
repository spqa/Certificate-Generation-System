/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author leeja
 */
public class DBConnect {
    
    private static Connection conn;

    public static Connection connectDatabase() throws SQLException {
        
        if (conn == null || conn.isClosed()) {
            Properties p = new Properties();
            File file = new File("config.properties");
            System.out.println(file.getAbsolutePath());
            
            // neu file khong ton tai
            if (!file.exists()) {
                try {
                    System.out.println("File not found!!!!!!!");
                    file.createNewFile();
                    p.setProperty("serverName", "localhost");
                    p.setProperty("dbName", "Certificate");
                    p.setProperty("port", "1433");
                    p.setProperty("userName", "sa");
                    p.setProperty("password", "1234567");
                    FileOutputStream fos = new FileOutputStream(file);
                    p.store(fos, "");
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                   InputStream input  = new FileInputStream(file);
                   p.load(input);
                } catch (FileNotFoundException ex) {
                  ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
           
            String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            StringBuilder sb = new StringBuilder();
            sb.append("jdbc:sqlserver://")
              .append(p.getProperty("serverName"))
              .append(":")              
              .append(p.getProperty("port"))
              .append(";databasename=")
              .append(p.getProperty("dbName"));
            
            try {
                Class.forName(driverName);
                conn = DriverManager.getConnection(sb.toString(), p.getProperty("userName"), p.getProperty("password"));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                ex.printStackTrace();
            }
        }
        return conn;
    }
}
