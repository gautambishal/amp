package org.digijava.module.translation.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.lucene.LuceneWorker;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.entity.MessageGroup;
import org.digijava.module.translation.form.NewAdvancedTrnForm;
import org.digijava.module.translation.lucene.LucTranslationModule;
import org.digijava.module.translation.util.ListChangesBuffer;

/**
 * New Advanced translation mode action. AMP-4911
 * @author Irakli Kobiashvili
 *
 */
public class ShowNewAdvancedTranslations extends Action{

	private static Logger logger = Logger.getLogger(ShowNewAdvancedTranslations.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		NewAdvancedTrnForm trnForm = (NewAdvancedTrnForm)form;

		//TODO check security above normally!
		User user = RequestUtils.getUser(request);
		if (user == null){
			throw new SecurityException("Translation is not granted to this user");		
		}

		Site site = RequestUtils.getSite(request);
		//TODO check site

		ListChangesBuffer<String, Message> buffer = TrnUtil.getBuffer(request.getSession());
		
		int totalPages = 0;

		//Save submitted translations.
		if (trnForm.getMessage()!=null && trnForm.getLocale()!=null && trnForm.getKey()!=null){
			
			for (int i = 0; i < trnForm.getKey().length; i++) {
				
				String key = trnForm.getKey()[i];
				String locale = trnForm.getLocale()[i];
				String newText = trnForm.getMessage()[i];
				
				//Try to find same translation in db
				Message original = TranslatorWorker.getInstance(key).getByKey(key, locale, site.getId());
				
				if (original==null){
					//save new record if not found in db
					Message newMessage = new Message();
					newMessage.setKey(key);
					newMessage.setLocale(locale);
					newMessage.setMessage(newText);
					buffer.operationAdd(newMessage);
					//TranslatorWorker.getInstance(key).save(newMessage);
				}else if (original!=null && !original.getMessage().equals(newText)){
					//update existing
					Message editedMessage = new Message();
					editedMessage.setKey(key);
					editedMessage.setLocale(locale);
					editedMessage.setMessage(newText);
					buffer.operationEdit(editedMessage);
					//TranslatorWorker.getInstance(key).update(original);
				}
			}
		}
		
		if (trnForm.getSearchTerm()!=null && !trnForm.getSearchTerm().trim().equals("")){

			//Search by key which is generated from input text. this will show exact match only.
			String searchKey = TranslatorWorker.generateTrnKey(trnForm.getSearchTerm());
			logger.info("Searching for "+trnForm.getSearchTerm()+", key generated is "+searchKey+"...");
			Collection<Message> results = TranslatorWorker.getAllTranslationsOfKey(searchKey, site.getId());
			Collection<MessageGroup> groups = TrnUtil.groupByKey(results);
			List<MessageGroup> groupsList = new ArrayList<MessageGroup>(groups);
			trnForm.setResultList(groupsList);
			logger.info("Finished. \nFound "+results.size()+" results in "+groupsList.size()+ " groups");
			
			//Now search by Lucene
			ServletContext context = request.getSession().getServletContext();
			
			//get results from Lucene
			Hits hits = LuceneWorker.search(Message.class, trnForm.getSearchTerm(), context);
			logger.info("Lucen found "+hits.length()+" records");
			if (hits !=null && hits.length()>0){
				int currentPage = (trnForm.getPageNumber()==null || trnForm.getPageNumber().intValue()==0) ? 0 : trnForm.getPageNumber().intValue()-1;
				int itemsPerPage = getItemsPerPage().intValue();
				int startItemNo = itemsPerPage * currentPage;
				int stopItemNo = startItemNo + itemsPerPage;
				
				totalPages = (int) Math.ceil((hits.length() + groupsList.size()) / itemsPerPage);
				
				//Store for keys of all found translations.
				Map<String, Float> scoresByKeys = new HashMap<String, Float>();

				for (int i = startItemNo; i < stopItemNo; i++) {
					Document doc = hits.doc(i);
					float score = hits.score(i);
					String key = doc.get(LucTranslationModule.FIELD_KEY);
					Float oldScore = scoresByKeys.get(key);
					//add only higher values
					if (oldScore == null || oldScore.floatValue() < score){
						scoresByKeys.put(key,new Float(score));
					}
//					printDoc(doc, score);
				}
				logger.info("Current page: "+currentPage+" of "+totalPages);
				logger.info("Items per page page: "+itemsPerPage);
				logger.info("Showing from: "+startItemNo + " to: "+stopItemNo);
				if (trnForm.getMessage()!=null && trnForm.getLocale()!=null && trnForm.getKey()!=null){
					logger.info("Submitted keys:"+trnForm.getKey().length + ", locales:" + trnForm.getLocale().length+", messages:"+trnForm.getMessage().length);
				}
				
				//get translations for all languages for found keys. 
				//This means that if we found English translation that matches searched text, then its French and Spanish versions will also be loaded.
				List<Message> lucMessages = TrnUtil.getMessagesForKeys(scoresByKeys.keySet());
				
				List<Message> mergedWithBuffer = buffer.marge(lucMessages);
				//Group the by keys. use also scores
				Collection<MessageGroup> lucGroups = TrnUtil.groupByKey(mergedWithBuffer, new TrnUtil.StandardMessageGroupFactory() , scoresByKeys);
				List<MessageGroup> sortedGroups  = new ArrayList<MessageGroup>(lucGroups);
				//Sort by scores
				Collections.sort(sortedGroups, new TrnUtil.MsgGroupScoreComparator()); 
				trnForm.setResultList(sortedGroups);
			}
		}else{
			logger.info("Nothing to search for");
		}
		trnForm.setTotalPages(new Integer((totalPages==0)?1:totalPages));
		trnForm.setChangesList(buffer.listChanges());
		trnForm.setPossibleLocales(getPossibleLocales(site));
		return mapping.findForward("forward");
	}

//	private void printDoc(Document doc, float score) throws IOException {
//		if(doc.get(LucTranslationModule.FIELD_LANG).equalsIgnoreCase("en")){
//			logger.info(
//					"["				+ score + 
//					"] key=" 		+ doc.get(LucTranslationModule.FIELD_KEY) +
//					"(" 			+ doc.get(LucTranslationModule.FIELD_LANG) +
//					") message=" 	+ doc.get(LucTranslationModule.FIELD_MESSAGE)
//				);
//		}
//	}

	private String[] getPossibleLocales(Site site) {
		Set<Locale> locales = SiteUtils.getTransLanguages(site);
		String result[] = new String[locales.size()];
		int c=0;
		for (Locale locale : locales) {
			result[c++] = locale.getCode();
		}
		return result;
	}

	private Integer getItemsPerPage(){
		return new Integer(20);
	}
	
}
