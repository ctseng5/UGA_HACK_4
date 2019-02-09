package csx060.uga.edu.uga_hack_4_app;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String uid;

    /**
     * Constructor for User
     * @param firstName
     * @param lastName
     * @param email
     * @param uid
     */
    public User(String firstName, String lastName, String email, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uid = uid;
    }

    /**
     * Default constructor for user
     */
    public User () {
        firstName = "";
        lastName = "";
        email = "";
        uid = "";
    }

    /**
     * Creates a user with a UID
     * @param uid
     */
    public User(String uid){
        this.uid = uid;
    }

    /**
     * Gets the first name
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the UID
     * @return
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the UID
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
