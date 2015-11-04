/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.sql.CallableStatement;
import system.Course.Course;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import system.Mark.DataTableMark;
import system.Mark.Mark;
import system.Payment.Payment;
import system.Subject.Subject;
import system.admin.Admin;
import system.student.Student;

/**
 *
 * @author super
 */
public class AdminPage extends javax.swing.JFrame {

    DefaultTableModel StudentTblModel;
    EnterMarkPageFrame emp;
    Vector<Course> lstCourse;
    int adminID;
    List<Student> lstStudent;
    DefaultListModel<Student> feedbackModel;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    String year;
    String month;

    /**
     * Creates new form NewJFrame
     *
     * @param AdminID
     */
    public AdminPage(int AdminID) {
        java.util.Date date = new java.util.Date();
        DateFormat df = new SimpleDateFormat("YYYY-MM");
        year = df.format(date).substring(2, 4);
        month = df.format(date).substring(5);
        StudentTblModel = Student.getStudentTblModel();
        this.adminID = AdminID;
        lstStudent = Student.getAllStudent();
        initComponents();
        LoadAdminData();
        ComboBoxData();
        LoadCourseList();
        LoadOtherControlStudentManager();
        LoadDataStudent();
        LoadFeedbackPage();
        tblStudentData.getSelectionModel().addListSelectionListener(tblStudentChangeListner);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int rs = JOptionPane.showConfirmDialog(null, "Are you sure want to exit?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.CLOSED_OPTION, new ImageIcon(getClass().getResource("/res/Help60.png")));
                if (rs == 0) {
                    System.exit(0);
                }
            }

        });
    }

    private void LoadAdminData() {
        Admin CurrentAdmin = Admin.GetAdminByID(adminID);
        lblUserName.setText(CurrentAdmin.getFullname());
        txtAdminFullName.setText(CurrentAdmin.getFullname());
        lblUserName.setText(CurrentAdmin.getFullname());
        txtAdminAddress.setText(CurrentAdmin.getAddress() + "");
        txtAdminDOB.setText(CurrentAdmin.getDOB() + "");
        txtAdminEmail.setText(CurrentAdmin.getEmail() + "");
        txtAdmnPhone.setText(CurrentAdmin.getPhone() + "");
        if (CurrentAdmin.getGender() != null) {
            if (CurrentAdmin.getGender().equals("Male")) {
                rdAdminFemale.setSelected(true);
            } else if (CurrentAdmin.getGender().equals("Female")) {
                rdAdminFemale.setSelected(true);
            }
        }
    }

    ;

    private String checkGender() {
        if (rdFemale.isSelected()) {
            return "Female";
        } else if (rdMale.isSelected()) {
            return "Male";
        }
        return null;
    }

    private int CheckFeeID() {
        if (rdAll.isSelected()) {
            return 1;
        } else if (rdInstallment.isSelected()) {
            return 2;
        }
        return 0;
    }
    
    private boolean validateForm() {

        if (txtStudentName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "The Fullname can not be empty!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
            return false;
        } else if (grGender.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please choose type of Gender!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
            return false;
        } else if (grPayment.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please choose student Payment!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
            return false;
        } else {
            return true;
        }

    }

    private void ComboBoxData() {
        Connection conn = null;
        try {
            conn = DBConnect.connectDatabase();
            PreparedStatement preStmt = conn.prepareStatement(" SELECT * FROM Course ");
            ResultSet rsCourse = preStmt.executeQuery();
            lstCourse = new Vector<>();
            while (rsCourse.next()) {
                Course temp = new Course(rsCourse.getInt(1), rsCourse.getString(2), rsCourse.getInt(3));
                lstCourse.add(temp);

            }
            DefaultComboBoxModel<Course> ModelCbCourse = new DefaultComboBoxModel<>(lstCourse);
            cbCourseName.setModel(ModelCbCourse);

            DefaultListModel<Subject> ModelSJ = new DefaultListModel<>();

            PreparedStatement prestmt = conn.prepareStatement("SELECT * FROM Subject WHERE CourseId=?");
            prestmt.setInt(1, ((Course) cbCourseName.getSelectedItem()).getId());
            ResultSet rsSubj = prestmt.executeQuery();

            while (rsSubj.next()) {
                Subject temp = new Subject(rsSubj.getInt(1), rsSubj.getInt(2), rsSubj.getString(3));
                ModelSJ.addElement(temp);
            }
            lstSubjects.setModel(ModelSJ);

            rsCourse.close();
            rsSubj.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void LoadCourseList() {
        DefaultListModel<Course> CourseModel = new DefaultListModel<>();

        for (Course lstCourse1 : lstCourse) {
            CourseModel.addElement(lstCourse1);
        }

        JListCourse.setModel(CourseModel);

        JListCourse.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                List<Subject> lstSub = Subject.getListSubject(((Course) JListCourse.getSelectedValue()).getId());
                DefaultTableModel tblCourseInfoModel = new DefaultTableModel();
                tblCourseInfoModel.addColumn("Subject ID");
                tblCourseInfoModel.addColumn("Subject Name");
                for (Subject lstSub1 : lstSub) {
                    String[] temp = {lstSub1.getSubID() + "", lstSub1.getName()};
                    tblCourseInfoModel.addRow(temp);
                }
                tblCourseInformation.setModel(tblCourseInfoModel);
                tblCourseInformation.getColumnModel().getColumn(0).setMaxWidth(70);
                lblFee.setText("Total Fee: " + ((Course) JListCourse.getSelectedValue()).getMoney());
            }
        });

        //table data
