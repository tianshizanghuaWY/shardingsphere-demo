package com.tianyilan.shardingsphere.demo;

import com.tianyilan.shardingsphere.demo.hint.HintService;
import com.tianyilan.shardingsphere.demo.service.HealthLevelService;
import com.tianyilan.shardingsphere.demo.service.HealthRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tianyilan.shardingsphere.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private HealthRecordService recordService;

	@Autowired
	private HealthLevelService healthLevelService;

	@Autowired
	private HintService hintService;
	
	@Test
	public void testProcessUsers() throws Exception {
		userService.processUsers();
	}

	@Test
	public void testProcessRecords() throws Exception {
		recordService.processHealthRecords();
	}

	@Test
	public void testProcessHintDatabase() throws Exception {
		hintService.processWithHintValueForShardingDatabases(1);
	}

	@Test
	public void testSelectUsers() throws Exception {
		userService.getUsers();
	}

	@Test
	public void testProcessHintMasterOnly() throws Exception {
		hintService.processWithHintValueMaster();
	}
}
