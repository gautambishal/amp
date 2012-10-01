/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.List;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.contentrepository.form.LabelForm;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class LabelAction extends MultiAction {

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		LabelForm lForm		= (LabelForm) form;
		if ( "add".equals(lForm.getAction()) )
			modeAdd(mapping, lForm, request, response);
		else if ( "remove".equals(lForm.getAction()) ) {
			modeRemove(mapping, lForm, request, response);
		}
		
		return null;
	}
	
	public void modeAdd(ActionMapping mapping, LabelForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String applyClickAction			= request.getParameter("applyClick");
		if ( form.getDocUUID() != null && form.getDocUUID().length() > 0 && 
				form.getLabelUUIDs() != null && form.getLabelUUIDs().length > 0) {
			Node docNode				= DocumentManagerUtil.getWriteNode(form.getDocUUID(), request);
			NodeWrapper nw				= new NodeWrapper(docNode);
			List<Label> existingLabels	= nw.getLabels();
			for (int i=0; i<form.getLabelUUIDs().length; i++ ) {
				Node labelNode	= DocumentManagerUtil.getWriteNode(form.getLabelUUIDs()[i], request);
				Label label		= new Label(labelNode);
				if ( existingLabels.contains(label) ){
					if ( !"true".equals(applyClickAction) )
						nw.removeLabel( labelNode.getUUID() );
				}
				else
					nw.addLabel(labelNode);
			}
			
		}
		DocumentManagerUtil.logoutJcrSessions(request.getSession());
		
	}
	public void modeRemove(ActionMapping mapping, LabelForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if ( form.getDocUUID() != null && form.getDocUUID().length() > 0 && 
				form.getLabelUUIDs() != null && form.getLabelUUIDs().length > 0) {
			Node docNode	= DocumentManagerUtil.getWriteNode(form.getDocUUID(), request);
			NodeWrapper nw	= new NodeWrapper(docNode);
			for (int i=0; i<form.getLabelUUIDs().length; i++ ) {
				nw.removeLabel( form.getLabelUUIDs()[i] );
			}
			
		}
		DocumentManagerUtil.logoutJcrSessions(request.getSession());
	}

}
