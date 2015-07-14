package models.word;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationMapper implements ResultSetMapper<Relation> {
	public Relation map(final int index, final ResultSet r, StatementContext ct) throws SQLException {
		return new Relation(r.getString("follow_word"), r.getBoolean("is_last"));
	}
}

