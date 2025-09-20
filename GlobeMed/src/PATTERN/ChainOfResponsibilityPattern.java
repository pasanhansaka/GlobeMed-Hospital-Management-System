package PATTERN;

import CONTROLLER.BillingController;
import MODEL.Bill;
import MODEL.InsuranceClaim;

public class ChainOfResponsibilityPattern {

    // Abstract handler class
    public interface ClaimHandler {

        void setNext(ClaimHandler next);

        boolean handleClaim(InsuranceClaim claim);
    }

    // Concrete handler for validating claim amount
    public static class AmountValidator implements ClaimHandler {

        private ClaimHandler next;

        @Override
        public void setNext(ClaimHandler next) {
            this.next = next;
        }

        @Override
        public boolean handleClaim(InsuranceClaim claim) {
            if (claim.getClaimAmount() <= 0) {
                System.out.println("Claim amount validation failed: Amount must be greater than zero.");
                return false;
            }
            if (next != null) {
                return next.handleClaim(claim);
            }
            return true;
        }
    }

    // Concrete handler for checking insurance provider
    public static class ProviderValidator implements ClaimHandler {

        private ClaimHandler next;

        @Override
        public void setNext(ClaimHandler next) {
            this.next = next;
        }

        @Override
        public boolean handleClaim(InsuranceClaim claim) {
            if (claim.getInsuranceProvider() == null || claim.getInsuranceProvider().trim().isEmpty()) {
                System.out.println("Insurance provider validation failed: Provider cannot be empty.");
                return false;
            }
            if (next != null) {
                return next.handleClaim(claim);
            }
            return true;
        }
    }

    // Concrete handler for checking bill existence
    public static class BillExistenceChecker implements ClaimHandler {

        private ClaimHandler next;
        private BillingController billingController;

        public BillExistenceChecker(BillingController billingController) {
            this.billingController = billingController;
        }

        @Override
        public void setNext(ClaimHandler next) {
            this.next = next;
        }

        @Override
        public boolean handleClaim(InsuranceClaim claim) {
            Bill bill = billingController.getBillById(claim.getBillId());
            if (bill == null) {
                System.out.println("Bill existence check failed: Bill ID " + claim.getBillId() + " does not exist.");
                return false;
            }
            if (next != null) {
                return next.handleClaim(claim);
            }
            return true;
        }
    }

    // Concrete handler for processing claim
    public static class ClaimProcessorHandler implements ClaimHandler {

        private ClaimHandler next;
        private BillingController billingController;

        public ClaimProcessorHandler(BillingController billingController) {
            this.billingController = billingController;
        }

        @Override
        public void setNext(ClaimHandler next) {
            this.next = next;
        }

        @Override
        public boolean handleClaim(InsuranceClaim claim) {
            claim.setStatus("Processing");

            System.out.println("Processing claim for bill ID: " + claim.getBillId());

            if (next != null) {
                return next.handleClaim(claim);
            }
            return true;
        }
    }

    public static class ClaimProcessingChain {

        private ClaimHandler chain;

        public ClaimProcessingChain(BillingController billingController) {
            // Build the chain of responsibility
            AmountValidator amountValidator = new AmountValidator();
            ProviderValidator providerValidator = new ProviderValidator();
            BillExistenceChecker billExistenceChecker = new BillExistenceChecker(billingController);
            ClaimProcessorHandler claimProcessor = new ClaimProcessorHandler(billingController);

            amountValidator.setNext(providerValidator);
            providerValidator.setNext(billExistenceChecker);
            billExistenceChecker.setNext(claimProcessor);

            this.chain = amountValidator;
        }

        public boolean processClaim(InsuranceClaim claim) {
            return chain.handleClaim(claim);
        }
    }
}
