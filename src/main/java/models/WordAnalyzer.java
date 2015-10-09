package models;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.google.common.collect.Lists;
import models.word.PeriodConst;

import java.util.List;

/**
 * kuromojiを使って解析したり
 * 文言チェックしたり
 */
public class WordAnalyzer {
	private static Tokenizer token = null;
	private static synchronized Tokenizer getTokenizer() {
		if (token == null) {
			final Tokenizer.Builder builder = new Tokenizer.Builder();
			builder.mode(Tokenizer.Mode.SEARCH);
			token = builder.build();
		}
		return token;
	}

	// 入力した文字列を使って解析し、文言ごとにスペースで区切る
	public static List<String> analyze(final String sentence) {

		Tokenizer search = getTokenizer();
		final List<Token> tokens = search.tokenize(sentence);
		final List<String> analiezed = Lists.newArrayList();
		for (final Token token : tokens) {
			final String word = token.getSurface();
			analiezed.add(word);
		}
		return analiezed;
	}

	public static boolean isAllHalfNumericAndSymbols(final String word) {
		return word != null && !word.isEmpty() && word.length() == word.getBytes().length;
	}
	public static boolean isContainsPeriodWord(final String word) {
		for (final String period : PeriodConst.period) {
			if (word.contains(period)) return true;
		}
		return false;
	}

	public static String[] splitBySentenceEnd(String sentence) {
		final String separator = System.getProperty("line.separator");
		for (final String period : PeriodConst.period) {
			 sentence =  sentence.replace(period, period + separator);
		}
		return sentence.split(separator);
	}
}
