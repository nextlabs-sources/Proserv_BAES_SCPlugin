package com.nextlabs.smartclassifier.plugin.baes.action.sendfilteredemail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;

import com.nextlabs.smartclassifier.constant.ActionResult;
import com.nextlabs.smartclassifier.constant.SolrPredefinedField;
import com.nextlabs.smartclassifier.constant.SystemConfigKey;
import com.nextlabs.smartclassifier.mail.Mail;
import com.nextlabs.smartclassifier.mail.MailRecipient;
import com.nextlabs.smartclassifier.mail.MailService;
import com.nextlabs.smartclassifier.plugin.DataSourceManager;
import com.nextlabs.smartclassifier.plugin.action.ActionOutcome;
import com.nextlabs.smartclassifier.plugin.action.ExecuteOncePerRule;
import com.nextlabs.smartclassifier.plugin.action.SharedFolderAction;
import com.nextlabs.smartclassifier.plugin.baes.action.discard.DatabaseUtil;
import com.nextlabs.smartclassifier.solr.QueryEngine;

public class SendFilteredEmail 
	extends SharedFolderAction implements ExecuteOncePerRule {
	
	private static final Logger logger = LogManager.getLogger(SendFilteredEmail.class);
	private static final String ACTION_NAME = "SEND_FILTERED_EMAIL";
	private static final String DATA_SOURCE_NAME = "BAE-System";
	private File snapshotFile;
	private String snapshotFileName;
	Map<String, String> sytemConfigs;
	private HttpSolrClient client;
	private static String fields= "id directory record_category_t document_name last_modified_date record_activity_t";

	
	public SendFilteredEmail() {
		super(ACTION_NAME);
	}
	
	@Override
	public ActionOutcome execute(final String solrQuery) throws Exception {

		sytemConfigs = getSystemConfigs();

		String targetFolder = getParameterByKey("snapshots-folder");

		ActionOutcome outcome = new ActionOutcome();
		outcome.setResult(ActionResult.SUCCESS);
		outcome.setMessage("Email sent successfully.");

		logger.debug("Trying to get the list of documents");

		try {

			String indexerURL = getSystemConfigs().get(SystemConfigKey.INDEXER_URL);
            String userName = getSystemConfigs().get(SystemConfigKey.INDEXER_USERNAME);
            String passWord = getSystemConfigs().get(SystemConfigKey.INDEXER_PASSWORD);

            if (StringUtils.isNotBlank(indexerURL)) {

            	logger.debug("The indexer URL found is = " + indexerURL);

    			QueryEngine queryEngine = new QueryEngine(indexerURL, userName, passWord);
    			
				client = queryEngine.getHttpSolrClient();

				long totalNumberOfDocuments = queryEngine.getDocumentCount(solrQuery);

				logger.debug("The number of documents found = " + totalNumberOfDocuments);

				SolrDocumentList documentList = null;

				if (totalNumberOfDocuments > 0 || getParameterByKey("send-empty").equalsIgnoreCase("true")) {

					try {

						createSnapshotFile(getRuleId());

						SolrQuery query = new SolrQuery();
						query.setFields(fields);
						query.setQuery(solrQuery);
						query.setRows(500);
						query.setSort(SolrPredefinedField.ID, ORDER.asc);

						String cursorMark = CursorMarkParams.CURSOR_MARK_START;
						boolean done = false;
						long numberOfDocumentsFound = 0;
						List<SolrDocument> filterFiles;

						while (!done) {

							query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);

							QueryRequest req = new QueryRequest(query);
						    req.setBasicAuthCredentials(userName, passWord);
						    QueryResponse response = req.process(client);

							String nextCursorMark = response.getNextCursorMark();

							documentList = response.getResults();

							numberOfDocumentsFound = documentList.size();

							logger.info("Number of document found is" + numberOfDocumentsFound);

							filterFiles = new ArrayList<SolrDocument>();

							checkExpiredDate(documentList, filterFiles);

							appendToSnapshotFile(filterFiles);

							if (cursorMark.equals(nextCursorMark)) {
								done = true;
							}

							cursorMark = nextCursorMark;
						}

						File targetFile = new File(targetFolder, FilenameUtils.getName(snapshotFile.getAbsolutePath()));

						FileUtils.moveFile(snapshotFile, targetFile);

						logger.info("File moved to " + targetFile);

						outcome = sendEmail(FilenameUtils.getName(targetFile.getAbsolutePath()), outcome);

					} catch (Exception err) {
						logger.error(err.getMessage(), err);
						outcome.setResult(ActionResult.FAIL);
						outcome.setMessage("Email send failed." + err.toString());
					}

				} else {
					logger.info("Skip sending email because matched documents is 0");
					outcome.setMessage("Skip sending email because matched documents is 0.");
				}

			} else {
				logger.info("Skip sending email indexer url is not defined");
				outcome.setMessage("Skip sending email indexer url is not defined");
			}

		} catch (Exception err) {
			logger.error(ACTION_NAME + ": " + err.getMessage(), err);

			outcome.setResult(ActionResult.FAIL);
			outcome.setMessage("Email send failed. " + err.getMessage());
		} finally {
			if (client != null)
				client.close();
		}

		return outcome;

	}
	
	private void createSnapshotFile(Long ruleID) throws IOException {

		String pwd = Paths.get(".").toAbsolutePath().getParent().getParent().normalize().toString() + "\\" + "snapshots"
				+ "\\";

		snapshotFileName = pwd + ruleID + "_" + System.currentTimeMillis() + ".csv";

		logger.info("The snapshot file name = " + snapshotFileName);

		snapshotFile = new File(snapshotFileName);

		if (!snapshotFile.getParentFile().exists()) {
			snapshotFile.getParentFile().mkdirs();
			logger.info("Created the local snapshot directory");
		}

		String header = "id | directory | document_name\n";

		FileUtils.writeStringToFile(snapshotFile, header, "UTF-8", true);

	}
	
	private void appendToSnapshotFile(List<SolrDocument> filterFiles) throws IOException {

		if (filterFiles != null && filterFiles.size() > 0) {

			logger.debug("Total number of records to be written to the snapshot = " + filterFiles.size());

			for (SolrDocument solrDocument : filterFiles) {
				String line = "";
				line += "\"" + solrDocument.get("id") + "\"|";
				line += "\"" + solrDocument.get("directory") + "\"|";
				line += "\"" + solrDocument.get("document_name") + "\"\n";
				FileUtils.writeStringToFile(snapshotFile, line, "UTF-8", true);
			}

		}

	}
	
	
	private void checkExpiredDate(List<SolrDocument> matchedFiles, List<SolrDocument> filterFiles) {
		try {
			DatabaseUtil util = new DatabaseUtil(DataSourceManager
					.getInstance().getDBDS(DATA_SOURCE_NAME), false);

			for (SolrDocument matchedFile : matchedFiles) {

				try {
					String sResult[] = util
							.getRetentionPeriod((String) matchedFile
									.get(getParameterByKey("record-category-key-name")
											+ "_t"));

					if (sResult != null) {
						if (sResult[0].equalsIgnoreCase("Standard")) {
							try {
								if (util.handleSimpleCase(
										getParameterByKey("last-modified-date-key-name"),
										sResult[1], matchedFile))
									filterFiles.add(matchedFile);
							} catch (Exception e) {
								logger.error(e.toString(), e);
							}
						} else {
							try {
								if (util.handleComplexCase(
										getParameterByKey("activity-key-name"),
										sResult[1], sResult[2], matchedFile))
									filterFiles.add(matchedFile);
							} catch (Exception e) {
								logger.error(e.toString(), e);
							}
						}
					} else {
						logger.info("Skip checking " + matchedFile.get("id")
								+ " due to no db matching record category");
					}
				} catch (Exception e) {
					logger.error("Error checking file: "
							+ matchedFile.get("id"));
				}

			}
		} catch (Exception err) {
			logger.error(err.getMessage(), err);
		}
	}
	
	protected ActionOutcome sendEmail(String fileName, ActionOutcome outcome) throws Exception {

		Mail email = new Mail();

		email.setSubject(getParameterByKey("email-subject"));

		StringBuffer emailBody = new StringBuffer(getParameterByKey("email-content"));
		

		String url = sytemConfigs.get(SystemConfigKey.SMART_CLASSIFIER_URL) + "download?id=";

		url += fileName;

		logger.debug("the download url =" + url);

		emailBody.append("<hr style=\"border-top: dotted 1px;\" />");

		emailBody.append("<p>Please click this <a href=\"" + url
				+ "\">link</a> to download the file, containing the documents found.</p>");

		email.setContent(emailBody.toString());

		email.setContentType("text/html");

		String[] recipientList = getParametersByKey("email-recipient")
				.toArray(new String[getParametersByKey("email-recipient").size()]);

		if (recipientList != null) {
			List<MailRecipient> emailAddresses = new ArrayList<MailRecipient>(recipientList.length);
			for (String recipient : recipientList) {
				emailAddresses.add(new MailRecipient(RecipientType.TO, new InternetAddress(recipient)));
			}

			// printing the emailaddresses
			for (MailRecipient emailAddress : emailAddresses) {
				logger.debug("Email To = " + emailAddress.getAddress());
			}

			email.setRecipients(emailAddresses);
		}

		if (MailService.sendMail(email)) {
			logger.info("Email sent successfully");
		} else {
			logger.error("Email sending failed");
			outcome.setResult(ActionResult.FAIL);
			outcome.setMessage("Email send failed.");
		}
		
		return outcome;
	}


}
