package hello;

public enum UserTypes {

    REGULAR("Regular User"),
    ADMIN("Administrator");

    private String description;

    UserTypes(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}