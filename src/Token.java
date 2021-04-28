public class Token {
    private final String type;
    private final String id;

    public Token() {
        type = null;
        id = null;
    }

    public Token(String type, String id) {
        this.type = type.trim();
        this.id = id.trim();
    }

    public String get() {
        return type;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%-9s:%5s", get(), getID());
    }
}
