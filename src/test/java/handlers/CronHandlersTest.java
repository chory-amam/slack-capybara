package handlers;

import com.github.masahitojp.botan.Botan;
import adapter.MockAdapter;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;

import handlers.cron.CronMessageHandlers;
import utils.HandlersTestUtils;
import utils.RegexTestPattern;
import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CronHandlersTest {
    Botan botan;
    @Before
    public void startUp() throws BotanException {
        botan = new Botan.BotanBuilder()
                .setAdapter(new MockAdapter())
                .setBrain(new LocalBrain())
                .setMessageHandlers(new CronMessageHandlers())
                .build();
        botan.start();
    }

    @After
    public void tearDown() {
        botan.stop();
    }

    @Test
    public void handlersRegistrationTest() {
        assertThat(botan.getHandlers().size(), is(3));
    }
    @Test
    public void regex() {
        new HandlersTestUtils().regexTest(
                botan,
                Arrays.asList(
                        new RegexTestPattern(CronMessageHandlers.JOB_ADD_DESCRIPTION, "botan job add \"* * * * *\" test"),
                        new RegexTestPattern(CronMessageHandlers.JOB_ADD_DESCRIPTION, "botan job new \"* * * * *\" test"),
                        new RegexTestPattern(CronMessageHandlers.JOB_LIST_DESCRIPTION, "botan job ls"),
                        new RegexTestPattern(CronMessageHandlers.JOB_LIST_DESCRIPTION, "botan job list"),
                        new RegexTestPattern(CronMessageHandlers.JOB_RM_DESCRIPTION, "botan job rm 1234"),
                        new RegexTestPattern(CronMessageHandlers.JOB_RM_DESCRIPTION, "botan job remove 1234"),
                        new RegexTestPattern(CronMessageHandlers.JOB_RM_DESCRIPTION, "botan job del 1234"),
                        new RegexTestPattern(CronMessageHandlers.JOB_RM_DESCRIPTION, "botan job delete 1234")
                )
        );
    }

    @Test
    public void noJobList() {
        new HandlersTestUtils().replyTest(botan, "botan job ls", "no jobs");
    }

    @Test
    public void jobAddAndDel() {

        new MockUp<Random>() {
            @Mock
            @SuppressWarnings("unused")
            public int nextInt(int i) {
                return 1111;
            }
        };

        // success
        new HandlersTestUtils().replyTest(botan, "botan job add \"* * * * *\" test", "1111");
        // register failed
        new HandlersTestUtils().replyTest(botan, "botan job add \"* * * *\" test", "job register failed:invalid pattern: \"* * * *\"");
        // list
        new HandlersTestUtils().replyTest(botan, "botan job ls", "1111: \"* * * * *\" test\n");
        // del failed
        new HandlersTestUtils().replyTest(botan, "botan job del 2222", "job rm failed: id 2222 not found");
        // del success
        new HandlersTestUtils().replyTest(botan, "botan job del 1111", "job rm successful");
    }


}
