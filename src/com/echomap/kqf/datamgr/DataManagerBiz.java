package com.echomap.kqf.datamgr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.echomap.kqf.EchoWriteConst;
import com.echomap.kqf.biz.TextParsingBiz;
import com.echomap.kqf.data.DocTag;
import com.echomap.kqf.data.TreeData;
import com.echomap.kqf.data.TreeTimeData;
import com.echomap.kqf.data.TreeTimeSubData;
import com.echomap.kqf.looper.data.NestedTreeData;

/**
 * 
 * @author mkatz
 */
public class DataManagerBiz {
	private final static Logger LOGGER = LogManager.getLogger(DataManagerBiz.class);
	private static DataManagerDB dataManagerDB = DataManagerDB.getDataManager();
	private final static Map<File, DataManagerBiz> dataManagerList = new HashMap<File, DataManagerBiz>();

	DataManagerDBData currentFileData = null;
	TreeTimeData lastAddedTime = null;

	/**
	 * 
	 * @param inputFile
	 * @return
	 */
	public static DataManagerBiz getDataManager(final String inputFile) {
		final File inputFile2 = new File(inputFile);
		return getDataManager(inputFile2);
	}

	/**
	 * 
	 * @param inputFile
	 * @return
	 */
	public static DataManagerBiz getDataManager(final File inputFile) {
		if (dataManagerList.containsKey(inputFile)) {
			return dataManagerList.get(inputFile);
		}
		final DataManagerBiz biz = new DataManagerBiz();
		dataManagerList.put(inputFile, biz);
		return biz;
	}

	/**
	 * START
	 */
	public void initialize(final File inputFile) {
		currentFileData = dataManagerDB.getDataForFile(inputFile);
	}

	public List<String> getTagListScene() {
		return currentFileData.getTagListScene();
	}

	public List<String> getTagListSubScenes() {
		return currentFileData.getTagListSubScenes();
	}

	public List<String> getTagListTimeDate() {
		return currentFileData.getTagListTimeDate();
	}

	public List<String> getTagListActors() {
		return currentFileData.getTagListActors();
	}

	public List<String> getTagListItems() {
		return currentFileData.getTagListItems();
	}

	private void parseSubItemData(final DataItem di, final String tag) {
		final Map<String, String> dmap = TextParsingBiz.parseNameValueAtDivided(tag);
		final Set<String> atdKeys = dmap.keySet();
		for (final Iterator<String> iter = atdKeys.iterator(); iter.hasNext();) {
			final String key = (String) iter.next();
			final String val = dmap.get(key);
			LOGGER.debug("TIME Key='" + key + "'='" + val + "'");
			final DataSubItem dsi = new DataSubItem();
			dsi.setName(key);
			dsi.setValue(val);
			// foundMatch = compareMetaTags(key, val);
			di.getDataSubItems().add(dsi);
		}
	}

	private void parseSubItemData(final DataItem di, final NestedTreeData tag) {
		final List<NestedTreeData> dmap = tag.getData();
		// Map<String, String>
		// final Set<String> atdKeys = dmap.keySet();
		for (final NestedTreeData data : dmap) {
			final String key = data.getTag();
			final String val = data.getValue();
			LOGGER.debug("data Key='" + key + "'='" + val + "'");
			final DataSubItem dsi = new DataSubItem();
			dsi.setName(key);
			dsi.setValue(val);
			// foundMatch = compareMetaTags(key, val);
			di.getDataSubItems().add(dsi);
		}
	}

	private void parseSubItemData(final DataItem di, final TreeTimeSubData ttsd, final String line) {
		final Map<String, String> dmap = ttsd.getData();
		if (dmap == null || dmap.size() < 1) {
			di.setName(line);
			final DataSubItem dsi = new DataSubItem();
			dsi.setName("value");
			dsi.setValue(line);
			di.getDataSubItems().add(dsi);
		} else {
			final Set<String> atdKeys = dmap.keySet();
			for (final Iterator<String> iter = atdKeys.iterator(); iter.hasNext();) {
				final String key = (String) iter.next();
				final String val = dmap.get(key);
				LOGGER.debug("TIME Key='" + key + "'='" + val + "'");
				final DataSubItem dsi = new DataSubItem();
				dsi.setName(key);
				dsi.setValue(val);
				// foundMatch = compareMetaTags(key, val);
				di.getDataSubItems().add(dsi);
			}
		}
	}

