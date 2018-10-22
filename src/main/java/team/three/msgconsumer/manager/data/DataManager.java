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
	private static String controlCacheName = "ctlCache";
	private static String dataCacheName = "dataCache";
	
	private DataManager() { }
	
	private EmbeddedCacheManager mgr;
	private Cache<String, String> controlCache;
	private Cache<String, Object> dataCache;
	
	public void init() {
		ConfigManager cm = ConfigManager.get();
		GlobalConfiguration gc = new GlobalConfigurationBuilder()
				.transport().defaultTransport()
				.clusterName(cm.getClusterName())
				.addProperty("configurationFile", "jgroups-tcp.xml")
				.machineId(cm.getMachineId())
				.rackId(cm.getRackId())
				.globalJmxStatistics()
				.build()
				;
		Configuration c = new ConfigurationBuilder()
				.clustering()
				.cacheMode(CacheMode.REPL_SYNC).sync()
				.build()
				;
		mgr = new DefaultCacheManager(gc, c);
		mgr.addListener(new ClusterListener());
		controlCache = mgr.getCache(controlCacheName);
		dataCache = mgr.getCache(dataCacheName);
	}
	
	public Cache<String, String> getCtlCache(){
		return controlCache;
	}
	
	public Cache<String, Object> getDataCache(){
		return dataCache;
	}
}
