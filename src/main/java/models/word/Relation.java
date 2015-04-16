package models.word;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface Relation {
	@SqlUpdate("create table relation (lead_word varchar(20), follow_word varchar(20), is_last bit);")
	void createRelationTable();

	@SqlUpdate("select count(*) from relation where lead_word = (:lead_word) and follow_word = (:follow_word) and is_last = (:is_last)")
	int relationCount(@Bind("lead_word") String lead_word, @Bind("follow_word") String follow_word, @Bind("is_last") boolean is_last);

	@SqlUpdate("insert into relation values(:lead_word, :follow_word, :is_last)")
	void relationInsert(@Bind("lead_word") String lead_word, @Bind("follow_word") String follow_word, @Bind("is_last") boolean is_last);

	@SqlUpdate("select * from relation where lead_word = (:lead_word) order by random() limit 1")
	String relationSelectByRandom(@Bind("lead_word") String lead_word);

	void close();
}
