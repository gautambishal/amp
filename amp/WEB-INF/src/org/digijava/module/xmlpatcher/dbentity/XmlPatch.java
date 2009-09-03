/**
 * XmlPatch.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org This represents the
 *         reference of an XML patch file. It holds key information about the
 *         execution and state of the patch
 */
public class XmlPatch implements Serializable, Comparable<XmlPatch> {

	/**
	 * This is the id of the patch. This is the actual name of the XML file,
	 * without its path
	 */
	protected String patchId;

	/**
	 * This is the location of the patch, the dir where this patch has been read
	 * from, relative to AMP app root
	 */
	protected String location;

	/**
	 * The date when the patch has been first found by the patcher module
	 */
	protected Date discovered;

	/**
	 * The state of the patch
	 * 
	 * @see org.digijava.module.xmlpatcher.util.XmlPatcherConstants
	 */
	protected Short state;

	/**
	 * The execution logs for this patch, if any
	 */
	protected List<XmlPatchLog> logs;

	public XmlPatch() {

	}

	/**
	 * Produces a new patch object using the patchId(file name) and location
	 * (directory of discovery). Marks the discovery time and flags the patch as
	 * OPEN
	 * 
	 * @param patchId
	 *            the name of the XML patch file
	 * @param location
	 *            the directory of the patch file relative to AMP root
	 */
	public XmlPatch(String patchId, String location) {
		this.patchId = patchId;
		this.location = location;
		this.setState(XmlPatcherConstants.PatchStates.OPEN);
		this.discovered = new Date(System.currentTimeMillis());
	}

	public String getPatchId() {
		return patchId;
	}

	public void setPatchId(String patchId) {
		this.patchId = patchId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDiscovered() {
		return discovered;
	}

	public void setDiscovered(Date discovered) {
		this.discovered = discovered;
	}

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public List<XmlPatchLog> getLogs() {
		return logs;
	}

	public void setLogs(List<XmlPatchLog> logs) {
		this.logs = logs;
	}

	@Override
	public int compareTo(XmlPatch o) {
		return this.getPatchId().compareTo(o.getPatchId());
	}

}