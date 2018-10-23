package team.three.msgconsumer.manager.data;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.manager.status.StatusManager;

public class DataManager {
	private static DataManager instance = null;
	public static DataManager get() {
		if( instance == null ) {
			synchronized (DataManager.class) {
				if( instance == null ) {
					instance = new DataManager();
				}
			}
		}
		return instance;
	}
	private static final String CONTROL_CACHE_NAME = "ctlCache";
	private static final String DATA_CACHE_NAME = "dataCache";
	private static final String MASTER_NODE = "master-node"; 
	private static final String MASTER_PRIORITY = "master-priority";
	
	private DataManager() { }
	
	private EmbeddedCacheManager mgr;
	private String machineId;
	private Cache<String, String> controlCache;
	private Cache<String, Object> dataCache;
	
	public void init() {
		ConfigManager cm = ConfigManager.get();
		machineId = cm.getMachineId();
		
		GlobalConfiguration gc = new GlobalConfigurationBuilder()
				.transport().defaultTransport()
				.clusterName(cm.getClusterName())
				.addProperty("configurationFile", "jgroups-tcp.xml")
				.machineId(cm.getMachineId())
				.rackId(cm.getRackId())
				.globalJmxStatistics().enable()
				.build()
				;
		Configuration c = new ConfigurationBuilder()
				.clustering()
				.cacheMode(CacheMode.REPL_SYNC).sync()
				.build()
				;
		mgr = new DefaultCacheManager(gc, c);
		mgr.addListener(new ClusterListener());
		
		//mgr.addListener(new Liste);
		
		controlCache = mgr.getCache(CONTROL_CACHE_NAME);
		dataCache = mgr.getCache(DATA_CACHE_NAME);
		
		String mst = getMasterNode();
		if( mst == null ) {
			controlCache.put(MASTER_NODE, machineId);
			controlCache.put(MASTER_PRIORITY, Integer.toString(Integer.MAX_VALUE));
			StatusManager.get().setPriority(Integer.MAX_VALUE - 1);
			StatusManager.get().setMaster();
		} else {
			StatusManager.get().setPriority(Integer.parseInt(controlCache.get(MASTER_PRIORITY)) - 1);
		}
		
	}
	private String getMasterNode() {
		return controlCache.get(MASTER_NODE);
	}
	
	public void setMaster() {
		if( !getMasterNode().equals(machineId) ) {
			controlCache.put(MASTER_NODE, machineId);
			controlCache.put(MASTER_PRIORITY, Integer.toString(StatusManager.get().getPriority()));
			StatusManager.get().setMaster();
		}
	}
	
	public Cache<String, String> getCtlCache(){
		return controlCache;
	}
	
	public Cache<String, Object> getDataCache(){
		return dataCache;
	}
}
