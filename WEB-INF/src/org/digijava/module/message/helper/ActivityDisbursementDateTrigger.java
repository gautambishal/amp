package org.digijava.module.message.helper;

import org.digijava.module.aim.dbentity.AmpActivity;

public class ActivityDisbursementDateTrigger extends Trigger {
	
	public static final String PARAM_NAME="name";
	public static final String PARAM_TRIGGER_SENDER="sender";	
	public static final String PARAM_URL="activity url";
	
	public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_TRIGGER_SENDER,PARAM_URL};
	
	public ActivityDisbursementDateTrigger(Object source) {
		if(! (source instanceof AmpActivity)) throw new RuntimeException("Incompatible object. Source must be an activity!"); 
		this.source=source;
		forwardEvent();
	}
	
	@Override
	protected Event generateEvent() {
		Event e=new Event(ActivityDisbursementDateTrigger.class);
		AmpActivity activity=(AmpActivity) source;
		e.getParameters().put(PARAM_NAME,activity.getName());		
		e.getParameters().put(PARAM_TRIGGER_SENDER,MessageConstants.SENDER_TYPE_SYSTEM);		
		e.getParameters().put(PARAM_URL, "aim/viewChannelOverview.do~tabIndex=0~ampActivityId="+activity.getAmpActivityId());
		return e;
	}

	@Override
	public String[] getParameterNames() {		
		return parameterNames;
	}

}
