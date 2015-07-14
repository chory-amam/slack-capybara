package models;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(JMockit.class)
public class CapybaraTest {

	@SuppressWarnings("unused")
	@Mocked
	private Database database;

	@Test
	public void icon取得テスト() {
		final String result = Capybara.choiceIcon();
		assertNotNull(result);
	}

	@Test
	public void 発言されたかのチェック() {
		new Expectations() {
			{
				Database.pickSentence();
				times = 1;
			}
		};

		final Capybara capybara = new Capybara();
		assertNotNull(capybara.getWord());
	}
}
