package fr.softeam.starpointsapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "Admin";

    public static final String LEADER = "Leader";

    public static final String USER = "Utilisateur";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
