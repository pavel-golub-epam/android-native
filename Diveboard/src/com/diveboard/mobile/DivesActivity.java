package com.diveboard.mobile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.diveboard.model.Dive;
import com.diveboard.model.DiveboardModel;
import com.diveboard.model.ScreenSetup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DivesActivity extends FragmentActivity {

	// Number of pages in Dives
	private int mNbPages = 1;
	
	// All you need to make the carousel
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private ViewGroup mLayout;
	private SeekBar mSeekBar;
	private View mRootView;
	private RelativeLayout mDiveFooter;
	
	// Thread for the data loading & the views associated
	private LoadDataTask mAuthTask = null;
	private View mLoadDataFormView;
	private View mLoadDataStatusView;
	private TextView mLoadDataStatusMessageView;
	
	// Model to display
	private DiveboardModel mModel;
	private TextView mNickname;
	private TextView mId;
	private TextView mLocation;
	private TextView mPublicNbDives;
	private TextView mTotalNbDives;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the view layout
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			getActionBar().hide();
		}
		else
		{
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		setContentView(R.layout.activity_dives);
		// Initialize data
		if (savedInstanceState == null)
		{
			mModel = new DiveboardModel(48, this);		
			mLoadDataFormView = findViewById(R.id.load_data_form);
			mLoadDataStatusView = findViewById(R.id.load_data_status);
			mLoadDataStatusMessageView = (TextView) findViewById(R.id.load_data_status_message);
			loadData();
		}
		else
		{
			mModel = savedInstanceState.getParcelable("model");
			createPages();
		}
	}
	
	
	
	public void loadData()
	{
		if (mAuthTask != null)
		{
			return;
		}
		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		mLoadDataStatusMessageView.setText(R.string.progress_load_data);
		showProgress(true);
		mAuthTask = new LoadDataTask();
		mAuthTask.execute((Void) null);
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoadDataStatusView.setVisibility(View.VISIBLE);
			mLoadDataStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoadDataStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoadDataFormView.setVisibility(View.VISIBLE);
			mLoadDataFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoadDataFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoadDataStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoadDataFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState);mModel
		outState.putParcelable("model", mModel);
	}
	
	/**
	 * ViewPager creation with all the fragments associated, resized according the screen size
	 */
	private void createPages()
	{
		//ViewTreeObserver allows to load all the layouts of the page before applying calculation
		((LinearLayout)findViewById(R.id.load_data_form)).setVisibility(View.VISIBLE);
		mLayout = (ViewGroup)findViewById(R.id.pager);
		ViewTreeObserver vto = mLayout.getViewTreeObserver(); 
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		        mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        //We do all calculation of the dimension of the elements of the page according to the UI mobile guide
		        ScreenSetup screenSetup = new ScreenSetup(mLayout.getMeasuredWidth(), mLayout.getMeasuredHeight());
				int margin = (screenSetup.getScreenWidth() - screenSetup.getDiveListFragmentHeight()) * (-1);
				int offset = ((screenSetup.getDiveListFragmentBannerHeight() + screenSetup.getDiveListFragmentBodyHeight()) / 2);
				//We set dynamically the size of each page
				mRootView = (View)findViewById(R.id.screen);
				//mRootView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, screenSetup.getScreenHeight()));
				
				RelativeLayout diveFooter = (RelativeLayout) findViewById(R.id.dive_footer);
				LinearLayout.LayoutParams diveFooterParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, screenSetup.getDiveListFooterHeight());
				diveFooter.setLayoutParams(diveFooterParams);
				
