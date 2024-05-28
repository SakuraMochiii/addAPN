# addAPN

`addAPN` The system provides the AIDL interface to set the APN. The package name is
com.wizarpos.wizarviewagentassistant, and the class name is
com.wizarpos.wizarviewagentassistant.APNManagerService. When the application uses the interface, it
must import the package and add permissions to the Android manifest file.

## overview

Please note that:
Insert SIM card first
The MCC, MNC should be the same as SIM card inserted

Permission
The application declares the following permissions in the manifest：
com.wizarpos.permission.WRITE_APN_SETTINGS

## Features

- **AddByAllArgs**: Add an APN setting. All paramers can not be set to Null.
- **Add**: String add(String name, String apn);
- **AddByMCCAndMNC**:  String addByMCCAndMNC(String name, String apn, String mcc, String mnc);Add an
  APN setting.
- **SetSelected**: boolean setSelected(String name); Set default APN.
- **getSelected**: Map getSelected();Get detail information of the selected APN.
- **Clear**: boolean clear();Clear all APN settings.
- **clearWithSlot**: boolean clearWithSlot();Clear APN settings by slot.
- **query**:  List query(String columnName, String value); query APN settings by column name and
  value.
- **queryByName**: List queryByName(String value);query APN settings by value.