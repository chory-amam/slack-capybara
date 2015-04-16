package models;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.util.List;

/**
 * kuromojiを使って解析するクラス
 */
public class WordAnalyzer {

	// 入力した文字列を使って解析し、文言ごとにスペースで区切る
	public static String analyze(final String sentence) {
		final Tokenizer.Builder builder = Tokenizer.builder();
		builder.mode(Tokenizer.Mode.SEARCH);
		Tokenizer search = builder.build();

		String result = "";
		int count = 1;
		final List<Token> tokens = search.tokenize(sentence);
		for(final Token token : tokens) {
			if (tokens.size() == count) {
				result += token.getSurfaceForm();
			} else {
				result += token.getSurfaceForm() + "¥s";
			}
			++count;
		}
		return result;
	}
}
