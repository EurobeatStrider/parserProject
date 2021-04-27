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

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%-9s:%5s", getType(), getId());
    }
}
