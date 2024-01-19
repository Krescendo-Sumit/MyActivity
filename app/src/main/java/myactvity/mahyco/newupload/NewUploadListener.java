package myactvity.mahyco.newupload;

public interface NewUploadListener {
    public void onResult(String result);

    public void onResultFarmer(String result);
    public void onResultDistributor(String result,String id);
}
