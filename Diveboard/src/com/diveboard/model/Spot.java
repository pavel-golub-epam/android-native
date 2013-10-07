package com.diveboard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class					Spot implements IModel
{
	private Integer				_id;
	private String				_shakenId;
	private String				_name;
	private double				_lat;
	private double				_lng;
	private int					_zoom;
	private int					_locationId;
	private Integer				_regionId;
	private int					_countryId;
	private Integer				_privateUserId;
	private String				_countryCode;
	private String				_countryName;
	private Picture				_countryFlagBig;
	private Picture				_countryFlagSmall;
	
	public						Spot(JSONObject json) throws JSONException
	{
		_id = (json.isNull("id")) ? null : json.getInt("id");
		_shakenId = json.getString("shaken_id");
		_name = json.getString("name");
		_lat = json.getDouble("lat");
		_lng = json.getDouble("lng");
		_zoom = json.getInt("zoom");
		_locationId = json.getInt("location_id");
		_regionId = (json.isNull("region_id")) ? null : json.getInt("region_id");
		_countryId = json.getInt("country_id");
		_privateUserId = (json.isNull("private_user_id")) ? null : json.getInt("private_user_id");
		_countryCode = json.getString("countr_code");
		_countryName = json.getString("country_name");
		_countryFlagBig = new Picture(json.getString("country_flag_big"));
		_countryFlagSmall = new Picture(json.getString("countr_flag_small"));
	}
	
	public void					save()
	{
		
	}

	public void					delete()
	{
		
	}

	public Integer getId() {
		return _id;
	}

	public void setId(Integer _id) {
		this._id = _id;
	}

	public String getShakenId() {
		return _shakenId;
	}

	public void setShakenId(String _shakenId) {
		this._shakenId = _shakenId;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	public double getLat() {
		return _lat;
	}

	public void setLat(double _lat) {
		this._lat = _lat;
	}

	public double getLng() {
		return _lng;
	}

	public void setLng(double _lng) {
		this._lng = _lng;
	}

	public int getZoom() {
		return _zoom;
	}

	public void setZoom(int _zoom) {
		this._zoom = _zoom;
	}

	public int getLocationId() {
		return _locationId;
	}

	public void setLocationId(int _locationId) {
		this._locationId = _locationId;
	}

	public Integer getRegionId() {
		return _regionId;
	}

	public void setRegionId(Integer _regionId) {
		this._regionId = _regionId;
	}

	public int getCountryId() {
		return _countryId;
	}

	public void setCountryId(int _countryId) {
		this._countryId = _countryId;
	}

	public Integer getPrivateUserId() {
		return _privateUserId;
	}

	public void setPrivateUserId(Integer _privateUserId) {
		this._privateUserId = _privateUserId;
	}

	public String getCountryCode() {
		return _countryCode;
	}

	public void setCountryCode(String _countryCode) {
		this._countryCode = _countryCode;
	}

	public String getCountryName() {
		return _countryName;
	}

	public void setCountryName(String _countryName) {
		this._countryName = _countryName;
	}

	public Picture getCountryFlagBig() {
		return _countryFlagBig;
	}

	public void setCountryFlagBig(Picture _countryFlagBig) {
		this._countryFlagBig = _countryFlagBig;
	}

	public Picture getCountryFlagSmall() {
		return _countryFlagSmall;
	}

	public void setCountryFlagSmall(Picture _countryFlagSmall) {
		this._countryFlagSmall = _countryFlagSmall;
	}
}
