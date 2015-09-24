package models;

import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;

@RunWith(JMockit.class)
public class DatabaseTest {
	@Test
	public void initializeで例外が発生しないこと() throws Exception {
		Database.initialize();
	}

	@Test
	public void splitBySentenceEndが正常に動く事() {
		{
			final String testString = "こんにちは。私は、ドラえもんです。";
			final String[] expectResult = {"こんにちは。", "私は、ドラえもんです。"};
			final String[] actualResult = Database.splitBySentenceEnd(testString);
			Assert.assertThat(actualResult, is(expectResult));
		}

		{
			final String testString = "こんにちは！私は、ドラえもんです！";
			final String[] expectResult = {"こんにちは！", "私は、ドラえもんです！"};
			final String[] actualResult = Database.splitBySentenceEnd(testString);
			Assert.assertThat(actualResult, is(expectResult));
		}

		{
			final String testString = "こんにちは!私は、ドラえもんです!";
			final String[] expectResult = {"こんにちは!", "私は、ドラえもんです!"};
			final String[] actualResult = Database.splitBySentenceEnd(testString);
			Assert.assertThat(actualResult, is(expectResult));
		}

		{
			final String testString = "こんにちは？私は、ドラえもんです？";
			final String[] expectResult = {"こんにちは？", "私は、ドラえもんです？"};
			final String[] actualResult = Database.splitBySentenceEnd(testString);
			Assert.assertThat(actualResult, is(expectResult));
		}

		{
			final String testString = "こんにちは?私は、ドラえもんです?";
			final String[] expectResult = {"こんにちは?", "私は、ドラえもんです?"};
			final String[] actualResult = Database.splitBySentenceEnd(testString);
			Assert.assertThat(actualResult, is(expectResult));
		}
	}
}
