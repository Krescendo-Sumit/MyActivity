package myactvity.mahyco.app;

/**
 * Created by 97260828 on 08-09-2017.
 */

public class ProductMaster {

    private final String prd_Desc;
    private final String Prd_code;

    public ProductMaster(String Prd_code, String prd_Desc){
        this.Prd_code = Prd_code;
        this.prd_Desc = prd_Desc;
    }

    public String Prd_Desc() {
        return prd_Desc;
    }

    public String  Prd_Code() {
        return Prd_code;
    }
    @Override
    public String toString() {
        return prd_Desc;
    }
}
