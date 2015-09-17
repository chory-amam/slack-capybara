package handlers;


import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import models.Capybara;
import models.Database;

@SuppressWarnings("unused")
public class CapybaraHandlers implements BotanMessageHandlers {

	@Override
	public void register(final Robot robot) {
		Database.initialize();
		robot.hear(
				"(?<body>.+)",
				"talk with capybara",
				message -> {
					Database.study(message.getMatcher().group("body"));
					message.reply(new Capybara().getWord());
				});
	}
}
