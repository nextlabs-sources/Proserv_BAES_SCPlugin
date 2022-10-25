package com.nextlabs.smartclassifier.plugin.baes.action.sendrecordemail;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrDocument;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseUtil {
	
	private static final Logger logger = LogManager.getLogger(DatabaseUtil.class);
	
	private ComboPooledDataSource dbSource;
	
	public DatabaseUtil(ComboPooledDataSource dbSource){
		this.dbSource = dbSource;
	}
	
	public String handleSimpleCase(String keyName, String sRetentionPeriod, SolrDocument doc, int iDays2Expired) 
			throws Exception{
		
		int iRetentionPeriod = Integer.parseInt(sRetentionPeriod);	
		
		if (iRetentionPeriod > 0) {
			
			java.util.Date dExpiredDate = (java.util.Date)doc.get(keyName);
			
			if (dExpiredDate!=null) {
				Calendar cal = Calendar.getInstance();
				
				java.util.Date currentDate = cal.getTime();
				
				
				cal.setTime(dExpiredDate);
				cal.add(Calendar.DATE, iRetentionPeriod);
				
				dExpiredDate = cal.getTime();
				
                Calendar cal1 = Calendar.getInstance();
				java.util.Date queryRangeDate = cal1.getTime();
				cal1.add(Calendar.DATE, iDays2Expired);	
				queryRangeDate= cal1.getTime();
				
				logger.info("Current date value :" + currentDate);
				logger.info("Expired date value :" + dExpiredDate + " for " + doc.get("id"));
				logger.info("Query end date value :" + queryRangeDate + " for " + doc.get("id"));
				
				
				
				if (iDays2Expired == 0) {
					logger.info("Skip checking range since days to expired is 0");
					return formatDateAsUTC(cal);
				}
				
				if (queryRangeDate.after(dExpiredDate)) {
					logger.info("In the query range");
					return formatDateAsUTC(cal);
				}
			}
			else{
				logger.info("Skip " + doc.get("id") +" since last modified date not found");
				return "No Last Modified tag";
			}
		}
		else{
			logger.info("Skip " + doc.get("id") +" since record is permanent");
			return "Permanent";
		}
		
		return null;
	}
	
	public String handleComplexCase(String keyName, String sRetentionPeriod, String sRecordActivityID, SolrDocument doc, int iDays2Expired) 
			throws Exception{
		int iRetentionPeriod = Integer.parseInt(sRetentionPeriod);
		
		if (iRetentionPeriod > 0) {
			String[] sData = ((String)doc.get(keyName + "_t")).split("-");
			
			if (sData!=null&&sData.length > 1) {
				java.util.Date dExpiredDate = getDateValue(getTitle(sData), sData[sData.length-1], sRecordActivityID);
				
				if(dExpiredDate == null) {
					logger.info("No record found from db, skip check");
					return null;
				}
				
				TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
				Calendar cal = Calendar.getInstance();
				
				java.util.Date currentDate = cal.getTime();
				
				cal.setTime(dExpiredDate);
				cal.add(Calendar.DATE, iRetentionPeriod);
				
				dExpiredDate = cal.getTime();
				
				 Calendar cal1 = Calendar.getInstance();
				 java.util.Date queryRangeDate = cal1.getTime();
				 cal1.add(Calendar.DATE, iDays2Expired);	
				 queryRangeDate= cal1.getTime();
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				
				logger.info("Current date value :" + simpleDateFormat.format(currentDate));
				logger.info("Expired date value :" + simpleDateFormat.format(dExpiredDate) + " for " + doc.get("id"));
				logger.info("Query end date value :" + simpleDateFormat.format(queryRangeDate) + " for " + doc.get("id"));
				
				if (iDays2Expired == 0) {
					logger.info("Skip checking range since days to expired is 0");
					return formatDateAsUTC(cal);
				}
				
				if (queryRangeDate.after(dExpiredDate)) {
					logger.info("In the query range");
					return formatDateAsUTC(cal);
				}
			}
			else{
				logger.info("No record activity tag for doc "+ doc.get("id") +" skip handling");
			}
		}
		else{
			logger.info("Skip " + doc.get("id") +" since record is permanent");
			return "Permanent";
		}
		
		return null;
	}
	
	public String formatDateAsUTC(Calendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date.getTime());
	}
	
	private String getTitle(String sData[]){
		
		String sResult = "";
		
		for (int i=0; i< sData.length-1; i++){
			
			sResult = sResult + sData[i] + "-"; 
		}
		
		return sResult.substring(0, sResult.length()-1);
	}
	
	public String[] getRetentionPeriod(String sRecordCategory){
		
		long lCurrentTime = System.nanoTime();
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		String[] sOut =  null;
		
		try {
			
			String sQuery = "SELECT RECORDTYPE, RETENTIONPERIOD, RECORDACTIVITY, CATEGORYNAME, AUTODELETE FROM RECORD_CATEGORY WHERE RECORDCATEGORY='%s'";
				
			sQuery = String.format(sQuery, sRecordCategory);
			
			logger.info("SQL Query string is :" + sQuery);

			conn = dbSource.getConnection();
			statement = conn.createStatement();
			
			rs = statement.executeQuery(sQuery);

			if (rs.next()) {
				sOut = new String[5];
				sOut[0] = rs.getString(1);
				sOut[1] = rs.getString(2);
				sOut[2] = rs.getString(3);
				sOut[3] = rs.getString(4);
				sOut[4] = rs.getString(5);
				logger.info(sOut[0] + "|" + sOut[1] + "|"+ sOut[2] + "|" + sOut[3] + "|"+ sOut[4]+" data found");
			}

		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		finally{
			try {
				if (null!=rs)
					rs.close();
				if (null!=statement)
					statement.close();
				if (conn!=null)
					conn.close();

			} catch (SQLException e) {}

			logger.info("Time spent for query getRetentionPeriod = " + ((System.nanoTime() - lCurrentTime)/1000000.00) +"ms");
		}
		return sOut;
	}
	
	private Date getDateValue(String sTitleValue, String sRefValue, String sActivityID){
		
		long lCurrentTime = System.nanoTime();
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		Date dDate = null;
		
		try {
			
			String sQuery = "SELECT DATEVALUE FROM ACTIVITY_DETAIL WHERE TITLEVALUE='%s' AND REFERENCEVALUE='%s' AND ACTIVITY='%s'";
				
			sQuery = String.format(sQuery, sTitleValue.trim(), sRefValue.trim(), sActivityID);
			
			logger.info("Query string is :" + sQuery);

			conn = dbSource.getConnection();
			statement = conn.createStatement();
			
			rs = statement.executeQuery(sQuery);

			if (rs.next()) {
				dDate = rs.getDate(1);
				logger.info(dDate + " data found");
			}
			
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		finally{
			try {
				if (null!=rs)
					rs.close();
				if (null!=statement)
					statement.close();
				if (conn!=null)
					conn.close();

			} catch (SQLException e) {}

			logger.info("Time spent for query getDateValue = " + ((System.nanoTime() - lCurrentTime)/1000000.00) +"ms");
		}
		return dDate;
	}

}
