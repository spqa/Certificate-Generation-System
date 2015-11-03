/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import system.DBConnect;

/**
 *
 * @author super
 */
public class Student {
    private int id;
    private String Username;
    private String Pass;
    private String Fullname;
    private String DOB;
    private String Gender;
    private int CourseID;
    private int FeeID;

    public Student() {
    }

    public Student(int id, String Username, String Pass, String Fullname, String DOB, String Gender, int CourseID, int FeeID) {
        this.id = id;
        this.Username = Username;
        this.Pass = Pass;
        this.Fullname = Fullname;
        this.DOB = DOB;
        this.Gender = Gender;
        this.CourseID = CourseID;
        this.FeeID = FeeID;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return Username;
    }

    public String getPass() {
        return Pass;
    }

    public String getFullname() {
        return Fullname;
    }

    public String getDOB() {
        return DOB;
    }

    public String getGender() {
        return Gender;
    }

    public int getCourseID() {
        return CourseID;
    }

    public int getFeeID() {
        return FeeID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPass(String Pass) {
        this.Pass = Pass;
    }

    public void setFullname(String Fullname) {
        this.Fullname = Fullname;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setCourseID(int CourseID) {
        this.CourseID = CourseID;
    }

    public void setFeeID(int FeeID) {
        this.FeeID = FeeID;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", Username=" + Username + ", Pass=" + Pass + ", Fullname=" + Fullname + ", DOB=" + DOB + ", Gender=" + Gender + ", CourseID=" + CourseID + ", FeeID=" + FeeID + '}';
    }
    
    
    public static Student getStudentById(int StudentId){
        Connection connection=null;
        
        try {
            connection = DBConnect.ConnectDatabase();
            PreparedStatement pre=connection.prepareStatement("Select * from Student where Stuid=?");
            pre.setInt(1, StudentId);
            ResultSet rs=pre.executeQuery();
            rs.next();
            return new Student(StudentId, rs.getString(2), rs.getString(3), rs.getNString(4), rs.getDate("DOB").toString(), rs.getString("Gender"), rs.getInt("CourseId"), rs.getInt("FeeId"));
            
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (connection!=null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
       
    }
    
    public static List<Student> getAllStudent(){
        Connection conn=null;
        
        List<Student> lstStudent=new ArrayList<>();
        try {
            conn = DBConnect.ConnectDatabase();
            PreparedStatement pre=conn.prepareStatement("select * from Student");
            ResultSet rs=pre.executeQuery();
            while (rs.next()) {                
                lstStudent.add(new Student(rs.getInt(1),rs.getString(2), rs.getString(3),rs.getNString(4), rs.getDate(5).toString(), rs.getString(6), rs.getInt(8), rs.getInt(9)));
            }
            return lstStudent;
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (conn!=null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        System.out.println(Student.getStudentById(1).toString());
    }
    
    public static DefaultTableModel getStudentTblModel(){
        DefaultTableModel tbl=new DefaultTableModel();
        tbl.addColumn("Id");
        tbl.addColumn("Fullname");
        tbl.addColumn("Gender");
        tbl.addColumn("Date Of Birth");
        tbl.addColumn("Course Name");
        tbl.addColumn("FeeType");
        return tbl;
    }
}
