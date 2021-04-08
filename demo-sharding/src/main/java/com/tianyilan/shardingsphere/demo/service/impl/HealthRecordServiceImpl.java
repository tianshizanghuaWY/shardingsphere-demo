package com.tianyilan.shardingsphere.demo.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianyilan.shardingsphere.demo.entity.HealthRecord;
import com.tianyilan.shardingsphere.demo.entity.HealthTask;
import com.tianyilan.shardingsphere.demo.repository.HealthRecordRepository;
import com.tianyilan.shardingsphere.demo.repository.HealthTaskRepository;
import com.tianyilan.shardingsphere.demo.service.HealthRecordService;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {	

	@Autowired
	private HealthRecordRepository healthRecordRepository;	

	@Autowired
	private HealthTaskRepository healthTaskRepository;

	@Override
	public void processHealthRecords() throws SQLException{
		insertHealthRecords();
	}
	
	private List<Long> insertHealthRecords() throws SQLException {
        List<Long> result = new ArrayList<>(10);
        for (Long i = 1L; i <= 10; i++) {
        	HealthRecord healthRecord = insertHealthRecord(i);
            insertHealthTask(i, healthRecord);
            result.add(healthRecord.getRecordId());
        }
        return result;
    }	
    
    private HealthRecord insertHealthRecord(final Long userId) throws SQLException {
    	HealthRecord healthRecord = new HealthRecord();
    	healthRecord.setUserId(userId);
    	healthRecord.setLevelId(userId % 5);
    	healthRecord.setRemark("Remark" + userId);
        healthRecordRepository.addEntity(healthRecord);
        return healthRecord;
    }

	/**
	 * record 和 task 一对多的关系
	 * @param userId
	 * @param healthRecord
	 * @throws SQLException
	 */
	private void insertHealthTask(final Long userId, final HealthRecord healthRecord) throws SQLException {
    	for(int i = 1; i < 4; i++){
			HealthTask healthTask = new HealthTask();
			healthTask.setRecordId(healthRecord.getRecordId());
			healthTask.setUserId(userId);
			healthTask.setTaskName("TaskName:" + userId + "-" + i);
			healthTaskRepository.addEntity(healthTask);
		}
    }
}
