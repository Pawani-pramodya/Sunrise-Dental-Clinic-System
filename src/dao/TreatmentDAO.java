package dao;

import util.DBConnection;

import java.sql.*;

public class TreatmentDAO {

    public void loadTreatments(
            javax.swing.JComboBox<String> comboBox) {

        String sql = "SELECT treatment_id, treatment_name "
                + "FROM treatments";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            comboBox.removeAllItems();

            comboBox.addItem("Select Treatment");

            while (rs.next()) {

                comboBox.addItem(
                        rs.getInt("treatment_id")
                        + " - "
                        + rs.getString("treatment_name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

/**
 * Gets the treatment name using treatment ID.
 *
 * @param treatmentId treatment ID
 * @return treatment name
 */
public String getTreatmentNameById(int treatmentId) {

    String sql =
            "SELECT treatment_name "
            + "FROM treatments "
            + "WHERE treatment_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, treatmentId);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("treatment_name");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return "";
}


/**
 * Gets the treatment fee using treatment ID.
 *
 * @param treatmentId treatment ID
 * @return treatment fee
 */
public double getTreatmentFeeById(int treatmentId) {

    String sql =
            "SELECT treatment_fee "
            + "FROM treatments "
            + "WHERE treatment_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, treatmentId);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("treatment_fee");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0.0;
}


}