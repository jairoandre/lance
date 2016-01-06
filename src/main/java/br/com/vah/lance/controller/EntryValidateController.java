package br.com.vah.lance.controller;

import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vah.lance.constant.EntryStatusEnum;
import br.com.vah.lance.entity.Comment;
import br.com.vah.lance.entity.Entry;
import br.com.vah.lance.service.DataAccessService;
import br.com.vah.lance.service.EntryService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class EntryValidateController extends AbstractController<Entry> {

	private @Inject transient Logger logger;

	private @Inject EntryService das;

	private @Inject LoginController loginController;
	
	private Comment comment;

	@PostConstruct
	public void init() {
		logger.info(this.getClass().getSimpleName() + " created.");
	}

	@Override
	public void onLoad() {
		super.onLoad();
		comment = createComment();
	}

	@Override
	public DataAccessService<Entry> getService() {
		return das;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return the comment
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@Override
	public Entry createNewItem() {
		return new Entry();
	}

	@Override
	public String editPage() {
		return "/pages/entry/valid.xhtml";
	}

	@Override
	public String listPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Comment createComment() {
		Comment comment = new Comment();
		comment.setAuthor(loginController.getUser());
		comment.setCreatedOn(new Date());
		comment.setEntry(getItem());
		return comment;
	}

	public String addComment() {
		getItem().getComments().add(comment);
		comment = createComment();
		return null;
	}

	@Override
	public String doSave() {
		getItem().setStatus(EntryStatusEnum.V);
		return super.doSave();
	}

	public String doDenySave() {
		getItem().setStatus(EntryStatusEnum.P);
		return super.doSave();
	}

	@Override
	public String back() {
		return "/pages/entry/list.xhtml?faces-redirect=true";
	}

}
