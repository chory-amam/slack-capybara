package models;

import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class WordAnalyzerTest {
    @Test
    public void すべての文字が半角英数字かどうか() {
        final String allHalfNumeric = "my name is capybara";
        final String allHalfSymbols = "!!!!!!";
        final String mixHalfNumericAndHalfSymbols = "hi, my name is capybara!!";

        assertThat(WordAnalyzer.isAllHalfNumericAndSymbols(allHalfNumeric), is(true));
        assertThat(WordAnalyzer.isAllHalfNumericAndSymbols(allHalfSymbols), is(true));
        assertThat(WordAnalyzer.isAllHalfNumericAndSymbols(mixHalfNumericAndHalfSymbols), is(true));

        final String zenkaku = "かぴっ";
        assertThat(WordAnalyzer.isAllHalfNumericAndSymbols(zenkaku), is(false));
    }

    @Test
    public void wordにピリオドを意味する文字が入っているかどうか() {
        final String containPeriodword1 = "帰ります。";
        final String containPeriodword2 = "このカード使えたんだ！";
        final String containPeriodword3 = "ご飯食べに行きますか？";
        final String containPeriodword4 = "help, me !";
        final String containPeriodword5 = "where are you from ?";
        final String containPeriodword6 = "休日もう終わりか．．．";
        final String containPeriodword7 = "ぼくは友達がすくない...";
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword1), is(true));
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword2), is(true));
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword3), is(true));
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword4), is(true));
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword5), is(true));
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword6), is(true));
        assertThat(WordAnalyzer.isContainsPeriodWord(containPeriodword7), is(true));

        final String notContainsPeriodWord1 = "私は、ドラえもんです、";
        final String notContainsPeriodWord2 = "sazaede, gozaima-su";
        assertThat(WordAnalyzer.isContainsPeriodWord(notContainsPeriodWord1), is(false));
        assertThat(WordAnalyzer.isContainsPeriodWord(notContainsPeriodWord2), is(false));
    }

    @Test
    public void splitBySpecialSymbolが正常に動くこと() {
        {
            final String testString = "こんにちは！私は、ドラえもんです。";
            final String[] expectResult = {"こんにちは！", "私は、ドラえもんです。"};
            final String[] actualResult = WordAnalyzer.splitBySpecialSymbol(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }
    }

    @Test
    public void splitBySentenceEndが正常に動く事() {
        final String separator = System.getProperty("line.separator");
        {
            final String testString = "こんにちは。私は、ドラえもんです。";
            final String expectResult = "こんにちは。" + separator + "私は、ドラえもんです。" + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは！私は、ドラえもんです！";
            final String expectResult = "こんにちは！" + separator + "私は、ドラえもんです！" + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは!私は、ドラえもんです!";
            final String expectResult = "こんにちは!" + separator + "私は、ドラえもんです!" + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは？私は、ドラえもんです？";
            final String expectResult = "こんにちは？" + separator + "私は、ドラえもんです？" + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは?私は、ドラえもんです?";
            final String expectResult = "こんにちは?" + separator + "私は、ドラえもんです?" + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは.私は、ドラえもんです.";
            final String expectResult = "こんにちは." + separator + "私は、ドラえもんです." + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは．私は、ドラえもんです．";
            final String expectResult = "こんにちは．" + separator + "私は、ドラえもんです．" + separator;
            final String actualResult = WordAnalyzer.lineBreakByPeriod(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }
    }

    @Test
    public void lineBreakByParenthesisが正常に動く事() {
        {
            final String testString = "こんにちは。「私は」ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは。『私は』ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは。【私は】ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは。［私は］ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは。[私は]ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは。{私は}ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは。｛私は｝ドラえもんです。";
            final String expectResult = "こんにちは。ドラえもんです。";
            final String actualResult = WordAnalyzer.lineBreakByParenthesis(testString);
            Assert.assertThat(actualResult, is(expectResult));
        }
    }

    @Test
    public void lineBreakBySpaceが正常に動く事() {
        final String separator = System.getProperty("line.separator");
        {
            final String testString = "こんにちは 私は ドラえもんです ";
            final String expectResult = "こんにちは" + separator + "私は" + separator + "ドラえもんです" + separator;
            final String actualResult = WordAnalyzer.lineBreakBySpace(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }

        {
            final String testString = "こんにちは　私は　ドラえもんです　";
            final String expectResult = "こんにちは" + separator + "私は" + separator + "ドラえもんです" + separator;
            final String actualResult = WordAnalyzer.lineBreakBySpace(testString, separator);
            Assert.assertThat(actualResult, is(expectResult));
        }
    }
}
