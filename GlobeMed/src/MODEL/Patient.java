package MODEL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient {

    private int patientId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String mobileNumber;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String city;

    public Patient() {
    }

    public Patient(int patientId, String firstName, String lastName, String dateOfBirth, String gender, String mobileNumber, String email, String addressLine1, String addressLine2, String postalCode, String city) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postalCode = postalCode;
        this.city = city;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Patient{"
                + "patientId=" + patientId
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", dateOfBirth='" + dateOfBirth + '\''
                + ", gender='" + gender + '\''
                + ", mobileNumber='" + mobileNumber + '\''
                + ", email='" + email + '\''
                + ", addressLine1='" + addressLine1 + '\''
                + ", addressLine2='" + addressLine2 + '\''
                + ", postalCode='" + postalCode + '\''
                + ", city='" + city + '\''
                + '}';
    }

    public static Patient fromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setDateOfBirth(rs.getString("date_of_birth"));
        patient.setGender(rs.getString("gender"));
        patient.setMobileNumber(rs.getString("phone"));
        patient.setEmail(rs.getString("email"));
        patient.setAddressLine1(rs.getString("line1"));
        patient.setAddressLine2(rs.getString("line2"));
        patient.setPostalCode(rs.getString("postal_code"));
        patient.setCity(rs.getString("city_name"));
        return patient;
    }
}
