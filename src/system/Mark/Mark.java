/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Mark;

/**
 *
 * @author c1409l3544
 */
public class Mark {
    private int StuId;
    private int SubID;
    private int mark;
    private int Grade;

    public Mark() {
    }

    public Mark(int StuId, int SubID, int mark, int Grade) {
        this.StuId = StuId;
        this.SubID = SubID;
        this.mark = mark;
        this.Grade = Grade;
    }

    public int getStuId() {
        return StuId;
    }

    public int getSubID() {
        return SubID;
    }

    public int getMark() {
        return mark;
    }

    public int getGrade() {
        return Grade;
    }

    public void setStuId(int StuId) {
        this.StuId = StuId;
    }

    public void setSubID(int SubID) {
        this.SubID = SubID;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setGrade(int Grade) {
        this.Grade = Grade;
    }
    
}
