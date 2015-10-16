package handlers;

import adapter.MockAdapter;
import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.HandlersTestUtils;
import utils.pattern.InvocationRegexPattern;
import utils.pattern.NotInvocationRegexPattern;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DiagnosticsHandlersTest {
    Botan botan;
    @Before
    public void setUp() throws BotanException {
        botan = new Botan.BotanBuilder()
                .setAdapter(new MockAdapter())
                .setBrain(new LocalBrain())
                .setMessageHandlers(new DiagnosticsHandlers())
                .build();
        botan.start();
    }

    @After
    public void tearDown() {
        botan.stop();
    }

    @Test
    public void handlersRegistrationTest() {
        assertThat(botan.getHandlers().size(), is(2));
    }
    @Test
    public void regex() {
        new HandlersTestUtils().regexTest(
                botan,
                Arrays.asList(
                        new InvocationRegexPattern("botan ping"),
                        new InvocationRegexPattern("botan PING"),
                        new InvocationRegexPattern("botan time"),
                        new InvocationRegexPattern("botan TIME"),
                        new NotInvocationRegexPattern("botan pingi"),
                        new NotInvocationRegexPattern("botan timefoo")
                )
        );
    }
    @Test
    public void time() {
        new MockUp<Date>() {
            @Mock
            @SuppressWarnings("unused")
            public String toString() {
                return "9999/12/31";
            }
        };
        new HandlersTestUtils().replyTest(botan, "botan time", "Server time is: 9999/12/31");
    }

    @Test
    public void ping() {
        new HandlersTestUtils().replyTest(botan, "botan ping", "pong");
        new HandlersTestUtils().replyTest(botan, "botan PING", "PONG");
    }
}
