package models.word;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface RelationQueries {
	@SqlUpdate("create table if not exists relation (lead_word varchar(20), follow_word varchar(20), is_last bit);")
	void createRelationTable();

	@SqlQuery("select follow_word, is_last from relation where lead_word = (:lead_word) and follow_word = (:follow_word) and is_last = (:is_last) LIMIT 1")
	@Mapper(RelationMapper.class)
	Relation relationGetOrNull(@Bind("lead_word") String lead_word, @Bind("follow_word") String follow_word, @Bind("is_last") boolean is_last);

	@SqlUpdate("insert into relation values(:lead_word, :follow_word, :is_last)")
	void relationInsert(@Bind("lead_word") String lead_word, @Bind("follow_word") String follow_word, @Bind("is_last") boolean is_last);

	@SqlQuery("select follow_word, is_last from relation where lead_word = (:lead_word) order by random() limit 1")
	@Mapper(RelationMapper.class)
	Relation relationSelectByRandom(@Bind("lead_word") String lead_word);

	void close();
}

