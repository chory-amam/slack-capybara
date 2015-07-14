package controllers;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import models.Capybara;

import models.Database;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pojos.StudyResultPojo;

import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class CapybaraControllerTest {

	@SuppressWarnings("unused")
	@Mocked
	private Database database;

	Server server;
	final ObjectMapper mapper = JsonFactory.create();

	@Before
	public void setUp() throws Exception {
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(CapybaraController.class, "/");

		server = new Server(8080);
		server.setHandler(handler);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		if (server != null) {
			server.stop();
		}
	}

	@Test
	public void お話しするところのテスト() throws Exception {

		final String wordForTest = "test";

		// RDBMSから取る部分はモックを使ってテスト
		new Expectations() {
			{
				Database.pickSentence();
				result = wordForTest;
			}
		};

		final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		final Future<Response> f = asyncHttpClient.prepareGet("http://localhost:8080/capybara").execute();

		final Response response = f.get();
		final String body = response.getResponseBody();

		assertThat(response.getStatusCode(), is(200));
		final Capybara value = mapper.fromJson(body, Capybara.class);

		assertNotNull(value.getWord());

	}

	@Test
	public void 登録するテスト() throws Exception {

		final String wordForTest = "ウェヒヒwww, しほんある。";

		// RDBMSから取る部分はモックを使ってテスト
		new Expectations() {
			{
				Database.study(wordForTest);
			}
		};

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		Future<Response> f = asyncHttpClient
				.preparePost("http://localhost:8080/capybara")
				.setHeader("Content-Length", "" + (wordForTest.getBytes().length))
				.setBody(wordForTest.getBytes("UTF-8"))
				.execute();

		final Response response = f.get();
		final String str = response.getResponseBody();
		final StudyResultPojo value = mapper.fromJson(str, StudyResultPojo.class);

		assertThat(response.getStatusCode(), is(200));
		assertThat(value.isResult(), is(true));

	}
}
