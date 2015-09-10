package listeners;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.listener.BotanMessageListener;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;

@SuppressWarnings("unused")
public class HelpMessageListener implements BotanMessageListenerRegister {
    @Override
    public void register(final Robot robot) {
        robot.respond("help", "show help", message -> {
            final StringBuilder builder = new StringBuilder();
            for (final BotanMessageListener listeners : robot.getListeners()) {
                builder.append(listeners.toString());
            }
            message.reply(builder.toString());
        });
    }
}
