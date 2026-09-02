/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

import service.DashboardService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pawan
 */
public class DashboardFrame extends javax.swing.JFrame {
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardFrame.class.getName());
    private DashboardService dashboardService;
    
       // ============================================================
    // ACTIVE BUTTON COLORS
    // ============================================================

    private final Color ACTIVE_COLOR =
            new Color(255, 255, 255);

    private final Color ACTIVE_TEXT =
            new Color(0, 102, 204);

    private final Color INACTIVE_COLOR =
            new Color(0, 102, 204);

    private final Color INACTIVE_TEXT =
            new Color(255, 255, 255);


    /**
     * Creates new form DashboardFrame
     */
    public DashboardFrame() {

        initComponents();

        dashboardService = new DashboardService();

        loadDashboardData();
        loadTodayAppointments();

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/images/dental.png")
        );

        Image img = icon.getImage().getScaledInstance( 70,70, Image.SCALE_SMOOTH );

        jLabel1.setIcon(new ImageIcon(img));

        // Modern cards
        String modernCardStyle ="arc: 20; background: #FFFFFF; "+ "border: 1,1,1,1,#E2E8F0;";

        cardPatients.putClientProperty(FlatClientProperties.STYLE, modernCardStyle );
        cardAppointments.putClientProperty(FlatClientProperties.STYLE,  modernCardStyle );
        cardTreatments.putClientProperty(FlatClientProperties.STYLE,modernCardStyle
        );

        // Table
        tableScrollPane.putClientProperty( FlatClientProperties.STYLE, "arc: 18; background: #FFFFFF; "  + "border: 1,1,1,1,#E2E8F0;"  );

        appointmentsTable.setRowHeight(40);
        appointmentsTable.setShowHorizontalLines(true);
        appointmentsTable.setShowVerticalLines(false);

        appointmentsTable.putClientProperty(
                FlatClientProperties.STYLE,
                "gridColor: #F1F5F9; "
                + "selectionBackground: #EFF6FF; "
                + "selectionForeground: #1E40AF; "
                + "font: 13 Segoe UI;"
        );

        appointmentsTable.getTableHeader()
                .putClientProperty(
                        FlatClientProperties.STYLE,
                        "background: #0066CC; "
                        + "foreground: #FFFFFF; "
                        + "font: 12 Segoe UI; "
                        + "border: 0,0,1,0,#E2E8F0;"
        );
        
           // ========================================================
        // DASHBOARD ACTIVE BY DEFAULT
        // ========================================================

        setActiveButton(jButton2);
}
    
        // ============================================================
    // ACTIVE BUTTON
    // ============================================================

    private void setActiveButton(
            javax.swing.JButton activeButton) {

        javax.swing.JButton[] buttons = {

            jButton2, // Dashboard
            jButton3, // Appointments
            jButton4, // Patients
            jButton5, // Search
            jButton6, // Billing
            jButton7  // Help

        };

        for (javax.swing.JButton button : buttons) {

            if (button == activeButton) {

                // ACTIVE
                button.setBackground(
                        ACTIVE_COLOR
                );

                button.setForeground(
                        ACTIVE_TEXT
                );

                button.setFont(
                        new java.awt.Font(
                                "Segoe UI",
                                java.awt.Font.BOLD,
                                14
                        )
                );

            } else {

                // INACTIVE
                button.setBackground(
                        INACTIVE_COLOR
                );

                button.setForeground(
                        INACTIVE_TEXT
                );

                button.setFont(
                        new java.awt.Font(
                                "Segoe UI",
                                java.awt.Font.PLAIN,
                                14
                        )
                );
            }

            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
        }
    }
    
    private void loadDashboardData() {

    int totalPatients =
            dashboardService.getTotalPatients();

    int todayAppointments =
            dashboardService.getTodayAppointments();

    int totalTreatments =
            dashboardService.getTotalTreatments();


    // Patients card
    jLabel7.setText(
            String.valueOf(totalPatients)
    );


    // Today's appointments card
    jLabel8.setText(
            String.valueOf(todayAppointments)
    );


    // Treatments card
    jLabel9.setText(
            String.valueOf(totalTreatments)
    );
}
    
    
    private void loadTodayAppointments() {

    DefaultTableModel model =
            new DefaultTableModel(
                    new String[]{
                        "ID",
                        "Patient Name",
                        "Dentist",
                        "Treatment",
                        "Time"
                    },
                    0
            ) {

                @Override
                public boolean isCellEditable(
                        int row,
                        int column) {

                    return false;
                }
            };


    String sql = """
            SELECT
                a.appointment_number,
                p.patient_name,
                d.dentist_name,
                t.treatment_name,
                TIME_FORMAT(
                    a.appointment_time,
                    '%h:%i %p'
                ) AS appointment_time

            FROM appointments a

            INNER JOIN patients p
                ON a.patient_id = p.patient_id

            INNER JOIN dentists d
                ON a.dentist_id = d.dentist_id

            INNER JOIN treatments t
                ON a.treatment_id = t.treatment_id

            WHERE a.appointment_date = CURDATE()

            ORDER BY a.appointment_time
            """;


    try (
        java.sql.Connection con =
                util.DBConnection.getConnection();

        java.sql.PreparedStatement pst =
                con.prepareStatement(sql);

        java.sql.ResultSet rs =
                pst.executeQuery()
    ) {

        while (rs.next()) {

            model.addRow(new Object[]{

                rs.getString("appointment_number"),
                rs.getString("patient_name"),
                rs.getString("dentist_name"),
                rs.getString("treatment_name"),
                rs.getString("appointment_time")
            });
        }


        appointmentsTable.setModel(model);
        formatAppointmentTable();


    } catch (Exception e) {

        e.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "Unable to load today's appointments.\n"
                + "Please check the database connection.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    
    private void formatAppointmentTable() {

    appointmentsTable.setRowHeight(40);

    appointmentsTable
            .getTableHeader()
            .setReorderingAllowed(false);


    appointmentsTable
            .getColumnModel()
            .getColumn(0)
            .setPreferredWidth(100);

    appointmentsTable
            .getColumnModel()
            .getColumn(1)
            .setPreferredWidth(180);

    appointmentsTable
            .getColumnModel()
            .getColumn(2)
            .setPreferredWidth(180);

    appointmentsTable
            .getColumnModel()
            .getColumn(3)
            .setPreferredWidth(180);

    appointmentsTable
            .getColumnModel()
            .getColumn(4)
            .setPreferredWidth(100);
}
    
    
    // ============================================================
    // REFRESH DASHBOARD
    // ============================================================

    public void refreshDashboard() {

        loadDashboardData();

        loadTodayAppointments();
    }
    
    private void showSearchAppointment() {

        AppoinmentFrame searchFrame = new AppoinmentFrame();
        jPanelMain.removeAll();
        jPanelMain.setLayout(new java.awt.BorderLayout());
        jPanelMain.add(searchFrame.getContentPane(), java.awt.BorderLayout.CENTER);
        jPanelMain.revalidate();
        jPanelMain.repaint();
        
    }
    
        private void showPatientFrame() {
        PatientFrame billFrame = new PatientFrame();
        jPanelMain.removeAll();
        jPanelMain.setLayout(new java.awt.BorderLayout());
        jPanelMain.add(billFrame.getContentPane(), java.awt.BorderLayout.CENTER);
        jPanelMain.revalidate();
        jPanelMain.repaint();
    } 
    
    private void showSearchFrame() {
        SearchFame searchFrame = new SearchFame();
        jPanelMain.removeAll();
        jPanelMain.setLayout(new java.awt.BorderLayout());
        jPanelMain.add(searchFrame.getContentPane(), java.awt.BorderLayout.CENTER);
        jPanelMain.revalidate();
        jPanelMain.repaint();
    }

    private void showBillFrame() {
        BillingFrame billFrame = new BillingFrame();
        jPanelMain.removeAll();
        jPanelMain.setLayout(new java.awt.BorderLayout());
        jPanelMain.add(billFrame.getContentPane(), java.awt.BorderLayout.CENTER);
        jPanelMain.revalidate();
        jPanelMain.repaint();
    }  
    
        private void showhelpFrame() {
        HelpFrame billFrame = new HelpFrame();
        jPanelMain.removeAll();
        jPanelMain.setLayout(new java.awt.BorderLayout());
        jPanelMain.add(billFrame.getContentPane(), java.awt.BorderLayout.CENTER);
        jPanelMain.revalidate();
        jPanelMain.repaint();
    } 
    
    private void showDashboardView() {
        jPanelMain.removeAll();
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // මුල් labels, cards සහ table එක නැවත add කිරීම
        jPanelMain.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));
        jPanelMain.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));
        jPanelMain.add(cardPatients, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));
        jPanelMain.add(cardAppointments, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, -1, -1));
        jPanelMain.add(cardTreatments, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 120, -1, -1));
        jPanelMain.add(tableScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 850, 270));

        jPanelMain.revalidate();
        jPanelMain.repaint();
        
        // Refresh cards and table every time Dashboard is opened.
        refreshDashboard();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanelMain = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cardPatients = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cardAppointments = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cardTreatments = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tableScrollPane = new javax.swing.JScrollPane();
        appointmentsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1150, 680));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1150, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(30, 41, 59));
        jLabel1.setText("SUNRISE DENTAL CLINIC");

        jButton1.setForeground(new java.awt.Color(41, 101, 214));
        jButton1.setText("Logout");
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setFocusPainted(false);
        jButton1.setMaximumSize(new java.awt.Dimension(60, 60));
        jButton1.setPreferredSize(new java.awt.Dimension(60, 60));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 866, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel3.setBackground(new java.awt.Color(0, 102, 204));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(210, 620));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setBackground(new java.awt.Color(0, 102, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("        Dashboard");
        jButton2.setAlignmentX(0.5F);
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton2.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 210, 60));

        jButton3.setBackground(new java.awt.Color(0, 102, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("        Appoinments");
        jButton3.setAlignmentX(0.5F);
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton3.setFocusPainted(false);
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton3.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 210, 60));

        jButton4.setBackground(new java.awt.Color(0, 102, 204));
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("        Patients");
        jButton4.setAlignmentX(0.5F);
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton4.setFocusPainted(false);
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton4.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton4.addActionListener(this::jButton4ActionPerformed);
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 210, 60));

        jButton5.setBackground(new java.awt.Color(0, 102, 204));
        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("        Search");
        jButton5.setAlignmentX(0.5F);
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton5.setFocusPainted(false);
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton5.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton5.addActionListener(this::jButton5ActionPerformed);
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 210, 60));

        jButton6.setBackground(new java.awt.Color(0, 102, 204));
        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("        Billing");
        jButton6.setAlignmentX(0.5F);
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton6.setFocusPainted(false);
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton6.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton6.addActionListener(this::jButton6ActionPerformed);
        jPanel3.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 210, 60));

        jButton7.setBackground(new java.awt.Color(0, 102, 204));
        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("        Help");
        jButton7.setAlignmentX(0.5F);
        jButton7.setBorder(null);
        jButton7.setBorderPainted(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton7.setFocusPainted(false);
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton7.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton7.addActionListener(this::jButton7ActionPerformed);
        jPanel3.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, 210, 60));

        jButton8.setBackground(new java.awt.Color(0, 102, 204));
        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("        Exit");
        jButton8.setAlignmentX(0.5F);
        jButton8.setBorder(null);
        jButton8.setBorderPainted(false);
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton8.setFocusPainted(false);
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.setMargin(new java.awt.Insets(10, 20, 5, 10));
        jButton8.setPreferredSize(new java.awt.Dimension(210, 60));
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel3.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 210, 60));

        jPanel1.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanelMain.setPreferredSize(new java.awt.Dimension(250, 120));
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(30, 41, 59));
        jLabel2.setText("Good Morning!");
        jPanelMain.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(100, 116, 139));
        jLabel3.setText("Here's today's clinic overview");
        jPanelMain.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        cardPatients.setBackground(new java.awt.Color(240, 247, 255));
        cardPatients.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 232, 240), 1, true));
        cardPatients.setPreferredSize(new java.awt.Dimension(250, 120));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Patients");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("23");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.setPreferredSize(new java.awt.Dimension(50, 26));

        javax.swing.GroupLayout cardPatientsLayout = new javax.swing.GroupLayout(cardPatients);
        cardPatients.setLayout(cardPatientsLayout);
        cardPatientsLayout.setHorizontalGroup(
            cardPatientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPatientsLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel4)
                .addContainerGap(175, Short.MAX_VALUE))
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardPatientsLayout.setVerticalGroup(
            cardPatientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPatientsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelMain.add(cardPatients, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        cardAppointments.setBackground(new java.awt.Color(240, 253, 244));
        cardAppointments.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 232, 240), 1, true));
        cardAppointments.setPreferredSize(new java.awt.Dimension(250, 120));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Today's Appointments");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 102));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("12");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel8.setPreferredSize(new java.awt.Dimension(50, 26));

        javax.swing.GroupLayout cardAppointmentsLayout = new javax.swing.GroupLayout(cardAppointments);
        cardAppointments.setLayout(cardAppointmentsLayout);
        cardAppointmentsLayout.setHorizontalGroup(
            cardAppointmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAppointmentsLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel5)
                .addContainerGap(86, Short.MAX_VALUE))
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardAppointmentsLayout.setVerticalGroup(
            cardAppointmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAppointmentsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanelMain.add(cardAppointments, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, -1, -1));

        cardTreatments.setBackground(new java.awt.Color(255, 247, 237));
        cardTreatments.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 232, 240), 1, true));
        cardTreatments.setPreferredSize(new java.awt.Dimension(250, 120));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Treatments");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 102));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("8");
        jLabel9.setPreferredSize(new java.awt.Dimension(50, 26));

        javax.swing.GroupLayout cardTreatmentsLayout = new javax.swing.GroupLayout(cardTreatments);
        cardTreatments.setLayout(cardTreatmentsLayout);
        cardTreatmentsLayout.setHorizontalGroup(
            cardTreatmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTreatmentsLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addContainerGap(155, Short.MAX_VALUE))
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardTreatmentsLayout.setVerticalGroup(
            cardTreatmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTreatmentsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanelMain.add(cardTreatments, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 120, -1, -1));

        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Patient Name", "Dentist", "Treatment", "Time"
            }
        ));
        appointmentsTable.setGridColor(new java.awt.Color(241, 245, 249));
        appointmentsTable.setPreferredSize(new java.awt.Dimension(850, 370));
        appointmentsTable.setRowHeight(32);
        appointmentsTable.setSelectionBackground(new java.awt.Color(235, 242, 254));
        tableScrollPane.setViewportView(appointmentsTable);

        jPanelMain.add(tableScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 850, 270));

        jPanel1.add(jPanelMain, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
               int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (result ==
                JOptionPane.YES_OPTION) {

            this.dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        setActiveButton(jButton6);
        showBillFrame();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         setActiveButton(jButton5);
         showSearchFrame();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        setActiveButton(jButton7);
        showhelpFrame();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        setActiveButton(jButton3);
        showSearchAppointment();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setActiveButton(jButton2);
        showDashboardView();    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        setActiveButton(jButton4);
        showPatientFrame();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
                int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (result ==
                JOptionPane.YES_OPTION) {

            System.exit(0);
        }
    }//GEN-LAST:event_jButton8ActionPerformed


    
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
        java.awt.EventQueue.invokeLater(() -> new DashboardFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable appointmentsTable;
    private javax.swing.JPanel cardAppointments;
    private javax.swing.JPanel cardPatients;
    private javax.swing.JPanel cardTreatments;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables
}
