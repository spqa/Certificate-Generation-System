/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.util.ArrayList;
import java.util.List;
import system.student.Student;

/**
 *
 * @author super
 */
public class Filter {

    public static List<Student> NameFilter(List<Student> lst, String name) {
        if (name != null) {
            List<Student> temp = new ArrayList<>();
            for (Student lst1 : lst) {
                if (lst1.getFullname().toLowerCase().contains(name.toLowerCase())) {
                    temp.add(lst1);
                }
            }
           
            return temp;
        }
        return lst;
    }
    
    private static String ExtractDOB(String DOB){
        String[] temp;
        temp=DOB.split("-");
        String Extracted="";
        for (int i = 0; i < temp.length; i++) {
            if (temp[i]!=null) {
                Extracted=Extracted+temp[i].trim();
            }
        }
        //System.out.println(Extracted);
        return Extracted;
    }

    public static List<Student> DOBFilter(List<Student> lst, String DOB) {
        if (true) {
            List<Student> temp = new ArrayList<>();
            for (Student lst1 : lst) {
                if (ExtractDOB(lst1.getDOB()).contains(ExtractDOB(DOB))) {
                    temp.add(lst1);
                }
            }
            return temp;
        }
        return lst;
    }

    public static List<Student> GenderFilter(List<Student> lst, String Gender) {
        if (Gender != "Not set") {
            List<Student> temp = new ArrayList<>();
            for (Student lst1 : lst) {
                if (lst1.getGender().equals(Gender)) {
                    temp.add(lst1);
                }
            }
            return temp;
        }
        return lst;
    }

    public static List<Student> CourseFilter(List<Student> lst, int courseID) {
        if (courseID != 0) {
            List<Student> temp = new ArrayList<>();
            for (Student lst1 : lst) {
                if (lst1.getCourseID() == courseID) {
                    temp.add(lst1);
                }
            }
            return temp;
        }
        return lst;
    }

    public static List<Student> FeeFilter(List<Student> lst, int Feetype) {
        if (Feetype !=0) {
            List<Student> temp = new ArrayList<>();
            for (Student lst1 : lst) {
                if (lst1.getFeeID()==Feetype) {
                    temp.add(lst1);
                }
            }
            return temp;
        }
        return lst;
    }
    
    public static void main(String[] args) {
        List<Student> temp=Filter.DOBFilter(Student.getAllStudent(), "1996-12-  ");
        for (Student temp1 : temp) {
            System.out.println(temp1.toString());
        }
        
        //System.out.println(ExtractDOB("1996-12-  "));
    }
}
