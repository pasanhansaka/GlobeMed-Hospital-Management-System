package GUI;

import CONTROLLER.BillingController;
import MODEL.Bill;
import MODEL.InsuranceClaim;
import MODEL.MySQL;
import PATTERN.ChainOfResponsibilityPattern;
import PATTERN.ChainOfResponsibilityPattern.ClaimProcessingChain;
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class BillingAndInsurance extends javax.swing.JFrame {

    private String currentUserRole;
    private BillingController billingController;
    private ClaimProcessingChain claimProcessor;
    private DefaultTableModel billsTableModel;
    private DefaultTableModel claimsTableModel;

    public BillingAndInsurance(String currentUserRole) {
        this.currentUserRole = currentUserRole;
        this.billingController = new BillingController();
        this.claimProcessor = new ChainOfResponsibilityPattern.ClaimProcessingChain(billingController);
        initComponents();
        setupTables();
        loadBillingData();
        loadInsuranceData();
        populateComboBoxes();
        clearBillForm();
        clearClaimForm();
    }

    private void setupTables() {
        billsTableModel = (DefaultTableModel) jTable1.getModel();
        claimsTableModel = (DefaultTableModel) jTable2.getModel();
        jTable1.setAutoCreateRowSorter(true);
        jTable2.setAutoCreateRowSorter(true);
    }

    private void populateComboBoxes() {
        try {
            jComboBox1.removeAllItems();
            jComboBox1.addItem("Select");
            ResultSet patients = MySQL.search("SELECT patient_id, first_name, last_name FROM patients");
            while (patients.next()) {
                jComboBox1.addItem(patients.getInt("patient_id") + " - "
                        + patients.getString("first_name") + " "
                        + patients.getString("last_name"));
            }

            jComboBox3.removeAllItems();
            jComboBox3.addItem("Select");
            List<Bill> bills = billingController.getAllBills();
            for (Bill bill : bills) {
                jComboBox3.addItem(bill.getBillId() + " - Patient: " + bill.getPatientId());
            }

            jComboBox4.removeAllItems();
            jComboBox4.addItem("Select");
            jComboBox4.addItem("ABC Insurance");
            jComboBox4.addItem("XYZ Health");
            jComboBox4.addItem("MediCare Plus");
            jComboBox4.addItem("HealthGuard");
            jComboBox4.addItem("Wellness Partners");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error populating combo boxes: " + e.getMessage());
        }
    }

    private void loadBillingData() {
        try {
            List<Bill> bills = billingController.getAllBills();
            billsTableModel.setRowCount(0);
            for (Bill bill : bills) {
                billsTableModel.addRow(new Object[]{
                    bill.getBillId(),
                    bill.getPatientId(),
                    String.format("Rs.%.2f", bill.getTotalAmount()),
                    String.format("Rs.%.2f", bill.getInsuranceCoverage()),
                    String.format("Rs.%.2f", bill.getPatientPayment()),
                    bill.getBillingDate(),
                    bill.getDueDate(),
                    bill.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading bills: " + e.getMessage());
        }
    }

    private void loadInsuranceData() {
        try {
            List<InsuranceClaim> claims = billingController.getAllInsuranceClaims();
            claimsTableModel.setRowCount(0);
            for (InsuranceClaim claim : claims) {
                claimsTableModel.addRow(new Object[]{
                    claim.getClaimId(),
                    claim.getBillId(),
                    getPatientNameFromBill(claim.getBillId()),
                    claim.getInsuranceProvider(),
                    String.format("Rs.%.2f", claim.getClaimAmount()),
                    claim.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading insurance claims: " + e.getMessage());
        }
    }

    private String getPatientNameFromBill(int billId) {
        try {
            Bill bill = billingController.getBillById(billId);
            if (bill != null) {
                ResultSet rs = MySQL.search("SELECT first_name, last_name FROM patients WHERE patient_id = ?", bill.getPatientId());
                if (rs.next()) {
                    return rs.getString("first_name") + " " + rs.getString("last_name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private void clearBillForm() {
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jComboBox1.setSelectedIndex(0);
    }

    private void clearClaimForm() {
        jTextField13.setText("");
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
    }
    
    private boolean validateBillFields() {
        // Validate patient selection
        if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient.");
            jComboBox1.requestFocus();
            return false;
        }
        
        // Validate total amount
        if (jTextField3.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total amount is required.");
            jTextField3.requestFocus();
            return false;
        }
        
        // Validate insurance coverage
        if (jTextField4.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insurance coverage amount is required.");
            jTextField4.requestFocus();
            return false;
        }
        
        return true;
    }

    private boolean validateClaimFields() {
        // Validate bill selection
        if (jComboBox3.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a bill.");
            jComboBox3.requestFocus();
            return false;
        }
        
        // Validate insurance provider selection
        if (jComboBox4.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an insurance provider.");
            jComboBox4.requestFocus();
            return false;
        }
        
        // Validate claim amount
        if (jTextField13.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Claim amount is required.");
            jTextField13.requestFocus();
            return false;
        }
        
        return true;
    }

    public void createBill() {
        try {
            if (!validateBillFields()) {
                return;
            }
            
            String patientSelection = (String) jComboBox1.getSelectedItem();
            int patientId = Integer.parseInt(patientSelection.split(" - ")[0]);

            double totalAmount = Double.parseDouble(jTextField3.getText());
            double insuranceCoverage = Double.parseDouble(jTextField4.getText());
            double patientPayment = totalAmount - insuranceCoverage;

            String billingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
            String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + thirtyDaysInMillis));

            Bill bill = new Bill();
            bill.setPatientId(patientId);
            bill.setTotalAmount(totalAmount);
            bill.setInsuranceCoverage(insuranceCoverage);
            bill.setPatientPayment(patientPayment);
            bill.setStatus("Pending");
            bill.setBillingDate(billingDate);
            bill.setDueDate(dueDate);

            if (billingController.addBill(bill)) {
                JOptionPane.showMessageDialog(this, "Bill created successfully!");
                loadBillingData();
                populateComboBoxes();
                clearBillForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create bill.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for amounts.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating bill: " + e.getMessage());
        }
    }

    public void submitClaim() {
        try {
            if (!validateClaimFields()) {
                return;
            }            
            
            String billSelection = (String) jComboBox3.getSelectedItem();
            int billId = Integer.parseInt(billSelection.split(" - ")[0]);

            String provider = (String) jComboBox4.getSelectedItem();
            double claimAmount = Double.parseDouble(jTextField13.getText());

            InsuranceClaim insuranceClaim = new InsuranceClaim();
            insuranceClaim.setBillId(billId);
            insuranceClaim.setInsuranceProvider(provider);
            insuranceClaim.setClaimAmount(claimAmount);
            insuranceClaim.setStatus("Submitted");
            insuranceClaim.setSubmissionDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            boolean processed = claimProcessor.processClaim(insuranceClaim);

            if (processed) {
                    if (billingController.addInsuranceClaim(insuranceClaim)) {
                        JOptionPane.showMessageDialog(this, "Insurance claim submitted successfully!");
                        loadInsuranceData();
                        clearClaimForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to save insurance claim to database.");
                    }
            } else {
                JOptionPane.showMessageDialog(this, "Claim validation failed. Please check the claim details.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error submitting claim: " + e.getMessage());
        }
    }

    public void deleteBill() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete.");
            return;
        }

        int billId = (Integer) billsTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete bill ID " + billId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (billingController.deleteBill(billId)) {
                JOptionPane.showMessageDialog(this, "Bill deleted successfully!");
                loadBillingData();
                populateComboBoxes();
                clearBillForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bill.");
            }
        }
    }

    public void deleteClaim() {
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a claim to delete.");
            return;
        }

        int claimId = (Integer) claimsTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete claim ID " + claimId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (billingController.deleteInsuranceClaim(claimId)) {
                JOptionPane.showMessageDialog(this, "Claim deleted successfully!");
                loadInsuranceData();
                clearClaimForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete claim.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("GlobeMed - Billing & Insurance");

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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill ID", "Patient", "Total Amount", "Insurance", "Due Amount", "Billing Date", "Due Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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

        jLabel3.setText("Patient");

        jLabel4.setText("Total Amount");

        jLabel5.setText("Billing Date");

        jLabel6.setText("Due Date");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton2.setText("Create Bill");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton3.setText("Delete Bill");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3)
                    .addComponent(jTextField4)
                    .addComponent(jTextField5)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(0, 152, Short.MAX_VALUE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Billing", jPanel3);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Claim ID ", "Bill", "Patient", "Provider", "Claimed Amount", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel14.setText("Bill");

        jLabel15.setText("Insurance Provider");

        jLabel16.setText("Calim Amount");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton6.setText("Submit Claim");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton7.setText("Delete Claim");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField13)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Insurance Claims", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // Create Bill
        createBill();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Submit Claim
        submitClaim();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int billId = (Integer) billsTableModel.getValueAt(selectedRow, 0);
                    Bill bill = billingController.getBillById(billId);
                    if (bill != null) {
                        for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                            if (jComboBox1.getItemAt(i).startsWith(bill.getPatientId() + " - ")) {
                                jComboBox1.setSelectedIndex(i);
                                break;
                            }
                        }
                        jTextField3.setText(String.valueOf(bill.getTotalAmount()));
                        jTextField4.setText(String.valueOf(bill.getInsuranceCoverage()));
                        jTextField5.setText(String.valueOf(bill.getPatientPayment()));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error loading bill details: " + e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = jTable2.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int claimId = (Integer) claimsTableModel.getValueAt(selectedRow, 0);
                    InsuranceClaim claim = billingController.getClaimById(claimId);
                    if (claim != null) {
                        for (int i = 0; i < jComboBox3.getItemCount(); i++) {
                            if (jComboBox3.getItemAt(i).startsWith(claim.getBillId() + " - ")) {
                                jComboBox3.setSelectedIndex(i);
                                break;
                            }
                        }
                        
                        for (int i = 0; i < jComboBox4.getItemCount(); i++) {
                            if (jComboBox4.getItemAt(i).equals(claim.getInsuranceProvider())) {
                                jComboBox4.setSelectedIndex(i);
                                break;
                            }
                        }
                        jTextField13.setText(String.valueOf(claim.getClaimAmount()));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error loading claim details: " + e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Delete Claim
        deleteClaim();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Delete Bill
        deleteBill();
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        FlatDarkLaf.setup();
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new BillingAndInsurance().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
