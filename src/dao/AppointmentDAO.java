package dao;

import model.Appointment;
import util.DBConnection;

import java.sql.*;

public class AppointmentDAO {

    // =====================================================
    // CHECK DENTIST AVAILABILITY FOR NEW APPOINTMENT
    // =====================================================

    /**
     * Checks whether a dentist is available for a new appointment.
     *
     * @param dentistId dentist ID
     * @param date appointment date
     * @param time appointment time
     * @return true if the dentist is available
     */
    public boolean isDentistAvailable(
            int dentistId,
            Date date,
            Time time) {

        String sql =
                "SELECT appointment_id "
                + "FROM appointments "
                + "WHERE dentist_id = ? "
                + "AND appointment_date = ? "
                + "AND appointment_time = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dentistId);
            ps.setDate(2, date);
            ps.setTime(3, time);

            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // CHECK DENTIST AVAILABILITY FOR UPDATE
    // =====================================================

    /**
     * Checks dentist availability while excluding the
     * appointment currently being updated.
     *
     * This prevents an appointment from conflicting with
     * itself during an update.
     */
    public boolean isDentistAvailableForUpdate(
            String appointmentNumber,
            int dentistId,
            Date date,
            Time time) {

        String sql =
                "SELECT appointment_id "
                + "FROM appointments "
                + "WHERE dentist_id = ? "
                + "AND appointment_date = ? "
                + "AND appointment_time = ? "
                + "AND appointment_number <> ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dentistId);
            ps.setDate(2, date);
            ps.setTime(3, time);
            ps.setString(4, appointmentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // SAVE APPOINTMENT
    // =====================================================

    /**
     * Inserts a new appointment into the database.
     */
    public boolean saveAppointment(Appointment appointment) {

        String sql =
                "INSERT INTO appointments "
                + "(appointment_number, patient_id, dentist_id, "
                + "treatment_id, appointment_date, appointment_time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,appointment.getAppointmentNumber());
            ps.setInt(2,appointment.getPatientId());
            ps.setInt(3,appointment.getDentistId());
            ps.setInt(4,appointment.getTreatmentId());
            ps.setDate(5,appointment.getAppointmentDate());
            ps.setTime( 6,appointment.getAppointmentTime() );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // GENERATE APPOINTMENT NUMBER
    // =====================================================

    /**
     * Generates the next appointment number.
     *
     * Example:
     * APT0001
     * APT0002
     * APT0003
     */
    public String generateAppointmentNumber() {

        String sql =
                "SELECT MAX(appointment_id) "
                + "FROM appointments";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                int lastId = rs.getInt(1);

                if (lastId == 0) {
                    return "APT0001";
                }

                return String.format(
                        "APT%04d",
                        lastId + 1
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "APT0001";
    }

    // =====================================================
    // SEARCH APPOINTMENT
    // =====================================================

    /**
     * Searches an appointment using its appointment number.
     *
     * The query also returns patient_id, dentist_id and
     * treatment_id because these IDs are required when
     * updating an existing appointment.
     */
    public ResultSet searchAppointment(
            String appointmentNumber) {

        String sql =
                "SELECT "
                + "a.appointment_id, "
                + "a.appointment_number, "
                + "a.patient_id, "
                + "a.dentist_id, "
                + "a.treatment_id, "
                + "p.patient_name, "
                + "p.address, "
                + "p.contact_number, "
                + "d.dentist_name, "
                + "t.treatment_name, "
                + "a.appointment_date, "
                + "a.appointment_time "
                + "FROM appointments a "
                + "INNER JOIN patients p "
                + "ON a.patient_id = p.patient_id "
                + "INNER JOIN dentists d "
                + "ON a.dentist_id = d.dentist_id "
                + "INNER JOIN treatments t "
                + "ON a.treatment_id = t.treatment_id "
                + "WHERE a.appointment_number = ?";

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, appointmentNumber);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // UPDATE APPOINTMENT
    // =====================================================

    /**
     * Updates the existing appointment record.
     *
     * The appointment number is used as the unique identifier.
     * Patient ID is not changed because the patient record is
     * already connected to the appointment.
     */
    public boolean updateAppointment(
            String appointmentNumber,
            int dentistId,
            int treatmentId,
            Date date,
            Time time) {

        String sql =
                "UPDATE appointments SET "
                + "dentist_id = ?, "
                + "treatment_id = ?, "
                + "appointment_date = ?, "
                + "appointment_time = ? "
                + "WHERE appointment_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dentistId);
            ps.setInt(2, treatmentId);
            ps.setDate(3, date);
            ps.setTime(4, time);
            ps.setString(5, appointmentNumber);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // DELETE APPOINTMENT
    // =====================================================

    /**
     * Deletes an appointment using appointment number.
     */
    public boolean deleteAppointment(String appointmentNumber) {

        String sql =
                "DELETE FROM appointments "
                + "WHERE appointment_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, appointmentNumber);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    
/**
 * Returns all appointment numbers.
 *
 * This method is used by the billing screen
 * to populate the appointment number combo box.
 *
 * @return array containing appointment numbers
 */
public String[] getAllAppointmentNumbers() {

    String sql =
            "SELECT appointment_number "
            + "FROM appointments "
            + "ORDER BY appointment_id";

    java.util.ArrayList<String> numbers =
            new java.util.ArrayList<>();

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            numbers.add(
                    rs.getString("appointment_number")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return numbers.toArray(new String[0]);
}


}

