package model;

/**
 * Represents a bill generated for a dental appointment.
 */
public class Bill {

    private int billId;
    private String appointmentNumber;
    private double consultationFee;
    private double treatmentFee;
    private double totalAmount;

    public Bill() {
    }

    public Bill(
            String appointmentNumber,
            double consultationFee,
            double treatmentFee) {

        this.appointmentNumber = appointmentNumber;
        this.consultationFee = consultationFee;
        this.treatmentFee = treatmentFee;
        this.totalAmount = consultationFee + treatmentFee;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(String appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getTreatmentFee() {
        return treatmentFee;
    }

    public void setTreatmentFee(double treatmentFee) {
        this.treatmentFee = treatmentFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

