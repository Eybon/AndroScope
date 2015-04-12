package io.github.eybon.AndroScope;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import java.io.*;
import android.util.Log;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.view.ContextMenu;
import android.widget.AdapterView;
import android.view.View.OnCreateContextMenuListener;
import android.content.Intent;
import android.app.ActionBar;
import android.util.Log;


public class ListImageActivity extends Activity
{

    public final static int REQUEST_LIST = 1;    
    public final static int RESULT_LIST = 2;

	private String nameRepository = "SavingData";
	private FileAdapter adapter;
	private ActionBar mActionBar;
    private int imageClick ;

    /**
    *   création de l'activité
    */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);

        imageClick = 0;

        Intent intent = getIntent();
    	String nameProject = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		File dir = new File(this.getFilesDir(),this.nameRepository+"/"+nameProject);     
        String path = dir.getAbsolutePath();
		//final String[] items = dir.list();
		File[] count = dir.listFiles() ;
        File[] items = new File[((count.length)/4)+1];
        for(int i=0; i<=(count.length)/4;i++){
            items[i] = new File(path);
        }

        adapter = new FileAdapter(this,items);
        ListView list = (ListView)findViewById(R.id.list); 
        list.setAdapter(adapter);

        createActionBar();
    } 

    /**
    *   Méthode qui recupere le numero de l'image qui à été clické
    */
    public void imageClicked(int value){
        Log.v("ListActivity","click : "+ value);
        imageClick = value;
        finish();
    }

    /**
    *   Méthode de gestion de la fin de l'activité
    */
    @Override
    public void finish() {
        // Prepare data intent 
        Intent data = new Intent();
        data.putExtra("result", imageClick);
        // Activity finished ok, return the data
        setResult(RESULT_LIST, data);
        super.finish();
    }

    /**
    *   Création de l'action bar
    */
    public void createActionBar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);

		mActionBar.setCustomView(R.menu.menu_list_activity);
		mActionBar.setDisplayShowCustomEnabled(true);
	}

}