package models;

import com.google.common.collect.Lists;
import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.util.List;

/**
 * kuromojiを使って解析したり
 * 文言チェックしたり
 */
public class WordAnalyzer {

	// 入力した文字列を使って解析し、文言ごとにスペースで区切る
	public static List<String> analyze(final String sentence) {
		final Tokenizer.Builder builder = Tokenizer.builder();
		builder.mode(Tokenizer.Mode.SEARCH);
		Tokenizer search = builder.build();
		final List<Token> tokens = search.tokenize(sentence);
		final List<String> analiezed = Lists.newArrayList();
		for (final Token token : tokens) {
			final String word = token.getSurfaceForm();
			analiezed.add(word);
		}
		return analiezed;
	}

	public static boolean isAllHalfNumericAndSymbols(final String word) {
		return word != null && !word.isEmpty() && word.length() == word.getBytes().length;
	}
	public static boolean isContainsPeriodWord(final String word) {
		return word.contains("/。|！|？|!|?/");
	}
}
