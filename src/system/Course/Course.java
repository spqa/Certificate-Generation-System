/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import system.DBConnect;

/**
 *
 * @author c1409l3544
 */
public class Course {
    private int id;
    private String name;
    private int money;

    public Course() {
    }

    public Course(int id, String name, int money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getMoney() {
        return money;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static Vector<Course> getAllCourses(){
        Vector<Course> lstTemp=new Vector<>();
        ResultSet rs=DBConnect.ExcuteStatement("Course");
        try {
            while (rs.next()) {
                lstTemp.add(new Course(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstTemp;
    }
}
