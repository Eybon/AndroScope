package io.github.eybon.AndroScope;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.io.*;
import android.util.Log;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import java.lang.Thread;
import java.util.concurrent.CountDownLatch;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;


public class MainActivity extends Activity
{

	CustomView drawingZone;

	Boolean visibiltyOfSideLayout;
	ActionBar mActionBar;

    String popUpContents[];
    PopupWindow popUpWindowConfig; 	

    String nameVideo = "video";
    Uri video;

    Boolean isPlaying;
    AnimationDrawable animation;

    private String nameRepository = "SavingData";
    private Boolean projectHaveName;
    private String nameProject;

    public final static String EXTRA_MESSAGE = "MESSAGE";
    public final static int REQUEST_LIST = 1;    
    public final static int RESULT_LIST = 2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        isPlaying = false;
        projectHaveName = false;

		drawingZone = (CustomView) findViewById(R.id.drawingZone);
		drawingZone.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});        

        if (savedInstanceState!=null){
            nameProject = savedInstanceState.getString("nameProject");
            projectHaveName = true;
            drawingZone.loadImages(nameProject);
        }

		createListenerForButton();	
		createActionBar();	
		popUpWindowConfig = createPopUpWindowConfig();

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nameProject", nameProject);
    } 

    public void createListenerForButton(){

		SeekBar sizePenBar = (SeekBar) findViewById(R.id.seek);
		sizePenBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				drawingZone.setSizePaint(progress/4);
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

		ImageButton background = (ImageButton) findViewById(R.id.button_background);
		background.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingZone.activateBackground();
			}
		});

		ImageButton oignons = (ImageButton) findViewById(R.id.button_oignons);
		oignons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingZone.activateOignons();
			}
		});

		ImageButton play = (ImageButton) findViewById(R.id.button_play);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//drawingZone.playImages();
				playVideo();
			}
		});		

		ImageButton crayon = (ImageButton) findViewById(R.id.button_crayon);
		crayon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingZone.activateCrayon();
			}
		});

		ImageButton gomme = (ImageButton) findViewById(R.id.button_gomme);
		gomme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawingZone.activateGomme();
			}
		}); 
    }

   	public PopupWindow createPopUpWindowConfig() {
        // add items on the array dynamically
        // format is Company Name:: ID
        List<String> itemList = new ArrayList<String>();
        itemList.add("New");
        itemList.add("Load");
        itemList.add("Save");

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
        popupWindow.setWidth(100);
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
                listItem.setPadding(5, 5, 5, 5);
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
				//Toast.makeText(MainActivity.this, "Image precedente", Toast.LENGTH_SHORT).show();
				drawingZone.previousImage();
		    }
		});	
		ImageView next = (ImageView) findViewById(R.id.menu_next);
		next.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				//Toast.makeText(MainActivity.this, "Image suivante", Toast.LENGTH_SHORT).show();
				drawingZone.nextImage();
		    }
		});	
		ImageView image = (ImageView) findViewById(R.id.menu_image);
		image.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	if(projectHaveName == true){
		    		savingProject();
					Intent intent = new Intent(MainActivity.this, ListImageActivity.class);
				    intent.putExtra(EXTRA_MESSAGE, nameProject);
				    startActivityForResult(intent, REQUEST_LIST);
		    	}
				//drawingZone.cleanImage();
		    }
		});	
	}

    public void setVisibilityOfSideLayout(){
		RelativeLayout left = (RelativeLayout) findViewById(R.id.layoutLeft);
		LinearLayout right = (LinearLayout) findViewById(R.id.layoutRight);
		Button hide = (Button) findViewById(R.id.button_hide);

    	if(visibiltyOfSideLayout==true){
				left.setVisibility(View.GONE);
				mActionBar.hide();
				right.setVisibility(View.GONE);
				visibiltyOfSideLayout=false;
				hide.setBackgroundResource(R.drawable.arrow_right);
    	}
    	else{
    			mActionBar.show();
				right.setVisibility(View.VISIBLE);
				left.setVisibility(View.VISIBLE);
				visibiltyOfSideLayout=true;
				hide.setBackgroundResource(R.drawable.arrow_left);
    	}

    }

	public void newProjectDialog() {

		projectHaveName = false;

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
				video = getOutputMediaFileUri(); // create a file to save the video
				takeVid.putExtra(MediaStore.EXTRA_OUTPUT, video); // set the image file name
				startActivityForResult(takeVid, 0);
				//drawingZone.setVideo(video.getPath());
			}
		});

		AlertDialog sourcesDialog = builder.create();
		sourcesDialog.show();
		sourcesDialog.getWindow().setLayout(130, 70);

		Button gallery = sourcesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		gallery.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(android.R.drawable.ic_menu_gallery), null, null, null);
		gallery.setText("");

		Button camera = sourcesDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		camera.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(android.R.drawable.ic_menu_camera), null, null, null);
		camera.setText("");
	}

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(){
		File f = getOutputMediaFile();
		if (f != null) {
			return Uri.fromFile(f);
		}
		return null;
	}

	/** Create a File for saving the video */
	private File getOutputMediaFile(){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(getApplicationContext().getFilesDir(), String.valueOf(R.string.app_name));
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d(String.valueOf(R.string.app_name), "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		//String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		return new File(mediaStorageDir.getPath() +/* File.separator +"VID_"+ timeStamp +*/ ".mp4");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_LIST || requestCode == REQUEST_LIST) {
			if (data.hasExtra("result")) {
				drawingZone.setCurrentImage(data.getExtras().getInt("result"));
			}
		}
	}


	public void savingProject(){

		if(projectHaveName==false){
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);

		    builder.setTitle("Nom du projet");

			final EditText input = new EditText(MainActivity.this);  
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
			input.setLayoutParams(lp);
			builder.setView(input);

		    // Add action buttons
		           builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		               		String name = input.getText().toString();
		                	drawingZone.saveImages(name+"/");
		                	nameProject = name + "/";
		                	projectHaveName = true;
		               }
		           })
		           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                   
		               }
		           }); 

			AlertDialog sourcesDialog = builder.create();
			sourcesDialog.show();	
		}
		else{
			drawingZone.saveImages();
		}
 	
	}

	public void loadingProject(){

		File dir = new File(this.getFilesDir(),this.nameRepository);       
		final String[] items = dir.list();

	    AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Choix du Projet : ")
	           .setItems(items, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	               		drawingZone.loadImages(items[which]);
	               		projectHaveName = true;
	               		nameProject = items[which]+"/";
	           }
	    });

	    AlertDialog sourcesDialog =  builder.create();
	    sourcesDialog.show();

	}

	public void playVideo(){
		ImageButton but = (ImageButton) findViewById(R.id.button_play);

		if(isPlaying==false){
			animation = drawingZone.playImages();

			drawingZone.setEmpty(true);

			but.setImageResource(android.R.drawable.ic_media_pause);

			ImageView imageAnim = (ImageView) findViewById(R.id.view_video);
	    	imageAnim.setBackgroundDrawable(animation);

		    // start the animation!
		    animation.start();
		    isPlaying = true;
		}
		else{
			but.setImageResource(android.R.drawable.ic_media_play);
			ImageView imageAnim = (ImageView) findViewById(R.id.view_video);
	    	imageAnim.setBackgroundResource(android.R.color.transparent);

			drawingZone.setEmpty(false);
			animation.stop();
			isPlaying = false;
		}

	}

}
