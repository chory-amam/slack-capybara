package utils.pattern;

public final class NotInvocationRegexPattern extends RegexTestPattern{
	public NotInvocationRegexPattern(final String pattern) {
		super(pattern, 0);
	}
	public NotInvocationRegexPattern(final String description, final String pattern) {
		super(description, pattern, 0);
	}
}
