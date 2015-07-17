package controllers;

import com.github.masahitojp.nineteen.Reviewer;
import com.github.masahitojp.nineteen.Song;
import com.github.masahitojp.nineteen.Token;
import models.Capybara;
import models.Database;
import org.boon.json.JsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import pojos.HaikuResulotPojo;
import pojos.StudyResultPojo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/haiku/")
public class HaikuController extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(HaikuController.class);
	private static final Marker MARKER = MarkerFactory.getMarker("HaikuController");
	private static final long serialVersionUID = 1L;

	private static byte[] getPostBody(HttpServletRequest request) throws IOException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		InputStream stream  = request.getInputStream();
		byte [] buffer = new byte[1024];
		while(true) {
			int len = stream.read(buffer);
			if(len < 0) {
				break;
			}
			bout.write(buffer, 0, len);
		}
		return bout.toByteArray();

	}
	public String toTestString(final Optional<Song> songOpt) {
		return songOpt.map(song -> song.getPhrases().stream()
				.map(list -> list.stream().map(Token::toString).collect(Collectors.joining()))
				.collect(Collectors.joining(" "))).orElse("");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String result = "";
		try{
			final byte[] bytes = getPostBody(req);
			final String body = new String(bytes, "UTF-8");
			LOGGER.info("post: {}", body);
			if(!body.equals("")) {
				final Optional<Song> songOpt = new Reviewer().find(body);
				result = toTestString(songOpt);
				LOGGER.info("result: {}", result);
			}
		} catch (final Exception e) {
			LOGGER.warn(MARKER, "", e);
		}

		res.setCharacterEncoding("UTF-8");
		final String json = JsonFactory.toJson(new HaikuResulotPojo(new String(result.getBytes("UTF-8"), "UTF-8"))) ;
		res.setContentType("application/json;charset=UTF-8");
		res.getWriter().println(json);
	}
}
