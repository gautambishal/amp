package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.util.Output;

/**
 * Connection between Activity and Sector.
 * This class initially was added to add percentage for Bolivia. AMP-2250
 * @author Irakli Kobiashvili
 *
 */
public class AmpActivityLocation implements Versionable,Serializable, Cloneable {
	private Long id;
	private AmpActivityVersion activity;
	private AmpLocation location;
	private Float locationPercentage;
	private String latitude;
	private String longitude;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AmpActivityVersion getActivity() {
		return activity;
	}
	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}
	public AmpLocation getLocation() {
		return location;
	}
	public void setLocation(AmpLocation location) {
		this.location = location;
	}
	public Float getLocationPercentage() {
		return locationPercentage;
	}
	public void setLocationPercentage(Float locationPercentage) {
		this.locationPercentage = locationPercentage;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpActivityLocation aux = (AmpActivityLocation) obj;
		if (this.location.equalsForVersioning(aux.getLocation())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Output getOutput() {
		Output out = this.location.getOutput();
		out.getOutputs().add(
				new Output(null, new String[] { " Percentage:&nbsp;" },
						new Object[] { this.locationPercentage != null ? this.locationPercentage : new Float(0) }));
		return out;
	}

	@Override
	public Object getValue() {
		return this.locationPercentage != null ? this.locationPercentage : new Float(0);
	}
	
	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpActivityLocation aux = (AmpActivityLocation) clone(); 
		aux.activity = newActivity;
		aux.id = null;
		return aux;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
