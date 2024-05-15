package com.cloudpos.addapn;

public class APNMode {
    private String carrier;
    private  String apn ;
    private  String mcc;
    private  String mnc;
    private String protocol;
    private String type;
    private String roaming_protocol;
    public String getCarrier() {
        return carrier;
    }
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public String getApn() {
        return apn;
    }
    public void setApn(String apn) {
        this.apn = apn;
    }
    public String getProtocol() { return protocol;}
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public String getRoaming_protocol() {
        return roaming_protocol;
    }
    public void setRoaming_protocol(String roaming_protocol) { this.roaming_protocol = roaming_protocol; }
    public String getMcc() {
        return mcc;
    }
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }
    public String getMnc() {
        return mnc;
    }
    public void setMnc(String mnc) {
        this.mnc = mnc;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String toApnString(){
        return "carrier = "+ getCarrier() + "\nmcc = " + getMcc() + "\nmnc = " +getMnc()
                + "\napn = " +getApn() + "\ntype = " +getType() + "\nprotocol = " + getProtocol()
                + "\nroaming_protocol = " + getRoaming_protocol();
    }
}
