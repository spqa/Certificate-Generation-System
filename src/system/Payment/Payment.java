/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.Payment;

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
public class Payment {
    private int id;
    private int StuId;
    private int Money;
    private String PaidDay;

    public Payment() {
    }

    public Payment(int id, int StuId, int Money, String PaidDay) {
        this.id = id;
        this.StuId = StuId;
        this.Money = Money;
        this.PaidDay = PaidDay;
    }

    public int getId() {
        return id;
    }

    public int getStuId() {
        return StuId;
    }

    public int getMoney() {
        return Money;
    }

    public String getPaidDay() {
        return PaidDay;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStuId(int StuId) {
        this.StuId = StuId;
    }

    public void setMoney(int Money) {
        this.Money = Money;
    }

    public void setPaidDay(String PaidDay) {
        this.PaidDay = PaidDay;
    }
    public static List<Payment> getPaymentByStuId(int StuID){
        Connection conn=null;
        
        List<Payment> lstPayment=new ArrayList<>();
        try {
            conn = DBConnect.connectDatabase();
            PreparedStatement pre=conn.prepareStatement("select * from Payment where StuId=?");
            pre.setInt(1, StuID);
            ResultSet rs=pre.executeQuery();
            while (rs.next()) {                
                lstPayment.add(new Payment(rs.getInt(1),StuID , rs.getInt(3), rs.getDate(4).toString()));
            }
            return lstPayment;
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (conn!=null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lstPayment;
    }
    public static DefaultTableModel getPaymentTable(int Studentid){
    DefaultTableModel temp=new DefaultTableModel();
    temp.addColumn("Paid");
    temp.addColumn("Day");
    
      List<Payment> lstPayment=  getPaymentByStuId(Studentid);
        for (Payment lstPayment1 : lstPayment) {
            String[] row={lstPayment1.getMoney()+"",lstPayment1.getPaidDay()};
            temp.addRow(row);
        }
    return temp;
    }
}
