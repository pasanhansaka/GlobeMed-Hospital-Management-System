package CONTROLLER;

import MODEL.Bill;
import MODEL.InsuranceClaim;
import MODEL.MySQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillingController {

    //find all bills
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT * FROM bills ORDER BY billing_date DESC");
            while (rs.next()) {
                bills.add(Bill.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bills;
    }

    //find a bill by id
    public Bill getBillById(int billId) {
        Bill bill = null;
        try {
            ResultSet rs = MySQL.search("SELECT * FROM bills WHERE bill_id = ?", billId);
            if (rs.next()) {
                bill = Bill.fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bill;
    }

    //add new bill
    public boolean addBill(Bill bill) {
        try {
            if (bill.getPatientId() <= 0 || bill.getTotalAmount() == 0) {
                return false;
            }

            String query = "INSERT INTO bills (patient_id, appointment_id, total_amount, insurance_coverage, patient_payment, status, billing_date, due_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            MySQL.iud(query, bill.getPatientId(), bill.getAppointmentId(), bill.getTotalAmount(),
                    bill.getInsuranceCoverage(), bill.getPatientPayment(), bill.getStatus(),
                    bill.getBillingDate(), bill.getDueDate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //update a existing bill
    public boolean updateBill(Bill bill) {
        try {
            String query = "UPDATE bills SET patient_id = ?, appointment_id = ?, total_amount = ?, insurance_coverage = ?, patient_payment = ?, status = ?, billing_date = ?, due_date = ? WHERE bill_id = ?";
            MySQL.iud(query, bill.getPatientId(), bill.getAppointmentId(), bill.getTotalAmount(),
                    bill.getInsuranceCoverage(), bill.getPatientPayment(), bill.getStatus(),
                    bill.getBillingDate(), bill.getDueDate(), bill.getBillId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a bill
    public boolean deleteBill(int billId) {
        try {
            String query = "DELETE FROM bills WHERE bill_id = ?";
            MySQL.iud(query, billId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //find all claims
    public List<InsuranceClaim> getAllInsuranceClaims() {
        List<InsuranceClaim> claims = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT * FROM insurance_claims ORDER BY submission_date DESC");
            while (rs.next()) {
                claims.add(InsuranceClaim.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    //find a claim by id
    public InsuranceClaim getClaimById(int claimId) {
        InsuranceClaim claim = null;
        try {
            ResultSet rs = MySQL.search("SELECT * FROM insurance_claims WHERE claim_id = ?", claimId);
            if (rs.next()) {
                claim = InsuranceClaim.fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claim;
    }

    //find claim by bill id
    public List<InsuranceClaim> getClaimsByBillId(int billId) {
        List<InsuranceClaim> claims = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT * FROM insurance_claims WHERE bill_id = ?", billId);
            while (rs.next()) {
                claims.add(InsuranceClaim.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    //add new claim
    public boolean addInsuranceClaim(InsuranceClaim claim) {
        try {
            String query = "INSERT INTO insurance_claims (bill_id, insurance_provider, claim_amount, status, submission_date) VALUES (?, ?, ?, ?, ?)";
            MySQL.iud(query, claim.getBillId(), claim.getInsuranceProvider(), claim.getClaimAmount(),
                    claim.getStatus(), claim.getSubmissionDate());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //update existing claim
    public boolean updateInsuranceClaim(InsuranceClaim claim) {
        try {
            String query = "UPDATE insurance_claims SET bill_id = ?, insurance_provider = ?, claim_amount = ?, status = ?, submission_date = ? WHERE claim_id = ?";
            MySQL.iud(query, claim.getBillId(), claim.getInsuranceProvider(), claim.getClaimAmount(),
                    claim.getStatus(), claim.getSubmissionDate(), claim.getClaimId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a claim
    public boolean deleteInsuranceClaim(int claimId) {
        try {
            String query = "DELETE FROM insurance_claims WHERE claim_id = ?";
            MySQL.iud(query, claimId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
