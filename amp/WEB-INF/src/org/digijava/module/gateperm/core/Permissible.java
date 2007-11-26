/**
 * Permissible.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * Permissible.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 29.08.2007
 */
public abstract class Permissible implements Identifiable {
	private static Logger logger = Logger.getLogger(Permissible.class);

	@Retention(RetentionPolicy.RUNTIME)
	public @interface PermissibleProperty {
		String type();

		public final static String PROPERTY_TYPE_ID = "ID";

		public final static String PROPERTY_TYPE_LABEL = "LABEL";
	}

	public static String getPermissiblePropertyName(Class permClass, String type) {
		Class c = permClass;
		while (!c.equals(Object.class)) {
			Field[] declaredFields = c.getDeclaredFields();
			for (int i = 0; i < declaredFields.length; i++) {
				if (declaredFields[i]
						.isAnnotationPresent(PermissibleProperty.class)) {
					PermissibleProperty annotation = declaredFields[i]
							.getAnnotation(PermissibleProperty.class);
					if (type.equals(annotation.type()))
						return declaredFields[i].getName();
				}
			}
			c = permClass.getSuperclass();
		}
		return null;
	}

	/**
	 * Returns a set of actions that this Permissible object implements. An
	 * object may not need all possible actions that a permission may allow. It
	 * may only need a subset of them
	 * 
	 * @return
	 */
	public abstract String[] getImplementedActions();

	/**
	 * Retruns the permission object associated with this permissible object.
	 * @param own if true, it will only return the permission associated with this specific object or null if none existing. Otherwise
	 * it returns the permission associated with this object, or, if null, the global permission associated with the object class
	 * @return the permission object
	 */
	public Permission getPermission(boolean own) {
	    PermissionMap pm;
	    if(own)
		pm=PermissionUtil.getOwnPermissionMapForPermissible(this);
	    else
		pm=PermissionUtil.getPermissionMapForPermissible(this);
	    if(pm==null) return null;
	    return pm.getPermission();
	}
		
	/**
	 * @return the object category to identify specific permission objects. This
	 *         is usually a constant stored in Permissible and it usually
	 *         represents its Class.
	 */
	public abstract Class getPermissibleCategory();

	/**
	 * Gets the list of linked permissions with this object. Queries the
	 * permissions to produce a set of unique allowed actions for this object.
	 * 
	 * @param scope
	 *            a map with the scope of the application (various variables
	 *            like request, session or parts of request, session -
	 *            currentMember, etc... that are relevant for the Gate logic All
	 *            the actions that are not implemented by the Permissible object
	 *            will be retained.
	 * @see getImplementedActions()
	 * @return the collection of unique allowed actions
	 */
	public Collection<String> getAllowedActions(Map scope) {
		//put the self into scope:
		scope.put(GatePermConst.ScopeKeys.PERMISSIBLE,this);
		
		
		  PermissionMap permissionMapForPermissible = PermissionUtil.getPermissionMapForPermissible(this);
		  Collection<String> actions = processPermissions(permissionMapForPermissible, scope);
		 
		  
		  
		  Collection implementedActions = Arrays.asList(getImplementedActions());
		  actions.retainAll(implementedActions);
			
		  
		return actions;
	}
	
	public static Collection<String> processPermissions(PermissionMap permissionMapForPermissible,Map scope) {
		Set<String> actions = new TreeSet<String>();
		if(permissionMapForPermissible!=null && permissionMapForPermissible.getPermission()!=null) {
			Set<String> allowedActions = permissionMapForPermissible.getPermission().getAllowedActions(scope);
			if(allowedActions!=null) actions.addAll(allowedActions);
			if(permissionMapForPermissible.getObjectIdentifier()==null)
			logger.info("Actions allowed by the Global Permission "+permissionMapForPermissible.getPermission().getName()+": "+actions); else
			logger.info("Actions allowed for object "+ permissionMapForPermissible.getObjectLabel() + " (id="+permissionMapForPermissible.getObjectIdentifier()
					+ ") of type " + permissionMapForPermissible.getPermissibleCategory() + " are "
					+ actions); }
		return actions;
	}
	

	public static Collection<String> getAllowedActions(Object permissibleIdentifier, Class permissibleClass,Map scope) {
		PermissionMap permissionMapForPermissible = PermissionUtil.getPermissionMapForPermissible(permissibleIdentifier, permissibleClass);
		 Collection<String> actions = processPermissions(permissionMapForPermissible, scope);
		 
		 //we cannot filter out the implemented actions yet, so we just return the whole list...
		 return actions;
	}
		
	
	/**
	 * Returns true if the current object is allowed to do the specified action
	 * for the given scope
	 * 
	 * @param actionName
	 *            the name of the action defined in GatePermConst class
	 * @param scope
	 *            the scope with the objects that represent the current state of
	 *            amp - eg: request,session,etc...
	 * @see GatePermConst
	 * @return
	 */
	public boolean canDo(String actionName, Map scope) {
		Collection<String> allowedActions = getAllowedActions(scope);
		return allowedActions.contains(actionName);
	}

	/**
	 * Static implementation of the same method
	 * @param actionName
	 * @param permissibleIdentifier
	 * @param permissibleClass
	 * @param scope
	 * @return
	 */
	public static boolean canDo(String actionName,Object permissibleIdentifier, Class permissibleClass, Map scope) {
		Collection<String> allowedActions = getAllowedActions(permissibleIdentifier, permissibleClass, scope);
		return allowedActions.contains(actionName);
	}

	
}
