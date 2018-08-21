package com.echomap.kqf.persist;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.echomap.kqf.data.FormatDao;
import com.echomap.kqf.data.Profile;
import com.echomap.kqf.data.ProfileData;

public class ProfilePersistV2 {
	private final static Logger LOGGER = LogManager.getLogger(ProfilePersistV2.class);

	public Profile convertFromV1toV2(final ProfileData profileData) {
		final Profile profile = new Profile();

		// pd.setText("titleOne", child.get("titleTwo", key));
		// pd.setText("titleTwo", child.get("titleThree", ""));
		// pd.setText("titleThree", child.get("titleThree", ""));

		// <Main Section>
		profile.setKey(profileData.getKey());
		profile.setMainTitle(setupValue(profileData, "titleOne", "Unnamed"));
		profile.setSubTitle(setupValue(profileData, "titleTwo", ""));
		profile.setSeriesTitle(profileData.getText("seriesTitle"));
		profile.setVolume(setupValue(profileData, "volume", "1"));
		profile.setInputFile(setupValue(profileData, "inputFile", ""));
		profile.setInputFilePrefix(setupValue(profileData, "inputFilePrefix", ""));
		profile.setAppendUnderscoreToPrefix(profileData.getSelected("appendUnderscoreToPrefix"));

		// <RegEx Section>
		final String regexpChapterText = "-=\\s+(?<cname>Chapter)\\s+(?<cnum>\\w+):\\s+(?<ctitle>.*)\\s+=-";
		final String regexpSectionText = "-=\\s+(?<sname>Section):\\s+(?<stitle>\\w+)\\s+=-";
		// profile.setRegexpChapterText(setupValue(profileData, "regexpChapter",
		// regexpChapterText));
		// profile.setRegexpSectionText(setupValue(profileData, "regexpSection",
		// regexpSectionText));
		profile.setRegexpChapter(setupValue(profileData, "regexpChapter", regexpChapterText));
		profile.setRegexpSection(setupValue(profileData, "regexpSection", regexpSectionText));

		// <Word Counter Section>
		// outputCountFileText
		profile.setOutputCountFile(setupValue(profileData, "ouputCountFile", ""));

		// <Formatting Section>
		profile.setOutputFormatSingleFile(setupValue(profileData, "ouputFile", ""));
		profile.setOutputFormatChpHtmlDir(setupValue(profileData, "outputDir", ""));
		profile.setOutputFormatChpTextDir(setupValue(profileData, "outputFormatChpTextDirText", ""));
		profile.setCbRemoveDiv(profileData.getSelected("cbRemoveDiv"));
		profile.setCbCenterStars(profileData.getSelected("cbCenterStars"));
		profile.setCbDropCapChapters(profileData.getSelected("cbDropCapChapters"));
		profile.setWantTextChptOutput(profileData.getSelected("wantTextChptOutput"));
		// center
		Integer counterDigitChoice = profileData.getInt("counterDigitChoice");
		if (counterDigitChoice == null)
			counterDigitChoice = 1;
		profile.setCounterDigitChoice(counterDigitChoice);

		// <DocTag Outliner Section>
		// profile.setOutputCSVAllFile(setupValue(profileData,
		// "outputOutlineFile1", ""));
		// profile.setOutputCSVOutlineFile(setupValue(profileData,
		// "outputOutlineFile", ""));
		profile.setOutputDocTagsOutlineFile(setupValue(profileData, "outputDocTagsOutlineFile", ""));
		profile.setOutputDocTagsSceneFile(setupValue(profileData, "outputDocTagsSceneFile", ""));
		Integer outputDocTagsMaxLineLength = profileData.getInt("outputDocTagsMaxLineLength");
		if (outputDocTagsMaxLineLength == null)
			outputDocTagsMaxLineLength = 70;
		profile.setOutputDocTagsMaxLineLength(outputDocTagsMaxLineLength);
		profile.setOutputDocTagsScenePrefix(setupValue(profileData, "outputDocTagsScenePrefix", ""));
		profile.setOutputDocTagsSubScenePrefix(setupValue(profileData, "outputDocTagsSubScenePrefix", ""));
		profile.setSceneCoalateDiv(setupValue(profileData, "sceneCoalateDiv", ""));
		profile.setDocTagsOutlineTags(setupValue(profileData, "outputDocTagsOutlineTags", ""));
		profile.setDocTagsOutlineCompressTags(setupValue(profileData, "outputDocTagsOutlineCTags", ""));
		profile.setDocTagsSceneTags(setupValue(profileData, "outputDocTagsSceneTags", ""));
		profile.setDocTagsSceneCompressTags(setupValue(profileData, "outputDocTagsSceneCoTags", ""));

		// Misnamed in V1
		profile.setOutputCSVOutlineFile(setupValue(profileData, "ouputOutlineFile", ""));
		// Misnamed in V1
		profile.setOutputCSVAllFile(setupValue(profileData, "ouputOutlineFile1", ""));

		// <Input/Output Section>
		profile.setOutputEncoding(setupValue(profileData, "outputEncoding", ""));
		profile.setChapterHeaderTag(setupValue(profileData, "chapterHeaderTag", "*]]"));
		profile.setSectionHeaderTag(setupValue(profileData, "sectionHeaderTag", "*]]"));
		profile.setFmtMode(setupValue(profileData, "fmtMode", ""));
		profile.setDocTagStart(setupValue(profileData, "docTagStart", "[["));
		profile.setDocTagEnd(setupValue(profileData, "docTagEnd", "*]]"));

		// Other Files Section
		profile.setOutputs(profileData.getOutputs());

		//
		// profile.setOuputFile(setupValue(profileData, "ouputFile", ""));
		// profile.setOutputDir(setupValue(profileData, "outputDir", ""));
		// profile.setFmtMode(setupValue(profileData, "ouputCountFile", ""));
		profile.setCbRemoveSectDiv(profileData.getSelected("cbRemoveSectDiv"));
		profile.setCbRemoveSectDiv(profileData.getSelected("cbRemoveSectDiv"));
		profile.setChpDiv(setupValue(profileData, "chpDiv", ""));// used?
		profile.setSecDiv(setupValue(profileData, "secDiv", ""));// used?

		return profile;
	}

