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
import utils.RegexTestPattern;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DiagnosticsHandlersTest {
    Botan botan;
    @Before
    public void startUp() throws BotanException {
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
                        new RegexTestPattern(DiagnosticsHandlers.PING_DESCRIPTION, "botan ping"),
                        new RegexTestPattern(DiagnosticsHandlers.PING_DESCRIPTION, "botan PING"),
                        new RegexTestPattern(DiagnosticsHandlers.TIME_DESCRIPTION, "botan time"),
                        new RegexTestPattern(DiagnosticsHandlers.TIME_DESCRIPTION, "botan TIME"),
                        new RegexTestPattern(DiagnosticsHandlers.PING_DESCRIPTION, "botan pingi", 0),
                        new RegexTestPattern(DiagnosticsHandlers.TIME_DESCRIPTION, "botan timefoo", 0)
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
