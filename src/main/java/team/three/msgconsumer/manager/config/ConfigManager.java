package team.three.msgconsumer.manager.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
	private static ConfigManager instance = null;
	public static ConfigManager get() {
		if( instance == null ) {
			synchronized (ConfigManager.class) {
				if( instance == null )
					instance = new ConfigManager();
			}
		}
		return instance;
	}
	private ConfigManager() {
		brokerType = null;
		archType = null;
		eqpSIdx = 0;
		eqpEIdx = 0;
		eqpCntPerCnt = 1;
		configFile = "./msgconsumer.conf";
		indiv = new HashMap<String, String>();
	}
	
	public static final String usage = 
			"usage:\n" +
			"------------------------------------------------\n" +
			"  -cf : config file default ./msgconsumer.conf\n"
			;
	
	
	private BrokerType brokerType;
	private String archType;
	private int eqpSIdx;
	private int eqpEIdx;
	private int eqpCntPerCnt;
	private String configFile;
	private Map<String, String> indiv;
	
	public void setConfig(String[] args) throws Exception {
		int i=0;
		if(args.length > 1 ) {
			if( args[0].equals("-cf") ) {
				configFile = args[1];
			}
		}
		
    	try( BufferedReader br = new BufferedReader( new FileReader(configFile))) {
    		String line;
    		String[] aTmp = null;
    		Map<String, String> tmp = null;
    		
    		Map<String, Map<String, String>> map = new HashMap<>();
    		Map<String, String> glb = new HashMap<>();
    		while( (line = br.readLine()) != null ) {
    			if( line.startsWith("[") ) {
    				line = line.split("#")[0].trim();
    				if( "[GLOBAL]".equals(line) ) {
    					tmp = glb;
    				} else {
    					tmp = new HashMap<>();
    					map.put(line, tmp);
    				}
    			} else if ( line != null && line.length() > 0 ) {
    				if( tmp == null )
    					continue;
    				line = line.split("#")[0].trim();
    				aTmp = line.split("=");
    				if( aTmp.length >= 2 ) {
    					tmp.put(aTmp[0].trim(), aTmp[1].trim());
    				}
    			}
    		}
    		
    		for( String key : glb.keySet() ) {
    			if( key.equals(ConfigConst.BROKER_TYPE)) {
    				brokerType = BrokerType.valueOf(glb.get(key));
    			} else if( key.equals(ConfigConst.EQP_ST_IDX)) {
    				eqpSIdx = Integer.parseInt(glb.get(key));
    			} else if( key.equals(ConfigConst.EQP_ED_IDX)) {
    				eqpEIdx = Integer.parseInt(glb.get(key));
    			} else if( key.equals(ConfigConst.EQP_PER_THREAD)) {
    				eqpCntPerCnt = Integer.parseInt(glb.get(key));
    			} else if( key.equals(ConfigConst.ARCH_TYPE)) {
    				archType = glb.get(key);
    			}
    		}
    		
    		for( String key : map.keySet() ) {
    			if( key.equals("[" + brokerType.getUpperString() + "]")){
    				this.indiv = map.get(key);
    			}
    		}
    	}
	}
	public BrokerType getBrokerType() {
		return brokerType;
	}
	public String getArchType() {
		return archType;
	}
	public int getEqpSIdx() {
		return eqpSIdx;
	}
	public int getEqpEIdx() {
		return eqpEIdx;
	}
	public int getEqpCntPerCnt() {
		return eqpCntPerCnt;
	}
	public Map<String, String> getIndiv() {
		return indiv;
	}
	@Override
	public String toString() {
		return "ConfigManager [brokerType=" + brokerType + ", archType=" + archType + ", eqpSIdx=" + eqpSIdx
				+ ", eqpEIdx=" + eqpEIdx + ", eqpCntPerCnt=" + eqpCntPerCnt + ", configFile=" + configFile + ", indiv="
				+ indiv + "]";
	}
}
