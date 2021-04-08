package com.tianyilan.shardingsphere.demo.hint;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.stereotype.Service;

/**
 * HintManager : 一个基于 ThreadLocal 的用于存储 Hint 规则的容器，使用它可以指定
 * 1) 数据库分片值
 * 2）数据表分片值
 * 3）是否只数据库分片
 * 4）是否只路由master
 */
@Service
public class HintServiceImpl implements HintService {

	@Override
	public void processWithHintValueForShardingDatabases(long databaseIdx) throws SQLException, IOException {

		//根据 Yaml配置初始化 -> ShardingDataSource
		DataSource dataSource = DataSourceHelper.getDataSourceForShardingDatabases();

		//HintManager、Connection、Statement 都实现了 AutoCloseable
		//JDK1.7 后，try 后，自动调用他们的 close() 方法
		try (HintManager hintManager = HintManager.getInstance();
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {

			//指定分库 -> HintShardingStrady
			HintManagerHelper.initializeHintManagerForShardingDatabases(hintManager, databaseIdx);

			ResultSet result = statement.executeQuery("select user_id, user_name from user");
			
			while (result.next()) {
				System.out.println(result.getLong(1) + ": " + result.getString(2));
            }
		}		
	}

	@Override
	public void processWithHintValueForShardingDatabasesAndTables() throws SQLException, IOException {
		DataSource dataSource = DataSourceHelper.getDataSourceForShardingDatabasesAndTables();
		try (HintManager hintManager = HintManager.getInstance();
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			HintManagerHelper.initializeHintManagerForShardingDatabasesAndTables(hintManager);

			ResultSet result = statement.executeQuery("select record_id, user_id, remark from health_record");
			
			while (result.next()) {
				System.out.println(result.getLong(1) + ": " + result.getLong(2) + ": " + result.getString(3));
            }
		}		
	}

	@Override
	public void processWithHintValueMaster() throws SQLException, IOException {
		DataSource dataSource = DataSourceHelper.getDataSourceForMaster();
		try (HintManager hintManager = HintManager.getInstance();
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()) {
			HintManagerHelper.initializeHintManagerForMaster(hintManager);

			ResultSet result = statement.executeQuery("select user_id, user_name from user");
			
			while (result.next()) {
				System.out.println(result.getLong(1) + ": " + result.getString(2));
            }
		}		
	}
}
