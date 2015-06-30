package com.vivian.baseproject.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

public class FileUtil {

	public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/nicehair/";
	public static final String CAMERA = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
	public static final String CACHE_IMG = Environment.getExternalStorageDirectory().getPath() + "/btmedia/fileCache/images/";

	/**
	 * 读取输入流数据
	 *
	 * @param inStream
	 * @return
	 */
	public static byte[] read(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建根目录
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 获取指定目录下的文件名称列表
	 * 
	 * @param folderPath
	 * @return
	 */
	public static String[] getImageNames(String folderPath) {
		File file01 = new File(folderPath);
		String[] files01 = file01.list();
		int imageFileNums = 0;
		try {
			for (int i = 0; i < files01.length; i++) {
				File file02 = new File(folderPath + "/" + files01);
				if (!file02.isDirectory()) {
					if (isImageFile(file02.getName())) {
						imageFileNums++;
					}
				}
			}
			String[] files02 = new String[imageFileNums];
			int j = 0;
			for (int i = 0; i < files01.length; i++) {
				File file02 = new File(folderPath + "/" + files01);
				if (!file02.isDirectory()) {
					if (isImageFile(file02.getName())) {
						files02[j] = file02.getName();
						j++;
					}
				}
			}
			return files02;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] {};
		}

	}

	/**
	 * 判断是否是图片文件
	 * 
	 * @param fileName
	 * @return
	 */
	private static boolean isImageFile(String fileName) {
		String fileEnd = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (fileEnd.equalsIgnoreCase("jpg")) {
			return true;
		} else if (fileEnd.equalsIgnoreCase("png")) {
			return true;
		} else if (fileEnd.equalsIgnoreCase("bmp")) {
			return true;
		} else if (fileEnd.equalsIgnoreCase("jpeg")) {
			return true;
		} else {
			return false;
		}
	}
}