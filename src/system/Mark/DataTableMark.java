/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Mark;

/**
 *
 * @author super
 */
public class DataTableMark {
    private int subjectID;
    private String Mark;

    public DataTableMark() {
    }

    public DataTableMark(int subjectID, String Mark) {
        this.subjectID = subjectID;
        this.Mark = Mark;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public String getMark() {
        return Mark;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public void setMark(String Mark) {
        this.Mark = Mark;
    }
    
}
