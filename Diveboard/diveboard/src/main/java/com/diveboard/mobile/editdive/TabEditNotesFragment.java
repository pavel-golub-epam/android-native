package com.diveboard.mobile.editdive;

import com.diveboard.mobile.ApplicationController;
import com.diveboard.mobile.R;
import com.diveboard.model.DiveboardModel;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class TabEditNotesFragment extends Fragment
{
	private Typeface					mFaceR;
	private Typeface					mFaceB;
	private DiveboardModel				mModel;
	private int							mIndex;
	
//	@Override
//	protected void onPause()
//	{
//		mModel.getDives().get(mIndex).setNotes(mNotes.getText().toString());
//		super.onPause();	
//	}
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ApplicationController AC = (ApplicationController)getActivity().getApplicationContext();
        if (AC.handleLowMemory() == true)
			return ;
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
    	ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tab_edit_notes, container, false);
    	
        
	    mFaceR = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Light.ttf");
	    mFaceB = ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.lato_regular);
	    mModel = ((ApplicationController)getActivity().getApplicationContext()).getModel();
		mIndex = getActivity().getIntent().getIntExtra("index", 0);
	    
	    EditDiveActivity.mNotes = (EditText) rootView.findViewById(R.id.notes);
	    EditDiveActivity.mNotes.setTypeface(mFaceR);
	    if (mModel.getDives().get(mIndex).getNotes() != null)
	    	EditDiveActivity.mNotes.setText(mModel.getDives().get(mIndex).getNotes());
	    
	    mFaceR = null;
	    mFaceB = null;
	    return rootView;
    }
}
