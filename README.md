# addAPN

`addAPN` This system provides the AIDL interface to set the APN. The package name is
com.wizarpos.wizarviewagentassistant, and the class name is
com.wizarpos.wizarviewagentassistant.APNManagerService. When the application uses the interface, it
must import the package and add permissions to the Android manifest file.

## Overview

Please note:
Insert the SIM card first. 
The MCC, MNC should be the same as the inserted SIM card.

Permissions: 
The application declares the following permissions in the manifest: 
com.wizarpos.permission.WRITE_APN_SETTINGS

## Features

- **AddByAllArgs**: Add an APN setting. Do not set any parameters to Null.
- **Add**: String add(String name, String apn);
- **AddByMCCAndMNC**:  String addByMCCAndMNC(String name, String apn, String mcc, String mnc); Add an
  APN setting.
- **SetSelected**: boolean setSelected(String name); Set the default APN.
- **getSelected**: Map getSelected(); Get detailed information of the selected APN.
- **Clear**: boolean clear(); Clear all APN settings.