	private String setupValue(final ProfileData profileData, final String key, final String defaultValue) {
		String val = profileData.getText(key);
		if (StringUtils.isEmpty(val))
			val = defaultValue;
		return val;
	}

	private String getFilePrefix(final Profile pr) {
		String filePrefixText = null;
		final String fp = pr.getInputFilePrefix();
		if (!StringUtils.isBlank(fp)) {
			filePrefixText = fp;
			if (pr.isAppendUnderscoreToPrefix())
				filePrefixText = filePrefixText + "_";
		}
		return filePrefixText;
	}

	public void setupDao(FormatDao formatDao, Profile selectedProfile, final String appVersion) {
		LOGGER.debug("setupDao: Called");

		// Setup the argument passed
		formatDao.setInputFilename(selectedProfile.getInputFile());
		formatDao.setOutputFilename(selectedProfile.getOutputFormatSingleFile());

		// String filePrefixText = inputFilePrefixText.getText();
		// if (filePrefixCheckbox.isSelected())
		// filePrefixText = filePrefixText + "_";
		formatDao.setFilePrefix(getFilePrefix(selectedProfile));

		formatDao.setOutputCountFile(selectedProfile.getOutputCountFile());
		formatDao.setOutputOutlineFile(selectedProfile.getOutputCSVOutlineFile());
		formatDao.setOutputOutlineFile1(selectedProfile.getOutputCSVAllFile());

		formatDao.setWriteChapters(selectedProfile.getOutputFormatChpHtmlDir());
		formatDao.setWriteChaptersText(selectedProfile.getOutputFormatChpTextDir());

		formatDao.setStoryTitle1(selectedProfile.getMainTitle());
		formatDao.setStoryTitle2(selectedProfile.getSubTitle());
		formatDao.setFormatMode(selectedProfile.getFmtMode());// "Sigil"

		// formatDao.setChapterDivider(regexpChapterText.getText());
		// formatDao.setSectionDivider(regexpSectionText.getText());
		formatDao.setRegexpChapter(selectedProfile.getRegexpChapter());
		formatDao.setRegexpSection(selectedProfile.getRegexpSection());

		formatDao.setDocTagStart(selectedProfile.getDocTagStart());
		formatDao.setDocTagEnd(selectedProfile.getDocTagEnd());

		formatDao.setChapterHeaderTag(selectedProfile.getChapterHeaderTag());
		formatDao.setSectionHeaderTag(selectedProfile.getSectionHeaderTag());

		if (selectedProfile.isCbCenterStars())
			formatDao.setCenterStars(true);
		if (selectedProfile.isCbDropCapChapters())
			formatDao.setDropCapChapter(true);
		if (selectedProfile.isCbRemoveDiv()) {
			formatDao.setRemoveChptDiv(true);
			formatDao.setRemoveSectDiv(true);
		}
		if (selectedProfile.isWantTextChptOutput())
			formatDao.setWantTextChptOutput(true);

		if (selectedProfile.getOutputEncoding() != null && selectedProfile.getOutputEncoding().length() > 0)
			formatDao.setOutputEncoding(selectedProfile.getOutputEncoding());

		final Integer itm = selectedProfile.getCounterDigitChoice();
		// new Integer((String)
		// counterDigitChoice.getSelectionModel().getSelectedItem());
		formatDao.setOutputFormatDigits(itm);

		//
		formatDao.setOutputDocTagsOutlineFile(selectedProfile.getOutputDocTagsOutlineFile());
		formatDao.setOutputDocTagsSceneFile(selectedProfile.getOutputDocTagsSceneFile());
		// formatDao.setOutputDocTagsOther1File(outputDocTagsOther1FileText.getText());
		formatDao.setDocTagsOutlineCompressTags(selectedProfile.getDocTagsOutlineCompressTags());
		formatDao.setDocTagsOutlineExpandTags(selectedProfile.getDocTagsOutlineTags());
		// ETagsText.getText());
		formatDao.setDocTagsSceneTags(selectedProfile.getDocTagsSceneTags());
		formatDao.setDocTagsSceneCoTags(selectedProfile.getDocTagsSceneCompressTags());
		formatDao.setDocTagsScenePrefix(selectedProfile.getOutputDocTagsScenePrefix());
		formatDao.setDocTagsSubScenePrefix(selectedProfile.getOutputDocTagsSubScenePrefix());
		// formatDao.setDocTagsOther1Tags(outputDocTagsOther1TagsText.getText());
		formatDao.setSceneCoalateDivider(selectedProfile.getSceneCoalateDiv());

		final Integer dtmllS = selectedProfile.getOutputDocTagsMaxLineLength();
		// outputDocTagsMaxLineLength.getText();
		Integer dtmllI = 70;
		if (dtmllS != null && dtmllS > 0)
			dtmllI = dtmllS;
		formatDao.setDocTagsMaxLineLength(dtmllI);

		// TODO LATER
		// final List<OtherDocTagData> thisProfileOutputList =
		// XferBiz.loadOutputsFromPrefs(titleOneText.getValue(),
		// getPrefs());
		formatDao.setOutputs(selectedProfile.getOutputs());
		// final List<OtherDocTagData> thisProfileOutputList = loadOutputs();
		// formatDao.setOutputs(thisProfileOutputList);
		// final List<OtherDocTagData> outputs

		formatDao.setSeriesTitle(selectedProfile.getSeriesTitle());
		formatDao.setVolume(selectedProfile.getVolume());

		formatDao.setVersion(appVersion);// appProps.getProperty(PROP_KEY_VERSION));
		LOGGER.debug("setupDao: Done");
	}

}
