package com.sopin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 
 * 项目名称：ChatSoftware 类名称：IOUtil 类描述：流操作相关关闭类 创建人：Infinite Justice 创建时间：Nov 30,
 * 2011 12:02:54 AM 修改人：Infinite Justice 修改时间：Nov 30, 2011 12:02:54 AM 修改备注：
 * 
 * @version
 * 
 */
public class IOUtil {
	/** 关闭字节输入流 */
	public static void close(InputStream is) {
		if (null != is) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 关闭字节输出流 */
	public static void close(OutputStream os) {
		if (null != os) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 关闭字符输出流 */
	public static void close(OutputStreamWriter osw) {
		try {
			if (null != osw) {
				osw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			osw = null;
		}
	}

	/** 关闭字符输出缓冲流 */
	public static void close(BufferedWriter bw) {
		try {
			if (null != bw) {
				bw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bw = null;
		}
	}

	public static void close(PrintWriter pw) {
		try {
			if (null != pw) {
				pw.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pw = null;
		}
	}

	public static void close(BufferedReader bw) {
		try {
			if (null != bw) {
				bw.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bw = null;
		}
	}

	/** 关闭字节输入流和输出流 */
	public static void close(InputStream is, OutputStream os) {
		close(is);
		close(os);
	}

	/** 关闭字符输出流和缓冲流 */
	public static void close(OutputStreamWriter osw, BufferedWriter bw) {
		close(bw);
		close(osw);
	}
}
