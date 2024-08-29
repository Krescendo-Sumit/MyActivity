package myactvity.mahyco.model;

public class GTVTravelActivityDataModel {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getKACode() {
        return KACode;
    }

    public void setKACode(String KACode) {
        this.KACode = KACode;
    }

    public String getGTVType() {
        return GTVType;
    }

    public void setGTVType(String GTVType) {
        this.GTVType = GTVType;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        ActivityType = activityType;
    }

    public String getActivityDt() {
        return ActivityDt;
    }

    public void setActivityDt(String activityDt) {
        ActivityDt = activityDt;
    }

    public String getVillageCode() {
        return VillageCode;
    }

    public void setVillageCode(String villageCode) {
        VillageCode = villageCode;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getLastCoordinates() {
        return LastCoordinates;
    }

    public void setLastCoordinates(String lastCoordinates) {
        LastCoordinates = lastCoordinates;
    }

    public String getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(String coordinates) {
        Coordinates = coordinates;
    }

    public String getGTVActivityKM() {
        return GTVActivityKM;
    }

    public void setGTVActivityKM(String GTVActivityKM) {
        this.GTVActivityKM = GTVActivityKM;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    int id;// integer auto increment,
    String ActivityId;// TEXT,
    String KACode;// TEXT,
    String GTVType;// TEXT,
    String ActivityName;// TEXT,
    String ActivityType;// TEXT,
    String ActivityDt;// TEXT,
    String VillageCode;// TEXT,
    String VillageName;// TEXT,
    String LastCoordinates;// TEXT,
    String Coordinates;// TEXT,

    public String getActualKM() {
        return ActualKM;
    }

    public void setActualKM(String actualKM) {
        ActualKM = actualKM;
    }

    public String getDistanceFromPunchKm() {
        return DistanceFromPunchKm;
    }

    public void setDistanceFromPunchKm(String distanceFromPunchKm) {
        DistanceFromPunchKm = distanceFromPunchKm;
    }

    String GTVActivityKM;// TEXT,
    String AppVersion;// TEXT,
    String Remark;// TEXT

    public String getRefrenceId() {
        return RefrenceId;
    }

    public void setRefrenceId(String refrenceId) {
        RefrenceId = refrenceId;
    }

    String RefrenceId;//	nvarchar(50)	Checked
    String ActualKM;//	decimal(18, 2)	Checked
    String DistanceFromPunchKm;//	decimal(18, 2)	Checked
    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    int isSynced;
}
