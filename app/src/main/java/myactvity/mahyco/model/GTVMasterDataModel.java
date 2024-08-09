package myactvity.mahyco.model;

public class GTVMasterDataModel {
    int id;// integer auto increment,
    String mdocode ;//text,

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMdocode() {
        return mdocode;
    }

    public void setMdocode(String mdocode) {
        this.mdocode = mdocode;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getStartaddress() {
        return startaddress;
    }

    public void setStartaddress(String startaddress) {
        this.startaddress = startaddress;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getTxtkm() {
        return txtkm;
    }

    public void setTxtkm(String txtkm) {
        this.txtkm = txtkm;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public int getSdate() {
        return sdate;
    }

    public void setSdate(int sdate) {
        this.sdate = sdate;
    }

    public String getGTVType() {
        return GTVType;
    }

    public void setGTVType(String GTVType) {
        this.GTVType = GTVType;
    }

    public String getGTVSession() {
        return GTVSession;
    }

    public void setGTVSession(String GTVSession) {
        this.GTVSession = GTVSession;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    String coordinate ;//text,
    String startaddress ;//text,
    String startdate;// text,
    String dist;// text,
    String taluka;// text,
    String village;// text,
    String imgname;// text,
    String imgpath;// text,
    String txtkm;// text,
    String place;// text,
    String vehicletype;// text,
    int sdate;// INTEGER,
    String GTVType;// text,
    String GTVSession;// text,
    String Remark;// text,
    int ParentId;// INTEGER

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    int isSynced;
}
