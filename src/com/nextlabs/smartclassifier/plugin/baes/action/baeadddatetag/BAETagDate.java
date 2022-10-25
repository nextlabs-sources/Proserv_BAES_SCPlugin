package com.nextlabs.smartclassifier.plugin.baes.action.baeadddatetag;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrDocument;

import com.nextlabs.jtagger.Tagger;
import com.nextlabs.jtagger.TaggerFactory;
import com.nextlabs.smartclassifier.constant.ActionResult;
import com.nextlabs.smartclassifier.constant.SolrPredefinedField;
import com.nextlabs.smartclassifier.plugin.action.ActionOutcome;
import com.nextlabs.smartclassifier.plugin.action.ExecuteOncePerFile;
import com.nextlabs.smartclassifier.plugin.action.SharedFolderAction;

public class BAETagDate
        extends SharedFolderAction implements ExecuteOncePerFile {

    private static final Logger logger = LogManager.getLogger(BAETagDate.class);
    private static final String ACTION_NAME = "BAE_ADD_DATE_TAG (SHARED FOLDER)";

    public BAETagDate() {
        super(ACTION_NAME);
    }

    /**
     * Execute the action of this solr Document
     *
     * @param solrDocument the Solr Document
     * @return <code>ActionOutcome</code> object to denote the result and message of this action
     */
    @Override
    public ActionOutcome execute(final SolrDocument solrDocument) throws Exception {
    	
        ActionOutcome outcome = new ActionOutcome();

        outcome.setResult(ActionResult.SUCCESS);
        outcome.setMessage("Tags has been added successfully.");

        String documentPath = (String) solrDocument.get(SolrPredefinedField.ID);
        String sExclude = getParameterByKey("exclude-folder");

        try {
        	boolean bIgnore = false;
        	
			//Added to handle exception case for folder that no need to discard
			if (sExclude.length()>0){
				
				String[] sPaths = documentPath.split("/");
				
				//Loop until second last, last is file name, ignore
				for (int i=0; i<sPaths.length-1; i++) {
					if(sPaths[i].contains(sExclude)) {
						bIgnore = true;
						break;
					}
				}
			}
			
			if (bIgnore) {
				logger.info("Ignore file " + documentPath + " since it is in exclude list");
				outcome.setResult(ActionResult.FAIL);
				outcome.setMessage("Ignore file " + documentPath + " since it is in exclude list");
				return outcome;
			}
			
        	String sTagName = getParameterByKey("tag-name");
            long lastModified = (long) solrDocument.get("last_modified_date_millisecond");
        	
            logger.info("Adding tag "+ sTagName +" to " + documentPath);
            addTag(sTagName,documentPath,lastModified);
        } catch (Exception e) {
            logger.error("Error adding tag to file: " + documentPath, e);
            outcome.setResult(ActionResult.FAIL);
            outcome.setMessage(e.getMessage());
        }

        return outcome;
    }
    
    private void addTag(String tagName, String documentPath, long lastModified) throws Exception {
    	
    	logger.debug("Tagging file " + documentPath);
    	Tagger tagger = null;
    	
    	if (new File(documentPath).exists()) {
           tagger = TaggerFactory.getTagger(documentPath);
            
            if(tagger == null) {
            	throw new Exception("File type not supported.");
            }
            
            if(!getParameterByKey("overwrite").equalsIgnoreCase("true")){
            	
            	if (tagger.getTag(tagName)!=null){
            		logger.info("Skip writing tag since tag already exist in the file");
            		tagger.close();
            		return;
            	}
            }
            
            boolean success = tagger.addTag(tagName, convertDateFormat(lastModified));

            if (success) {
                tagger.save(documentPath);
                tagger.close();
            } else {
                logger.warn("Unable to add tag to file.");
                tagger.close();
            }
        } else {
            logger.info("File does not exist.");
        }
    	
    }
    
    private String convertDateFormat(long lastModTime) {
        DateFormat dateFormat = new SimpleDateFormat(getParameterByKey("date-format"));
        return dateFormat.format(lastModTime);
    }
        
}
