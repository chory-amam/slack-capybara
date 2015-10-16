package handlers;

import adapter.MockAdapter;
import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.HandlersTestUtils;
import utils.pattern.InvocationRegexPattern;
import utils.pattern.NotInvocationRegexPattern;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HelpHandlersTest {
	Botan botan;
	@Before
	public void setUp() throws BotanException {
		botan = new Botan.BotanBuilder()
				.setAdapter(new MockAdapter())
				.setBrain(new LocalBrain())
				.setMessageHandlers(new HelpHandlers())
				.build();
		botan.start();
	}

	@After
	public void tearDown() {
		botan.stop();
	}

	@Test
	public void handlersRegistrationTest() {
		assertThat(botan.getHandlers().size(), is(1));
	}
	@Test
	public void regex() {
		new HandlersTestUtils().regexTest(
				botan,
				Arrays.asList(
						new InvocationRegexPattern("botan help"),
						new NotInvocationRegexPattern("botan helpfoo")
				)
		);
	}
	@Test
	public void help() {
		new HandlersTestUtils().replyTest(botan, "botan help", "> botan help\\z - Displays all of the commands.");
	}
}
