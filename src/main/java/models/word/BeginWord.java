package models.word;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface BeginWord {
	@SqlUpdate("create table  if not exists beginword (word varchar(20));")
	void createBeginWordTable();

	@SqlQuery("select count(word) from beginword where word = (:word)")
	int beginWordCount(@Bind("word") String word);

	@SqlUpdate("insert into beginword values(:word)")
	void beginWordInsert(@Bind("word") String word);

	@SqlQuery("select word from beginword order by random() limit 1")
	String beginWordSelectByRandom();

	void close();
}
