package controllers;

import java.io.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Capybara;
import models.Database;
import org.boon.json.JsonFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import pojos.StudyResultPojo;


public class SampleServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleServlet.class);
	private static final Marker MARKER = MarkerFactory.getMarker("SampleServlet");
	private static final long serialVersionUID = 1L;

	public static byte[] getPostBody(HttpServletRequest request) throws IOException {

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
		LOGGER.info("test");
		byte[] bytes;
		String test = null;
		try{
			bytes = getPostBody(req);
			test = new String(bytes, "UTF-8");
			Database.study(test);
		} catch (final Exception ignore) {

		}

		final String json = JsonFactory.toJson(new StudyResultPojo(true));

		res.getWriter().println(json);
	}
}
