package controllers;

import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Capybara;
import models.Database;
import org.boon.json.JsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import pojos.StudyResultPojo;

@WebServlet("/capybara/")
public class CapybaraController extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(CapybaraController.class);
	private static final Marker MARKER = MarkerFactory.getMarker("CapybaraController");
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


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		final String json = JsonFactory.toJson(new Capybara());
		res.getWriter().println(json);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		LOGGER.info("post");
		boolean result = false;
		try{
			final byte[] bytes = getPostBody(req);
			final String test = new String(bytes, "UTF-8");
			if(!test.equals("")) {
				Database.study(test);
				result = true;
			}
		} catch (final Exception e) {
			LOGGER.warn(MARKER, "", e);
		}

		final String json = JsonFactory.toJson(new StudyResultPojo(result));

		res.getWriter().println(json);
	}
}
