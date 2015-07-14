package models.word;

public class Relation {
	public String followWord;
	public boolean isLast;
	Relation(final String followWord, final boolean isLast) {
		this.followWord = followWord;
		this.isLast = isLast;
	}
}
