package service;

import dao.AppointmentDAO;
import dao.BillDAO;
import dao.TreatmentDAO;
import model.Bill;

import java.sql.ResultSet;

/**
 * Service class responsible for billing business logic.
 */
public class BillingService {

    private AppointmentDAO appointmentDAO;
    private TreatmentDAO treatmentDAO;
    private BillDAO billDAO;

    private static final double CONSULTATION_FEE = 1000.00;

    /**
     * Creates DAO objects required for billing.
     */
    public BillingService() {

        appointmentDAO = new AppointmentDAO();
        treatmentDAO = new TreatmentDAO();
        billDAO = new BillDAO();
    }

    /**
     * Loads appointment information required for billing.
     *
     * @param appointmentNumber appointment number
     * @return ResultSet containing appointment details
     */
    public ResultSet getAppointmentDetails(
            String appointmentNumber) {

        return appointmentDAO.searchAppointment(
                appointmentNumber
        );
    }

    /**
     * Gets the treatment name using treatment ID.
     *
     * @param treatmentId treatment ID
     * @return treatment name
     */
    public String getTreatmentName(int treatmentId) {

        return treatmentDAO.getTreatmentNameById(
                treatmentId
        );
    }

    /**
     * Gets the treatment fee using treatment ID.
     *
     * @param treatmentId treatment ID
     * @return treatment fee
     */
    public double getTreatmentFee(int treatmentId) {

        return treatmentDAO.getTreatmentFeeById(
                treatmentId
        );
    }

    /**
     * Returns the standard consultation fee.
     *
     * @return consultation fee
     */
    public double getConsultationFee() {

        return CONSULTATION_FEE;
    }

    /**
     * Calculates the total bill amount.
     *
     * @param consultationFee consultation fee
     * @param treatmentFee treatment fee
     * @return total amount
     */
    public double calculateTotal(
            double consultationFee,
            double treatmentFee) {

        return consultationFee + treatmentFee;
    }

    /**
     * Saves a generated bill.
     *
     * @param appointmentNumber appointment number
     * @param consultationFee consultation fee
     * @param treatmentFee treatment fee
     * @return true if bill is saved
     */
    public boolean saveBill(
            String appointmentNumber,
            double consultationFee,
            double treatmentFee) {

        if (billDAO.billExists(appointmentNumber)) {
            return false;
        }

        Bill bill =
                new Bill(
                        appointmentNumber,
                        consultationFee,
                        treatmentFee
                );

        return billDAO.saveBill(bill);
    }
}

