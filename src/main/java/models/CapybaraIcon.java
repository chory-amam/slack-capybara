package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CapybaraIcon {
	// todo icon keyを入力しよう
	enum Icon {
		basic(""),
		angry(""),
		sad(""),
		happy(""),
		rolling(""),
		warp(""),
		event("");

		public String key;
		Icon(final String key) {
			this.key = key;
		}
	}

	public static String choice() {
		final Icon[] icons = Icon.values();
		List<Icon> iconList = Arrays.asList(icons);
		Collections.shuffle(iconList);
		return iconList.get(0).key;
	}
}
