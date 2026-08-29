package Controller;

import service.BillingService;

public class BillingController {

    private final BillingService billingService;

    public BillingController() {
        this.billingService = new BillingService();
    }

    // Input validations before generating or saving bills
    public String validateBillingInputs(String appointmentNumber, double treatmentFee) {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return "Appointment number is required";
        }
        if (treatmentFee < 0) {
            return "Treatment fee cannot be negative";
        }
        return "VALID";
    }

    // Calculates the total invoice amount including fixed consultation fee
    public double calculateTotalAmount(double treatmentFee) {
        double consultationFee = billingService.getConsultationFee();
        return billingService.calculateTotal(consultationFee, treatmentFee);
    }

    // Retrieves treatment fee from DB
    public double getTreatmentFeeFromDB(int treatmentId) {
        return billingService.getTreatmentFee(treatmentId);
    }

    // Validates inputs and persists the bill into MySQL database
    public String generateAndSaveBill(String appointmentNumber, double treatmentFee) {
        String validation = validateBillingInputs(appointmentNumber, treatmentFee);
        if (!validation.equals("VALID")) {
            return validation;
        }

        double consultationFee = billingService.getConsultationFee();
        boolean saved = billingService.saveBill(appointmentNumber, consultationFee, treatmentFee);

        if (saved) {
            return "BILL_GENERATED_SUCCESS";
        } else {
            return "BILL_EXISTS_OR_FAILED";
        }
    }
}