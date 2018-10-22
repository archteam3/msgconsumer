package team.three.msgconsumer.manager.data;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

import team.three.msgconsumer.manager.status.StatusManager;

@Listener
public class ClusterListener {
	@ViewChanged
	public void viewChanged(ViewChangedEvent ev) {

		System.out.println("#### NEW : " + ev.getNewMembers());
		System.out.println("  ## OLD : " + ev.getOldMembers());
		
		if( ev.getNewMembers().size() == 1 ) {
			System.out.println("##=> make Master");
			StatusManager.get().setMaster();
		}
	}
}
