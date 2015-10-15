package handlers;

import com.github.masahitojp.botan.Botan;
import adapter.MockAdapter;
import com.github.masahitojp.botan.brain.LocalBrain;
import com.github.masahitojp.botan.exception.BotanException;

import com.github.masahitojp.botan.message.BotanMessage;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import handlers.cron.CronMessageHandlers;
import utils.RegexTestPattern;
import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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
    public void regexTest() {
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
                .stream()
                .forEach(pattern -> {
                    // before: set mock
                    final AtomicInteger times = new AtomicInteger();
                    MockUp<Consumer<BotanMessage>> spy = new MockUp<Consumer<BotanMessage>>() {
                        @Mock
                        @SuppressWarnings("unused")
                        public void accept(BotanMessage message) {
                            times.incrementAndGet();
                        }
                    };
                    botan.getHandlers()
                            .stream()
                            .filter(handler -> handler.getDescription().equals(pattern.getDescription()))
                            .forEach(handler -> handler.setHandle(spy.getMockInstance()));

                    // given: handlerが登録されている場合
                    // when: 規定のパターンのメッセージを受け取った時に
                    botan.receive(new BotanMessageSimple(pattern.getMessage()));
                    // then: 想定の回数分処理が呼ばれる
                    assertThat(times.get(), is(pattern.getTimes()));
                });
    }
    @Test
    public void MessageReplyTest() {
        // before: set mock for verify
        final AtomicReference<String> replayMessage = new AtomicReference<>();
        new MockUp<Random>() {
            @Mock
            @SuppressWarnings("unused")
            public int nextInt(int i) {
                return 1111;
            }
        };

        new MockUp<BotanMessage>(){
            @Mock
            @SuppressWarnings("unused")
            public void reply(String message) {
                replayMessage.set(message);
            }
        };

        // given: handlerが登録されている場合
        // when: 規定のパターンのメッセージを受け取った時に
        botan.receive(new BotanMessageSimple("botan job add \"* * * * *\" test"));
        // then: 想定されたメッセージが返信される
        assertThat(replayMessage.get(), is("1111"));
    }

    @Test
    public void jobListMessageReplyTest() {
        // before: set mock for verify
        final AtomicReference<String> replayMessage = new AtomicReference<>();

        new MockUp<BotanMessage>(){
            @Mock
            @SuppressWarnings("unused")
            public void reply(String message) {
                replayMessage.set(message);
            }
        };

        // given: handlerが登録されている場合
        // when: 規定のパターンのメッセージを受け取った時に
        botan.receive(new BotanMessageSimple("botan job ls"));
        // then: 想定されたメッセージが返信される
        assertThat(replayMessage.get(), is("no jobs"));
    }
}
