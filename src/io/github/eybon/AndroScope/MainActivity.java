package io.github.eybon.AndroScope;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.app.ActionBar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.graphics.Color;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity
{

	CustomView drawingZone;

	Boolean visibiltyOfSideLayout;
	ActionBar mActionBar;

    String popUpContents[];
    PopupWindow popUpWindowConfig; 	


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		drawingZone = new CustomView(this);
		drawingZone.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

        setContentView(R.layout.main);

		SeekBar sizePenBar = (SeekBar) findViewById(R.id.seek);
		sizePenBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				drawingZone.setSizePaint(progress);
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {

			}			
		});



        visibiltyOfSideLayout = true;
		Button hide = (Button) findViewById(R.id.button_hide);
		hide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setVisibilityOfSideLayout();
			}
		}); 
		
		createActionBar();		



		popUpWindowConfig = createPopUpWindowConfig();
    }

   	public PopupWindow createPopUpWindowConfig() {
        // add items on the array dynamically
        // format is Company Name:: ID
        List<String> itemList = new ArrayList<String>();
        itemList.add("New");
        itemList.add("Load");
        itemList.add("Save");
        itemList.add("");

        // convert to simple array
        popUpContents = new String[itemList.size()];
        itemList.toArray(popUpContents);

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewConfig = new ListView(this);
      
        // set our adapter and pass our pop up window contents
        listViewConfig.setAdapter(configAdapter(popUpContents));
      
        // set the item click listener
        listViewConfig.setOnItemClickListener(new ConfigOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
      
        // set the list view as pop up window content
        popupWindow.setContentView(listViewConfig);

        return popupWindow;
    }    

    private ArrayAdapter<String> configAdapter(String configArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, configArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                          
                String text = getItem(position);              

                // visual settings for the list item
                TextView listItem = new TextView(MainActivity.this);

                listItem.setText(text);
                listItem.setTag(position);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);
              
                return listItem;
            }
        };
      
        return adapter;
    }    


	public void createActionBar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);

		//LayoutInflater mInflater = LayoutInflater.from(this);
		//View mCustomView = mInflater.inflate(R.menu.menu3, null);

		mActionBar.setCustomView(R.menu.menu3);
		//mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);	

		ImageView config = (ImageView) findViewById(R.id.menu_config);
		config.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				//zToast.makeText(MainActivity.this, "Menu Config", Toast.LENGTH_SHORT).show();
				popUpWindowConfig.showAsDropDown(v, -5, 0);
		    }
		});	
		ImageView prev = (ImageView) findViewById(R.id.menu_prev);
		prev.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Image precedente", Toast.LENGTH_SHORT).show();
		    }
		});	
		ImageView next = (ImageView) findViewById(R.id.menu_next);
		next.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Image suivante", Toast.LENGTH_SHORT).show();
				//drawingZone.nextImage();
		    }
		});	
		ImageView image = (ImageView) findViewById(R.id.menu_image);
		image.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Activity affichage des images", Toast.LENGTH_SHORT).show();
				drawingZone.newImage();
		    }
		});	
	}

    public void setVisibilityOfSideLayout(){
		RelativeLayout left = (RelativeLayout) findViewById(R.id.layoutLeft);
		//LinearLayout right = (LinearLayout) findViewById(R.id.layoutRight);

    	if(visibiltyOfSideLayout==true){
				left.setVisibility(View.GONE);
				mActionBar.hide();
				//right.setVisibility(View.GONE);
				visibiltyOfSideLayout=false;
    	}
    	else{
    			mActionBar.show();
				//right.setVisibility(View.VISIBLE);
				left.setVisibility(View.VISIBLE);
				visibiltyOfSideLayout=true;
    	}

    }

	public void newProjectDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent pickVid = new Intent(Intent.ACTION_GET_CONTENT);
				pickVid.addCategory(Intent.CATEGORY_OPENABLE);
				pickVid.setType("video/*");
				startActivityForResult(pickVid, 1);
			}
		})
		.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent takeVid = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(takeVid, 0);
			}
		});

		AlertDialog sourcesDialog = builder.create();
		sourcesDialog.show();

		Button gallery = sourcesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		gallery.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(android.R.drawable.ic_menu_gallery), null, null, null);
		gallery.setText("");

		Button camera = sourcesDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		camera.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(android.R.drawable.ic_menu_camera), null, null, null);
		camera.setText("");
	}

}
