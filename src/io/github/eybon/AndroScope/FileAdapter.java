package io.github.eybon.AndroScope;

import android.app.Activity;
import java.io.File;
import android.os.Environment;
import android.content.Context;
import android.app.ListActivity;
import android.widget.ArrayAdapter; 
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import android.util.Log;


public class FileAdapter extends ArrayAdapter<File>
{

	private int pos;
	public ListImageActivity listImageActivity;

	FileAdapter(Activity parent, File[] files)
	{
		super(parent, R.layout.ligne_list_activity, files);
		listImageActivity = (ListImageActivity) parent;
	}

	public View getView (int position, View convertView, ViewGroup parent)
	{
		File item = getItem(position);
		String path = item.getAbsolutePath();
		pos = position*4;

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.ligne_list_activity, parent, false);

		ImageView imageView = (ImageView)view.findViewById(R.id.img1_image);
		imageView.setImageDrawable(Drawable.createFromPath(path+"/img"+pos));
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listImageActivity.imageClicked(pos);
			}
		});

		ImageView imageView2 = (ImageView)view.findViewById(R.id.img2_image);
		imageView2.setImageDrawable(Drawable.createFromPath(path+"/img"+(pos+1)));
		File f = new File(path+"/img"+(pos+1));
		if(!(f.isFile())){
			Log.v("FileAdapter","img not exist ");
			imageView2.setBackgroundColor(0xfff3f3f3);
		}
		else{
			imageView2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listImageActivity.imageClicked(pos+1);
				}
			});	
		}

		ImageView imageView3 = (ImageView)view.findViewById(R.id.img3_image);
		imageView3.setImageDrawable(Drawable.createFromPath(path+"/img"+(pos+2)));	
		f = new File(path+"/img"+(pos+2));
		if(!(f.isFile())){
			Log.v("FileAdapter","img not exist ");
			imageView3.setBackgroundColor(0xfff3f3f3);
		}
		else{
			imageView3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listImageActivity.imageClicked(pos+2);
				}
			});	
		}

		ImageView imageView4 = (ImageView)view.findViewById(R.id.img4_image);
		imageView4.setImageDrawable(Drawable.createFromPath(path+"/img"+(pos+3)));					
		f = new File(path+"/img"+(pos+3));
		if(!(f.isFile())){
			Log.v("FileAdapter","img not exist ");
			imageView4.setBackgroundColor(0xfff3f3f3);
		}
		else{
			imageView4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listImageActivity.imageClicked(pos+3);
				}
			});	
		}

		return view;
	}

}