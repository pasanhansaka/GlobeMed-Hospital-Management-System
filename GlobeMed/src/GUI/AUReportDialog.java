package GUI;

import CONTROLLER.PatientController;
import CONTROLLER.ReportController;
import CONTROLLER.StaffController;
import MODEL.MedicalReport;
import MODEL.Patient;
import MODEL.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class AUReportDialog extends javax.swing.JDialog {

    private MedicalReport report;
    private String currentUserRole;
    private ReportController reportController;
    private PatientController patientController;
    private StaffController staffController;
    private boolean isEditMode;

    public AUReportDialog(java.awt.Frame parent, boolean modal, MedicalReport report, String currentUserRole) {
        super(parent, modal);
        this.report = report;
        this.currentUserRole = currentUserRole;
        this.isEditMode = (report != null);

        this.reportController = new ReportController();
        this.patientController = new PatientController();
        this.staffController = new StaffController();

        initComponents();
        loadComboBoxData();

        if (isEditMode) {
            loadReportData();
            setTitle("Edit Medical Report");
            jLabel1.setText("Edit Medical Report");
        } else {
            setTitle("Add Medical Report");
            jLabel1.setText("Add Medical Report");
        }
    }

    private void loadComboBoxData() {
        try {
            // Load patients
            List<Patient> patients = patientController.getAllPatients();
            DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
            patientModel.addElement("Select Patient");

            for (Patient patient : patients) {
                patientModel.addElement(patient.getPatientId() + " - "
                        + patient.getFirstName() + " " + patient.getLastName());
            }
            jComboBox1.setModel(patientModel);

            // Load doctors
            List<User> doctors = staffController.getAllStaff();
            DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
            doctorModel.addElement("Select Doctor");

            for (User doctor : doctors) {
                if ("Doctor".equals(doctor.getRoleName())
                        || doctor.getRoleRoleId() == 2) { // Assuming role ID 2 is Doctor
                    doctorModel.addElement(doctor.getUserId() + " - "
                            + doctor.getFirstName() + " " + doctor.getLastName());
                }
            }
            jComboBox2.setModel(doctorModel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage());
        }
    }

    private boolean validateRequiredFields(boolean validatePasswordRequired) {
        // Check if patient is selected
        if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient.");
            jComboBox1.requestFocus();
            return false;
        }

        // Check if doctor is selected
        if (jComboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            jComboBox2.requestFocus();
            return false;
        }

        // Check if visit date is empty
        if (jTextField1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter visit date.");
            jTextField1.requestFocus();
            return false;
        }

        // Check if diagnosis is empty
        if (jTextField2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter diagnosis.");
            jTextField2.requestFocus();
            return false;
        }

        return true;
    }

    private void loadReportData() {
        if (report != null) {
            for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                String item = jComboBox1.getItemAt(i);
                if (item.startsWith(String.valueOf(report.getPatientId()) + " -")) {
                    jComboBox1.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < jComboBox2.getItemCount(); i++) {
                String item = jComboBox2.getItemAt(i);
                if (item.startsWith(String.valueOf(report.getDoctorId()) + " -")) {
                    jComboBox2.setSelectedIndex(i);
                    break;
                }
            }

            if (report.getVisitDate() != null) {
                jTextField1.setText(report.getVisitDate().toString());
            }

            jTextField2.setText(report.getDiagnosis() != null ? report.getDiagnosis() : "");
            jTextArea1.setText(report.getSymptoms() != null ? report.getSymptoms() : "");
            jTextArea2.setText(report.getTreatmentPlan() != null ? report.getTreatmentPlan() : "");
            jTextArea3.setText(report.getMedicationsPrescribed() != null ? report.getMedicationsPrescribed() : "");
            jTextArea4.setText(report.getNotes() != null ? report.getNotes() : "");
        } else {
            jTextField1.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    private void saveReport() {
        try {
            if (!validateRequiredFields(true)) {
                return;
            }

            String patientSelection = (String) jComboBox1.getSelectedItem();
            String doctorSelection = (String) jComboBox2.getSelectedItem();

            int patientId = Integer.parseInt(patientSelection.split(" - ")[0]);
            int doctorId = Integer.parseInt(doctorSelection.split(" - ")[0]);

            MedicalReport medicalReport;
            if (isEditMode) {
                medicalReport = report;
                medicalReport.setPatientId(patientId);
                medicalReport.setDoctorId(doctorId);
            } else {
                medicalReport = new MedicalReport();
                medicalReport.setPatientId(patientId);
                medicalReport.setDoctorId(doctorId);
                medicalReport.setCreated_at(java.time.LocalDateTime.now().toString());
            }

            LocalDate visitDate = LocalDate.parse(jTextField1.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            medicalReport.setVisitDate(visitDate);

            medicalReport.setDiagnosis(jTextField2.getText().trim());
            medicalReport.setSymptoms(jTextArea1.getText().trim());
            medicalReport.setTreatmentPlan(jTextArea2.getText().trim());
            medicalReport.setMedicationsPrescribed(jTextArea3.getText().trim());
            medicalReport.setNotes(jTextArea4.getText().trim());

            boolean success;
            if (isEditMode) {
                success = reportController.updateReport(medicalReport);
            } else {
                success = reportController.addReport(medicalReport);
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        (isEditMode ? "Report updated" : "Report added") + " successfully!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to " + (isEditMode ? "update" : "add") + " report!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving report: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setText("Add Medical Report");

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
                                .addGap(135, 135, 135)
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
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Patient:");

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setText("Doctor:");

        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Visit Date (YYYY-MM-DD):");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Diagnosis:");

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Symptoms:");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea1.setRows(3);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("Treatment Plan:");

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea2.setRows(3);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Medications Prescribed:");

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea3.setRows(3);
        jScrollPane3.setViewportView(jTextArea3);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Notes:");

        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea4.setRows(3);
        jScrollPane4.setViewportView(jTextArea4);

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel9)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        saveReport();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    public static void main(String args[]) {
//        FlatDarkLaf.setup();
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                AUReportDialog dialog = new AUReportDialog(new javax.swing.JFrame(), true, null, "Doctor");
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration       
}
