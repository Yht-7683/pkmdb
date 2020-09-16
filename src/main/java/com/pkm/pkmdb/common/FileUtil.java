package com.pkm.pkmdb.common;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public final static String CHAR_SET_UTF8 = "UTF-8";
	public final static String CHAR_SET_GBK = "GBK";
	public final static int NEW_LINE_CHAR_LENGTH = System.getProperty(
			"line.separator").getBytes().length;
	private static final String FILE_PATH = "deploy/data/www/static/upload/";
	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param fileName
	 *            待删除的文件名
	 * @return 文件删除成功返回true,否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile()) {

				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除临时文件;
	 *
	 * @param fileName
	 */
	public static void destroyFile(String fileName) {
		String filePath = "/"+FILE_PATH+fileName;
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// 删除子文件
				if (files[i].isFile()) {
					flag = deleteFile(files[i].getAbsolutePath());
					if (!flag) {
						break;
					}
				}
				// 删除子目录
				else {
					flag = deleteDirectory(files[i].getAbsolutePath());
					if (!flag) {
						break;
					}
				}
			}
		}

		if (!flag) {
			return false;
		}

		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean deleteDirFiles(String dir) {
		boolean flag = true;
		File dirFile = new File(dir);
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// 删除子文件
				if (files[i].isFile()) {
					flag = deleteFile(files[i].getAbsolutePath());
					if (!flag) {
						break;
					}
				}
				// 删除子目录
				else {
					flag = deleteDirectory(files[i].getAbsolutePath());
					if (!flag) {
						break;
					}
				}
			}
		}
		return flag;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 
	 * @param fileName
	 *            文件路径+文件名
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void write(String fileName, String charSet, String content) {
		write(fileName, content, charSet, false);
	}

	/**
	 * 追写文件
	 * 
	 * @param fileName
	 * @param content
	 * @param append
	 * @throws Exception
	 */
	public static void write(String fileName, String content, String charSet,
			boolean append) {
		try {
			File file = new File(fileName);
			File fileParent = file.getParentFile();
			if (!fileParent.exists()) {
				boolean exist = fileParent.mkdirs();
				if (!exist) {
					throw new RuntimeException("生成文件路径"
							+ fileParent.getAbsolutePath() + "失败!");
				}
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, append), charSet));
			try {
				bw.write(content);
			} finally {
				bw.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("写入文件失败", e);
		}

	}

	/**
	 * 获取文件的总行数,文件需要每一行均含有固定长度
	 * 
	 * @param fileName
	 * @param lineByteSize
	 *            单行的固定字节数 ，小于1表示无固定长度
	 * @return
	 * @throws Exception
	 */
	public static long getFileRowCount(String fileName, String charSet,
			long lineByteSize, int newLineCharLength) {
		try {
			long rowCount = 0;
			if (lineByteSize > 0) {
				long len = new File(fileName).length();
				rowCount = (len + newLineCharLength)
						/ (lineByteSize + newLineCharLength);// 加上换行符字节数
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(fileName), charSet));
				try {
					while (br.readLine() != null) {
						rowCount++;
					}
				} finally {
					br.close();
				}
			}
			return rowCount;

		} catch (Exception e) {
			throw new RuntimeException("读取文件失败", e);
		}

	}

	/**
	 * 读取固定行长度的文件，每行记录存放到List中返回
	 * 
	 * @param fileName
	 *            文件名
	 * @param startLineIndex
	 *            起始行,从0开始
	 * @param lineByteSize
	 *            每行固定的字节数 ,小于等于0表示无固定长度
	 * @param maxLine
	 *            读取的最大行数,List.size的最大值
	 * @return
	 * @throws Exception
	 */
	public static List<String> readFileToList(String fileName, String charSet,
			long startLineIndex, long lineByteSize, long maxLine,
			int newLineCharLength) {
		try {
			List<String> list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charSet));
			String buf = null;
			long lineNum = 0;
			int lineIndex = 0;
			try {
				if (lineByteSize > 0) {
					br.skip(startLineIndex * (lineByteSize + newLineCharLength));
				} else {
					while (lineIndex < startLineIndex) {
						br.readLine();
						lineIndex++;
					}
				}
				while (++lineNum <= maxLine && (buf = br.readLine()) != null) {
					list.add(buf);
				}
			} finally {
				br.close();
			}
			return list;

		} catch (Exception e) {
			throw new RuntimeException("读取文件失败", e);
		}

	}

	/**
	 * 解压ZIP，返回解压后的所有文件名（绝对路径）列表
	 */
	public static List<String> unZip(String path) throws IOException {
		int count = -1;
		int index = -1;
		int buffer = 256;

		File src = new File(path);
		String savepath = src.getAbsolutePath();
		savepath = savepath.substring(0, savepath.lastIndexOf("."))
				+ File.separatorChar;
		new File(savepath).mkdir();

		List<String> unZipFiles = new ArrayList<String>();
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(path)));

			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				byte data[] = new byte[buffer];

				String temp = entry.getName();

				index = temp.lastIndexOf("/");
				if (index != -1)
					temp = temp.substring(index + 1);
				temp = savepath + temp;

				File f = new File(temp);
				f.createNewFile();

				BufferedOutputStream bos = null;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(f),
							buffer);
					while ((count = zis.read(data, 0, buffer)) != -1) {
						bos.write(data, 0, count);
					}
					bos.flush();
				} finally {
					if (bos != null)
						bos.close();
				}
				unZipFiles.add(temp);
			}
		} catch (Exception e) {
			throw new RuntimeException("解压文件失败", e);
		} finally {
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					//logger.error("", e);
				}
			}
		}

		return unZipFiles;

	}
	
	 /** 
     * @param srcFile：压缩文件路径 
     * @param dest：解压到的目录 
     * @param deleteFile：解压完成后是否删除文件 
     * @throws Exception 
     */  
    public static void unZip(String srcFile,String dest,boolean deleteFile)  throws Exception {  
        File file = new File(srcFile);  
        if(!file.exists()) {  
            throw new Exception("解压文件不存在!");  
        }  
        ZipFile zipFile = new ZipFile(file);  
        Enumeration e = zipFile.entries();  
        while(e.hasMoreElements()) {  
            ZipEntry zipEntry = (ZipEntry)e.nextElement();  
            if(zipEntry.isDirectory()) {  
                String name = zipEntry.getName();  
                name = name.substring(0,name.length()-1);  
                File f = new File(dest + name);  
                f.mkdirs();  
            } else {  
                File f = new File(dest + zipEntry.getName());  
                f.getParentFile().mkdirs();  
                f.createNewFile();  
                InputStream is = zipFile.getInputStream(zipEntry);  
                FileOutputStream fos = new FileOutputStream(f);  
                int length = 0;  
                byte[] b = new byte[1024];  
                while((length=is.read(b, 0, 1024))!=-1) {  
                    fos.write(b, 0, length);  
                }  
                is.close();  
                fos.close();  
            }  
        }  
          
        if (zipFile != null) {  
            zipFile.close();  
        }  
          
        if(deleteFile) {  
            file.deleteOnExit();  
        }  
    }
    
    /**
     * 解压zip格式的压缩包
     * 
     * @param filePath
     *            压缩文件路径
     * @param outPath
     *            输出路径
     * @return 解压成功或失败标志
     */
    public static Boolean unZip(String inPath, String outPath) {
    	  String unzipfile = inPath; // 解压缩的文件名
    	  try {
    	   ZipInputStream zin = new ZipInputStream(new FileInputStream(
    	     unzipfile),Charset.forName("GBK"));
    	   ZipEntry entry;
    	   // 创建文件夹
    	   while ((entry = zin.getNextEntry()) != null) {
    	    if (entry.isDirectory()) {
    	     File directory = new File(outPath, entry.getName());
    	     if (!directory.exists()) {
    	      if (!directory.mkdirs()) {
    	       System.exit(0);
    	      }
    	     }
    	     zin.closeEntry();
    	    } else {
    	     File myFile = new File(entry.getName());
    	     FileOutputStream fout = new FileOutputStream(outPath
    	       + myFile.getPath());
    	     DataOutputStream dout = new DataOutputStream(fout);
    	     byte[] b = new byte[1024];
    	     int len = 0;
    	     while ((len = zin.read(b)) != -1) {
    	      dout.write(b, 0, len);
    	     }
    	     dout.close();
    	     fout.close();
    	     zin.closeEntry();
    	    }
    	   }
    	   return true;
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	   return false;
    	  }
    	 }

	/**
	 * 读取固定行长度的文件，每行记录存放到List中返回
	 * 
	 * @param fileName
	 *            文件名
	 * @param startLineIndex
	 *            起始行,从0开始
	 * @param lineByteSize
	 *            每行固定的字节数 ,小于等于0表示无固定长度
	 * @param maxLine
	 *            读取的最大行数,List.size的最大值
	 * @return
	 * @throws Exception
	 */
	public static List<String> readFileToList(String fileName, String charSet) {
		try {
			List<String> list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charSet));
			String buf = null;
			try {
				while ((buf = br.readLine()) != null) {
					list.add(buf);
				}
			} finally {
				br.close();
			}
			return list;

		} catch (Exception e) {
			throw new RuntimeException("读取文件失败", e);
		}

	}
	
	public static void main(String[] args) {
		unZip("C:\\Users\\Administrator\\Desktop\\dfg.zip","C:\\Users\\Administrator\\Desktop\\x\\");
		
		File f = new File("C:\\Users\\Administrator\\Desktop\\dfg.zip");
		System.out.println(f.getName());
	}

}