//				mDiveFooter = (RelativeLayout)findViewById(R.id.dive_footer);
//				LinearLayout.LayoutParams dive_footer_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, screenSetup.getDiveListFooterHeight());
//				mDiveFooter.setLayoutParams(dive_footer_params);
				//Returns the bitmap of each fragment (page) corresponding to the circle layout of the main picture of a page
				//Each circle must be white with a transparent circle in the center
				Bitmap bitmap = ImageHelper.getRoundedLayer(screenSetup);
				//We load the first background of the activity
				DownloadImageTask task = new DownloadImageTask((RelativeLayout)findViewById(R.id.screen));
				task.execute(0);
				//We create the pager with the associated pages
				if (mModel.getDives() == null)
					System.out.println("null"); // a gérer
				else
					mNbPages = mModel.getDives().size();
				mPager = (ViewPager) findViewById(R.id.pager);
		        mPagerAdapter = new DivesPagerAdapter(getSupportFragmentManager(), mModel.getDives(), screenSetup, bitmap);
		        mPager.setAdapter(mPagerAdapter);
		        mPager.setPageMargin(margin + offset);
		        //mPager.setOffscreenPageLimit(((screenwidth / mFragmentWidth) + 1));
		        mPager.setOffscreenPageLimit(3);
		        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		        //The tracking bar is set
		        mSeekBar = (SeekBar)findViewById(R.id.seekBar);
		        RelativeLayout.LayoutParams seekBarParams = new RelativeLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 30);
		        seekBarParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		        seekBarParams.setMargins(0, 0, 0, screenSetup.getDiveListWhiteSpace4());
		        mSeekBar.setLayoutParams(seekBarParams);
		        mSeekBar.setMax(mModel.getDives().size() - 1);
		        //Events when the user changes a page
		        mPager.setOnPageChangeListener(new OnPageChangeListener()
		        {
					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
						if (arg0 == 0)
						{
							DownloadImageTask task = new DownloadImageTask((RelativeLayout)findViewById(R.id.screen));
							task.execute(mPager.getCurrentItem());
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						mSeekBar.setProgress(arg0);		
					}

					@Override
					public void onPageSelected(int arg0) {

					}
		        	
		        });
		      //Events when the user changes the seek bar
		        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		        {		        	
		        	@Override
		        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		        	{
		        		if (fromUser == true)
		        		{
		        			mPager.setCurrentItem(progress, true);
		        		}
		        	}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}
		        });
		    } 
		});
	}
	
	private class DownloadImageTask extends AsyncTask<Integer, Void, Bitmap>
	{
		private final WeakReference<RelativeLayout> rootViewReference;
		
		public DownloadImageTask(RelativeLayout rootView)
		{
			rootViewReference = new WeakReference<RelativeLayout>(rootView);
		}
		
		protected Bitmap doInBackground(Integer... args)
		{
			Bitmap result = null;
			try {
				result = mModel.getDives().get(args[0]).getThumbnailImageUrl().getPicture(getApplicationContext());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (result != null)
				return ImageHelper.fastblur(result, 30);
			return null;
		}
		
		protected void onPostExecute(Bitmap result)
		{
			if (rootViewReference != null && result != null)
			{
				final RelativeLayout rootView = rootViewReference.get();
				if (rootView != null)
				{
					rootView.setBackgroundDrawable(new BitmapDrawable(getResources(), result));	
				}
			}
		}
	}
	
	/**
	 * Represents an asynchronous task used to load data
	 */
	private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(Void... params) {
			mModel.loadData();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				createPages();
			}
		}
		
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	/**
	 * Generate the dives pages
	 */
	private class DivesPagerAdapter extends FragmentStatePagerAdapter
	{
		private ArrayList<Dive> mDives;
		private ScreenSetup mScreenSetup;
		private Bitmap mRoundedLayer;
		
		public DivesPagerAdapter(FragmentManager fragmentManager, ArrayList<Dive> dives, ScreenSetup screenSetup, Bitmap rounded_layer) {
            super(fragmentManager);
			mDives = dives;
			mScreenSetup = screenSetup;
			mRoundedLayer = rounded_layer;
        }

        @Override
        public Fragment getItem(int position) {
            return new DivesFragment(mDives.get(position), mScreenSetup, mRoundedLayer);
        }

        @Override
        public int getCount() {
            return mNbPages;
        }
	}
}
