package com.echomap.kqf.looper.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LooperDao {

	private File inputFile;
	private CountDao totalCount = null;
	private CountDao chaptCount = null;

	private final List<ChapterDao> chapters = new ArrayList<ChapterDao>();

	private boolean inSpecial = false;
	private boolean inLongDocTag = false;
	private String htmlLine = null;
	private boolean lastLineWasChapter = false;
	private Integer thisLineCharacterCount = 0;

	public LooperDao() {

	}

	public void clear() {
		setInSpecial(false);
		setInLongDocTag(false);
		setHtmlLine(null);
	}

	public void preReadLine() {
		// TODO Auto-generated method stub

	}

	public void postReadLine() {
		// TODO Auto-generated method stub
	}

	public void inReadLine() {
		// TODO Auto-generated method stub
	}

	public String getHtmlLine() {
		return htmlLine;
	}

	public void setHtmlLine(String htmlLine) {
		this.htmlLine = htmlLine;
	}

	public boolean isInSpecial() {
		return inSpecial;
	}

	public void setInSpecial(boolean inSpecial) {
		this.inSpecial = inSpecial;
	}

	public boolean isInLongDocTag() {
		return inLongDocTag;
	}

	public void setInLongDocTag(boolean inLongDocTag) {
		this.inLongDocTag = inLongDocTag;
	}

	public boolean isLastLineWasChapter() {
		return lastLineWasChapter;
	}

	public void setLastLineWasChapter(boolean lastLineWasChapter) {
		this.lastLineWasChapter = lastLineWasChapter;
	}

	public Integer getThisLineCharacterCount() {
		return thisLineCharacterCount;
	}

	public void setThisLineCharacterCount(Integer thisLineCharacterCount) {
		this.thisLineCharacterCount = thisLineCharacterCount;
	}

	public Integer addThisLineCharacterCount(final Integer newNum) {
		this.thisLineCharacterCount += newNum;
		return this.thisLineCharacterCount;
	}

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public void InitializeCount() {
		setTotalCount(new CountDao());
		setChaptCount(new CountDao());
		getChapters().clear();

		getTotalCount().setNumChapters(1);
		getChaptCount().addOneToNumLines();
	}

	public CountDao getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(CountDao totalCount) {
		this.totalCount = totalCount;
	}

	public CountDao getChaptCount() {
		return chaptCount;
	}

	public void setChaptCount(CountDao chaptCount) {
		this.chaptCount = chaptCount;
	}

	public List<ChapterDao> getChapters() {
		return chapters;
	}

}