	public void addTime(final TreeTimeData found, final String line) {
		currentFileData.getDatalistTimeDate().add(found);

//		if (lastAddedTime != null && found != null) {
//			final String valLast = lastAddedTime.getDataParsedByName(EchoWriteConst.WORD_MARKER);
//			final String valFound = found.getDataParsedByName(EchoWriteConst.WORD_MARKER);
//			if (!StringUtils.isEmpty(valLast) && !StringUtils.isEmpty(valFound)) {
//				final Integer valLastI = Integer.valueOf(valLast);
//				final Integer valFoundI = Integer.valueOf(valFound);
//				if(valFoundI<valLastI) {
//					
//				}
//			}
//		}
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_TIME);
		di.setName(found.getTag());
		di.setRawValue(line);// found.getTag());
		parseSubItemData(di, found.getTag());
		// setupLastTime(di);
		lastAddedTime = found;
	}

	public void addTime(final TreeTimeData ttd, final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalistTimeDate().add(ttd);

		// final String value = ttsd.getDataByKey(EchoWriteConst.WORD_MARKER);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_TIME);
		di.setName(EchoWriteConst.WORD_MARKER);
		di.setRawValue(line);// found.getTag());
		parseSubItemData(di, ttsd, line);
		// setupLastTime(ttsd);
	}

	public void addChapter(final NestedTreeData ttd) {
		currentFileData.getDatalist().add(ttd);

		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_CHAPTER);
		di.setName(ttd.getTag());
		di.setRawValue(ttd.getTag());
		parseSubItemData(di, ttd);
	}

	public void addSection(final NestedTreeData ttd) {
		currentFileData.getDatalist().add(ttd);

		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_SECTION);
		di.setName(ttd.getTag());
		di.setRawValue(ttd.getTag());
		parseSubItemData(di, ttd);
	}

	public void addMeta(final DocTag metaDocTag) {
		final DataItem di = new DataItem();
		currentFileData.getMetadataList().add(di);
		di.setCategory(EchoWriteConst.WORD_META);
		di.setName(metaDocTag.getName());
		di.setRawValue(metaDocTag.getFullText());
		// metaDocTag.getValue();
		// metaDocTag.getAddedLines();
		// parseSubItemData(di, ttsd, line);
	}

	public void addScene(final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalist().add(ttsd);

		final String value = ttsd.getDataByKey(EchoWriteConst.WORD_NAME);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_SCENE);
		di.setName(value);
		di.setRawValue(line);
		parseSubItemData(di, ttsd, line);
		// setLastTime(di);
	}

	public void addSubScene(final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalist().add(ttsd);

		final String value = ttsd.getDataByKey(EchoWriteConst.WORD_NAME);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_SUBSCENE);
		di.setName(value);
		di.setRawValue(line);
		parseSubItemData(di, ttsd, line);
		// setLastTime(di);
	}

	public void addMisc(final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalist().add(ttsd);

		final String value = ttsd.getDataByKey(EchoWriteConst.WORD_NAME);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_OTHER);
		di.setName(value);
		di.setRawValue(line);
		parseSubItemData(di, ttsd, line);
		// setLastTime(di);
	}

	public void addTime(final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalist().add(ttsd);

		final String value = ttsd.getDataByKey(EchoWriteConst.WORD_DESC);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_TIMEDESC);
		di.setName(value);
		di.setRawValue(line);
		parseSubItemData(di, ttsd, line);
		// setLastTime(di);
	}

	public void addActor(final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalist().add(ttsd);

		final String value = ttsd.getDataByKey(EchoWriteConst.WORD_DESC);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_ACTOR);
		di.setName(value);
		di.setRawValue(line);
		parseSubItemData(di, ttsd, line);
		// setLastTime(di);
	}

	public void addItem(final TreeTimeSubData ttsd, final String line) {
		currentFileData.getDatalist().add(ttsd);

		final String value = ttsd.getDataByKey(EchoWriteConst.WORD_DESC);
		final DataItem di = new DataItem();
		currentFileData.getDataItemList().add(di);
		di.setCategory(EchoWriteConst.WORD_INVENTORY);
		di.setName(value);
		di.setRawValue(line);
		parseSubItemData(di, ttsd, line);
		// setLastTime(di);
	}

	public TreeTimeData findTimeDate(String dateTime) {
		TreeTimeData found = null;
		if (dateTime == null) {
			if (currentFileData.getDatalistTimeDate().size() < 1) {
				dateTime = "marker: 0dark";
			} else {
				return null;
			}
		}

		for (final TreeTimeData treeTimeData : currentFileData.getDatalistTimeDate()) {
			// DATA_MANAGER.getDatalistTimeDate()) {
			if (treeTimeData != null && treeTimeData.getTag() != null)
				if (treeTimeData.getTag().compareTo(dateTime) == 0) {
					found = treeTimeData;
				}
		}
		if (found == null) {
			final List<DataItem> list = selectTimes();
			for (final DataItem data : list) {
				if (data != null && data.getName() != null)
					if (data.getName().compareTo(dateTime) == 0) {
						found = new TreeTimeData(dateTime); // TODO??
						// found.setTag(data.getName());
					}
			}
		}
		return found;
	}

	private List<DataItem> selectTimes() {
		return getItemsByCategory("time");
	}

	public void printDatalist() {
		LOGGER.debug("DATA:--->>");
		for (final TreeData nestedTreeData : currentFileData.getDatalist()) {
			LOGGER.debug(nestedTreeData);
		}
		LOGGER.debug(":---:");
		for (final DataItem dataItem : currentFileData.getDataItemList()) {
			LOGGER.debug(dataItem);
		}

		LOGGER.debug("<<---DATA.");
	}

	public List<DataItem> getMetaItems() {
		final List<DataItem> diList = new ArrayList<>();
		for (final DataItem dataItem : currentFileData.getMetadataList()) {
			diList.add(dataItem);
		}
		return diList;

	}

	public List<DataItem> getItems() {
		final List<DataItem> diList = new ArrayList<>();
		for (final DataItem dataItem : currentFileData.getDataItemList()) {
			diList.add(dataItem);
		}
		return diList;
	}

	public List<DataItem> getItemsByCategory(final List<String> tagSceneTags) {
		final List<DataItem> diList = new ArrayList<>();
		for (final DataItem dataItem : currentFileData.getDataItemList()) {
			if (tagSceneTags.contains(dataItem.getCategory())) {
				diList.add(dataItem);
			}
		}
		return diList;
	}

	public List<DataItem> getItemsByCategory(final String tagSceneTags) {
		final List<DataItem> diList = new ArrayList<>();
		for (final DataItem dataItem : currentFileData.getDataItemList()) {
			if (dataItem.getCategory().compareTo(tagSceneTags) == 0) {
				diList.add(dataItem);
			}
		}
		return diList;
	}

	public static void close() {
		LOGGER.info("Closing Data Layers");
		DataManagerDB.close();
		LOGGER.info("Closing Data Biz Layer");
		dataManagerList.clear();
		LOGGER.info("Closed Data Layers");
	}

	public void postProcess(final File inputFile) {
		final DataManagerDB dataManagerDB = DataManagerDB.getDataManager();
		dataManagerDB.saveDataForFile(currentFileData);
	}

	public void startProcess(final File inputFile) {
		final DataManagerDB dataManagerDB = DataManagerDB.getDataManager();
		dataManagerDB.saveDataForFile(currentFileData);
		currentFileData = dataManagerDB.getDataForFile(inputFile);
	}

	public DataManagerDBData getDataForFile(final File inputFile) {
		final DataManagerDB dataManagerDB = DataManagerDB.getDataManager();
		final DataManagerDBData data = dataManagerDB.getDataForFile(inputFile);
		currentFileData = data;
		return data;
	}

	public void clearDataForFile(final File inputFile) {
		if (inputFile == null)
			return;
		final DataManagerDB dataManagerDB = DataManagerDB.getDataManager();
		dataManagerDB.clearDataForFile(inputFile);
		initialize(inputFile);
	}

}
