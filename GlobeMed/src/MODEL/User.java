package MODEL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private int roleRoleId;
    private String specialization;
    private String department;
    private int isActive;
    private String created_at;
    private Integer addressId;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private Integer cityId;
    private String cityName;

    private String roleName;

    public User() {
    }

    public User(int userId, String username, String password, String firstName, String lastName,
            String email, String mobileNumber, int roleRoleId, String specialization,
            String department, int isActive, String created_at) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.roleRoleId = roleRoleId;
        this.specialization = specialization;
        this.department = department;
        this.isActive = isActive;
        this.created_at = created_at;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getRoleRoleId() {
        return roleRoleId;
    }

    public void setRoleRoleId(int roleRoleId) {
        this.roleRoleId = roleRoleId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "User{"
                + "userId=" + userId
                + ", username='" + username + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", email='" + email + '\''
                + ", mobileNumber='" + mobileNumber + '\''
                + ", roleRoleId=" + roleRoleId
                + ", specialization='" + specialization + '\''
                + ", department='" + department + '\''
                + ", isActive=" + isActive
                + ", created_at='" + created_at + '\''
                + ", roleName='" + roleName + '\''
                + '}';
    }

    public static User fromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setMobileNumber(rs.getString("phone"));
        user.setRoleRoleId(rs.getInt("role_role_id"));
        user.setSpecialization(rs.getString("specialization"));
        user.setDepartment(rs.getString("department"));
        user.setIsActive(rs.getInt("is_active"));
        user.setCreated_at(rs.getString("created_at"));

        try {
            user.setRoleName(rs.getString("role_name"));
        } catch (SQLException e) {
            user.setRoleName("Unknown");
        }

        return user;
    }
}
