package CONTROLLER;

import MODEL.MySQL;
import MODEL.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class StaffController {

    //find all users
    public List<User> getAllStaff() {
        List<User> staff = new ArrayList<>();
        try {
            ResultSet rs = MySQL.search("SELECT u.*, r.role_name FROM users u JOIN role r ON u.role_role_id = r.role_id ORDER BY u.first_name");
            while (rs.next()) {
                User user = User.fromResultSet(rs);
                user.setRoleName(rs.getString("role_name"));
                staff.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }

    //find user by id
    public User getStaffById(int userId) {
        User user = null;
        try {
            ResultSet rs = MySQL.search("SELECT u.*, r.role_name FROM users u JOIN role r ON u.role_role_id = r.role_id WHERE u.user_id = ?", userId);
            if (rs.next()) {
                user = User.fromResultSet(rs);
                user.setRoleName(rs.getString("role_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    //add new user
    public boolean addStaff(User user) {
        try {
            String hashedPassword = hashPassword(user.getPassword());
            if (hashedPassword == null) {
                return false;
            }

            // First, handle address insertion if address data is provided
            Integer addressId = null;
            if (user.getAddressLine1() != null && !user.getAddressLine1().isEmpty()
                    && user.getCityId() > 0) {

                // Insert into address table
                String addressQuery = "INSERT INTO address (line1, line2, postal_code, city_city_id) VALUES (?, ?, ?, ?)";
                MySQL.iud(addressQuery, user.getAddressLine1(), user.getAddressLine2(),
                        user.getPostalCode(), user.getCityId());

                // Get the generated address ID
                ResultSet rs = MySQL.search("SELECT LAST_INSERT_ID() as address_id");
                if (rs.next()) {
                    addressId = rs.getInt("address_id");
                }
            }

            // Insert into users table
            String query = "INSERT INTO users (username, password, first_name, last_name, email, phone, "
                    + "role_role_id, specialization, department, is_active, created_at, address_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            MySQL.iud(query, user.getUsername(), hashedPassword, user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getMobileNumber(), user.getRoleRoleId(), user.getSpecialization(),
                    user.getDepartment(), user.getIsActive(), user.getCreated_at(), addressId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //update a user
    public boolean updateStaff(User user) {
        try {
            // Handle address update or creation
            Integer addressId = user.getAddressId();

            if (user.getAddressLine1() != null && !user.getAddressLine1().isEmpty()
                    && user.getCityId() > 0) {

                if (addressId != null && addressId > 0) {
                    // Update existing address
                    String addressQuery = "UPDATE address SET line1 = ?, line2 = ?, postal_code = ?, city_city_id = ? WHERE address_id = ?";
                    MySQL.iud(addressQuery, user.getAddressLine1(), user.getAddressLine2(),
                            user.getPostalCode(), user.getCityId(), addressId);
                } else {
                    // Insert new address
                    String addressQuery = "INSERT INTO address (line1, line2, postal_code, city_city_id) VALUES (?, ?, ?, ?)";
                    MySQL.iud(addressQuery, user.getAddressLine1(), user.getAddressLine2(),
                            user.getPostalCode(), user.getCityId());

                    // Get the generated address ID
                    ResultSet rs = MySQL.search("SELECT LAST_INSERT_ID() as address_id");
                    if (rs.next()) {
                        addressId = rs.getInt("address_id");
                    }
                }
            }

            // Update user information
            String query;
            Object[] params;

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String hashedPassword = hashPassword(user.getPassword());
                if (hashedPassword == null) {
                    return false;
                }

                query = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, "
                        + "email = ?, phone = ?, role_role_id = ?, specialization = ?, department = ?, "
                        + "is_active = ?, created_at = ?, address_id = ? WHERE user_id = ?";
                params = new Object[]{user.getUsername(), hashedPassword, user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getMobileNumber(), user.getRoleRoleId(), user.getSpecialization(),
                    user.getDepartment(), user.getIsActive(), user.getCreated_at(), addressId, user.getUserId()};
            } else {
                query = "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ?, "
                        + "phone = ?, role_role_id = ?, specialization = ?, department = ?, is_active = ?, "
                        + "created_at = ?, address_id = ? WHERE user_id = ?";
                params = new Object[]{user.getUsername(), user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getMobileNumber(), user.getRoleRoleId(), user.getSpecialization(),
                    user.getDepartment(), user.getIsActive(), user.getCreated_at(), addressId, user.getUserId()};
            }

            MySQL.iud(query, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete a user
    public boolean deleteStaff(int userId) {
        try {
            String query = "DELETE FROM users WHERE user_id = ?";
            MySQL.iud(query, userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //authentication login user
    public User authenticateUser(String username, String plainPassword) {
        User user = null;
        try {
            ResultSet rs = MySQL.search("SELECT u.*, r.role_name FROM users u JOIN role r ON u.role_role_id = r.role_id WHERE u.username = ? AND u.is_active = 1",
                    username);
            if (rs.next()) {
                user = User.fromResultSet(rs);
                user.setRoleName(rs.getString("role_name"));

                String storedHash = rs.getString("password");

                if (!verifyPassword(plainPassword, storedHash)) {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // Password hashing
    private String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verify password
    private boolean verifyPassword(String inputPassword, String storedHash) {
        try {
            byte[] combined = Base64.getDecoder().decode(storedHash);

            byte[] salt = new byte[16];
            byte[] storedHashedPassword = new byte[combined.length - 16];
            System.arraycopy(combined, 0, salt, 0, 16);
            System.arraycopy(combined, 16, storedHashedPassword, 0, storedHashedPassword.length);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] inputHashedPassword = md.digest(inputPassword.getBytes());

            return MessageDigest.isEqual(inputHashedPassword, storedHashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    //find city by id
    public int getCityIdByName(String cityName) {
        try {
            ResultSet rs = MySQL.search("SELECT city_id FROM city WHERE city_name = ?", cityName);
            if (rs.next()) {
                return rs.getInt("city_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
