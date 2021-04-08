package com.tianyilan.shardingsphere.demo.hint;

import org.apache.shardingsphere.api.hint.HintManager;

public class HintManagerHelper {

	static void initializeHintManagerForShardingDatabasesAndTables(final HintManager hintManager) {
		hintManager.addDatabaseShardingValue("health_record", 3L);
		hintManager.addTableShardingValue("health_record", 2L);
	}

	/**
	 * 指定 hint 分库策略
	 * @param hintManager
	 * @param databaseIdx 分库index
	 */
	static void initializeHintManagerForShardingDatabases(final HintManager hintManager, long databaseIdx) {
		hintManager.setDatabaseShardingValue(databaseIdx);
	}

	/**
	 * 强制查询master
	 * @param hintManager
	 */
	static void initializeHintManagerForMaster(final HintManager hintManager) {
		hintManager.setMasterRouteOnly();
	}
}
