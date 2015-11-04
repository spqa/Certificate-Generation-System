/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import system.DBConnect;

/**
 *
 * @author super
 */
public class Admin {
    private int id;
    private String Fullname;
    private String user;
    private String Pass;
    private String DOB;
    private String Gender;
    private String Phone;
    private String Email;
    private String Address;

    public Admin() {
    }

    public Admin(int id, String Fullname, String user, String Pass, String DOB, String Gender, String Phone, String Email, String Address) {
        this.id = id;
        this.Fullname = Fullname;
        this.user = user;
        this.Pass = Pass;
        this.DOB = DOB;
        this.Gender = Gender;
        this.Phone = Phone;
        this.Email = Email;
        this.Address = Address;
    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return Fullname;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return Pass;
    }

    public String getDOB() {
        return DOB;
    }

    public String getGender() {
        return Gender;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullname(String Fullname) {
        this.Fullname = Fullname;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String Pass) {
        this.Pass = Pass;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    @Override
    public String toString() {
        return "Admin{" + "id=" + id + ", Fullname=" + Fullname + ", user=" + user + ", Pass=" + Pass + ", DOB=" + DOB + ", Gender=" + Gender + ", Phone=" + Phone + ", Email=" + Email + ", Address=" + Address + '}';
    }
    
    public static Admin GetAdminByID(int AdminID){
        Connection conn=null;
        try {
            conn = DBConnect.connectDatabase();
            PreparedStatement pre =conn.prepareStatement("select * from Admin where aid=?");
            pre.setInt(1, AdminID);
            ResultSet rs=pre.executeQuery();
            rs.next();
            Admin temp=new Admin();
            temp.setId(AdminID);
            temp.setAddress(rs.getString("Address"));
            if (rs.getDate("DOB")!=null) {
                temp.setDOB(rs.getDate("DOB").toString());
            }
            temp.setFullname(rs.getString("FullName"));
            temp.setGender(rs.getNString("Gender"));
            temp.setPass(rs.getString("APass"));
            temp.setPhone(rs.getString("Phone"));
            temp.setEmail(rs.getString("Email"));
            temp.setUser(rs.getString("AUser"));
//            System.out.println(temp.toString());
            return temp;
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (conn!=null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        Admin admin =Admin.GetAdminByID(1);
    }
}
