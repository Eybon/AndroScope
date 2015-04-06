package io.github.eybon.AndroScope;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff.Mode;
import android.os.Environment;
import android.graphics.Bitmap.CompressFormat;
import java.util.*;
import java.io.*;
import java.lang.Thread;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class CustomView extends View{

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	Context context;
	private Paint mPaint;
	private int mColor;

	private ArrayList<Bitmap> backgroundList;
	private ArrayList<Bitmap> imageList;
	private int indexCurrentImage;

	private boolean empty;
	private boolean backgroundActivate;
	private boolean oignonsActivate;
	private int nbOignons;

	private int widthView;
	private int heightView;

	private String nameRepository = "SavingData";
	private String nameImg = "img";
	private String nameCurrentProject;

	private MediaMetadataRetriever metadata;

	public void initDrawer(Context c){
		context=c;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mColor = Color.GREEN;
		mPaint.setColor(mColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(10);	

		imageList = new ArrayList<Bitmap>();
		indexCurrentImage = 0;
		backgroundList = new ArrayList<Bitmap>();

		empty = false;
		backgroundActivate = false;
		oignonsActivate = true;
		nbOignons = 3;
		metadata = null;

	}

	public CustomView(Context c){
		super(c);
		initDrawer(c);
	}

	public CustomView(Context c, AttributeSet attrs){
		super (c, attrs);
		initDrawer(c);	
	}

	public CustomView(Context c, AttributeSet attrs, int defStyle){
		super (c, attrs, defStyle);
		initDrawer(c);
	}

	public void cleanImage(){
		Log.d("DrawingZone", "newImage - x : "+widthView+" y : "+heightView);
		imageList.set(indexCurrentImage, Bitmap.createBitmap(widthView, heightView, Bitmap.Config.ARGB_8888) );
		mCanvas = new Canvas(imageList.get(indexCurrentImage) );
		invalidate();
	}

	public void activateBackground(){
		if( backgroundActivate == false){
			backgroundActivate = true;
		}
		else{
			backgroundActivate = false;
		}
		invalidate();
	}

	public void activateOignons(){
		if( oignonsActivate == false){
			oignonsActivate = true;
		}
		else{
			oignonsActivate = false;
		}
		invalidate();
	}

	public void setEmpty(Boolean b){
		empty = b;
		invalidate();
	}

	public void nextImage(){
		indexCurrentImage++;
		Log.d("DrawingZone", "--- Next Image ---");
		Log.d("DrawingZone", "value indexCurrentImage : "+indexCurrentImage);
		Log.d("DrawingZone", "nextImage  width: "+widthView+" height: "+heightView);
		int size = imageList.size();
		Log.d("DrawingZone", "nextImage, size of imageList : "+size);
		if(indexCurrentImage >= size){
			imageList.add(null);
			imageList.set( indexCurrentImage ,Bitmap.createBitmap(widthView, heightView, Bitmap.Config.ARGB_8888) );
		}
		//canvas.drawColor(0, Mode.CLEAR);
		mCanvas = new Canvas(imageList.get(indexCurrentImage));
		invalidate();
	}

	public void previousImage(){
		if(indexCurrentImage-1 >= 0){
			indexCurrentImage --;
			Log.d("DrawingZone", "value indexCurrentImage : "+indexCurrentImage);
			mCanvas = new Canvas(imageList.get(indexCurrentImage));
		}
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		widthView=w;
		heightView=h;
		Log.d("DrawingZone", "onSizeChanged - x : "+widthView+" y : "+heightView);

		super.onSizeChanged(w, h, oldw, oldh);
		if(mCanvas == null){
			imageList.add( Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888) );
			mCanvas = new Canvas(imageList.get(indexCurrentImage));
			mCanvas.drawColor(0, Mode.CLEAR);
			//mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		}
		else {
			for(int i = 0;i<imageList.size();i++){
				imageList.set(i, Bitmap.createScaledBitmap(imageList.get(i), w, h, true) );
			}	
			mCanvas = new Canvas(imageList.get(indexCurrentImage));
			//mBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, true);;
		}
		Log.d("DrawingZone", "onSizeChanged - image : "+imageList.get(indexCurrentImage).toString());
		//mCanvas = new Canvas(mBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d("DrawingZone", " -- onDraw -- : ");

		if(empty==false){
			mBitmapPaint.setAlpha(150);
			if(backgroundActivate == true){
				if(metadata!=null){
					int time = 250*indexCurrentImage;
					Bitmap background = metadata.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST);
					background = Bitmap.createScaledBitmap(background, widthView, heightView, true);
					canvas.drawBitmap( background, 0, 0, mBitmapPaint);
				}
			}

			mBitmapPaint.setAlpha(100);
			int count = 0;
			if(oignonsActivate == true){
				Log.d("DrawingZone", "onDraw - oignonsActivate : "+oignonsActivate+"  indexCurrentImage : "+indexCurrentImage);
				for(int i = indexCurrentImage - 1 ; i>=0 ;--i){
					Log.d("DrawingZone", "onDraw - index : "+i);
					if(count<3){
						canvas.drawBitmap( imageList.get(i), 0, 0, mBitmapPaint);
					}
					mBitmapPaint.setAlpha(mBitmapPaint.getAlpha()-25);
					count ++;
				}
			}	

			mBitmapPaint.setAlpha(200);
			canvas.drawBitmap( imageList.get(indexCurrentImage), 0, 0, mBitmapPaint);
			canvas.drawPath( mPath, mPaint);
		}
		else{
			Bitmap bit = Bitmap.createBitmap(widthView, heightView, Bitmap.Config.ARGB_8888);
			canvas.drawBitmap( bit, 0, 0, mBitmapPaint);			
		}
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;

			case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;

			case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	public void setColorPaint(int color){
		mPaint.setColor(color);
	}

	public void setSizePaint(int size){
		Log.d("DrawingZone", "SetSize value : "+size+"=====");
		mPaint.setStrokeWidth(size);
	}

	public void setCurrentImage(int val){
		indexCurrentImage = val;
	}

	public void activateCrayon(){
		mPaint.setColor(mColor);
	}

	public void activateGomme(){
		mPaint.setColor(Color.WHITE);
	}	

	public void saveImages(){
		for(int i = 0; i<imageList.size();i++){
			storeImage(imageList.get(i),nameCurrentProject,nameImg+i);
		}
	}

	public void saveImages(String nameProject){

		nameCurrentProject = nameProject;

		for(int i = 0; i<imageList.size();i++){
			storeImage(imageList.get(i),nameProject,nameImg+i);
		}
	}

	private boolean storeImage(Bitmap imageData, String nameProject, String filename) {          

		//get path to external storage (SD card)
		File file = new File(context.getFilesDir(),this.nameRepository+"/"+nameProject);
		Log.v("Saving", "Saving image file: " +file.toString());

		//create storage directories, if they don't exist
		file.mkdirs();

		try {
			String filePath = file.toString() + "/"+filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			//choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.PNG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}
		return true;
	}

	public void loadImages(String nameProject){

		nameCurrentProject = nameProject;

		File file = new File(context.getFilesDir(),this.nameRepository+"/"+nameProject);
		ArrayList<Bitmap> images = new ArrayList<Bitmap>();
		File filePath = null;

		boolean exist = true;
		int i = 0;
		// If no file on external storage, look in internal storage
		while(exist == true)	
		{
			try {
				filePath = new File(file,nameImg+i);
				FileInputStream fi = new FileInputStream(filePath);
				images.add( BitmapFactory.decodeStream(fi).copy(Bitmap.Config.ARGB_8888, true) );
			} 
			catch (Exception ex) {
				Log.v("Load","Stopping load at : "+ i +" name : "+ filePath.toString());
				exist = false;
			}
			i++;
		}
		
		imageList = new ArrayList<Bitmap>(images);
		indexCurrentImage = 0;
		invalidate();
	}

	public AnimationDrawable playImages(){

	    AnimationDrawable animation = new AnimationDrawable();
	    for(int i=0;i<imageList.size();i++){
	    	animation.addFrame(new BitmapDrawable(imageList.get(i)) , 500);
	    }
	    animation.setOneShot(false);
	    Log.v("Load","At playImages size : "+ imageList.size() +" animation : "+ animation.toString());

	    return animation;
	}

	public void setVideo(String path){
		metadata = new MediaMetadataRetriever();
		metadata.setDataSource(this.context, Uri.parse(path));
	}


}