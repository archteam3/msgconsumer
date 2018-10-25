package team.three.msgconsumer.business;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.infinispan.Cache;

import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.manager.data.DataManager;
import team.three.msgconsumer.props.IdMaker;

public class Reporter extends Thread {
	private ServerSocket listener;
	private AtomicBoolean isRun;
	
	public Reporter() {
		super();
		isRun = new AtomicBoolean(true);
	}
	
	@Override
	public void run() {
		try {
			listener = new ServerSocket(ConfigManager.get().getReporterPort());
			while(true) {
				Socket socket = listener.accept();
				try {
					PrintWriter out = 
							new PrintWriter(socket.getOutputStream(), true);
					out.println(getResult());
				} finally {
					socket.close();
				}
			}
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stopThread() {
		isRun.set(false);
	}
	
	public String getResult() {
		Cache<String, Object> dc = DataManager.get().getDataCache();
		ConfigManager cm = ConfigManager.get();
		int totalCnt = 0;
		int abnormalCnt = 0;
		long elapsedTime = 0L;
		for( String key : dc.keySet() ) {
			BizItem itm = (BizItem)dc.get(key);
			totalCnt += itm.getTotalCnt();
			abnormalCnt += itm.getAbnormalCnt();
			elapsedTime += itm.getElapsedNano();
		}
		elapsedTime = elapsedTime / totalCnt;
		
		return "Equipment Cnt : " + (cm.getEqpEIdx() - cm.getEqpSIdx() + 1)
				+ " (" + IdMaker.makeEqpId(cm.getEqpSIdx()) + " ~ " + IdMaker.makeEqpId(cm.getEqpEIdx()) + "\n"
				+ " TotalMsgCnt : " + totalCnt + "  AbnormalCnt : " + abnormalCnt + "  ElapsedTime(ns) : " + elapsedTime 
				+ "\n"
				;
		
	} 
}
