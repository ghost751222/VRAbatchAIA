package com.macaron.vra.dao.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.macaron.vra.entity.QaTaskJob;
import com.macaron.vra.util.JacksonUtil;

@Repository
public class QaTaskJobDaoImpl {
	

	@Autowired
	@Qualifier("postgreSqlJdbc")
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<QaTaskJob> findAll(){
		String sql = "select * from qa_task_job";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<QaTaskJob>(QaTaskJob.class));	
	}

	public List<QaTaskJob> findByApplicationFormID(String applicationFormID){
		String sql = "select * from qa_task_job where psae_ApplicationFormID =:applicationFormID";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("applicationFormID", applicationFormID);
		return jdbcTemplate.query(sql,params, new BeanPropertyRowMapper<QaTaskJob>(QaTaskJob.class));	
	}
	
	public List<QaTaskJob> findByPolicyNo(String policyNo){	
		String sql = "select * from qa_task_job where psae_PolicyNo =:policyNo";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("policyNo", policyNo);
		return jdbcTemplate.query(sql,params, new BeanPropertyRowMapper<QaTaskJob>(QaTaskJob.class));	
	}
	
	public List<QaTaskJob> findByListenEndTimeRange(Date start_date,Date end_date){
		String sql = "select * from qa_task_job where listen_end_time between :start_date::timestamp and :end_date::timestamp";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("start_date", start_date);
		params.put("end_date", end_date);
		return jdbcTemplate.query(sql,params, new BeanPropertyRowMapper<QaTaskJob>(QaTaskJob.class));	
	}

	public List<QaTaskJob> findAllBySendDateAndListenEndTimeRange(Date start_date,Date end_date) {
		String sql = "select * from qa_task_job where send_date = CAST(GETDATE() AS DATE) or (listen_end_time between :start_date and :end_date)";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("start_date", start_date);
		params.put("end_date", end_date);
		return jdbcTemplate.query(sql, params,new BeanPropertyRowMapper<>(QaTaskJob.class));
	}

	public List<QaTaskJob> findAllByListenEndTimeRange(Date start_date,Date end_date) {
		String sql = "select * from qa_task_job where (listen_end_time between :start_date and :end_date)";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("start_date", start_date);
		params.put("end_date", end_date);
		return jdbcTemplate.query(sql, params,new BeanPropertyRowMapper<>(QaTaskJob.class));
	}


	
	public List<QaTaskJob> findByTaskDate(Date taskDate){
		String sql = "select * from qa_task_job where cast(task_date as date) = cast(:taskDate as date) ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskDate", taskDate);
		return jdbcTemplate.query(sql, params,new BeanPropertyRowMapper<>(QaTaskJob.class));
	}


	public List<QaTaskJob> findByInsertTime(Date insertTime){
		String sql = "select * from qa_task_job where cast(insert_time as date) = cast(:insertTime as date)";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("insertTime", insertTime);
		return jdbcTemplate.query(sql, params,new BeanPropertyRowMapper<>(QaTaskJob.class));
	}

	public int update(QaTaskJob qa_task_job)  {
		String sql = "update qa_task_job set psae_TMRPendingCode=:psae_TMRPendingCode,psae_TMRPendingCode1=:psae_TMRPendingCode1,psae_TMRPendingCode2=:psae_TMRPendingCode2," +
				"psae_SCFPendingCode=:psae_SCFPendingCode,psae_SCFPendingCode1=:psae_SCFPendingCode1,psae_SCFPendingCode2=:psae_SCFPendingCode2," +
				"psae_SCFRejectRemedyPendingCode1=:psae_SCFRejectRemedyPendingCode1,psae_SCFRejectRemedyPendingCode2=:psae_SCFRejectRemedyPendingCode2," +

				"psae_TMRPendingCodeName=:psae_TMRPendingCodeName,psae_TMRPendingCode1Name=:psae_TMRPendingCode1Name,psae_TMRPendingCode2Name=:psae_TMRPendingCode2Name," +
				"psae_SCFPendingCodeName=:psae_SCFPendingCodeName,psae_SCFPendingCode1Name=:psae_SCFPendingCode1Name,psae_SCFPendingCode2Name=:psae_SCFPendingCode2Name," +
				"psae_SCFRejectRemedyPendingCode1Name=:psae_SCFRejectRemedyPendingCode1Name,psae_SCFRejectRemedyPendingCode2Name=:psae_SCFRejectRemedyPendingCode2Name," +

				"psae_PolicyNo=:psae_PolicyNo,qa_remark=:qa_remark,case_content=:case_content where id=:id";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id",qa_task_job.getId());

		params.put("psae_TMRPendingCode",qa_task_job.getPsae_TMRPendingCode());
		params.put("psae_TMRPendingCode1",qa_task_job.getPsae_TMRPendingCode1());
		params.put("psae_TMRPendingCode2",qa_task_job.getPsae_TMRPendingCode2());
		params.put("psae_SCFPendingCode",qa_task_job.getPsae_SCFPendingCode());
		params.put("psae_SCFPendingCode1",qa_task_job.getPsae_SCFPendingCode1());
		params.put("psae_SCFPendingCode2",qa_task_job.getPsae_SCFPendingCode2());
		params.put("psae_SCFRejectRemedyPendingCode1",qa_task_job.getPsae_SCFRejectRemedyPendingCode1());
		params.put("psae_SCFRejectRemedyPendingCode2",qa_task_job.getPsae_SCFRejectRemedyPendingCode2());

		params.put("psae_TMRPendingCodeName",qa_task_job.getPsae_TMRPendingCodeName());
		params.put("psae_TMRPendingCode1Name",qa_task_job.getPsae_TMRPendingCode1Name());
		params.put("psae_TMRPendingCode2Name",qa_task_job.getPsae_TMRPendingCode2Name());
		params.put("psae_SCFPendingCodeName",qa_task_job.getPsae_SCFPendingCodeName());
		params.put("psae_SCFPendingCode1Name",qa_task_job.getPsae_SCFPendingCode1Name());
		params.put("psae_SCFPendingCode2Name",qa_task_job.getPsae_SCFPendingCode2Name());
		params.put("psae_SCFRejectRemedyPendingCode1Name",qa_task_job.getPsae_SCFRejectRemedyPendingCode1Name());
		params.put("psae_SCFRejectRemedyPendingCode2Name",qa_task_job.getPsae_SCFRejectRemedyPendingCode2Name());

		params.put("psae_PolicyNo",qa_task_job.getPsae_PolicyNo());
		params.put("case_content",qa_task_job.getCase_content());
		params.put("qa_remark",qa_task_job.getQa_remark());
		return jdbcTemplate.update(sql, params);
		
	}
	
	public int[] delListById(List<QaTaskJob> qa_task_jobs) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(qa_task_jobs.toArray());
		String sql = "delete from  qa_task_job where id = :id";
		return jdbcTemplate.batchUpdate(sql, batch);
	}
	
	public int delByPolicyNo(String policyNo) {
		String sql = "delete from  qa_task_job where task @> '{\"psae_PolicyNo\": \":policyNo\"}'";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("policyNo",policyNo);
		return jdbcTemplate.update(sql,params);
	}


	public List<QaTaskJob> findByIsDealDone(String isDealDone) {
		String sql = "select * from qa_task_job where psae_IsDealDone =:isDealDone";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("isDealDone", isDealDone);
		return jdbcTemplate.query(sql,params, new BeanPropertyRowMapper<QaTaskJob>(QaTaskJob.class));
	}
}
