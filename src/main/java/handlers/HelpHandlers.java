package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

import java.util.StringJoiner;

@SuppressWarnings("unused")
public class HelpHandlers implements BotanMessageHandlers {
    @Override
    public void register(final Robot robot) {
        robot.respond("help\\z", "Displays all of the commands.", message -> {
            final StringJoiner builder = new StringJoiner("\n");
            robot.getHandlers().stream().sorted()
                .forEach(handler -> builder.add(handler.toString()));
            message.reply(builder.toString());
        });
    }
}
