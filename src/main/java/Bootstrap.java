import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(final String[] args) {
		initialize();
		final Tokenizer tokenizer = Tokenizer.builder().build();
		for (final Token token : tokenizer.tokenize("こんにちは、ぼく、ドラえもんです")) {
			log.info(token.getSurfaceForm() + "\t" + token.getAllFeatures());
		}
	}

	public static void initialize() {
		final DataSource ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "capybara", "password");
		final DBI dbi = new DBI(ds);
		final Handle h = dbi.open();
		h.execute("create table something(id int primary key, name varchar(100))");
		h.execute("insert into something (id, name) values (?, ?)", 1, "capybara");
		String name = h.createQuery("select name from something where id = :id")
				.bind("id", 1)
				.map(StringMapper.FIRST)
				.first();
		log.info(name);
		h.close();
	}
}
