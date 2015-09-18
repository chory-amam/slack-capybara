package handlers;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CapybaraHandlersTest {

	@Test
	public void testIsValid() throws Exception {
		assertThat(CapybaraHandlers.isValid("ここ面白いよ!"), is(true));
		assertThat(CapybaraHandlers.isValid(""), is(false));
		assertThat(CapybaraHandlers.isValid(null), is(false));
		// urlが来たときに studyさせないように
		assertThat(CapybaraHandlers.isValid("http://localhost:9000/funny/site/ ここ面白いよ!"), is(false));
		assertThat(CapybaraHandlers.isValid("https://localhost:9000/funny/site/ ここ面白いよ!"), is(false));
	}
}
