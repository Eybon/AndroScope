package io.github.eybon.AndroScope;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ConfigOnItemClickListener implements OnItemClickListener {
   
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

        // get the context and main activity to access variables
        Context mContext = v.getContext();
        MainActivity mainActivity = ((MainActivity) mContext);
       
        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);
       
        // dismiss the pop up
        mainActivity.popUpWindowConfig.dismiss();
       
        // get the text and set it as the button text
       
        //Toast.makeText(mContext, "Selected Positon is: " + arg2, 100).show();
        if(arg2==0){
            mainActivity.newProjectDialog();
        }
        if(arg2==1){
            mainActivity.loadingProject();
        }
        if(arg2==2){
            mainActivity.savingProject();
        }
       
    }

}