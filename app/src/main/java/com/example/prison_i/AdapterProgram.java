package com.example.prison_i;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterProgram extends RecyclerView.Adapter<AdapterProgram.ProgramViewHolder>{

    private  String[] userNameData;
    private String[] userStepsData;
   // private String[] userAddressData;
    private String[] userStatusText;
    private int[] userlocStatus;


    public AdapterProgram(String[] data , String[] StatusText , String[] StepsData ,int[] locStatus )
    //, String[] StepsData , String[] addressData , String[] StatusText, int[] ImageStatusText){

    {  this.userNameData = data;
        this.userStepsData = StepsData;
      //  this.userAddressData = addressData;
        this.userStatusText = StatusText;
        this.userlocStatus = locStatus;
    }


    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycler_view_item,viewGroup,false);

        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder programViewHolder, int i) {
        String name = userNameData[i];
        String steps = userStepsData[i];
      //  String address = userAddressData[i];
        String statusT = userStatusText[i];
        int imageStatusT = userlocStatus[i];


        programViewHolder.textView.setText(name);
        programViewHolder.textDataView.setText(steps);
     //   programViewHolder.textAddressView.setText(address);
        programViewHolder.textStatusView.setText(statusT);

        if(imageStatusT == 0){programViewHolder.imgview.setImageResource(R.drawable.greenbutton); }
        else if(imageStatusT == 1){programViewHolder.imgview.setImageResource(R.drawable.bulebutton);
            Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
            animation.setDuration(500); //1 second duration for each animation cycle
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
            animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
           programViewHolder.imgview.startAnimation(animation);}
        //else if(imageStatusT == 2){programViewHolder.imgview.setImageResource(R.drawable.greenbutton); }*/
         }

    @Override
    public int getItemCount() {
        return  userNameData.length;
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        ImageView imgview;
        TextView textView;
        TextView textDataView;
        TextView textAddressView;
        TextView textStatusView;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);

            imgview = (ImageView) itemView.findViewById(R.id.statusImageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textDataView = (TextView) itemView.findViewById(R.id.textView2);
         //   textAddressView = (TextView) itemView.findViewById(R.id.AddressTextView);
            textStatusView = (TextView) itemView.findViewById(R.id.statusView);

        }
    }
}
