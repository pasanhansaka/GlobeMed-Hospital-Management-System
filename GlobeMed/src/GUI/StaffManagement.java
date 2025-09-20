package GUI;

import CONTROLLER.StaffController;
import MODEL.MySQL;
import MODEL.User;
import PATTERN.DecoratorPattern;
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class StaffManagement extends javax.swing.JFrame {

    private String currentUserRole;
    private StaffController staffController;
    private DefaultTableModel tableModel;

    public StaffManagement(String currentUserRole) {
        this.currentUserRole = currentUserRole;
        this.staffController = new StaffController();
        initComponents();
        setupTable();
        loadStaffData();
        defaultButtons();
        populateComboBoxes();

        // Only admin can manage staff
        if (!"Administrator".equalsIgnoreCase(currentUserRole)) {
            JOptionPane.showMessageDialog(this,
                    "Access denied. Only administrators can manage staff.",
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void setupTable() {
        String[] columnNames = {"User ID", "Username", "First Name", "Last Name",
            "Email", "Mobile Number", "Role", "Speciality", "Department"};
        tableModel = new DefaultTableModel(columnNames, 0);
        jTable1.setModel(tableModel);
        jTable1.setAutoCreateRowSorter(true);
    }

    private void loadStaffData() {
        try {
            List<User> staff = staffController.getAllStaff();
            tableModel.setRowCount(0);
            for (User user : staff) {
                if (user != null) {
                    tableModel.addRow(new Object[]{
                        user.getUserId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getMobileNumber(),
                        user.getRoleName(),
                        user.getSpecialization(),
                        user.getDepartment()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void defaultButtons() {
        jButton2.setEnabled(true);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        clearForm();
    }

    private void UDVButtons() {
        jButton2.setEnabled(false);
        jButton3.setEnabled(true);
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
    }

    private void clearForm() {
        jTextField10.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField11.setText("");
        jPasswordField1.setText("");
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
    }

    private void populateComboBoxes() {
        try {
            jComboBox3.removeAllItems();
            jComboBox3.addItem("Select");
            ResultSet roles = MySQL.search("SELECT * FROM role");
            while (roles.next()) {
                jComboBox3.addItem(roles.getString("role_name"));
            }

            jComboBox2.removeAllItems();
            jComboBox2.addItem("Select");
            ResultSet cities = MySQL.search("SELECT * FROM city");
            while (cities.next()) {
                jComboBox2.addItem(cities.getString("city_name"));
            }

            jComboBox4.removeAllItems();
            jComboBox4.addItem("Select");
            jComboBox4.addItem("Cardiology");
            jComboBox4.addItem("Neurology");
            jComboBox4.addItem("Orthopedics");
            jComboBox4.addItem("Pediatrics");
            jComboBox4.addItem("General Medicine");
            jComboBox4.addItem("Dermatology");
            jComboBox4.addItem("Oncology");
            jComboBox4.addItem("Psychiatry");
            jComboBox4.addItem("ENT (Ear, Nose, Throat)");
            jComboBox4.addItem("Ophthalmology");
            jComboBox4.addItem("Gynecology & Obstetrics");
            jComboBox4.addItem("Urology");
            jComboBox4.addItem("Radiology");
            jComboBox4.addItem("Emergency Medicine");
            jComboBox4.addItem("Anesthesiology");
            jComboBox4.addItem("Dental");
            jComboBox4.addItem("Physiotherapy");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error populating combo boxes: " + e.getMessage());
        }
    }

    private void populateForm(User user) {
        if (user != null) {
            jTextField10.setText(user.getUsername() != null ? user.getUsername() : "");
            jTextField2.setText(user.getFirstName() != null ? user.getFirstName() : "");
            jTextField3.setText(user.getLastName() != null ? user.getLastName() : "");
            jTextField6.setText(user.getEmail() != null ? user.getEmail() : "");
            jTextField5.setText(user.getMobileNumber() != null ? user.getMobileNumber() : "");

            if (user.getRoleName() != null) {
                for (int i = 0; i < jComboBox3.getItemCount(); i++) {
                    if (jComboBox3.getItemAt(i).equals(user.getRoleName())) {
                        jComboBox3.setSelectedIndex(i);
                        break;
                    }
                }
            }

            jTextField7.setText(user.getSpecialization() != null ? user.getSpecialization() : "");

            if (user.getDepartment() != null) {
                for (int i = 0; i < jComboBox4.getItemCount(); i++) {
                    if (jComboBox4.getItemAt(i).equals(user.getDepartment())) {
                        jComboBox4.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private int getRoleId(String roleName) {
        switch (roleName.toLowerCase()) {
            case "doctor":
                return 1;
            case "nurse":
                return 2;
            case "pharmacist":
                return 3;
            case "billing specialist":
                return 4;
            case "administrator":
                return 5;
            default:
                return 0;
        }
    }

    private boolean validateRequiredFields(boolean validatePasswordRequired) {
        // Validate username
        if (jTextField10.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.");
            jTextField10.requestFocus();
            return false;
        }

        // Validate password
        if (validatePasswordRequired && jPasswordField1.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Password is required.");
            jPasswordField1.requestFocus();
            return false;
        }

        // Validate first name
        if (jTextField2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required.");
            jTextField2.requestFocus();
            return false;
        }

        // Validate last name
        if (jTextField3.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required.");
            jTextField3.requestFocus();
            return false;
        }

        // Validate email
        if (jTextField6.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.");
            jTextField6.requestFocus();
            return false;
        }

        // Validate mobile number
        if (jTextField5.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mobile number is required.");
            jTextField5.requestFocus();
            return false;
        }

        // Validate role selection
        if (jComboBox3.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a role.");
            jComboBox3.requestFocus();
            return false;
        }

        // Validate department selection
        if (jComboBox4.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a department.");
            jComboBox4.requestFocus();
            return false;
        }

        return true;
    }

    public void addUser() {
        try {
            if (!validateRequiredFields(true)) {
                return;
            }

            User user = new User();
            user.setUsername(jTextField10.getText());
            user.setPassword(new String(jPasswordField1.getPassword()));
            user.setFirstName(jTextField2.getText());
            user.setLastName(jTextField3.getText());
            user.setEmail(jTextField6.getText());
            user.setMobileNumber(jTextField5.getText());
            user.setRoleRoleId(getRoleId(jComboBox3.getSelectedItem().toString()));
            user.setSpecialization(jTextField7.getText());
            user.setDepartment(jComboBox4.getSelectedItem().toString());
            user.setIsActive(1);
            user.setCreated_at(java.time.LocalDate.now().toString());

            if (staffController.addStaff(user)) {
                JOptionPane.showMessageDialog(this, "Staff member added successfully!");
                loadStaffData();
                defaultButtons();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add staff member.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding staff: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser() {
        try {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a staff member to update.");
                return;
            }

            if (!validateRequiredFields(false)) {
                return;
            }

            int userId = (int) jTable1.getValueAt(row, 0);
            User user = new User();
            user.setUserId(userId);
            user.setUsername(jTextField10.getText());

            // Only update password if it's not empty
            String password = new String(jPasswordField1.getPassword());
            if (!password.isEmpty()) {
                user.setPassword(password);
            }

            user.setFirstName(jTextField2.getText());
            user.setLastName(jTextField3.getText());
            user.setEmail(jTextField6.getText());
            user.setMobileNumber(jTextField5.getText());
            user.setRoleRoleId(getRoleId(jComboBox3.getSelectedItem().toString()));
            user.setSpecialization(jTextField7.getText());
            user.setDepartment(jComboBox4.getSelectedItem().toString());
            user.setIsActive(1);
            user.setCreated_at(java.time.LocalDate.now().toString());

            if (staffController.updateStaff(user)) {
                JOptionPane.showMessageDialog(this, "Staff member updated successfully!");
                loadStaffData();
                defaultButtons();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update staff member.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating staff: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a staff member to delete.");
                return;
            }

            int userId = (int) jTable1.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete staff member ID " + userId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (staffController.deleteStaff(userId)) {
                    JOptionPane.showMessageDialog(this, "Staff member deleted successfully!");
                    loadStaffData();
                    defaultButtons();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete staff member.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting staff: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void viewPermissions() {
        try {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a staff member to view permissions.");
                return;
            }

            String roleName = jTable1.getValueAt(row, 6).toString();

            // use decorator pattern
            DecoratorPattern.BasicUser basicUser = new DecoratorPattern.BasicUser(
                    jTable1.getValueAt(row, 1).toString(),
                    jTable1.getValueAt(row, 2).toString(),
                    jTable1.getValueAt(row, 3).toString(),
                    jTable1.getValueAt(row, 4).toString()
            );

            DecoratorPattern.UserComponent decoratedUser = basicUser;

            switch (roleName.toLowerCase()) {
                case "doctor":
                    decoratedUser = new DecoratorPattern.DoctorDecorator(decoratedUser);
                    break;
                case "nurse":
                    decoratedUser = new DecoratorPattern.NurseDecorator(decoratedUser);
                    break;
                case "pharmacist":
                    decoratedUser = new DecoratorPattern.PharmacistDecorator(decoratedUser);
                    break;
                case "billing specialist":
                    decoratedUser = new DecoratorPattern.BillingSpecialistDecorator(decoratedUser);
                    break;
                case "administrator":
                    decoratedUser = new DecoratorPattern.AdminDecorator(decoratedUser);
                    break;
            }

            StringBuilder permissions = new StringBuilder();
            permissions.append("User: ").append(decoratedUser.getFirstName()).append(" ").append(decoratedUser.getLastName()).append("\n");
            permissions.append("Role: ").append(roleName).append("\n");
            permissions.append("Permissions: ").append(decoratedUser.getPermissions()).append("\n\n");
            permissions.append("Detailed Permissions:\n");
            permissions.append("View Patient Records: ").append(decoratedUser.canViewPatientRecords()).append("\n");
            permissions.append("Schedule Appointments: ").append(decoratedUser.canScheduleAppointments()).append("\n");
            permissions.append("Access Billing: ").append(decoratedUser.canAccessBilling()).append("\n");
            permissions.append("Prescribe Medication: ").append(decoratedUser.canPrescribeMedication()).append("\n");
            permissions.append("Generate Reports: ").append(decoratedUser.canGenerateReports()).append("\n");

            JOptionPane.showMessageDialog(this, permissions.toString(),
                    "Permissions for " + decoratedUser.getFirstName() + " " + decoratedUser.getLastName(),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error viewing permissions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField11 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("GlobeMed - Staff Management");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("X");
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(125, 125, 125)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID ", "Username", "First Name", "Last Name", "Email", "Mobile Number", "Role", "Speciality", "Department", " Address", "Postal Code", "City"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel3.setText("First Name");

        jLabel4.setText("Last Name");

        jLabel7.setText("Mobile Number");

        jLabel8.setText("Email");

        jLabel9.setText("Speciality");

        jLabel10.setText("Line 2");

        jLabel11.setText("Postal Code");

        jLabel12.setText("City");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton5.setText("View Permissions");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel13.setText("Username");

        jLabel14.setText("Role");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel15.setText("Line 1");

        jLabel16.setText("Department");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel17.setText("Password");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField5)
                    .addComponent(jTextField10)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPasswordField1)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField8)
                    .addComponent(jTextField9)
                    .addComponent(jTextField11)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel9)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel15)
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(jLabel13)
                        .addGap(3, 3, 3)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton3)
                    .addComponent(jButton5))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Home home = new Home(currentUserRole);
        home.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Add User
        addUser();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Update User
        updateUser();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Delete User
        deleteUser();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // View Permissions
        viewPermissions();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int userId = (int) jTable1.getValueAt(selectedRow, 0);
                    User user = staffController.getStaffById(userId);
                    if (user != null) {
                        populateForm(user);
                        UDVButtons();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error loading staff member: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    public static void main(String args[]) {
        FlatDarkLaf.setup();
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new StaffManagement().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
