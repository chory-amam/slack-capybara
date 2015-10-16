package utils;

import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.message.BotanMessage;
import com.github.masahitojp.botan.message.BotanMessageSimple;
import mockit.Mock;
import mockit.MockUp;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * HandlersのUnitTest用のUtilityクラス
 */
public class HandlersTestUtils {
	public HandlersTestUtils() {
	}

	/**
	 * Handlersで登録した正規表現文字列パターンで処理が反応することのテスト
	 * @param botan Botan本体
	 * @param list チェック対象
	 */
	public void regexTest(final Botan botan, final List<RegexTestPattern> list) {

		Collections.unmodifiableList(list)
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
					assertThat(times.get(), is(pattern.getInvocations()));
				});
	}

	public void replyTest(final Botan botan, final String receivedMessage, final String replyMessage) {
		// before: set mock for verify
		final AtomicReference<String> replayMessage = new AtomicReference<>();

		new MockUp<BotanMessage>() {
			@Mock
			@SuppressWarnings("unused")
			public void reply(String message) {
				replayMessage.set(message);
			}
		};

		// given: handlerが登録されている場合
		// when: 規定のパターンのメッセージを受け取った時に
		botan.receive(new BotanMessageSimple(receivedMessage));
		// then: 想定されたメッセージが返信される
		assertThat(replayMessage.get(), is(replyMessage));
	}
}
