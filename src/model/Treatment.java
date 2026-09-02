package model;

/**
 * Represents a treatment offered by the dental clinic.
 */
public class Treatment {

    private int treatmentId;
    private String treatmentName;
    private double treatmentFee;

    public Treatment() {
    }

    public Treatment(
            int treatmentId,
            String treatmentName,
            double treatmentFee) {

        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.treatmentFee = treatmentFee;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public double getTreatmentFee() {
        return treatmentFee;
    }

    public void setTreatmentFee(double treatmentFee) {
        this.treatmentFee = treatmentFee;
    }

    @Override
    public String toString() {
        return treatmentName;
    }
}