package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.brain.BotanBrain;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Function;

@SuppressWarnings("unused")
public class PlusPlusHandlers implements BotanMessageHandlers {
    private static String KEY_FORMAT = "plusplus.key.%s";
    @Override
    public void register(final Robot robot) {
        robot.respond(
                "\\s*(?<body>.+)\\Q++\\E",
                "string++",
                message -> {
                    final String body = message.getMatcher().group("body");
                    final int result = incr(message.getBrain(), String.format(KEY_FORMAT, body));
                    message.reply(String.format("%s : total(%d)", body, result));
                });
    }

    private final StampedLock lock = new StampedLock();
    private int getInteger(BotanBrain brain, String key, Function<Integer, Integer> func) {
        final long stamp = lock.writeLock();
        final int before;
        final String value = brain.getData().getOrDefault(key, "");
        if (!value.equals("")) {
            before = Integer.parseInt(value);
        } else {
            before = 0;
        }
        final int result = func.apply(before);

        brain.getData().put(key, String.valueOf(result));
        lock.unlock(stamp);
        return result;
    }

    public int incr(BotanBrain brain, final String key) {
        return getInteger(brain, key, t -> t + 1);
    }
}
