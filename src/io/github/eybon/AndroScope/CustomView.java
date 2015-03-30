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

/**
* Created by X on 14/03/15.
*/
public class CustomView extends View{

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	Context context;
	private Paint mPaint;

	int x1;
	int y1;

	public CustomView(Context c){
		super(c);
		context=c;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(20);		
	}

	public CustomView(Context c, AttributeSet attrs){
		super (c, attrs);
		context=c;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(20);		
	}

	public CustomView(Context c, AttributeSet attrs, int defStyle){
		super (c, attrs, defStyle);
		context=c;
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(20);
	}

	public void newImage(){
		Log.d("DrawingZone", "newImage - x : "+x1+" y : "+y1);
		Log.d("DrawingZone", "newImage - mPaint : "+mPaint.toString());
		mBitmap = Bitmap.createBitmap(x1, y1, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		invalidate();
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		x1=w;
		y1=h;
		Log.d("DrawingZone", "onSizeChanged - x : "+x1+" y : "+y1);
		Log.d("DrawingZone", "onSizeChanged - mPaint : "+mPaint.toString());

		super.onSizeChanged(w, h, oldw, oldh);
		Log.d("DrawingZone", "onSizeChanged - x : "+x1+" y : "+y1);
		Log.d("DrawingZone", "onSizeChanged - mPaint : "+mPaint.toString());

		if(mCanvas == null)
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		else
			mBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, true);
		mCanvas = new Canvas(mBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath( mPath, mPaint);
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

}