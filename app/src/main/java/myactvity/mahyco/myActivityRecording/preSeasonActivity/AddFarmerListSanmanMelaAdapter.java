package myactvity.mahyco.myActivityRecording.preSeasonActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import myactvity.mahyco.R;

public class AddFarmerListSanmanMelaAdapter extends RecyclerView.Adapter<AddFarmerListSanmanMelaAdapter.AddFarmerListHolder> {

    private Context context;
    private ArrayList<SanmanMelaModel> mlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences locdata;
    SharedPreferences.Editor loceditor;


    public AddFarmerListSanmanMelaAdapter(Context context, ArrayList<SanmanMelaModel> mlist) {
        this.context = context;
        this.mlist = mlist;

        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        locdata = context.getSharedPreferences("locdata", 0); // 0 - for private mode
        loceditor = locdata.edit();
        setHasStableIds(true);

    }

    @NonNull
    @Override
    public AddFarmerListSanmanMelaAdapter.AddFarmerListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_sanman_mela_farmer_details, null);
        return new AddFarmerListSanmanMelaAdapter.AddFarmerListHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final AddFarmerListSanmanMelaAdapter.AddFarmerListHolder demoModelViewHolder, final int i) {


        if (!mlist.isEmpty()) {
            final SanmanMelaModel sanmanMelaModel = mlist.get(i);
            demoModelViewHolder.txtSNo.setText(String.valueOf(i + 1));
            demoModelViewHolder.txtFarmerNo.setText(sanmanMelaModel.getFarmerContactNo());
            demoModelViewHolder.txtFarmerName.setText(sanmanMelaModel.getFarmerName());
        }
    }

    @Override
    public int getItemCount() {
        return (mlist != null ? mlist.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class AddFarmerListHolder extends RecyclerView.ViewHolder {

        TextView txtFarmerNo, txtFarmerName, txtSNo;

        AddFarmerListHolder(@NonNull View itemView) {
            super(itemView);
            txtSNo = (TextView) itemView.findViewById(R.id.txtSNo);
            txtFarmerName = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtFarmerNo = (TextView) itemView.findViewById(R.id.txtFarmerNo);
        }
    }

}
