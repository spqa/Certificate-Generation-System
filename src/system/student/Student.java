/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.student;

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
    
}
