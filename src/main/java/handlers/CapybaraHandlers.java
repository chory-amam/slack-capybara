package handlers;


import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
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
					final String body = message.getMatcher().group("body");
					if(isValid(body)) {
						Database.study(body);
					}
					message.reply(new Capybara().getWord());
				});
	}

	@VisibleForTesting
	public static boolean isValid(final String message) {
		return !Strings.isNullOrEmpty(message) && (!message.contains("://"));
	}
}
