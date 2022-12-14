 INSERT INTO ACTION_PLUGINS (ID, DISPLAY_ORDER, CLASS_NAME, NAME, DISPLAY_NAME, DESCRIPTION, FIRE_ONCE_PER_RULE, REPOSITORY_TYPE)
 VALUES (1608169924988, 5, 'com.nextlabs.smartclassifier.plugin.baes.action.sendrecordemail.SendRecordEmail', 'SEND_RECORD_REPORTING_EMAIL', 'BAE email for records reporting', 'Email for records reporting', 1,'SHARED FOLDER');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1608169924900, 1608169924988, 1, 'P', 0, 0, 'String', 'Folder for storing snapshot files (must be on the UI machine)', 'snapshots-folder', 1, '\\BAESC02W12R2\shared')

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1608169924901, 1608169924988, 2, 'P', 1, 0, 'Email', 'Recipient', 'email-recipient', 0)

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1608169924902, 1608169924988, 3, 'P', 0, 0, 'String', 'Subject', 'email-subject', 0)

INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1608169924903, 1608169924988, 4, 'P', 0, 0, 'Text', 'Content', 'email-content', 0)
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1608169924904, 1608169924988, 5, 'P', 0, 0, 'Boolean', 'Send Empty Email', 'send-empty', 1, 'false')
 
  INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1608169924905, 1608169924988, 6, 'P', 0, 0, 'String', 'Record category key', 'record-category-key-name', 1, 'record_category');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1608169924906, 1608169924988, 7, 'P', 0, 0, 'String', 'Last modified key', 'last-modified-date-key-name', 1, 'last_modified_date');
  
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER, FIXED_VALUE)
 VALUES (1608169924907, 1608169924988, 8, 'P', 0, 0, 'String', 'Activity Key', 'activity-key-name', 1, 'record_activity');
 
 INSERT INTO ACTION_PLUGIN_PARAMS (ID, ACTION_PLUGIN_ID, DISPLAY_ORDER, PARAM_TYPE, COLLECTIONS, KEY_VALUE, DATA_TYPE, LABEL, IDENTIFIER, FIXED_PARAMETER)
 VALUES (1608169924908, 1608169924988, 9, 'P', 0, 0, 'String', 'Day/s to expired', 'days-to-expired', 0);
 
 