package com.vivian.baseproject.tools;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOManager {
	public String accessToken;
	public static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/btmedia/";
	public static final String filePath = basePath + "fileCache/";
	public static final String imagesPath = basePath + "fileCache/images/";
	public static final String downloadPath = filePath + "downloads/";
	//后边加了用户唯一标识码id或者user_unique_key
	public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/btmedia/fileCache/" + "user_";
	private static String LAST_NAME;
	private String fileName;
	private static IOManager manager;
	public static final String LATEST_ARTICLE_LIST = "latest_article_list";
	public static final String HOT_ARTICLE_LIST = "hot_article_list";
	public static final String TAG_MESSAGE = "tag_message";// 话题头信息
	public static final String AUCTION_MESSAGE = "auction_message";// 竞拍头信息
	public static final String AUTHOR_MESSAGE = "AUTHOR_MESSAGE";// 其他的作者头信息
	public static final String SPECIAL_ARTICLE_LIST = "special_article_list";// 专题列表
	public static final String DRAFT_ARTICLE_LIST = "DRAFT_ARTICLE_LIST";// 草稿
	public static final String PENDING_ARTICLE_LIST = "PENDING_ARTICLE_LIST";// 待审核
	public static final String PUBLISHED_ARTICLE_LIST = "PUBLISHED_ARTICLE_LIST";// 发布
	public static final String REJECTED_ARTICLE_LIST = "REJECTED_ARTICLE_LIST";// 被拒
	public static final String USER_ARTICLE_LIST = "USER_ARTICLE_LIST";// 某个用户的文章列表
	public static final String HOME_ARTICLE_LIST = "HOME_ARTICLE_LIST";// 首页文章列表
	public static final String ENGLISH_TAG_LIST = "ENGLISH_TAG_LIST";// 英文专题文章列表
	public static final String SHANGYEJIAZHI_TAG_LIST = "SHANGYEJIAZHI_TAG_LIST";// 商业价值专题文章列表
	public static final String INSIDE_VERIFIED_AUTHOR_LIST = "INSIDE_VERIFIED_AUTHOR_LIST";// 内部推荐作者
	public static final String OUTSIDE_VERIFIED_AUTHOR_LIST = "OUTSIDE_VERIFIED_AUTHOR_LIST";// 外部推荐作者
	public static final String FANS_LIST = "FANS_LIST";// 粉丝
	public static final String FIND_ARTICLE_LSIT = "FIND_ARTICLE_LSIT"; // find list
	public static final String COMMENT_VOTES_LIST = "COMMENT_VOTES_LIST";

	public static final String SEARCH_RESULT_LIST = "SEARCH_RESULT_LIST";// 搜索结果

	public static final String CREDITS_LIST = "CREDITS_LIST";// 积分明细列表（对账单）
	public static final String INFORM_LIST = "INFORM_LIST";// 通知列表
	public static final String CONVERSATION_LIST = "CONVERSATION_LIST";
	public static final String WEALTH_LIST = "WEALTH_LIST";
	public static final String UNREADINFORM = "UNREADINFORM";
	public static final String UNIONACCOUNT = "UNIONACCOUNT";

	public static IOManager getManager() {
		if (manager == null) {
			manager = new IOManager();
		}
		return manager;
	}

	public void setFileName(String filename) {
		fileName = filename;
	}

	protected String getFileName() {
		return fileName;
	}

	public IOManager() {
		// /LAST_NAME = "." + accessToken + "_" + "cache";
		LAST_NAME = ".txt";
	}

	public void writeObject(Object object, String filename) {
		String filePath;
		if (filename.equals(HOME_ARTICLE_LIST) || filename.equals(FIND_ARTICLE_LSIT)) {
			filePath = basePath + filename + LAST_NAME;
		} else {
			filePath = path + "/" + filename + LAST_NAME;
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		if (object != null) {
			try {
				file.createNewFile();
				FileOutputStream fileWriter = new FileOutputStream(filePath);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileWriter);
				outputStream.writeObject(object);
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void writeObject(Object object, String filename, String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(filePath + filename + LAST_NAME);
		if (file.exists()) {
			file.delete();
		}
		if (object != null) {
			try {
				file.createNewFile();
				FileOutputStream fileWriter = new FileOutputStream(filePath + filename + LAST_NAME);
				ObjectOutputStream outputStream = new ObjectOutputStream(fileWriter);
				outputStream.writeObject(object);
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public Object readObject(String fileName) {
		String filePath;
		if (fileName.equals(HOME_ARTICLE_LIST) || fileName.equals(FIND_ARTICLE_LSIT)) {
			filePath = basePath + fileName + LAST_NAME;
		} else {
			filePath = path + "/" + fileName + LAST_NAME;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fileInputStream = null;
		ObjectInputStream inputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			inputStream = new ObjectInputStream(fileInputStream);
			Object lists = inputStream.readObject();
			inputStream.close();
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param fileName
	 * @return
	 */
	public Object readDownloadObject(String fileName) {
		String filePath;
		filePath = downloadPath + fileName + LAST_NAME;
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fileInputStream = null;
		ObjectInputStream inputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			inputStream = new ObjectInputStream(fileInputStream);
			Object lists = inputStream.readObject();
			inputStream.close();
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void deleteFile(String fileName) {
		String filePath = path + "/" + fileName + LAST_NAME;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	// 删除所有的文件
	// 不删除首页推荐和发现列表内容
	public void deleteFile(File file) {

		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (int i = 0; i < childFile.length; i++) {
				deleteFile(childFile[i]);
			}

			file.delete();
		}
		if (file.isFile()) {
			file.delete();
			return;
		}
	}

	// 递归获取文件夹大小
	public static long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

}
