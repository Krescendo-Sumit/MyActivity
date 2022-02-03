package myactvity.mahyco.helper;

public class POPDisplayModel {

    private String uId;
    private String toolCategory;
    private String cropType;
    private String product;
    private String toolCounts;
    private String  toolName;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getToolCategory() {
        return toolCategory;
    }

    public void setToolCategory(String toolCategory) {
        this.toolCategory = toolCategory;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getProductName() {
        return product;
    }

    public void setProductName(String productName) {
        this.product = productName;
    }

    public String getCount() {
        return toolCounts;
    }

    public void setCount(String posterCount) {
        this.toolCounts = posterCount;
    }


    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
}
