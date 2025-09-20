package MODEL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InsuranceClaim {

    private int claimId;
    private int billId;
    private String insuranceProvider;
    private double claimAmount;
    private String status;
    private String submissionDate;
    private String processedDate;
    private String notes;

    public InsuranceClaim() {
    }

    public InsuranceClaim(int claimId, int billId, String insuranceProvider, double claimAmount, String status, String submissionDate) {
        this.claimId = claimId;
        this.billId = billId;
        this.insuranceProvider = insuranceProvider;
        this.claimAmount = claimAmount;
        this.status = status;
        this.submissionDate = submissionDate;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "InsuranceClaim{"
                + "claimId=" + claimId
                + ", billId=" + billId
                + ", insuranceProvider='" + insuranceProvider + '\''
                + ", claimAmount=" + claimAmount
                + ", status='" + status + '\''
                + ", submissionDate='" + submissionDate + '\''
                + ", processedDate='" + processedDate + '\''
                + ", notes='" + notes + '\''
                + '}';
    }

    public static InsuranceClaim fromResultSet(ResultSet rs) throws SQLException {
        InsuranceClaim claim = new InsuranceClaim();
        claim.setClaimId(rs.getInt("claim_id"));
        claim.setBillId(rs.getInt("bill_id"));
        claim.setInsuranceProvider(rs.getString("insurance_provider"));
        claim.setClaimAmount(rs.getDouble("claim_amount"));
        claim.setStatus(rs.getString("status"));
        claim.setSubmissionDate(rs.getString("submission_date"));

        try {
            claim.setProcessedDate(rs.getString("processed_date"));
            claim.setNotes(rs.getString("notes"));
        } catch (SQLException e) {
        }

        return claim;
    }

}