//        List<Subject> lstSub = Subject.getListSubject(((Course) JListCourse.getSelectedValue()).getId());
//        DefaultTableModel tblCourseInfoModel = new DefaultTableModel();
//        tblCourseInfoModel.addColumn("Subject ID");
//        tblCourseInfoModel.addColumn("Subject Name");
//        for (Subject lstSub1 : lstSub) {
//            String[] temp = {lstSub1.getSubID() + "", lstSub1.getName()};
//            tblCourseInfoModel.addRow(temp);
//        }
//        tblCourseInformation.setModel(tblCourseInfoModel);
//
//        lblFee.setText("Total Fee: " + ((Course) JListCourse.getSelectedValue()).getMoney());
    }

    

    private void EditSwitch(boolean bool) {
        txtAdminAddress.setEditable(bool);
        txtAdminDOB.setEditable(bool);
        txtAdminEmail.setEditable(bool);
        txtAdmnPhone.setEditable(bool);
        txtAdminFullName.setEditable(bool);
        rdAdminFemale.setEnabled(bool);
        rdAdminMale.setEnabled(bool);
    }

    private void LoadFeedbackPage() {

        sldFontSize.setValue(11);

        feedbackModel = new DefaultListModel<>();
        Connection conn = null;
        try {
            conn = DBConnect.connectDatabase();
            PreparedStatement pre = conn.prepareStatement("SELECT FeedBack.[Content], FeedBack.StuId, Student.FullName FROM Student INNER JOIN FeedBack ON Student.StuId = FeedBack.StuId");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                feedbackModel.addElement(new Student(rs.getInt(2), rs.getNString(3), rs.getNString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        listStudentFeedback.setModel(feedbackModel);
        listStudentFeedback.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listStudentFeedback.getSelectedIndex() >= 0) {
                    txtFeedback.setText(feedbackModel.getElementAt(listStudentFeedback.getSelectedIndex()).getFeedback());
                }
            }
        });
    }

    //Student Manager Method
    private void LoadDataStudent() {
        if (StudentTblModel.getRowCount() > 0) {
            for (int i = StudentTblModel.getRowCount() - 1; i >= 0; i--) {
                StudentTblModel.removeRow(i);
            }
        }

        for (Student lstStudent1 : lstStudent) {
            String[] temp = {lstStudent1.getId() + "", lstStudent1.getFullname(), lstStudent1.getGender(), lstStudent1.getDOB(), getCourseById(lstStudent1.getCourseID()), getFeeTypeByID(lstStudent1.getFeeID())};
            StudentTblModel.addRow(temp);
        }

        tblStudentData.setModel(StudentTblModel);
        tblStudentData.getColumnModel().getColumn(0).setMaxWidth(50);
        tblStudentData.getColumnModel().getColumn(1).setMinWidth(180);

    }

    private void LoadOtherControlStudentManager() {
        try {
            //setup Format Text Field Text
            MaskFormatter mf = new MaskFormatter("####-##-##");
            mf.install(txtFormatDate);
            MaskFormatter mk = new MaskFormatter("####-##-##");
            mk.install(txtStudentDOB);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Wrong date!!", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        DefaultComboBoxModel<Course> ModelCbCourseStu = new DefaultComboBoxModel<>();
        ModelCbCourseStu.addElement(new Course(0, "Not set", 0));
        for (Course lstCourse1 : lstCourse) {
            ModelCbCourseStu.addElement(lstCourse1);

        }

        jComboBox2.setModel(ModelCbCourseStu);

        SearchName.getDocument().addDocumentListener(docSearchListener);
        txtFormatDate.getDocument().addDocumentListener(docSearchListener);
    }

    DocumentListener docSearchListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            ExecuteFilter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            ExecuteFilter();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            ExecuteFilter();
        }
    };

    ListSelectionListener tblStudentChangeListner = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (tblStudentData.getSelectedRow() >= 0) {
                tblSubject.setModel(Mark.getTableMark(Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString())));
                for (Course lstCourse1 : lstCourse) {
                    if (lstCourse1.getName().equals(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 4))) {
                        lblTotalFee.setText("Total Fees: " + lstCourse1.getMoney());
                    }
                }
                tblPayment.setModel(Payment.getPaymentTable(Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString())));
            }

            tblSubject.getColumnModel().getColumn(0).setMinWidth(190);
        }
    };

    private String getCourseById(int id) {
        for (Course lstCourse1 : lstCourse) {
            if (lstCourse1.getId() == id) {
                return lstCourse1.getName();
            }
        }
        return null;
    }

    private String getFeeTypeByID(int ID) {
        if (ID == 1) {
            return "All";
        } else if (ID == 2) {
            return "Monthly";
        }
        return null;
    }

    private void ExecuteFilter() {
        List<Student> lstTemp = new ArrayList<>();
        lstTemp = lstStudent;
        lstTemp = Filter.CourseFilter(lstTemp, ((Course) jComboBox2.getSelectedItem()).getId());
//        for (Student lstStu1 : lstTemp) {
//            System.out.println(lstStu1.toString());
//        }
        lstTemp = Filter.NameFilter(lstTemp, SearchName.getText());
        //System.out.println(SearchName.getText());
//        for (Student lstStu1 : lstTemp) {
//            System.out.println(lstStu1.toString());
//        }
        lstTemp = Filter.DOBFilter(lstTemp, txtFormatDate.getText());
//        for (Student lstStu1 : lstTemp) {
//            System.out.println(lstStu1.toString());
//        }

        if (cbFeeType.getSelectedItem().equals("All")) {
            lstTemp = Filter.FeeFilter(lstTemp, 1);
        } else if (((String) cbFeeType.getSelectedItem()).equals("Monthly")) {
            lstTemp = Filter.FeeFilter(lstTemp, 2);
        }

        lstTemp = Filter.GenderFilter(lstTemp, (String) cbGender.getSelectedItem());
        for (int i = StudentTblModel.getRowCount() - 1; i >= 0; i--) {
            StudentTblModel.removeRow(i);
        }
        for (Student lstStu1 : lstTemp) {
            String[] row = {lstStu1.getId() + "", lstStu1.getFullname(), lstStu1.getGender(), lstStu1.getDOB(), getCourseById(lstStu1.getCourseID()), getFeeTypeByID(lstStu1.getFeeID())};
            StudentTblModel.addRow(row);
        }
        tblStudentData.setModel(StudentTblModel);
        tblStudentData.getColumnModel().getColumn(0).setMaxWidth(50);
        tblStudentData.getColumnModel().getColumn(1).setMinWidth(180);
    }

    private void SearchID() throws NumberFormatException {
        try {
            if (!SearchID.getText().trim().equals("")) {
                for (int i = StudentTblModel.getRowCount() - 1; i >= 0; i--) {
                    StudentTblModel.removeRow(i);
                }
                for (Student lstStu1 : lstStudent) {
                    if (lstStu1.getId() == Integer.parseInt(SearchID.getText())) {
                        String[] row = {lstStu1.getId() + "", lstStu1.getFullname(), lstStu1.getGender(), lstStu1.getDOB(), getCourseById(lstStu1.getCourseID()), getFeeTypeByID(lstStu1.getFeeID())};
                        StudentTblModel.addRow(row);
                    }
                }
                tblStudentData.setModel(StudentTblModel);
                tblStudentData.getColumnModel().getColumn(0).setMaxWidth(50);
                tblStudentData.getColumnModel().getColumn(1).setMinWidth(180);

            } else {
                LoadDataStudent();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "The ID you Entered is Invalid!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grGender = new javax.swing.ButtonGroup();
        grPayment = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        mnEditStudent = new javax.swing.JMenuItem();
        mnMark = new javax.swing.JMenuItem();
        mnDelete = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnUserPass = new javax.swing.JMenuItem();
        mnRefresh = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        btnAddStudent = new javax.swing.JButton();
        txtStudentName = new javax.swing.JTextField();
        rdMale = new javax.swing.JRadioButton();
        rdFemale = new javax.swing.JRadioButton();
        rdAll = new javax.swing.JRadioButton();
        rdInstallment = new javax.swing.JRadioButton();
        cbCourseName = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSubjects = new javax.swing.JList();
        jButton6 = new javax.swing.JButton();
        txtStudentDOB = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblStudentData = new javax.swing.JTable();
        SearchName = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSubject = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPayment = new javax.swing.JTable();
        lblTotalFee = new javax.swing.JLabel();
        cbFeeType = new javax.swing.JComboBox();
        cbGender = new javax.swing.JComboBox();
        txtFormatDate = new javax.swing.JFormattedTextField();
        jButton3 = new javax.swing.JButton();
        SearchID = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCourseInformation = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        JListCourse = new javax.swing.JList();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblFee = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        listStudentFeedback = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        sldFontSize = new javax.swing.JSlider();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtFeedback = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        rdAdminMale = new javax.swing.JRadioButton();
        rdAdminFemale = new javax.swing.JRadioButton();
        txtAdminDOB = new javax.swing.JTextField();
        txtAdminFullName = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtAdmnPhone = new javax.swing.JTextField();
        txtAdminEmail = new javax.swing.JTextField();
        txtAdminAddress = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jPopupMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        mnEditStudent.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mnEditStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/EditPop25.png"))); // NOI18N
        mnEditStudent.setText("Edit Student Data");
        mnEditStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEditStudentActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnEditStudent);

        mnMark.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mnMark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/EditPop25.png"))); // NOI18N
        mnMark.setText("Edit Mark");
        mnMark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMarkActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnMark);

        mnDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Delete25.png"))); // NOI18N
        mnDelete.setText("Delete ");
        mnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnDelete);
        jPopupMenu1.add(jSeparator1);

        mnUserPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ChangePass25.png"))); // NOI18N
        mnUserPass.setText("Check Username And Password");
        mnUserPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnUserPassActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnUserPass);

        mnRefresh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/refresh25.png"))); // NOI18N
        mnRefresh.setText("Refresh");
        mnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRefreshActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnRefresh);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("SimSun", 1, 18)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ABCLogon5050.png"))); // NOI18N
        jLabel1.setText("Admin Page");

        lblUserName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Admin260.png"))); // NOI18N
        lblUserName.setText("(Name of user)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel3.setText("Fullname:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel4.setText("Date of Birth:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel5.setText("Gender:");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel6.setText("Subject(s):");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel7.setText("Course Name:");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel8.setText("Type of Fees:");

        btnClear.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ClearReset40.png"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnAddStudent.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnAddStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/AddStu40.png"))); // NOI18N
        btnAddStudent.setText("Add");
        btnAddStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStudentActionPerformed(evt);
            }
        });

        txtStudentName.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        grGender.add(rdMale);
        rdMale.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        rdMale.setText("Male");

        grGender.add(rdFemale);
        rdFemale.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        rdFemale.setText("Female");

        grPayment.add(rdAll);
        rdAll.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        rdAll.setText("All");

        grPayment.add(rdInstallment);
        rdInstallment.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        rdInstallment.setText("Installment");

        cbCourseName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        cbCourseName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbCourseName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCourseNameActionPerformed(evt);
            }
        });

        jScrollPane1.setAutoscrolls(true);

        lstSubjects.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        lstSubjects.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstSubjects.setMaximumSize(new java.awt.Dimension(39, 106));
        lstSubjects.setSelectionBackground(new java.awt.Color(0, 153, 153));
        jScrollPane1.setViewportView(lstSubjects);

        jButton6.setText("Enter Mark");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        txtStudentDOB.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(44, 44, 44)
                        .addComponent(rdAll)
                        .addGap(104, 104, 104)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdInstallment)
                            .addComponent(rdFemale)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdMale)
                            .addComponent(txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStudentDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addComponent(cbCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(btnClear)
                .addGap(234, 234, 234)
                .addComponent(btnAddStudent)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(174, 174, 174))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cbCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStudentDOB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(rdMale)
                                    .addComponent(rdFemale))
                                .addGap(83, 83, 83))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rdAll)
                                .addComponent(jLabel8)
                                .addComponent(rdInstallment)))))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addContainerGap(117, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClear)
                            .addComponent(btnAddStudent))
                        .addGap(59, 59, 59))))
        );

        jTabbedPane1.addTab("Add Student", new javax.swing.ImageIcon(getClass().getResource("/res/add40.png")), jPanel3); // NOI18N

        jPanel4.setComponentPopupMenu(jPopupMenu1);

        tblStudentData.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblStudentData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fullname", "Gender", "Date Of Birth", "Course Name", "Fee Type"
            }
        ));
        tblStudentData.setComponentPopupMenu(jPopupMenu1);
        tblStudentData.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblStudentData.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblStudentData);
        if (tblStudentData.getColumnModel().getColumnCount() > 0) {
            tblStudentData.getColumnModel().getColumn(0).setMaxWidth(50);
            tblStudentData.getColumnModel().getColumn(1).setMinWidth(180);
        }

        SearchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchNameActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        tblSubject.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject", "Mark", "Grade"
            }
        ));
        jScrollPane2.setViewportView(tblSubject);
        if (tblSubject.getColumnModel().getColumnCount() > 0) {
            tblSubject.getColumnModel().getColumn(0).setMinWidth(180);
        }

        tblPayment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblPayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Paid", "Day", "Description"
            }
        ));
        jScrollPane4.setViewportView(tblPayment);

        lblTotalFee.setText("Total:");

        cbFeeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not set", "All", "Monthly" }));
        cbFeeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFeeTypeActionPerformed(evt);
            }
        });

        cbGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not set", "Male", "Female" }));
        cbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGenderActionPerformed(evt);
            }
        });

        txtFormatDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFormatDateActionPerformed(evt);
            }
        });

        jButton3.setText("Add New Payment");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        SearchID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(SearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SearchName, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(txtFormatDate, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbFeeType, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(jButton3))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFormatDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFeeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalFee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(1, 1, 1))))
        );

        jTabbedPane1.addTab("Student Manager", new javax.swing.ImageIcon(getClass().getResource("/res/Manager40.png")), jPanel4); // NOI18N

        tblCourseInformation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject ID", "Subject Name"
            }
        ));
        jScrollPane5.setViewportView(tblCourseInformation);
        if (tblCourseInformation.getColumnModel().getColumnCount() > 0) {
            tblCourseInformation.getColumnModel().getColumn(0).setMaxWidth(70);
        }

        JListCourse.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(JListCourse);

        jLabel15.setFont(new java.awt.Font("SimSun", 1, 18)); // NOI18N
        jLabel15.setText("Course Name");

        jLabel16.setFont(new java.awt.Font("SimSun", 1, 18)); // NOI18N
        jLabel16.setText("Course Information");

        lblFee.setText("Total Fee:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(lblFee, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addGap(31, 31, 31)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFee, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Subject Information", new javax.swing.ImageIcon(getClass().getResource("/res/Subject40.png")), jPanel6, ""); // NOI18N

        listStudentFeedback.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(listStudentFeedback);

        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Print45.png"))); // NOI18N
        jButton1.setToolTipText("Print This Feedback.");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ColorChooser45.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        sldFontSize.setToolTipText("Slide to change font size.");
        sldFontSize.setMaximumSize(new java.awt.Dimension(200, 26));
        sldFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldFontSizeStateChanged(evt);
            }
        });
        jToolBar1.add(sldFontSize);

        txtFeedback.setColumns(20);
        txtFeedback.setLineWrap(true);
        txtFeedback.setRows(5);
        jScrollPane8.setViewportView(txtFeedback);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addGap(26, 26, 26))
        );

        jTabbedPane1.addTab("Feedback", new javax.swing.ImageIcon(getClass().getResource("/res/Feedback12845.png")), jPanel7); // NOI18N

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel9.setText("Fullname:");

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setText("Date of Birth:");

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setText("Gender:");

        buttonGroup3.add(rdAdminMale);
        rdAdminMale.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        rdAdminMale.setText("Male");
        rdAdminMale.setEnabled(false);

        buttonGroup3.add(rdAdminFemale);
        rdAdminFemale.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        rdAdminFemale.setText("Female");
        rdAdminFemale.setEnabled(false);

        txtAdminDOB.setEditable(false);
        txtAdminDOB.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        txtAdminFullName.setEditable(false);
        txtAdminFullName.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setText("Phone:");

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel13.setText("Email:");

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setText("Address:");

        txtAdmnPhone.setEditable(false);
        txtAdmnPhone.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        txtAdminEmail.setEditable(false);
        txtAdminEmail.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        txtAdminAddress.setEditable(false);
        txtAdminAddress.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/edit40.png"))); // NOI18N
        btnEdit.setText("Edit Information");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ChangePass40.png"))); // NOI18N
        jButton4.setText("Change Password");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/LogOut45.png"))); // NOI18N
        jButton5.setText("Log Out");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAdminDOB, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(txtAdminFullName)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(rdAdminMale)
                                .addGap(50, 50, 50)
                                .addComponent(rdAdminFemale)))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12))
                                .addGap(85, 85, 85)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtAdmnPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(txtAdminEmail)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(72, 72, 72)
                                .addComponent(txtAdminAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)))
                        .addContainerGap(18, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnEdit)
                        .addGap(104, 104, 104)
                        .addComponent(jButton4)
                        .addGap(125, 125, 125)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtAdminFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtAdmnPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(rdAdminMale)
                            .addComponent(rdAdminFemale)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtAdminDOB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(txtAdminEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtAdminAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(73, 73, 73))
        );

        jTabbedPane1.addTab("My Information", new javax.swing.ImageIcon(getClass().getResource("/res/Admin40.png")), jPanel5); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbCourseNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCourseNameActionPerformed
        Connection conn = null;
        try {
            DefaultListModel<Subject> ModelSJ = new DefaultListModel<>();

            conn = DBConnect.connectDatabase();
            PreparedStatement prestmt = conn.prepareStatement("select * from Subject where courseid=?");
            prestmt.setInt(1, ((Course) cbCourseName.getSelectedItem()).getId());
            ResultSet rs = prestmt.executeQuery();

            while (rs.next()) {
                Subject temp = new Subject(rs.getInt(1), rs.getInt(2), rs.getString(3));
                ModelSJ.addElement(temp);
            }
            lstSubjects.setModel(ModelSJ);
        } catch (SQLException ex) {
            Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }//GEN-LAST:event_cbCourseNameActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (emp == null) {
            emp = new EnterMarkPageFrame(((Course) cbCourseName.getSelectedItem()).getId());
            emp.setLocationRelativeTo(this);
            emp.setVisible(true);
        } else {
            emp.setVisible(true);
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnAddStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStudentActionPerformed
        // TODO add your handling code here:
        if (validateForm()) {
            Student s = new Student();
            s.setGender(checkGender());
            s.setFullname(txtStudentName.getText());
            s.setFeeID(CheckFeeID());

            s.setDOB(txtStudentDOB.getText());
            s.setCourseID(((Course) cbCourseName.getSelectedItem()).getId());
            s.setUsername("C" + year + month + "L" + (lstStudent.get(lstStudent.size() - 1).getId() + 1000));
            s.setPass(Integer.toString(new Random().nextInt(80000) + 10000));
            Connection connection = null;
            try {
                connection = DBConnect.connectDatabase();
                CallableStatement pre = connection.prepareCall("{call AddStudent(?,?,?,?,?,?,?)}");
                pre.setString(1, s.getUsername());
                pre.setString(2, s.getPass());
                pre.setString(3, s.getFullname());
                //Format date to make it able to add to database
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateUtil = dateFormatter.parse(s.getDOB());
                Date date = new Date(dateUtil.getTime());
                pre.setDate(4, date);
                pre.setString(5, s.getGender());
                pre.setInt(6, s.getCourseID());
                pre.setInt(7, s.getFeeID());
                boolean rs = pre.execute();
                if (emp != null) {
                    if (rs == false) {
                        CallableStatement call = DBConnect.connectDatabase().prepareCall("{CALL MaxIdStudnent()}");
                        ResultSet RecentStudent = call.executeQuery();
                        RecentStudent.next();
                        int StuId = RecentStudent.getInt(1);
                        List<DataTableMark> lstMark = emp.getMarkInformation();
                        PreparedStatement MarkInsert = DBConnect.connectDatabase().prepareStatement("UPDATE Mark SET Mark=? WHERE SubId=? AND StuId=?");
                        for (DataTableMark lstMark1 : lstMark) {
                            if (!lstMark1.getMark().equals("")) {
                                MarkInsert.setFloat(1, Float.parseFloat(lstMark1.getMark()));
                                MarkInsert.setInt(2, lstMark1.getSubjectID());
                                MarkInsert.setInt(3, StuId);
                                MarkInsert.addBatch();
                            }
                        }
                        int[] rs1 = MarkInsert.executeBatch();
                        MarkInsert.close();
                        JOptionPane.showMessageDialog(null, "Student and Mark add Successfully!", "Message", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/res/ok40.png")));
                    }
                } else if (rs == false) {
                    JOptionPane.showMessageDialog(null, "Student add Successfully!", "Message", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/res/ok40.png")));
                }

            } catch (SQLException ex) {
                Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "The date you entered is not valid", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The marks you entered contain invalid character!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
            } finally {
                if (connection != null) {
                    try {
                        lstStudent = Student.getAllStudent();
                        LoadDataStudent();
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }//GEN-LAST:event_btnAddStudentActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        txtStudentDOB.setText(null);
        txtStudentName.setText(null);
        grGender.clearSelection();
        grPayment.clearSelection();
        emp = null;
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        //Edit Button Event
        if (btnEdit.getText().equals("Edit Information")) {
            btnEdit.setText("OK");
            btnEdit.setMinimumSize(new Dimension(153, 49));
            btnEdit.setIcon(new ImageIcon(getClass().getResource("/res/ok40.png")));
            EditSwitch(true);
        } else if (btnEdit.getText().equals("OK")) {
            btnEdit.setText("Edit Information");
            btnEdit.setIcon(new ImageIcon(getClass().getResource("/res/edit40.png")));
            Connection conn = null;

            try {
                conn = DBConnect.connectDatabase();
                PreparedStatement pre = conn.prepareStatement("update admin set Fullname=?,DOB=?,Gender=?,Phone=?,Email=?,Address=? where Aid=?");
                pre.setNString(1, txtAdminFullName.getText());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateUtil = dateFormat.parse(txtAdminDOB.getText());
                Date date = new Date(dateUtil.getTime());
                pre.setDate(2, date);
                if (rdAdminFemale.isSelected()) {
                    pre.setString(3, "Female");
                } else if (rdAdminMale.isSelected()) {
                    pre.setString(3, "Male");
                }
                pre.setString(4, txtAdmnPhone.getText());
                pre.setString(5, txtAdminEmail.getText());
                pre.setString(6, txtAdminEmail.getText());
                pre.setInt(7, adminID);
                boolean rs = pre.execute();
                if (rs == false) {
                    JOptionPane.showMessageDialog(this, "Edit Successfully!!", "Information", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/res/ok50.png")));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Gender is not set!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "The date you entered is not valid!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete40.png")));
            } finally {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            EditSwitch(false);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new ChangePassForm(adminID);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int rs = JOptionPane.showConfirmDialog(this, "Are you sure want to Logout?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getResource("/res/Help60.png")));
        if (rs == 0) {
            this.dispose();
            MainPage main = new MainPage();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        ExecuteFilter();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void txtFormatDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFormatDateActionPerformed
        // TODO add your handling code here:
//        System.out.println(txtFormatDate.getText());
    }//GEN-LAST:event_txtFormatDateActionPerformed

    private void SearchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchNameActionPerformed
        // TODO add your handling code here:
        ExecuteFilter();
    }//GEN-LAST:event_SearchNameActionPerformed

    private void cbGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGenderActionPerformed
        // TODO add your handling code here:
        ExecuteFilter();
    }//GEN-LAST:event_cbGenderActionPerformed

    private void mnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRefreshActionPerformed
        // TODO add your handling code here:
        lstStudent = Student.getAllStudent();
        LoadDataStudent();
    }//GEN-LAST:event_mnRefreshActionPerformed

    private void cbFeeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFeeTypeActionPerformed
        // TODO add your handling code here:
        ExecuteFilter();
    }//GEN-LAST:event_cbFeeTypeActionPerformed

    private void mnEditStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEditStudentActionPerformed
        // TODO add your handling code here:
        if (tblStudentData.getSelectedRow() >= 0) {
            jTabbedPane1.addTab("Edit Student", new ImageIcon(getClass().getResource("/res/edit40.png")), new EditTab(jTabbedPane1, Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString())));
        }


    }//GEN-LAST:event_mnEditStudentActionPerformed

    private void mnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteActionPerformed
        // TODO add your handling code here:
        if (tblStudentData.getSelectedRow() >= 0) {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure want to delete this Student? \n This can not be undone!", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/res/warn45.png")));
            if (confirm == 0) {
                PreparedStatement pre = null;
                try {
                    pre = DBConnect.connectDatabase().prepareStatement("Delete from Student where StuId=?");
                    pre.setInt(1, Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString()));
                    boolean rs = pre.execute();
                    lstStudent = Student.getAllStudent();
                    LoadDataStudent();

                    if (rs == false) {
                        JOptionPane.showMessageDialog(null, "Delete Successfully", "Message", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/res/ok40.png")));
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (pre.getConnection() != null) {
                            pre.getConnection().close();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_mnDeleteActionPerformed

    private void mnMarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMarkActionPerformed
        // TODO add your handling code here:
        if (tblStudentData.getSelectedRow() >= 0) {
            jTabbedPane1.addTab("Edit Mark", new ImageIcon(getClass().getResource("/res/edit40.png")), new MarkEdit(Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString()), jTabbedPane1));
        }
    }//GEN-LAST:event_mnMarkActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (tblStudentData.getSelectedRow() >= 0) {
            new EnterPaymentPage(Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString()));
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void SearchIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchIDActionPerformed
        // TODO add your handling code here:
        SearchID();
    }//GEN-LAST:event_SearchIDActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!txtFeedback.equals("")) {
            try {
                txtFeedback.print();
            } catch (PrinterException ex) {
                Logger.getLogger(AdminPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No content to print", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/res/Delete45.png")));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        txtFeedback.setForeground(JColorChooser.showDialog(null, "Choose text color", Color.BLACK));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void sldFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldFontSizeStateChanged
        // TODO add your handling code here:
        txtFeedback.setFont(txtFeedback.getFont().deriveFont(Font.BOLD, sldFontSize.getValue()));
    }//GEN-LAST:event_sldFontSizeStateChanged

    private void mnUserPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnUserPassActionPerformed
        // TODO add your handling code here:
        if (tblStudentData.getSelectedRow() >= 0) {
            int id = Integer.parseInt(tblStudentData.getValueAt(tblStudentData.getSelectedRow(), 0).toString());
            for (Student Student1 : lstStudent) {
                if (Student1.getId() == id) {
                    JOptionPane.showMessageDialog(null, "Username: " + Student1.getUsername() + " - Password: " + Student1.getPass(), "Username And Password", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/res/ChangePass40.png")));
                    break;
                }
            }
        }
    }//GEN-LAST:event_mnUserPassActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPage(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList JListCourse;
    private javax.swing.JFormattedTextField SearchID;
    private javax.swing.JTextField SearchName;
    private javax.swing.JButton btnAddStudent;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox cbCourseName;
    private javax.swing.JComboBox cbFeeType;
    private javax.swing.JComboBox cbGender;
    private javax.swing.ButtonGroup grGender;
    private javax.swing.ButtonGroup grPayment;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblFee;
    private javax.swing.JLabel lblTotalFee;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JList listStudentFeedback;
    private javax.swing.JList lstSubjects;
    private javax.swing.JMenuItem mnDelete;
    private javax.swing.JMenuItem mnEditStudent;
    private javax.swing.JMenuItem mnMark;
    private javax.swing.JMenuItem mnRefresh;
    private javax.swing.JMenuItem mnUserPass;
    private javax.swing.JRadioButton rdAdminFemale;
    private javax.swing.JRadioButton rdAdminMale;
    private javax.swing.JRadioButton rdAll;
    private javax.swing.JRadioButton rdFemale;
    private javax.swing.JRadioButton rdInstallment;
    private javax.swing.JRadioButton rdMale;
    private javax.swing.JSlider sldFontSize;
    private javax.swing.JTable tblCourseInformation;
    private javax.swing.JTable tblPayment;
    private javax.swing.JTable tblStudentData;
    private javax.swing.JTable tblSubject;
    private javax.swing.JTextField txtAdminAddress;
    private javax.swing.JTextField txtAdminDOB;
    private javax.swing.JTextField txtAdminEmail;
    private javax.swing.JTextField txtAdminFullName;
    private javax.swing.JTextField txtAdmnPhone;
    private javax.swing.JTextArea txtFeedback;
    private javax.swing.JFormattedTextField txtFormatDate;
    private javax.swing.JFormattedTextField txtStudentDOB;
    private javax.swing.JTextField txtStudentName;
    // End of variables declaration//GEN-END:variables
}
