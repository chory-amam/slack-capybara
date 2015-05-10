package models;

import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(JMockit.class)
public class ConfigReaderTest {
	@Test
	public void port取得テスト() {
		final ConfigReader reader = ConfigReader.getInstance();
		assertThat(reader.getPort(), is(8080));
	}

	@Test
	public void データベースURIの取得テスト() {
		final ConfigReader reader = ConfigReader.getInstance();
		assertThat(reader.getDatabaseURI(), is("jdbc:h2:./db/h2/slack_capybara"));
	}

	@Test
	public void データベースIDの取得テスト() {
		final ConfigReader reader = ConfigReader.getInstance();
		assertThat(reader.getDatabaseId(), is("capybara"));
	}

	@Test
	public void データベースpasswordの取得テスト() {
		final ConfigReader reader = ConfigReader.getInstance();
		assertThat(reader.getDatabasePassword(), is("password"));
	}
}
