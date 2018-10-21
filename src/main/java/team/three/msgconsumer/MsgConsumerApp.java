package team.three.msgconsumer;

import team.three.msgconsumer.manager.config.ConfigManager;

/**
 * Hello world!
 *
 */
public class MsgConsumerApp 
{
    public static void main( String[] args )
    {
        ConfigManager cm = ConfigManager.get();

        System.out.println("Done~~~");
    }
}
