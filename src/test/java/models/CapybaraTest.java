package models;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class CapybaraTest {

	@Test
	public void icon取得テスト(@Mocked ConfigReader reader) {
		new Expectations() {
			{
				reader.getBasicIcon(); times=1; result="basic";
				reader.getHappyIcon(); times=1; result="happy";
				reader.getAngryIcon(); times=1; result="angry";
				reader.getSadIcon(); times=1; result="sad";
				reader.getOneOthericon(); times=1; result="other";
			}
		};
		for (int i = 0; i <= 4; ++i) {
			final String result = Capybara.choiceIcon(i);
			assertNotNull(result);
		}
	}

	@Test
	public void icon番号取得テスト() {
		// 0から4の数字のどれかになること
		// 10回テストする
		for (int i=0; i < 10; ++i) {
			final int result = Capybara.getIconNumber();
			assertTrue(0 <= result);
			assertTrue(result <= 4);
		}
	}

	@Test
	public void 発言されたかのチェック(@Mocked Database database) {
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
