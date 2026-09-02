package dao;

import util.DBConnection;

import java.sql.*;

public class DentistDAO {

    public void loadDentists(
            javax.swing.JComboBox<String> comboBox) {

        String sql = "SELECT dentist_id, dentist_name "
                + "FROM dentists";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            comboBox.removeAllItems();

            comboBox.addItem("Select Dentist");

            while (rs.next()) {

                comboBox.addItem(
                        rs.getInt("dentist_id")
                        + " - "
                        + rs.getString("dentist_name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}