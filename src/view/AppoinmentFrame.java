/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import dao.DentistDAO;
import dao.TreatmentDAO;
import service.AppointmentService;

import java.sql.Date;
import java.sql.Time;
import javax.swing.JOptionPane;

public class AppoinmentFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppoinmentFrame.class.getName());
    private String appointmentNumber;

    private boolean updateMode = false;
    private String selectedAppointmentNumber = null;

    private int selectedPatientId = -1;
    /**
     * Creates new form AppoinmentFrame
     */
    public AppoinmentFrame() {
        initComponents();
        applyModernStyles();
        
        loadDentists();
        loadTreatments();
        generateAppointmentNumber();


    }
    
    public AppoinmentFrame(String appointmentNumber) {

        initComponents();
        applyModernStyles();

        loadDentists();
        loadTreatments();

        this.selectedAppointmentNumber = appointmentNumber;
        this.appointmentNumber = appointmentNumber;
        this.updateMode = true;

        loadAppointmentForUpdate(appointmentNumber);
}
    
    private void loadAppointmentForUpdate(
        String appointmentNumber) {

    try {

        dao.AppointmentDAO dao =
                new dao.AppointmentDAO();

        java.sql.ResultSet rs =
                dao.searchAppointment(
                        appointmentNumber
                );

        if (rs != null && rs.next()) {

            // Store existing IDs
            selectedPatientId =
                    rs.getInt("patient_id");

            selectedAppointmentNumber =
                    rs.getString("appointment_number");

            this.appointmentNumber =
                    selectedAppointmentNumber;

            // -----------------------------
            // Patient details
            // -----------------------------

            txtPatientName.setText(
                    rs.getString("patient_name")
            );

            txtAddress.setText(
                    rs.getString("address")
            );

            txtContactNumber.setText(
                    rs.getString("contact_number")
            );

            // -----------------------------
            // Appointment number
            // -----------------------------

            txtApptNo.setText(
                    rs.getString("appointment_number")
            );

            // Appointment number cannot change
            txtApptNo.setEditable(false);

            // -----------------------------
            // Dentist
            // -----------------------------

            int dentistId =
                    rs.getInt("dentist_id");

            for (int i = 0;
                    i < comboDentist.getItemCount();
                    i++) {

                String item =
                        comboDentist.getItemAt(i);

                if (item.startsWith(
                        dentistId + " - ")) {

                    comboDentist.setSelectedIndex(i);
                    break;
                }
            }

            // -----------------------------
            // Treatment
            // -----------------------------

            int treatmentId =
                    rs.getInt("treatment_id");

            for (int i = 0;
                    i < comboTreatment.getItemCount();
                    i++) {

                String item =
                        comboTreatment.getItemAt(i);

                if (item.startsWith(
                        treatmentId + " - ")) {

                    comboTreatment.setSelectedIndex(i);
                    break;
                }
            }

            // -----------------------------
            // Date
            // -----------------------------

            java.sql.Date appointmentDate =
                    rs.getDate(
                            "appointment_date"
                    );

            txtDate.setDate(appointmentDate);

            // -----------------------------
            // Time
            // -----------------------------

            String appointmentTime =
                    rs.getString(
                            "appointment_time"
                    );

            for (int i = 0;
                    i < comboTime.getItemCount();
                    i++) {

                String item =
                        comboTime.getItemAt(i);

                if (item.equalsIgnoreCase(
                        appointmentTime)) {

                    comboTime.setSelectedIndex(i);
                    break;
                }
            }

            // -----------------------------
            // Change button to UPDATE
            // -----------------------------

            btnSave.setText("UPDATE");

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Appointment not found.",
                    "Update Appointment",
                    JOptionPane.WARNING_MESSAGE
            );

            this.dispose();
        }

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Error loading appointment details.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

    
    private void loadDentists() {

        DentistDAO dentistDAO = new DentistDAO();

        dentistDAO.loadDentists(comboDentist);
}

    private void loadTreatments() {

        TreatmentDAO treatmentDAO = new TreatmentDAO();

        treatmentDAO.loadTreatments(comboTreatment);
}
    
    private void generateAppointmentNumber() {

        AppointmentService service =
                new AppointmentService();

        txtApptNo.setText(
                service.getNextAppointmentNumber()
        );

        txtApptNo.setEditable(false);
}
    
    private void applyModernStyles() {
    // 1. Modern Rounded Corners for all Inputs
        txtPatientName.putClientProperty("JComponent.roundRect", true);
        txtContactNumber.putClientProperty("JComponent.roundRect", true);
        txtAddress.putClientProperty("JComponent.roundRect", true);
        txtApptNo.putClientProperty("JComponent.roundRect", true);
        txtDate.putClientProperty("JComponent.roundRect", true);

        // 2. Modern Dropdowns
        comboDentist.putClientProperty("JComponent.roundRect", true);
        comboTreatment.putClientProperty("JComponent.roundRect", true);
        comboTime.putClientProperty("JComponent.roundRect", true);

        // 3. Native FlatLaf Placeholders (replaces standard empty boxes)
        txtPatientName.putClientProperty("JTextField.placeholderText", "Enter full patient name");
        txtContactNumber.putClientProperty("JTextField.placeholderText", "07X XXX XXXX");
        txtAddress.putClientProperty("JTextField.placeholderText", "Enter residential address");

        // 4. Read-only Style for Appointment Number
        txtApptNo.setEditable(false);
        txtApptNo.setText("APT0001");
        txtApptNo.setBackground(new java.awt.Color(241, 245, 249));

        // 5. Modern Button Styling & Flat Hover Effects
        btnSave.putClientProperty("JButton.buttonType", "roundRect");
        btnSave.setBackground(new java.awt.Color(0, 122, 255));
        btnSave.setForeground(java.awt.Color.WHITE);
        btnSave.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        btnClear.putClientProperty("JButton.buttonType", "roundRect");
        btnClear.setBackground(new java.awt.Color(240, 247, 255));
        btnClear.setForeground(new java.awt.Color(2, 132, 199));
        btnClear.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        // 6. White Card Rounded Border & Padding
        whiteCardPanel.putClientProperty("JComponent.roundRect", true);
    }
   
    
private Time convertTime(String time) {

    try {

        java.text.SimpleDateFormat format =
                new java.text.SimpleDateFormat(
                        "hh:mm a"
                );

        java.util.Date date =
                format.parse(time);

        return new Time(date.getTime());

    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }
}

private void clearForm() {

        txtPatientName.setText("");
        txtContactNumber.setText("");
        txtAddress.setText("");

        comboDentist.setSelectedIndex(0);
        comboTreatment.setSelectedIndex(0);
        comboTime.setSelectedIndex(0);

        txtDate.setDate(null);
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        whiteCardPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPatientName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtContactNumber = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtApptNo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        comboDentist = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        comboTreatment = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtDate = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        comboTime = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(940, 620));
        jPanel1.setPreferredSize(new java.awt.Dimension(940, 620));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        whiteCardPanel.setBackground(new java.awt.Color(255, 255, 255));
        whiteCardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(15, 43, 72));
        jLabel3.setText("PATIENT INFORMATION");
        whiteCardPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 18, 220, 22));

        jLabel4.setText("Patient Name");
        whiteCardPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 46, 150, 16));

        txtPatientName.setBackground(new java.awt.Color(248, 250, 252));
        txtPatientName.setForeground(new java.awt.Color(126, 144, 169));
        txtPatientName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(203, 213, 225), 1, true));
        whiteCardPanel.add(txtPatientName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 66, 400, 36));

        jLabel5.setText("Address");
        whiteCardPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 150, 16));

        jLabel6.setText("Contact Number");
        whiteCardPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 46, 150, 16));

        txtContactNumber.setBackground(new java.awt.Color(248, 250, 252));
        txtContactNumber.setForeground(new java.awt.Color(148, 163, 184));
        txtContactNumber.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(203, 213, 225), 1, true));
        whiteCardPanel.add(txtContactNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 66, 400, 36));

        txtAddress.setBackground(new java.awt.Color(248, 250, 252));
        txtAddress.setForeground(new java.awt.Color(148, 163, 184));
        txtAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(203, 213, 225), 1, true));
        whiteCardPanel.add(txtAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 820, 36));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(15, 43, 72));
        jLabel7.setText("APPOINTMENT INFORMATION");
        whiteCardPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 185, 260, 20));

        jLabel8.setText("Dentist ");
        whiteCardPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 212, 150, 16));

        txtApptNo.setBackground(new java.awt.Color(248, 250, 252));
        txtApptNo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(203, 213, 225), 1, true));
        whiteCardPanel.add(txtApptNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 232, 400, 36));

        jLabel9.setText("Time");
        whiteCardPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 278, 100, 16));

        comboDentist.setBackground(new java.awt.Color(248, 250, 252));
        comboDentist.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Dentist" }));
        whiteCardPanel.add(comboDentist, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 232, 400, 36));

        jLabel10.setText("Appoinment No");
        whiteCardPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 212, 150, 16));

        comboTreatment.setBackground(new java.awt.Color(248, 250, 252));
        comboTreatment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Treatment" }));
        whiteCardPanel.add(comboTreatment, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 298, 320, 36));

        jLabel11.setText("Treatment");
        whiteCardPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 278, 150, 16));

        txtDate.setBackground(new java.awt.Color(248, 250, 252));
        whiteCardPanel.add(txtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 298, 220, 36));

        jLabel12.setText("Date");
        whiteCardPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 278, 100, 16));

        comboTime.setBackground(new java.awt.Color(248, 250, 252));
        comboTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Time", "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM" }));
        whiteCardPanel.add(comboTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 298, 220, 36));

        btnSave.setBackground(new java.awt.Color(0, 102, 204));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(this::btnSaveActionPerformed);
        whiteCardPanel.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 190, 42));

        btnClear.setBackground(new java.awt.Color(240, 247, 255));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnClear.setForeground(new java.awt.Color(2, 132, 199));
        btnClear.setText("Clear");
        btnClear.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(186, 230, 253), 1, true));
        btnClear.addActionListener(this::btnClearActionPerformed);
        whiteCardPanel.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 380, 140, 42));

        jPanel1.add(whiteCardPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 85, 880, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(15, 43, 72));
        jLabel1.setText("REGISTER NEW APPOINTMENT");
        jLabel1.setPreferredSize(new java.awt.Dimension(450, 30));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 450, 30));

        jLabel2.setForeground(new java.awt.Color(100, 116, 139));
        jLabel2.setText("Create a new patient appointment");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 350, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    String patientName = txtPatientName.getText().trim();
    String address = txtAddress.getText().trim();
    String contact = txtContactNumber.getText().trim();

    // Patient name
    if (patientName.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter patient name.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        txtPatientName.requestFocus();
        return;
    }

    if (!patientName.matches("[a-zA-Z ]+")) {
        JOptionPane.showMessageDialog(
                this,
                "Patient name should contain letters only.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        txtPatientName.requestFocus();
        return;
    }

    // Address
    if (address.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter patient address.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        txtAddress.requestFocus();
        return;
    }

    // Contact number
    if (contact.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter contact number.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        txtContactNumber.requestFocus();
        return;
    }

    if (!contact.matches("07\\d{8}")) {
        JOptionPane.showMessageDialog(
                this,
                "Please enter a valid contact number.\nExample: 0712345678",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        txtContactNumber.requestFocus();
        return;
    }

    // Dentist
    if (comboDentist.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a dentist.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        comboDentist.requestFocus();
        return;
    }

    // Treatment
    if (comboTreatment.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Please select a treatment.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        comboTreatment.requestFocus();
        return;
    }

    // Date
    if (txtDate.getDate() == null) {
        JOptionPane.showMessageDialog(
                this,
                "Please select appointment date.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        txtDate.requestFocus();
        return;
    }

    // Check past date
    java.util.Date selectedDate = txtDate.getDate();
    java.util.Date today = new java.util.Date();

    java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat("yyyy-MM-dd");

    try {
        java.util.Date selected =
                sdf.parse(sdf.format(selectedDate));

        java.util.Date current =
                sdf.parse(sdf.format(today));

        if (selected.before(current)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Appointment date cannot be in the past.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            txtDate.requestFocus();
            return;
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Invalid date.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // Time
    if (comboTime.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Please select appointment time.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
        comboTime.requestFocus();
        return;
    }

        // Save or Update appointment
        try {

            String dentistText =
                    comboDentist
                            .getSelectedItem()
                            .toString();

            int dentistId =
                    Integer.parseInt(
                            dentistText.split(" - ")[0]
                    );

            String treatmentText =
                    comboTreatment
                            .getSelectedItem()
                            .toString();

            int treatmentId =
                    Integer.parseInt(
                            treatmentText.split(" - ")[0]
                    );

            Date appointmentDate =
                    new Date(
                            txtDate.getDate().getTime()
                    );

            String selectedTime =
                    comboTime
                            .getSelectedItem()
                            .toString();

            Time appointmentTime =
                    convertTime(selectedTime);

            AppointmentService service =
                    new AppointmentService();

            // =================================================
            // UPDATE MODE
            // =================================================

            if (updateMode) {

                boolean updated =
                        service.updateAppointment(
                                selectedAppointmentNumber,
                                selectedPatientId,
                                patientName,
                                address,
                                contact,
                                dentistId,
                                treatmentId,
                                appointmentDate,
                                appointmentTime
                        );

                if (updated) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Appointment updated successfully!\n\n"
                            + "Appointment No: "
                            + selectedAppointmentNumber,
                            "Update Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Return to search screen
                    SearchFame searchFrame =
                            new SearchFame();

                    searchFrame.setLocationRelativeTo(this);
                    searchFrame.setVisible(true);

                    this.dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Unable to update appointment.\n"
                            + "The selected dentist may already "
                            + "be booked for this date and time.",
                            "Update Failed",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

            // =================================================
            // NEW APPOINTMENT MODE
            // =================================================

            } else {

                boolean saved =
                        service.saveAppointment(
                                patientName,
                                address,
                                contact,
                                dentistId,
                                treatmentId,
                                appointmentDate,
                                appointmentTime
                        );

                if (saved) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Appointment saved successfully!\n\n"
                            + "Appointment No: "
                            + txtApptNo.getText(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    clearForm();

                    generateAppointmentNumber();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "This dentist is already booked "
                            + "for the selected date and time.",
                            "Booking Conflict",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error processing appointment.\n"
                    + "Please try again.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearForm();
        generateAppointmentNumber();
    }//GEN-LAST:event_btnClearActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        //java.awt.EventQueue.invokeLater(() -> new AppoinmentFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> comboDentist;
    private javax.swing.JComboBox<String> comboTime;
    private javax.swing.JComboBox<String> comboTreatment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtApptNo;
    private javax.swing.JTextField txtContactNumber;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtPatientName;
    private javax.swing.JPanel whiteCardPanel;
    // End of variables declaration//GEN-END:variables
}
