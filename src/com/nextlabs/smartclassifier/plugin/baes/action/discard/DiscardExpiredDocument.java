package com.nextlabs.smartclassifier.plugin.baes.action.discard;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrDocument;

import com.nextlabs.smartclassifier.constant.ActionResult;
import com.nextlabs.smartclassifier.constant.SolrPredefinedField;
import com.nextlabs.smartclassifier.plugin.DataSourceManager;
import com.nextlabs.smartclassifier.plugin.action.ActionOutcome;
import com.nextlabs.smartclassifier.plugin.action.ExecuteOncePerFile;
import com.nextlabs.smartclassifier.plugin.action.SharedFolderAction;

public class DiscardExpiredDocument 
	extends SharedFolderAction implements ExecuteOncePerFile {	
	
	private static final Logger logger = LogManager.getLogger(DiscardExpiredDocument.class);
	private static final String ACTIONP_NAME = "BAE_DISCARD";
	private static final String DATA_SOURCE_NAME = "BAE-System";
	
	public DiscardExpiredDocument() {
		super(ACTIONP_NAME);
	}
	
	@Override
	public ActionOutcome execute(final SolrDocument document)
			throws Exception {
		ActionOutcome outcome = new ActionOutcome();
		outcome.setResult(ActionResult.SUCCESS);
		outcome.setMessage("Record(s) have been discarded successfully.");
		
		outcome = checkExpiredDate(document, outcome);
		
		return outcome;
	}
	
	private ActionOutcome checkExpiredDate(SolrDocument document, ActionOutcome outcome) {
		try {
			DatabaseUtil util = new DatabaseUtil(DataSourceManager.getInstance().getDBDS(DATA_SOURCE_NAME), true);
			String sExclude = getParameterByKey("exclude-folder");
			
			try {
				boolean bIgnore = false;
				String sDocId = (String) document.get(SolrPredefinedField.ID);
				
				//Added to handle exception case for folder that no need to discard
				if (sExclude.length()>0){
					
					String[] sPaths = sDocId.split("/");
					
					//Loop until second last, last is file name, ignore
					for (int i=0; i<sPaths.length-1; i++) {
						if(sPaths[i].contains(sExclude)) {
							bIgnore = true;
							break;
						}
					}
				}
				
				if (bIgnore) {
					logger.info("Ignore file " + sDocId + " since it is in exclude list");
					outcome.setResult(ActionResult.FAIL);
					outcome.setMessage("Ignore file " + sDocId + " since it is in exclude list");
					return outcome;
				}
								
				String sResult[] = util.getRetentionPeriod((String)document.get(getParameterByKey("record-category-key-name") + "_t"));
				
				if (sResult != null) {
					if (sResult[0].equalsIgnoreCase("Standard")) {
						try {
							if(util.handleSimpleCase(getParameterByKey("last-modified-date-key-name"), sResult[1], document))
								FileUtils.deleteQuietly(new File(sDocId));
						} catch (Exception e) {
							logger.error(e.toString(), e);
							outcome.setResult(ActionResult.FAIL);
							outcome.setMessage("Faile to discard record. " + e.toString());
						}
					}
					else{
						try {
							if(util.handleComplexCase(getParameterByKey("activity-key-name"), sResult[1], sResult[2], document))
								FileUtils.deleteQuietly(new File(sDocId));
						} catch (Exception e) {
							logger.error(e.toString(), e);
							outcome.setResult(ActionResult.FAIL);
							outcome.setMessage("Faile to discard record. " + e.toString());
						}
					}
				}
				else{
					logger.info("Skip deleting " + document.get("id") +" due to no db matching record category");
					outcome.setResult(ActionResult.FAIL);
					outcome.setMessage("Skip deleting " + document.get("id") +" due to no db matching record category");
					
				}
				
			} catch (Exception e) {
				logger.error("Error deleting file: " + document.get("id"));
				outcome.setResult(ActionResult.FAIL);
				outcome.setMessage("Faile to discard record. " + e.toString());
			}
		} catch(Exception err) {
			logger.error(err.getMessage(), err);
			outcome.setResult(ActionResult.FAIL);
			outcome.setMessage("Faile to discard record. " + err.toString());
		}
		return outcome;
	}
}
