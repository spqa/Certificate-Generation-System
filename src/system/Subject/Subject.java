/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import system.DBConnect;

/**
 *
 * @author c1409l3544
 */
public class Subject {
    private int SubID;
    private int CourseID;
    private String name;

    public Subject() {
    }

    public Subject(int SubID, int CourseID, String name) {
        this.SubID = SubID;
        this.CourseID = CourseID;
        this.name = name;
    }

    public int getSubID() {
        return SubID;
    }

    public int getCourseID() {
        return CourseID;
    }

    public String getName() {
        return name;
    }

    public void setSubID(int SubID) {
        this.SubID = SubID;
    }

    public void setCourseID(int CourseID) {
        this.CourseID = CourseID;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    public static List<Subject> getListSubject(int CourseID){
    List<Subject> lstTemp=new ArrayList<>();
        Connection conn=DBConnect.ConnectDatabase();
        try {
            PreparedStatement pre=conn.prepareStatement("select * from subject where courseid=?");
            pre.setInt(1, CourseID);
            ResultSet rs=pre.executeQuery();
            while(rs.next()){
                Subject s=new Subject(rs.getInt(1), rs.getInt(2), rs.getString(3));
                lstTemp.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstTemp;
    }
}
