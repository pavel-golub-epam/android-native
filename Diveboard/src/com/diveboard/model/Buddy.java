package com.diveboard.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

public class					Buddy implements IModel
{
	private String				_fullpermalink;
	private String				_nickname;
	private String				_location;
	private Picture				_picture_small;
	private String				_class;
	private Picture				_picture_large;
	private Picture				_picture;
	private Integer				_id;
	private String				_permalink;
	private String				_vanity_url;
	private String				_shaken_id;
	
	public						Buddy(JSONObject json)
	{
		try
		{
			_fullpermalink = (json.isNull("fullpermalink")) ? null : json.getString("fullpermalink");
			_nickname = (json.isNull("nickname")) ? null : json.getString("nickname");
			_location = (json.isNull("location")) ? null : json.getString("location");
			_picture_small = (json.isNull("picture_small")) ? null : new Picture(json.getString("picture_small"));
			_class = (json.isNull("class")) ? null : json.getString("class");
			_picture_large = (json.isNull("picture_large")) ? null : new Picture(json.getString("picture_large"));
			_picture = (json.isNull("picture")) ? null : new Picture(json.getString("picture"));
			_id = (json.isNull("id")) ? null : json.getInt("id");
			_permalink = (json.isNull("permalink")) ? null : json.getString("permalink");
			_vanity_url = (json.isNull("vanity_url")) ? null : json.getString("vanity_url");
			_shaken_id = (json.isNull("shaken_id")) ? null : json.getString("shaken_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ArrayList<Pair<String, String>> getEditList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearEditList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyEdit(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	public String getFullpermalink() {
		return _fullpermalink;
	}

	public void setFullpermalink(String _fullpermalink) {
		this._fullpermalink = _fullpermalink;
	}

	public String getNickname() {
		return _nickname;
	}

	public void setNickname(String _nickname) {
		this._nickname = _nickname;
	}

	public String getLocation() {
		return _location;
	}

	public void setLocation(String _location) {
		this._location = _location;
	}

	public Picture getPictureSmall() {
		return _picture_small;
	}

	public void setPictureSmall(Picture _picture_small) {
		this._picture_small = _picture_small;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public Picture getPictureLarge() {
		return _picture_large;
	}

	public void setPictureLarge(Picture _picture_large) {
		this._picture_large = _picture_large;
	}

	public Picture getPicture() {
		return _picture;
	}

	public void setPicture(Picture _picture) {
		this._picture = _picture;
	}

	public Integer getId() {
		return _id;
	}

	public void setId(Integer _id) {
		this._id = _id;
	}

	public String getPermalink() {
		return _permalink;
	}

	public void setPermalink(String _permalink) {
		this._permalink = _permalink;
	}

	public String getVanityUrl() {
		return _vanity_url;
	}

	public void setVanityUrl(String _vanity_url) {
		this._vanity_url = _vanity_url;
	}

	public String getShakenId() {
		return _shaken_id;
	}

	public void setShakenId(String _shaken_id) {
		this._shaken_id = _shaken_id;
	}
}