package team.three.msgconsumer.manager.arch;

import java.util.ArrayList;
import java.util.List;

import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.props.IdMaker;

public class ArchitectureOne implements IArchitecture {
	private IMsgBroker msgBroker;
	private List<Thread> thdLst;

	@Override
	public void build(IMsgBroker broker) throws Exception {
		// TODO Auto-generated method stub
		thdLst = new ArrayList<>();
		this.msgBroker = broker;
		msgBroker.connect();
		
		ConfigManager cm = ConfigManager.get();
		int tmp = cm.getEqpEIdx() - cm.getEqpSIdx() + 1;
		int threadCnt = tmp / cm.getEqpCntPerCnt();
		if( tmp % cm.getEqpCntPerCnt() != 0 )
			threadCnt++;
		
		int stdIdx = cm.getEqpSIdx();
		int endIdx;
		
		for( int i=0; i<threadCnt; i++) {
			tmp = stdIdx + cm.getEqpCntPerCnt() - 1;
			if( tmp > cm.getEqpEIdx() ) {
				endIdx = cm.getEqpEIdx();
			} else {
				endIdx = tmp;
			}
			
			List<String> eqpList = new ArrayList<>();
			for( int j=stdIdx; j<=endIdx; j++) {
				eqpList.add(IdMaker.makeEqpId(j));
			}
			
			thdLst.add(msgBroker.createArchOneThread(eqpList));
			stdIdx = endIdx + 1;
		}
		
		for(Thread t : thdLst) {
			t.start();
		}

		for(Thread t : thdLst) {
			t.join();
		}
	}

	@Override
	public void destruct() {
		// TODO Auto-generated method stub
		
	}

}
