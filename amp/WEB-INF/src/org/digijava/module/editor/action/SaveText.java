/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.editor.action;


import java.util.Date;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.Hibernate;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SaveText
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        EditorForm formBean = (EditorForm) form;

        User user = RequestUtils.getUser(request);
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
        Site site;
        if (moduleInstance == null) {
            site = RequestUtils.getSite(request);
        }
        else {
            site = moduleInstance.getSite();
        }

        boolean exists = true;
        Editor editor = DbUtil.getEditor(site.getSiteId(), formBean.getKey(),
                                     formBean.getLang());
        if (editor == null) {
            editor = new Editor();
            exists = false;
            editor.setSiteId(site.getSiteId());
            editor.setEditorKey(formBean.getKey());
            editor.setLanguage(formBean.getLang());
        }
        
        // If Activity Versioning is active and the text has changed then create
		// a new record.
        if (!formBean.getContent().equals(formBean.getOriginalContent()) && formBean.getNewId()!=null) {
			exists = false;
			editor.setEditorKey(formBean.getNewId());
		}

        if (formBean.getContent() != null) {
            editor.setBody(formBean.getContent());
        }
        if (formBean.getTitle() != null) {
            editor.setTitle(formBean.getTitle());
        }
        editor.setLanguage(formBean.getLang());
        editor.setLastModDate(new Date());
        editor.setUser(user);
        editor.setTitle(formBean.getTitle());
        editor.setUrl(formBean.getReturnUrl());

        if (exists) {
            PersistenceManager.updateObject(editor);
        } else {
            PersistenceManager.createObject(editor);
        }

        return new ActionForward((formBean.getReturnUrl() == null ? "/" : formBean.getReturnUrl()), true);
    }
}
