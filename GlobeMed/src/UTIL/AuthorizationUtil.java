package UTIL;

import PATTERN.ProxyPattern;

public class AuthorizationUtil {

    public static boolean hasPatientManagementAccess(String userRole) {
        ProxyPattern.PatientRecordProxy proxy = new ProxyPattern.PatientRecordProxy(userRole);
        return proxy.isAuthorized();
    }

    //check patient managing permission
    public static void checkPatientManagementAccess(String userRole) {
        if (!hasPatientManagementAccess(userRole)) {
            throw new SecurityException("Access denied: " + userRole
                    + " does not have permission to access Patient Management.");
        }
    }

}
