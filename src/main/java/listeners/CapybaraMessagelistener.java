package listeners;


import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;

import com.github.masahitojp.botan.utils.BotanUtils;
import models.Capybara;
import models.Database;

@SuppressWarnings("unused")
public class CapybaraMessagelistener implements BotanMessageListenerRegister {

	@Override
	public void register() {
		Database.initialize();
		BotanUtils.hear(
				"(?<body>.+)",
				"talk with capybara",
				message -> {
						Database.study(message.getMatcher().group("body"));
						message.reply(new Capybara().getWord());
				});
	}
}
