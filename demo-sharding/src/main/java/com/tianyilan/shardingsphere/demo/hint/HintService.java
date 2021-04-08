package com.tianyilan.shardingsphere.demo.hint;

import java.io.IOException;
import java.sql.SQLException;

public interface HintService {

	/**
	 * Hint 分库策略测试 -> 指定分库
	 * @param databaseIdx 分库index
	 * @throws SQLException
	 * @throws IOException
	 */
	public void processWithHintValueForShardingDatabases(long databaseIdx) throws SQLException, IOException;
	
	public void processWithHintValueForShardingDatabasesAndTables() throws SQLException, IOException;
	
	public void processWithHintValueMaster() throws SQLException, IOException;
}
