package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

@SuppressWarnings("unused")
public class HelpHandlers implements BotanMessageHandlers {
    @Override
    public void register(final Robot robot) {
        robot.respond("help", "show help", message -> {
			final StringBuilder builder = new StringBuilder();
			robot.getHandlers().stream().sorted()
					.forEach(handler -> builder.append(handler.toString()));
			message.reply(builder.toString());
		});
    }
}
