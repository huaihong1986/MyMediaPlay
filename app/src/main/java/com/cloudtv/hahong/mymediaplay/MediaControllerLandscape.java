package com.cloudtv.hahong.mymediaplay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.lang.reflect.Field;

@SuppressLint("NewApi")
public class MediaControllerLandscape extends android.widget.MediaController {

	private static String TAG = "MediaControllerLandscape";

	private View mView;

	private WindowManager mWindowManager;
	    private Window mWindow;
	    private View mDecor;
	    private TextView full_screen;

	public interface onFullScreenListener {
		public void fullScreen();
	}
	
	private onFullScreenListener mListener;
	
	public void setonFullScreenListener(onFullScreenListener listener){
		mListener = listener;
	}

	 public MediaControllerLandscape(Context context) {
		 super(context);
         setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	  }
	 
	  
	  private void setnew(){
		  try {

				Field window = android.widget.MediaController.class
						.getDeclaredField("mWindow");


				window.setAccessible(true);
			
				mWindow = (Window) window.get(this);

				

	            this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
				

			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block


				e1.printStackTrace();
			}catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block


				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block


				e.printStackTrace();
			}
	  }

	@Override
	public void setAnchorView(View view) {
		super.setAnchorView(view);
		mView = LayoutInflater.from(getContext()).inflate(
				R.layout.landscape_mediacontroller, null);
		
		
		
		try {
            ImageButton pause = (ImageButton) mView.findViewById(R.id.pause);
			SeekBar sb = (SeekBar) mView.findViewById(R.id.mediacontroller_progress);

			full_screen = (TextView) mView.findViewById(R.id.full_screen);
			full_screen.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mListener!=null){
						mListener.fullScreen();
					}
				}
			});

			TextView currentTime = (TextView) mView.findViewById(R.id.starttime);
			TextView endTime = (TextView) mView.findViewById(R.id.endtime);

			

			Field mRoot = android.widget.MediaController.class
					.getDeclaredField("mRoot");
			mRoot.setAccessible(true);
			ViewGroup mRootVg = (ViewGroup) mRoot.get(this);
			mRootVg.setBackgroundResource(R.color.transparent);
			mRootVg.removeAllViews();

			mRootVg.addView(mView);


			Field mProgress = android.widget.MediaController.class
					.getDeclaredField("mProgress");
			mProgress.setAccessible(true);
			mProgress.set(this, sb);
			Field mSeekListener = android.widget.MediaController.class
					.getDeclaredField("mSeekListener");
			mSeekListener.setAccessible(true);
			sb.setOnSeekBarChangeListener((OnSeekBarChangeListener) mSeekListener
					.get(this));

            Field mPauseButton = android.widget.MediaController.class
                    .getDeclaredField("mPauseButton");
            mPauseButton.setAccessible(true);
            mPauseButton.set(this, pause);

            Field mPauseListener = android.widget.MediaController.class
                    .getDeclaredField("mPauseListener");
            mPauseListener.setAccessible(true);
            pause.setOnClickListener((OnClickListener) mPauseListener
                    .get(this));
			
			Field mCurrentTime = android.widget.MediaController.class
					.getDeclaredField("mCurrentTime");
			mCurrentTime.setAccessible(true);
			mCurrentTime.set(this, currentTime);
			
			Field mEndTime = android.widget.MediaController.class
					.getDeclaredField("mEndTime");
			mEndTime.setAccessible(true);
			mEndTime.set(this, endTime);


			
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	private ViewGroup findSeekBarParent(ViewGroup vg) {

		
		ViewGroup viewGroup = null;
		for (int i = 0; i < vg.getChildCount(); i++) {
			View view = vg.getChildAt(i);
			if (view instanceof SeekBar) {
				viewGroup = (ViewGroup) view.getParent();
				break;
			} else if (view instanceof ViewGroup) {
				viewGroup = findSeekBarParent((ViewGroup) view);
			} else {
				continue;
			}
		}
		return viewGroup;
	}

	@Override
	public void show(int timeout) {
		super.show(timeout);

	}

	@Override
	public void hide() {
		super.hide();

	}
	
	  @Override
	  public boolean onTouchEvent(MotionEvent event) {
	        return super.onTouchEvent(event);
	    }
	  
	  public void setAsstentTVShow(int ver){
		  full_screen.setVisibility(ver);
	  }


}
