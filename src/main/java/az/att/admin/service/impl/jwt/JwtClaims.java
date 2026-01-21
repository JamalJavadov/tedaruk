package az.att.admin.service.impl.jwt;

public final class JwtClaims {

    private JwtClaims() {
        // prevent instantiation
    }

    public static final String USER_ID = "userId";
    public static final String PIN = "pin";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String SUBJECT = "sub";
    public static final String TIN = "tin";
    public static final String PERMISSIONS = "permissions";
}

