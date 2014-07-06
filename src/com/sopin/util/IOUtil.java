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
 * ��Ŀ���ƣ�ChatSoftware �����ƣ�IOUtil ����������������عر��� �����ˣ�Infinite Justice ����ʱ�䣺Nov 30,
 * 2011 12:02:54 AM �޸��ˣ�Infinite Justice �޸�ʱ�䣺Nov 30, 2011 12:02:54 AM �޸ı�ע��
 * 
 * @version
 * 
 */
public class IOUtil {
	/** �ر��ֽ������� */
	public static void close(InputStream is) {
		if (null != is) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** �ر��ֽ������ */
	public static void close(OutputStream os) {
		if (null != os) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** �ر��ַ������ */
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

	/** �ر��ַ���������� */
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

	/** �ر��ֽ�������������� */
	public static void close(InputStream is, OutputStream os) {
		close(is);
		close(os);
	}

	/** �ر��ַ�������ͻ����� */
	public static void close(OutputStreamWriter osw, BufferedWriter bw) {
		close(bw);
		close(osw);
	}
}
