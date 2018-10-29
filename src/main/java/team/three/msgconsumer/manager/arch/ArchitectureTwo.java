package team.three.msgconsumer.manager.arch;

import java.util.ArrayList;
import java.util.List;

import org.infinispan.Cache;

import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.manager.data.DataManager;
import team.three.msgconsumer.props.IdMaker;

public class ArchitectureTwo implements IArchitecture {
	private IMsgBroker msgBroker;
	private Thread msgDispatcher;

	@Override
	public void build(IMsgBroker broker) throws Exception {
		// TODO Auto-generated method stub
		this.msgBroker = broker;
		msgBroker.connect();
		
		// CtlCache initialize
		Cache<String, Integer> cache = DataManager.get().getEqpCache();
		ConfigManager cm = ConfigManager.get();
		List<String> eqpLst = new ArrayList<>();
		String eqpId = null;
		for( int i=cm.getEqpSIdx(); i<=cm.getEqpEIdx(); i++) {
			eqpId = IdMaker.makeEqpId(i);
			cache.put(eqpId, 0);
			eqpLst.add(eqpId);
		}
		
		TaskManager.get().init();
		msgDispatcher = broker.createArchTwoThread(eqpLst);
		msgDispatcher.start();
	}

	@Override
	public void destruct() {
		// TODO Auto-generated method stub
		msgDispatcher.interrupt();
		msgBroker.disconnect();
	}

}
