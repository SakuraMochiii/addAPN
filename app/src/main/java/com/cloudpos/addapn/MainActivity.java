package com.cloudpos.addapn;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.wizarviewagentassistant.aidl.IAPNManagerService;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private final String Tag = "add_apn";
    private TextView mAddApn, mApnMessage, mStatus;
    private Button setSelected, mAddMccMnc, mAdd;
    private ArrayList<APNMode>[] apnItems;
    private IAPNManagerService iapnManagerService;
    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iapnManagerService = IAPNManagerService.Stub.asInterface(service);
            Log.d("IAPNManagerService", "IAPNManagerService  bind success.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initParams();
    }

    private void initParams() {
        mAddApn = findViewById(R.id.add_apn);
        mAddMccMnc = findViewById(R.id.addByMCCAndMNC);
        mAdd = findViewById(R.id.add);
        setSelected = findViewById(R.id.setSelected);
        mApnMessage = findViewById(R.id.apn_message);
        mApnMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        mApnMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lines = mApnMessage.getHeight();
                int lastLine = mApnMessage.getLayout().getHeight();
                Log.i("total lines: ", "" + lines);
                Log.i("text lines: ", "" + lastLine);
                if (lastLine >= lines) {
                    mApnMessage.scrollTo(0, mApnMessage.getLayout().getLineTop(mApnMessage.getLineCount()) - mApnMessage.getHeight());
                }
            }
        });
        mStatus = findViewById(R.id.status);
        mApnMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        ArrayList<APNMode> modeList = getApnItems();
        apnItems = new ArrayList[]{modeList};
        mAddApn.setOnClickListener(listener);
        mAddMccMnc.setOnClickListener(listener);
        mAdd.setOnClickListener(listener);
        setSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select an Option");
                List<String> names = new ArrayList<>(modeList.size());
                String[] options = new String[modeList.size()];
                for (APNMode mode : modeList) {
                    names.add(mode.getCarrier());
                }
                names.toArray(options);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (iapnManagerService == null) {
                                return;
                            }
                            boolean result = iapnManagerService.setSelected(options[which]);
                            mApnMessage.append("Set selected apn ：" + options[which] + " " + result + "\n");
                            mStatus.setText("Set selected apn staus：" + result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (apnItems[0] != null && apnItems[0].size() > 0) {
                Log.d("onClick", "onClick0");
                for (int pos = 0; pos < apnItems[0].size(); pos++) {
                    try {
                        Log.d("onClick", "onClick1");
                        Log.d("onClick", apnItems[0].get(pos).getCarrier() + " " +
                                apnItems[0].get(pos).getApn() + " " +
                                apnItems[0].get(pos).getMcc() + " " +
                                apnItems[0].get(pos).getMnc());
                        String status = "";
                        if (v.getId() == R.id.add_apn) {
                            status = iapnManagerService.addByAllArgs(
                                    apnItems[0].get(pos).getCarrier(),
                                    apnItems[0].get(pos).getApn(),
                                    apnItems[0].get(pos).getMcc(),
                                    apnItems[0].get(pos).getMnc(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    apnItems[0].get(pos).getProtocol(),
                                    apnItems[0].get(pos).getRoaming_protocol(),
                                    apnItems[0].get(pos).getType(),
                                    null,
                                    null,
                                    null
                            );
                        } else if (v.getId() == R.id.addByMCCAndMNC) {
                            status = iapnManagerService.addByMCCAndMNC(
                                    apnItems[0].get(pos).getCarrier(),
                                    apnItems[0].get(pos).getApn(),
                                    apnItems[0].get(pos).getMcc(),
                                    apnItems[0].get(pos).getMnc());
                        } else if (v.getId() == R.id.add) {
                            status = iapnManagerService.add(
                                    apnItems[0].get(pos).getCarrier(),
                                    apnItems[0].get(pos).getApn());
                        }
                        Log.d("onClick", "onClick2");
                        mStatus.setText("add apn staus：" + status);
                        mApnMessage.append("add apn staus：" + status);
                        Log.e(Tag, "add apn staus = " + status);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                mStatus.setText("No APN needs to be added!");
            }
        }
    };

    private ArrayList<APNMode> getApnItems() {
        ArrayList<APNMode> datas = new ArrayList<APNMode>();
        ArrayList<APNMode> newDatas = new ArrayList<APNMode>();
        String tagName;
        XmlResourceParser xmlPullParser = getResources().getXml(R.xml.apn);
        try {
            int mEventType = xmlPullParser.getEventType();
            while (mEventType != XmlPullParser.END_DOCUMENT) {
                if (mEventType == XmlPullParser.START_TAG) {
                    tagName = xmlPullParser.getName();
                    if (tagName.equals("apn")) {
                        APNMode item = parseToApnItem(xmlPullParser);
                        datas.add(item);
                    }
                }
                mEventType = xmlPullParser.next();
            }
            if (datas != null && datas.size() > 0) {
                StringBuilder message = new StringBuilder();
                for (int pos = 0; pos < datas.size(); pos++) {
                    message.append("APN: " + datas.get(pos).toApnString() + "\n\n");
                    newDatas.add(datas.get(pos));
                }
                mApnMessage.setText(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDatas;
    }

    public static APNMode parseToApnItem(XmlPullParser xmlPullParser) {
        APNMode mainItem = new APNMode();
        String carrier = xmlPullParser.getAttributeValue(null, "carrier");
        String mcc = xmlPullParser.getAttributeValue(null, "mcc");
        String mnc = xmlPullParser.getAttributeValue(null, "mnc");
        String apn = xmlPullParser.getAttributeValue(null, "apn");
        String type = xmlPullParser.getAttributeValue(null, "type");
        String protocol = xmlPullParser.getAttributeValue(null, "protocol");
        String roaming_protocol = xmlPullParser.getAttributeValue(null, "roaming_protocol");
        mainItem.setCarrier(carrier);
        mainItem.setMcc(mcc);
        mainItem.setMnc("0" + mnc); //getAttributeValue ignores the leading 0, so it adds 0
        mainItem.setApn(apn);
        mainItem.setType(type);
        mainItem.setProtocol(protocol.toUpperCase());
        mainItem.setRoaming_protocol(roaming_protocol.toUpperCase());
        return mainItem;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindServer();
    }

    private void bindServer() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.wizarpos.wizarviewagentassistant",
                "com.wizarpos.wizarviewagentassistant.APNManagerService");
        intent.setComponent(componentName);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    /**
     * clear apn
     *
     * @param view
     */
    public void clearApn(View view) {
        try {
            if (iapnManagerService == null) {
                return;
            }
            boolean retval = iapnManagerService.clear();
            mStatus.setText("Clear apn staus：" + retval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear apn
     *
     * @param view
     */
    public void clearWithSlot(View view) {
        try {
            if (iapnManagerService == null) {
                return;
            }
            boolean retval = iapnManagerService.clearWithSlot(0);
            mStatus.setText("Clear apn with slot: 0 staus：" + retval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get selected apn
     *
     * @param view
     */
    public void getSelected(View view) {
        try {
            if (iapnManagerService == null) {
                return;
            }
            Map result = iapnManagerService.getSelected();
            mApnMessage.append("Get selected apn ：" + JSONObject.toJSONString(result) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set selected apn
     *
     * @param view
     */
    public void setSelected(View view) {
        try {
            if (iapnManagerService == null) {
                return;
            }
            boolean result = iapnManagerService.setSelected("BeMobile");
            mStatus.setText("Set selected apn staus：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * query selected apn by name and value
     *
     * @param view
     */
    public void queryAll(View view) {
        try {
            if (iapnManagerService == null) {
                return;
            }
            List result = iapnManagerService.query("name", "BeMobile");
            mApnMessage.append("query apn by name and value ：" + JSONObject.toJSONString(result) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * query selected apn by value
     *
     * @param view
     */
    public void queryByName(View view) {
        try {
            if (iapnManagerService == null) {
                return;
            }
            List result = iapnManagerService.queryByName("BeMobile");
            mApnMessage.append("query apn by value ：" + JSONObject.toJSONString(result) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}