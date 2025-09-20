package PATTERN;

public class DecoratorPattern {

    // Abstract component interface
    public interface UserComponent {

        String getPermissions();

        boolean canViewPatientRecords();

        boolean canScheduleAppointments();

        boolean canAccessBilling();

        boolean canPrescribeMedication();

        boolean canGenerateReports();

        String getUsername();

        String getFirstName();

        String getLastName();

        String getEmail();
    }

    // Concrete component - Basic User
    public static class BasicUser implements UserComponent {

        private String username;
        private String firstName;
        private String lastName;
        private String email;

        public BasicUser(String username, String firstName, String lastName, String email) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        @Override
        public String getPermissions() {
            return "Basic User";
        }

        @Override
        public boolean canViewPatientRecords() {
            return false;
        }

        @Override
        public boolean canScheduleAppointments() {
            return false;
        }

        @Override
        public boolean canAccessBilling() {
            return false;
        }

        @Override
        public boolean canPrescribeMedication() {
            return false;
        }

        @Override
        public boolean canGenerateReports() {
            return false;
        }

        @Override
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // Abstract decorator class
    public static abstract class UserDecorator implements UserComponent {

        protected UserComponent userComponent;

        public UserDecorator(UserComponent userComponent) {
            this.userComponent = userComponent;
        }

        @Override
        public String getPermissions() {
            return userComponent.getPermissions();
        }

        @Override
        public boolean canViewPatientRecords() {
            return userComponent.canViewPatientRecords();
        }

        @Override
        public boolean canScheduleAppointments() {
            return userComponent.canScheduleAppointments();
        }

        @Override
        public boolean canAccessBilling() {
            return userComponent.canAccessBilling();
        }

        @Override
        public boolean canPrescribeMedication() {
            return userComponent.canPrescribeMedication();
        }

        @Override
        public boolean canGenerateReports() {
            return userComponent.canGenerateReports();
        }

        @Override
        public String getUsername() {
            return userComponent.getUsername();
        }

        @Override
        public String getFirstName() {
            return userComponent.getFirstName();
        }

        @Override
        public String getLastName() {
            return userComponent.getLastName();
        }

        @Override
        public String getEmail() {
            return userComponent.getEmail();
        }
    }

    // Concrete decorators for different roles
    // Doctor decorator
    public static class DoctorDecorator extends UserDecorator {

        public DoctorDecorator(UserComponent userComponent) {
            super(userComponent);
        }

        @Override
        public String getPermissions() {
            return userComponent.getPermissions() + ", Doctor";
        }

        @Override
        public boolean canViewPatientRecords() {
            return true;
        }

        @Override
        public boolean canScheduleAppointments() {
            return true;
        }

        @Override
        public boolean canPrescribeMedication() {
            return true;
        }

        @Override
        public boolean canGenerateReports() {
            return true;
        }
    }

    // Nurse decorator
    public static class NurseDecorator extends UserDecorator {

        public NurseDecorator(UserComponent userComponent) {
            super(userComponent);
        }

        @Override
        public String getPermissions() {
            return userComponent.getPermissions() + ", Nurse";
        }

        @Override
        public boolean canViewPatientRecords() {
            return true;
        }

        @Override
        public boolean canScheduleAppointments() {
            return true;
        }

        @Override
        public boolean canGenerateReports() {
            return true;
        }
    }

    // Pharmacist decorator
    public static class PharmacistDecorator extends UserDecorator {

        public PharmacistDecorator(UserComponent userComponent) {
            super(userComponent);
        }

        @Override
        public String getPermissions() {
            return userComponent.getPermissions() + ", Pharmacist";
        }

        @Override
        public boolean canViewPatientRecords() {
            return true;
        }

        @Override
        public boolean canPrescribeMedication() {
            return true;
        }
    }

    // Billing Specialist decorator
    public static class BillingSpecialistDecorator extends UserDecorator {

        public BillingSpecialistDecorator(UserComponent userComponent) {
            super(userComponent);
        }

        @Override
        public String getPermissions() {
            return userComponent.getPermissions() + ", Billing Specialist";
        }

        @Override
        public boolean canAccessBilling() {
            return true;
        }

        @Override
        public boolean canGenerateReports() {
            return true;
        }
    }

    // Administrator decorator
    public static class AdminDecorator extends UserDecorator {

        public AdminDecorator(UserComponent userComponent) {
            super(userComponent);
        }

        @Override
        public String getPermissions() {
            return userComponent.getPermissions() + ", Administrator";
        }

        @Override
        public boolean canViewPatientRecords() {
            return true;
        }

        @Override
        public boolean canScheduleAppointments() {
            return true;
        }

        @Override
        public boolean canAccessBilling() {
            return true;
        }

        @Override
        public boolean canPrescribeMedication() {
            return true;
        }

        @Override
        public boolean canGenerateReports() {
            return true;
        }
    }
}
