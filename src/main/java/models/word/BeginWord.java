package models.word;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface BeginWord {
	@SqlUpdate("create table begin_word (word varchar(20));")
	void createBeginWordTable();

	@SqlUpdate("select count(*) from begin_word where word = (:word)")
	int beginWordCount(@Bind("word") String word);

	@SqlUpdate("insert into begin_word values(:word)")
	void beginWordInsert(@Bind("word") String word);

	@SqlUpdate("select * from begin_word order by random() limit 1")
	String beginWordSelectByRandom();

	void close();
}
