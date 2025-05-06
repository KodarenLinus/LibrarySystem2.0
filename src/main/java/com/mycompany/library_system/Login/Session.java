package com.mycompany.library_system.Login;

/**
 * Singleton-klass som representerar en inloggad användares session.
 * Innehåller information som användarens ID, e-postadress och användartyp (kund eller personal).
 * Endast en instans av denna klass får existera under en session.
 * 
 * Används för att lagra inloggningsdata temporärt under programmets gång.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class Session {
    
    private static Session instance;

    // Användardata
    private int userId;
    private String email;
    private String role; // "customer" eller "staff"
    
    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // För kunder
    public void setCustomer(int userId, String email) {
        this.userId = userId;
        this.email = email;
        this.role = "customer";
    }

    // För personal
    public void setStaff(int userId, String email) {
        this.userId = userId;
        this.email = email;
        this.role = "staff";
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isStaff() {
        return "staff".equalsIgnoreCase(role);
    }

    public boolean isCustomer() {
        return "customer".equalsIgnoreCase(role);
    }

    public void clear() {
        instance = null;
    }
}
