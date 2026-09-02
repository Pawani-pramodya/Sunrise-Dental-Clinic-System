package dao;

import model.Bill;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object for billing operations.
 */
public class BillDAO {

    /**
     * Saves a generated bill in the database.
     *
     * @param bill bill object
     * @return true if bill is saved successfully
     */
    public boolean saveBill(Bill bill) {

        String sql =
                "INSERT INTO bills "
                + "(appointment_number, consultation_fee, "
                + "treatment_fee, total_amount) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bill.getAppointmentNumber());
            ps.setDouble(2, bill.getConsultationFee());
            ps.setDouble(3, bill.getTreatmentFee());
            ps.setDouble(4, bill.getTotalAmount());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks whether a bill already exists
     * for the given appointment.
     *
     * @param appointmentNumber appointment number
     * @return true if a bill exists
     */
    public boolean billExists(String appointmentNumber) {

        String sql =
                "SELECT bill_id "
                + "FROM bills "
                + "WHERE appointment_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, appointmentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
