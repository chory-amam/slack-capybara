package models;

import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class DatabaseTest {
	@Test
	public void initializeで例外が発生しないこと() throws Exception {
		Database.initialize();
	}
}
