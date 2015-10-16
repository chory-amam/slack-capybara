package utils.pattern;

public final class InvocationRegexPattern extends RegexTestPattern{
	public InvocationRegexPattern(final String pattern) {
		super(pattern, 1);
	}

	public InvocationRegexPattern(final String description, final String pattern) {
		super(description, pattern, 1);
	}
}
