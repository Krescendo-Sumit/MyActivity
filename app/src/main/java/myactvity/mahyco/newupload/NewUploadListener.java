package myactvity.mahyco.newupload;

public interface NewUploadListener {
    public void onResult(String result);

    public void onResultFarmer(String result);
    public void onResultDistributor(String result,String id);
    public void onPosteringDataUpload(String result,String id);
    void onResultStartTravel(String result);

    void onResultEndTravel(String result);

    void onResultNewRetailerAndDistributorTagged(String result);

    void onVillageMeetingDone(String result,String id);
    void onCropShowDone(String result,String id);
}
