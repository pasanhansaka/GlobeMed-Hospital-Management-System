package GUI;

import CONTROLLER.AppointmentController;
import CONTROLLER.PatientController;
import CONTROLLER.StaffController;
import MODEL.Appointment;
import MODEL.Patient;
import MODEL.User;
import PATTERN.CommandPattern;
import com.formdev.flatlaf.FlatDarkLaf;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AppointmentSheduling extends javax.swing.JFrame {

    private String currentUserRole;
    private AppointmentController appointmentController;
    private CommandPattern.AppointmentInvoker invoker;
    private DefaultTableModel tableModel;
    private PatientController patientController;
    private StaffController staffController;

    public AppointmentSheduling(String currentUserRole) {
        this.currentUserRole = currentUserRole;
        this.appointmentController = new AppointmentController();
        this.invoker = new CommandPattern.AppointmentInvoker();
        this.patientController = new PatientController();
        this.staffController = new StaffController();
        initComponents();
        setupTable();
        loadAppointmentData();
        defaultButtons();
        populateComboBoxes();
    }

    private void setupTable() {
        tableModel = (DefaultTableModel) jTable1.getModel();
        jTable1.setAutoCreateRowSorter(true);
    }

    private void loadAppointmentData() {
        try {
            java.util.List<Appointment> appointments = appointmentController.getAllAppointments();
            tableModel.setRowCount(0);
            for (Appointment appointment : appointments) {
                tableModel.addRow(new Object[]{
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    appointment.getDoctorId(),
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTime(),
                    appointment.getAppointmentType(),
                    appointment.getReason(),
                    appointment.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
        }
    }

    private void defaultButtons() {
        jButton2.setEnabled(true);
        jButton3.setEnabled(false);
        clearForm();
    }

    private void cancelButtons() {
        jButton2.setEnabled(false);
        jButton3.setEnabled(true);
    }

    private void clearForm() {
        jTextField4.setText("");
        jTextField6.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
    }

    private void populateComboBoxes() {
        try {
            // Load patients
            List<Patient> patients = patientController.getAllPatients();
            DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
            patientModel.addElement("Select");

            for (Patient patient : patients) {
                patientModel.addElement(patient.getPatientId() + " - "
                        + patient.getFirstName() + " " + patient.getLastName());
            }
            jComboBox1.setModel(patientModel);

            // Load doctors
            List<User> doctors = staffController.getAllStaff();
            DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
            doctorModel.addElement("Select");

            for (User doctor : doctors) {
                if ("Doctor".equals(doctor.getRoleName())
                        || doctor.getRoleRoleId() == 2) { // Assuming role ID 2 is Doctor
                    doctorModel.addElement(doctor.getUserId() + " - "
                            + doctor.getFirstName() + " " + doctor.getLastName());
                }
            }
            jComboBox2.setModel(doctorModel);

            // Populate Time
            jComboBox3.removeAllItems();
            jComboBox3.addItem("Select");
            jComboBox3.addItem("10:00:00");
            jComboBox3.addItem("10:30:00");
            jComboBox3.addItem("11:00:00");
            jComboBox3.addItem("11:30:00");
            jComboBox3.addItem("12:00:00");

            // Populate Type
            jComboBox4.removeAllItems();
            jComboBox4.addItem("Select");
            jComboBox4.addItem("Consultation");
            jComboBox4.addItem("Channeling");
            jComboBox4.addItem("Check-up");
            jComboBox4.addItem("Follow-up");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage());
        }
    }

    private void populateForm(Appointment appointment) {

        for (int i = 0; i < jComboBox1.getItemCount(); i++) {
            String item = jComboBox1.getItemAt(i);
            if (item.startsWith(appointment.getPatientId() + " - ")) {
                jComboBox1.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < jComboBox2.getItemCount(); i++) {
            String item = jComboBox2.getItemAt(i);
            if (item.startsWith(appointment.getDoctorId() + " - ")) {
                jComboBox2.setSelectedIndex(i);
                break;
            }
        }

        jTextField4.setText(appointment.getAppointmentDate());
        jComboBox3.setSelectedItem(appointment.getAppointmentTime());
        jComboBox4.setSelectedItem(appointment.getAppointmentType());
        jTextField6.setText(appointment.getReason());
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

        // Check if time is selected
        if (jComboBox3.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a time.");
            jComboBox3.requestFocus();
            return false;
        }

        // Check if type is selected
        if (jComboBox4.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment type.");
            jComboBox4.requestFocus();
            return false;
        }

        // Check if reason is empty
        if (jTextField6.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a reason for the appointment.");
            jTextField6.requestFocus();
            return false;
        }

        // Add date validation
        if (jTextField4.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date.");
            jTextField4.requestFocus();
            return false;
        }

        // Validate date format (YYYY-MM-DD)
        String date = jTextField4.getText().trim();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Please enter date in YYYY-MM-DD format.");
            jTextField4.requestFocus();
            return false;
        }

        return true;
    }

    public void scheduleAppointment() {
        try {
            if (!validateRequiredFields(true)) {
                return;
            }

            String patientSelection = jComboBox1.getSelectedItem().toString();
            String doctorSelection = jComboBox2.getSelectedItem().toString();

            int patientId = Integer.parseInt(patientSelection.split(" - ")[0]);
            int doctorId = Integer.parseInt(doctorSelection.split(" - ")[0]);

            String date = jTextField4.getText();
            String time = jComboBox3.getSelectedItem().toString();
            String type = jComboBox4.getSelectedItem().toString();
            String reason = jTextField6.getText();

            List<String> bookedSlots = appointmentController.getAvailableSlots(doctorId, date);
            if (bookedSlots.contains(time)) {
                JOptionPane.showMessageDialog(this, "This time slot is already booked. Please choose another time.");
                return;
            }

            Appointment appointment = new Appointment();
            appointment.setPatientId(patientId);
            appointment.setDoctorId(doctorId);
            appointment.setAppointmentDate(date);
            appointment.setAppointmentTime(time);
            appointment.setAppointmentType(type);
            appointment.setReason(reason);
            appointment.setStatus("Scheduled");

            CommandPattern.AppointmentCommand command
                    = new CommandPattern.ScheduleAppointmentCommand(appointment, appointmentController);

            invoker.executeCommand(command);

            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
            loadAppointmentData();
            defaultButtons();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error scheduling appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelAppointment() {
        try {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.");
                return;
            }

            int appointmentId = (int) jTable1.getValueAt(selectedRow, 0);

            // Use Command pattern
            CommandPattern.AppointmentCommand command
                    = new CommandPattern.CancelAppointmentCommand(appointmentId, appointmentController);

            invoker.executeCommand(command);

            JOptionPane.showMessageDialog(this, "Appointment cancelled successfully!");
            loadAppointmentData();
            defaultButtons();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cancelling appointment: " + e.getMessage());
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("GlobeMed - Appoinment Sheduling");

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
                "Appoinment ID ", "Patient", "Doctor", "Date", "Time", "Type", " Reason", "Status"
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

        jLabel4.setText("Doctor");

        jLabel5.setText("Date");

        jLabel6.setText("Time");

        jLabel7.setText("Type");

        jLabel8.setText("Reason");

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jButton2.setText("Schedule");
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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 187, Short.MAX_VALUE))
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(29, Short.MAX_VALUE))
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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int appointmentId = (int) jTable1.getValueAt(selectedRow, 0);
                    Appointment appointment = appointmentController.getAppointmentById(appointmentId);
                    if (appointment != null) {
                        populateForm(appointment);
                        cancelButtons();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error loading appointment: " + e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Schedule Appoinment
        scheduleAppointment();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Cancel Appoinment
        cancelAppointment();
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        FlatDarkLaf.setup();
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new AppointmentSheduling().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
