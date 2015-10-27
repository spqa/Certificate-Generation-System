/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Subject;

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
    
}
