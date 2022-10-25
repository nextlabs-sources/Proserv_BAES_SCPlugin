package com.nextlabs.smartclassifier.plugin.baes.action.baecopy;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrDocument;

import com.nextlabs.smartclassifier.constant.ActionResult;
import com.nextlabs.smartclassifier.constant.RollbackErrorType;
import com.nextlabs.smartclassifier.constant.SolrPredefinedField;
import com.nextlabs.smartclassifier.exception.RollbackException;
import com.nextlabs.smartclassifier.plugin.action.ActionOutcome;
import com.nextlabs.smartclassifier.plugin.action.ExecuteOncePerFile;
import com.nextlabs.smartclassifier.plugin.action.SharedFolderAction;
	
public class BAECopy 
	extends SharedFolderAction implements ExecuteOncePerFile{
	
	private static final Logger logger = LogManager.getLogger(BAECopy.class);
	private static final String ACTION_NAME = "BAE_COPY";
	
	private File copiedFile;
	
	public BAECopy() {
		super(ACTION_NAME);
	}
	
	@Override
	public ActionOutcome execute(final SolrDocument document)
			throws Exception {
		ActionOutcome outcome = new ActionOutcome();
		String target = getParameterByKey("target");
		String sExclude = getParameterByKey("exclude-folder");
		copiedFile = null;
		
		outcome.setResult(ActionResult.SUCCESS);
		outcome.setMessage("Files have been copied and tags have been removed successfully.");
		
		try {
			String sDocId = (String) document.get(SolrPredefinedField.ID);
			String sRecordCategory = (String)document.get(getParameterByKey("record-category-key-name") + "_t");
			File sourceFile = new File(sDocId);
			boolean bIgnore = false;
			
			//Added to handle exception case for folder that no need to copy
			if (sExclude.length()>0) {
				String[] sPaths = sDocId.split("/");
				
				//Loop until second last, last is file name, ignore
				for (int i=0; i<sPaths.length-1; i++){
					if(sPaths[i].contains(sExclude)){
						bIgnore = true;
						break;
					}	
				}
			}
			
			if (bIgnore) {
				logger.info("Ignore file " + sDocId + " since it is in exclude list.");
				
				outcome.setResult(ActionResult.FAIL);
				outcome.setMessage("Ignore file " + sDocId + " since it is in exclude list.");
				return outcome;
			}
			
			String sVersioning = getParameterByKey("enable-versioning");
			String sPrefix = "";
			
			
			if (sVersioning.equalsIgnoreCase("true")){
				
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				if(document.get(getParameterByKey("file-prefix-key-name")+"_tdt")!=null){
					sPrefix =formatter.format(document.get(getParameterByKey("file-prefix-key-name") + "_tdt")); 
				}
				else{
					sPrefix = (String) document.get(getParameterByKey("file-prefix-key-name")+"_t");
				}
													
				if(sPrefix!=null){
					sPrefix = sPrefix.replace("/", "-");
					sPrefix = sPrefix.replace(":", ".");
				}
				else{
					sPrefix = "";
				}
			}
			
			String sFileExt = FilenameUtils.getExtension(sourceFile.getAbsolutePath());
			String sFileNameWithoutExt = FilenameUtils.getBaseName(sourceFile.getAbsolutePath());
						
			target = target + getSubFolderPath(sRecordCategory);
			
			if (sPrefix.length() > 0 && sVersioning.equalsIgnoreCase("true")) {
				copiedFile = new File(target, sFileNameWithoutExt+ " "+ sPrefix + "."+ sFileExt);
			} else {
				copiedFile = new File(target, sFileNameWithoutExt+ "."+ sFileExt);
			}
			
			//Check file existence using isFile()
			if(copiedFile.isFile()){
				logger.info("Target file " + copiedFile + " exists, skip copying");
				return outcome;
			}
			
			if(!copiedFile.isFile()|| !FileUtils.contentEquals(copiedFile, sourceFile)) {
				FileUtils.copyFile(sourceFile, copiedFile);
				logger.info("File copied from "+ sourceFile + " to " + target);
			} else {
				logger.info("File already up-to-date.");
			}
		} catch (Exception e) {
			logger.error("Unable to copy file: " + document.get("id"), e);
			outcome.setResult(ActionResult.FAIL);
			outcome.setMessage("Unable to copy file." + e.toString());
		}
		
		return outcome;
	}
	
	private String getSubFolderPath(String sCategory){
		
		String sMultiLevel = getParameterByKey("auto-multi-level");
		String sResult = "";
				
		if (sMultiLevel!=null && sMultiLevel.equalsIgnoreCase("true")){
			
			if(sCategory!=null){
			
				String sPath[]= sCategory.trim().split("\\.");	
				
				for (int i=0; i< sPath.length; i++){
					
					if(i==0){
						sResult = File.separator + sPath[i] + File.separator;
					}
					else if(i==1){
						sResult = sResult + sPath[i-1] + "." + sPath[i] + File.separator;
					}
					else if(i==2){
						sResult = sResult + sPath[i-2] + "." + sPath[i-1] + "." + sPath[i] + File.separator;
					}
					else if(i==3){
						sResult = sResult + sPath[i-3] + "." + sPath[i-2] + "." + sPath[i-1] + "." + sPath[i] + File.separator;
					}
					else if(i==4){
						sResult = sResult + sPath[i-4] + "." + sPath[i-3] + "." + sPath[i-2] + "." + sPath[i-1] + "." + sPath[i] + File.separator;
					}
				}
				
			}
			
		}
		else{
			
			if(sCategory!=null){
				return File.separator + sCategory.trim() + File.separator;
			}
		}
		
		return sResult;
	}
	
	/**
     * Undo copy file action by delete copied file if exist
     */
    @Override
    public void rollback() 
    		throws RollbackException {
        if (copiedFile != null) {
        	try {
                if (copiedFile.exists()) {
                    logger.debug("Deleting the copied file " + copiedFile);
                    copiedFile.delete();
                }
            } catch (Exception err) {
                logger.error("Unable to remove copied file " + copiedFile.getAbsolutePath() + " for rollback.", err);
        		
                RollbackException exception = new RollbackException(err.getMessage());
        		exception.setRollbackErrorType(RollbackErrorType.DELETE_FAILED);
        		exception.setTargetAbsoluteFilePath(copiedFile.getAbsolutePath());
        		throw exception;
            }
        }
    }
	
}
