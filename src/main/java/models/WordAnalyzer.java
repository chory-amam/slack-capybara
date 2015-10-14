package models;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import models.word.SymbolConst;
import org.apache.commons.lang3.tuple.Pair;

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
        for (final String period : SymbolConst.PERIOD) {
            if (word.contains(period)) return true;
        }
        return false;
    }

    public static String[] splitBySpecialSymbol(String sentence) {
        final String separator = System.getProperty("line.separator");
        sentence = lineBreakByPeriod(sentence, separator);
        sentence = lineBreakBySpace(sentence, separator);
        sentence = lineBreakByParenthesis(sentence);
        return sentence.split(separator);
    }

    @VisibleForTesting
    public static String lineBreakBySpace(String sentence, String separator) {
        for (final String space : SymbolConst.SPACE) {
            sentence = sentence.replace(space, separator);
        }
        return sentence;
    }

    @VisibleForTesting
    public static String lineBreakByParenthesis(String sentence) {
        for (final Pair<String, String> parenthesis: SymbolConst.PARENTHESIS) {
            final String regex = parenthesis.getLeft() + ".*" + parenthesis.getRight();
            sentence = sentence.replaceAll(regex, "");
        }
        return sentence;
    }

    @VisibleForTesting
    public static String lineBreakByPeriod(String sentence, String separator) {
        for (final String period : SymbolConst.PERIOD) {
            sentence = sentence.replace(period, period + separator);
        }
        return sentence;
    }
}
