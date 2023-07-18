package com.mahyco.customercomplaint.ccfinterfaces;

import com.mahyco.customercomplaint.ccfnetwork.CCFDepotPojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFDistrictPojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFStatePojoModel;
import com.mahyco.customercomplaint.ccfnetwork.CCFTalukaPojoModel;

public interface CCFAllListListener {
    //    void onStateListResponse(List<CCFStateResponse> stateResponseList);
    void onStateListResponse(CCFStatePojoModel ccfStatePojoModel);

//    void onTalujaListResponse(List<CCFTalukaResponse> talukaResponseList);
    void onTalukaListResponse(CCFTalukaPojoModel ccfTalukaPojoModel);

    //    void onDistrictListResponse(List<CCFDistrictResponse> districtResponseList);
    void onDistrictListResponse(CCFDistrictPojoModel ccfDistrictPojoModel);
    void onDepotListResponse(CCFDepotPojoModel ccfDepotPojoModel);

    void onFailure(String string,Throwable t);
}
