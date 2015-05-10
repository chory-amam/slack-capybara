package controllers;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import models.Capybara;
import models.Database;
import ninja.siden.App;
import ninja.siden.Stoppable;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
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

	Stoppable stop;
	final ObjectMapper mapper =  JsonFactory.create();

	@Before
	public void setUp() throws Exception {
		final App app = new App();
		new CapybaraController(app).defineRoutes();
		stop = app.listen(8080);
	}

	@After
	public void tearDown() throws Exception {
		if (stop != null) {
			stop.stop();
		}
	}

	@Test
	public void お話しするところのテスト() throws Exception {

		final String wordForTest = "test";

		// RDBMSから取る部分はモックを使ってテスト
		new Expectations() {
			{
				Database.pickSentence();result = wordForTest;
			}
		};

		final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		final Future<Response> f = asyncHttpClient.prepareGet("http://localhost:8080/capybara").execute();

		final Response response = f.get();
		final String body = response.getResponseBody();
		final Capybara value = mapper.fromJson(body, Capybara.class);

		assertThat(response.getStatusCode(), is(200));
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
				.setBody(wordForTest.getBytes())
				.execute();

		final Response response = f.get();
		final String str = response.getResponseBody();
		final StudyResultPojo value = mapper.fromJson(str, StudyResultPojo.class);

		assertThat(response.getStatusCode(), is(200));
		assertThat(value.isResult(), is(true));

	}
}
