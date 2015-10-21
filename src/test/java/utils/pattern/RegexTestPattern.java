package utils.pattern;

public abstract class RegexTestPattern {
    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public int getInvocations() {
        return invocations;
    }

    private final String description;
    private final String message;
    private final int invocations;

    public RegexTestPattern(final String message) {
        this.description = "";
        this.message = message;
        this.invocations = 1;
    }

    public RegexTestPattern(final String message, final int invocations) {
        this.description = "";
        this.message = message;
        this.invocations = invocations;
    }

    public RegexTestPattern(final String description, final String message) {
        this.description = description;
        this.message = message;
        this.invocations = 1;
    }

    public RegexTestPattern(final String description, final String message, final int invocations) {
        this.description = description;
        this.message = message;
        this.invocations = invocations;
    }
}
