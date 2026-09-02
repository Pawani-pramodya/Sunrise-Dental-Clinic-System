package dao;

import model.Patient;
import util.DBConnection;

import java.sql.*;

public class PatientDAO {

    /**
     * Saves a new patient record into the patients table.
     *
     * @param patient patient information to be saved
     * @return generated patient ID, or -1 if the operation fails
     */
    public int savePatient(Patient patient) {

        String sql = "INSERT INTO patients "
                + "(patient_name, address, contact_number) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, patient.getPatientName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getContactNumber());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Updates an existing patient's personal information.
     *
     * @param patientId existing patient ID
     * @param patient updated patient information
     * @return true if the patient was updated successfully
     */
    public boolean updatePatient(
            int patientId,
            Patient patient) {

        String sql =
                "UPDATE patients SET "
                + "patient_name = ?, "
                + "address = ?, "
                + "contact_number = ? "
                + "WHERE patient_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patient.getPatientName());
            ps.setString(2, patient.getAddress());
            ps.setString(3, patient.getContactNumber());
            ps.setInt(4, patientId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}