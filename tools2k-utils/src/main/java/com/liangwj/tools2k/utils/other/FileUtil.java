package com.liangwj.tools2k.utils.other;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理工具集
 * 
 * @author rock
 *
 */
public class FileUtil {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileUtil.class);

	/** 处理一行的回调 */
	public static interface LineParser {
		/** 返回是否继续处理 */
		boolean onLine(int index, String line);
	}

	/**
	 * 读取文件，然后逐行回调
	 * 
	 * @param filePath
	 * @param callback
	 * @throws IOException
	 */
	public static int parserLine(String filePath, LineParser callback) throws IOException {
		Assert.notNull(callback, "callback must not be null");
		Assert.notNull(filePath, "filePath must not be null");

		BufferedReader reader = null;
		InputStream is = null;

		File file = new File(filePath);
		is = new FileInputStream(file);
		int index = 0; // 行号

		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF8"));

			String line;

			while ((line = reader.readLine()) != null) {
				index++;

				line = line.trim(); // 先去空格
				if (!StringUtils.hasText(line)) {
					// 如果为空就不管了
					continue;
				}

				boolean ok = callback.onLine(index, line);
				if (!ok) {
					// 如果处理结果是false 就终止
					break;
				}
			}
		} catch (IOException e) {
			LogUtil.traceError(log, e, "处理文件时出错:" + filePath);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}

		return index;
	}

	/**
	 * 读取文件
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream is = new FileInputStream(new File(filePath));
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();

		return out.toString("UTF-8");
	}

	public static String readFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		String ret = readFile(is);
		is.close();
		return ret;
	}

	public static String readFile(InputStream is) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		byte[] lens = out.toByteArray();
		String result = new String(lens);
		return result;
	}

	/**
	 * 从classpath读取文件
	 * 
	 * @param thisObj
	 *            调用本方法的当前对象，需要从当前对象中获得ClassLoader，所以必须将对象传进来
	 * @param className
	 * @return
	 */
	public static String readFileFromClassPath(Object thisObj, String className) {
		InputStream is = thisObj.getClass().getClassLoader().getResourceAsStream(className);
		if (is != null) {
			try {
				return readFile(is);
			} catch (IOException e) {
				log.error("无法从 classpath中读取资源", e);
			}
		}
		return null;
	}

	/**
	 * 写文件
	 * 
	 * @param path
	 *            文件名和路径
	 * @param content
	 *            要写的内容
	 * @param override
	 *            是否覆盖原文件
	 * @throws IOException
	 */
	public static void write(String path, String content, boolean override) throws IOException {
		if (path == null || content == null) {
			return;
		}

		// 构建文件全路径
		Path filePath = FileSystems.getDefault().getPath(path);

		// 如果目录不存在，就创建目录
		File parent = filePath.toFile().getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		File file = filePath.toFile();
		if (file.exists() && !override) {
			// 如果文件存在，并且不允许覆盖，就退出了事
			return;
		}

		if (file.exists()) {
			// 如果文件存在，就删除文件
			file.delete();
		}

		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		out.write(content.getBytes("UTF-8"));
		out.close();
	}

	/**
	 * 写文件
	 * 
	 * @throws IOException
	 */
	public static void write(String path, String content) throws IOException {
		write(path, content, true);
	}

	private static final SimpleDateFormat FORMAT_FOR_SAVE_FILE = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 将上传的文件流，写到一个文件中
	 * 
	 * @param multipartFile
	 *            上传文件对象
	 * @param saveFilePath
	 *            文件准备放置的路径
	 * @return
	 * @throws IOException
	 */
	public static Path save(MultipartFile multipartFile, String saveFilePath) throws IOException {
		if (multipartFile == null || multipartFile.isEmpty()) {
			// 如果上传文件的数据，就返回 null
			return null;
		}

		// 构建文件全路径
		Path filePath = FileSystems.getDefault().getPath(saveFilePath);

		// 必须使用全路径，否则下面的transferTo会报错
		File file = filePath.toAbsolutePath().toFile();

		// 如果目录不存在，就创建目录
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		// 将上传文件流写入文件
		// file.createNewFile();
		multipartFile.transferTo(file);

		return filePath;
	}

	/**
	 * 生成随机文件名。例如 myroot/20160831/2323456574678.jpg
	 * 
	 * @param multipartFile
	 *            上传文件对象
	 * @param rootPath
	 *            文件准备放置的根路径，保存时会在后面拼接时间。可以是相对路径
	 * @param defaultExtName
	 *            默认的扩展名，如果原始文件没有扩展名，就用这个
	 * @return
	 */
	public static String getRandomFileName(MultipartFile multipartFile, String rootPath, String defaultExtName) {

		Assert.notNull(multipartFile, "multipartFile不能为空");
		Assert.isTrue(!multipartFile.isEmpty(), "multipartFile中必须有文件数据");
		Assert.notNull(rootPath, "rootPath不能为空");

		// 原始文件名
		String originalFilename = multipartFile.getOriginalFilename();

		// 获取文件扩展名
		String extName = defaultExtName;
		int index = originalFilename.lastIndexOf(".");
		if (index > 0) {
			// 如果能找到“.” 就用.后面的作为文件扩展名
			extName = originalFilename.substring(index + 1);
		}
		if (extName == null) {
			// 防止扩展名为 null
			extName = "";
		}

		// 格式例子 myroot/20160831/2323456574678.jpg
		String randomFileName = String.format("%s/%s/%d.%s",
				rootPath, FORMAT_FOR_SAVE_FILE.format(new Date()),
				System.nanoTime(),
				extName);

		return randomFileName;
	}

	/**
	 * 从classpath中，将文件读入到byte[]
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] loadFileFromClassPath(String fileName) throws IOException {
		// InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
		InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
		if (is != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			byte[] buf = new byte[4096];
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			byte[] bytes = out.toByteArray();
			return bytes;
		} else {
			return null;
		}
	}

}
