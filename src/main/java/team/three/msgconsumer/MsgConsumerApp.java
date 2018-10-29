package team.three.msgconsumer;

import team.three.msgconsumer.business.Reporter;
import team.three.msgconsumer.manager.arch.ArchSelector;
import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.manager.data.DataManager;
import team.three.msgconsumer.manager.status.StatusManager;

/**
 * Message Consumer
 */
public class MsgConsumerApp 
{
    public static void main( String[] args ) throws Exception
    {
        ConfigManager cm = ConfigManager.get();
        cm.setConfig(args);
        System.out.println(cm.toString());
        DataManager dm = DataManager.get();
        dm.init();
        System.out.println("------> isMaster : " + StatusManager.get().isMaster() 
        		+ "    priority:" + StatusManager.get().getPriority());
        Reporter report = new Reporter();
        report.start();
        ArchSelector.get().build();
    }
}
