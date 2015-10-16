package handlers;

import adapter.MockAdapter;
import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;
import handlers.misawa.MisawaHandlers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.HandlersTestUtils;
import utils.pattern.InvocationRegexPattern;
import utils.pattern.NotInvocationRegexPattern;
import utils.pattern.RegexTestPattern;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MisawaHandlersTest {
	Botan botan;
	@Before
	public void setUp() throws BotanException {
		botan = new Botan.BotanBuilder()
				.setAdapter(new MockAdapter())
				.setBrain(new LocalBrain())
				.setMessageHandlers(new MisawaHandlers())
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
						new InvocationRegexPattern("botan misawa"),
						new InvocationRegexPattern("botan MISAWA"),
						new NotInvocationRegexPattern("botan misawa-jigoku")
				)
		);
	}
}
