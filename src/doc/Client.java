package doc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private final String charsetName="UTF-8";
	private InputStream is;
	private OutputStream os;
	BufferedReader reader = null;
	PrintWriter writer=null;
	
	public Client() { 
		Socket sc = null;
		try {
			sc = new Socket("localhost", 8000);
			System.out.println("come to server..");
			is = sc.getInputStream();
			os=sc.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(is,charsetName));
			writer=new PrintWriter(new OutputStreamWriter(os,charsetName));
			new Thread(){
				public void run(){
					try {
						String str = reader.readLine();
						System.out.println("server 传来消息" + str);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			new Thread(){
				public void run(){
					try {
						byte bt[] = new byte[1024];
						while (true) {
							System.in.read(bt);
							String msg = new String(bt, charsetName);
							if("quit".equals(msg)){
								break;
							}
							writer.write(msg);
							System.out.println("读取控制台--->"+msg);
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
