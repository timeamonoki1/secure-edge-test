public class User {

    private String id;
    private String username;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }

        User rhs = (User) other;
        return (rhs.id.equals(this.id) && rhs.username.equals(this.username) && rhs.email.equals(this.email));
    }
}
