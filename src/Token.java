public class Token {
    private final String type;
    private final String id;

    private static final String toStringFormat = "%-9s:%20s";

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

    public static String toStringHeader(){
        StringBuilder header = new StringBuilder();
        header.append(String.format(toStringFormat, "Type","Value"));
        header.append("\n");
        return header.toString();
    }

    @Override
    public String toString() {
        return String.format(toStringFormat, getType(), getId());
    }
}
