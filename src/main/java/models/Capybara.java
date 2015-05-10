package models;

import com.google.common.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Capybara {
	String word;

	enum Icon {
		basic(":cap:"),
		angry(":cap_angry:"),
		sad(":cap_cry:"),
		happy(":cap_r:"),
		rolling(":cap_spin3:"),
		warp(":cap_wp1:"),
		onsen(":cap_onsen:"),
		event(":cap_carp:");

		public String key;
		Icon(final String key) {
			this.key = key;
		}
	}

	@VisibleForTesting
	public static String choiceIcon() {
		final Icon[] icons = Icon.values();
		List<Icon> iconList = Arrays.asList(icons);
		Collections.shuffle(iconList);
		return iconList.get(0).key;
	}

	public Capybara() {
		this.word = choiceIcon() + " < " + Database.pickSentence();
	}

	public String getWord() {
		return word;
	}
}
