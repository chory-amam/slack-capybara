package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.brain.BotanBrain;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

import java.nio.ByteBuffer;
import java.util.Optional;
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
    private int getInteger(BotanBrain brain, byte[] key, Function<Integer, Integer> func) {
        final long stamp = lock.writeLock();
        final int before;
        final ByteBuffer buffer;
        final Optional<byte[]> value = brain.get(key);
        if (value.isPresent()) {
            buffer = ByteBuffer.wrap(value.get());
            before = buffer.getInt();
        } else {
            before = 0;
            buffer = ByteBuffer.allocate(4);
        }
        final int result = func.apply(before);
        buffer.clear();
        buffer.putInt(result);
        brain.put(key, buffer.array());
        lock.unlock(stamp);
        return result;
    }

    public int incr(BotanBrain brain, final String key) {
        return getInteger(brain, key.getBytes(), t -> t + 1);
    }
}
