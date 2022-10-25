INSERT INTO ACTION_PLUGINS (ID, DISPLAY_ORDER, CLASS_NAME, NAME, DISPLAY_NAME, DESCRIPTION, FIRE_ONCE_PER_RULE, REPOSITORY_TYPE)
 VALUES (1312300900000001, 1, 'com.nextlabs.smartclassifier.plugin.baes.action.baecopy.BAECopy', 'BAE_COPY', 'BAE Copy', 'BAE Copy with versioning capability', 0 ,'SHARED FOLDER');

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1412301601000001, 1312300900000001, 1, 'P', 0, 0, 'String', 'Destination folder', 'target', 0);
 
INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000002, 1312300900000001, 2, 'P', 0, 0, 'String', 'Exclude folder/s for file copy', 'exclude-folder', 1, 'RecordCenter');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000003, 1312300900000001, 3, 'P', 0, 0, 'Boolean', 'File copy versioning', 'enable-versioning', 1, 'true');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000004, 1312300900000001, 4, 'P', 0, 0, 'String', 'File prefix name for versioning', 'file-prefix-key-name', 1, 'record_declaration_date');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000005, 1312300900000001, 5, 'P', 0, 0, 'Boolean', 'Auto multi level',  'auto-multi-level', 1, 'true');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000006, 1312300900000001, 6, 'P', 0, 0, 'String', 'Record category key', 'record-category-key-name', 1, 'record_category');
 
 INSERT INTO ACTION_PLUGINS (ID, DISPLAY_ORDER, CLASS_NAME, NAME, DISPLAY_NAME, DESCRIPTION, FIRE_ONCE_PER_RULE, REPOSITORY_TYPE)
 VALUES (1312300900000002, 2, 'com.nextlabs.smartclassifier.plugin.baes.action.discard.DiscardExpiredDocument', 'BAE_DISCARD', 'BAE discard record', 'Discard/Delete expired record/s', 0,'SHARED FOLDER');
 
INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000011, 1312300900000002, 1, 'P', 0, 0, 'String', 'Exclude folder/s for deletion', 'exclude-folder', 1, 'exclude');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000012, 1312300900000002, 2, 'P', 0, 0, 'String', 'Record category key', 'record-category-key-name', 1, 'record_category');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000013, 1312300900000002, 3, 'P', 0, 0, 'String', 'Last modified key', 'last-modified-date-key-name', 1, 'last_modified_date');
  
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000014, 1312300900000002, 4, 'P', 0, 0, 'String', 'Activity Key', 'activity-key-name', 1, 'record_activity');
 
 INSERT INTO ACTION_PLUGINS (ID, DISPLAY_ORDER, CLASS_NAME, NAME, DISPLAY_NAME, DESCRIPTION, FIRE_ONCE_PER_RULE, REPOSITORY_TYPE)
 VALUES (1312300900000003, 3, 'com.nextlabs.smartclassifier.plugin.baes.action.sendfilteredemail.SendFilteredEmail', 'SEND_FILTERED_EMAIL', 'BAE email for expired record/s', 'Email for expired record/s', 1,'SHARED FOLDER');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000021, 1312300900000003, 1, 'P', 0, 0, 'String', 'Folder for storing snapshot files (must be on the UI machine)', 'snapshots-folder', 1, '\\BAESC02W12R2\shared')

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1412301601000022, 1312300900000003, 2, 'P', 1, 0, 'Email', 'Recipient', 'email-recipient', 0)

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1412301601000023, 1312300900000003, 3, 'P', 0, 0, 'String', 'Subject', 'email-subject', 0)

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1412301601000024, 1312300900000003, 4, 'P', 0, 0, 'Text', 'Content', 'email-content', 0)
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000025, 1312300900000003, 5, 'P', 0, 0, 'Boolean', 'Send Empty Email', 'send-empty', 1, 'false')
 
  INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000026, 1312300900000003, 6, 'P', 0, 0, 'String', 'Record category key', 'record-category-key-name', 1, 'record_category');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000027, 1312300900000003, 7, 'P', 0, 0, 'String', 'Last modified key', 'last-modified-date-key-name', 1, 'last_modified_date');
  
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1412301601000028, 1312300900000003, 8, 'P', 0, 0, 'String', 'Activity Key', 'activity-key-name', 1, 'record_activity');
 
 --Add tag for last modified date plugin`
INSERT INTO ACTION_PLUGINS (ID, DISPLAY_ORDER, CLASS_NAME, NAME, DISPLAY_NAME, DESCRIPTION, FIRE_ONCE_PER_RULE, REPOSITORY_TYPE)
 VALUES (1512388900000003, 4, 'com.nextlabs.smartclassifier.plugin.baes.action.baeadddatetag.BAETagDate', 'BAE_ADD_DATE_TAG (SHARED FOLDER)', 'BAE Retain Last Modified Date', 'BAE Retain last modified date', 0,'SHARED FOLDER');
 
INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1512301601008821, 1512388900000003, 1, 'P', 0, 0, 'String', 'Exclude folder/s for deletion', 'exclude-folder', 1, 'exclude');
 
INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1512301601008822, 1512388900000003, 2, 'P', 0, 0, 'String', 'Date Format', 'date-format', 1, 'dd/MM/yyyy - hh:mm:ss');
 
INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1512301601008823, 1512388900000003, 1, 'P', 0, 0, 'Boolean', 'Overwrite existing tag value', 'overwrite', 0, 'true');

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
VALUES (1512301601008824, 1512388900000003, 2, 'P', 0, 0, 'String', 'Tag Name', 'tag-name', 0, 'last_modified_date_plugin');

UPDATE SYSTEM_CONFIGS SET VALUE='id file_id directory folder_url document_name repository_type file_type creation_date last_modified_date author last_author site_url serverrelativeurl_t repository_path last_modified_date_millisecond record_category_t last_modified_date record_activity_t record_declaration_date_t record_declaration_date_tdt' WHERE IDENTIFIER ='solr.indexing.query.ruleEngineFields';
 
 