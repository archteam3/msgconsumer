package team.three.msgconsumer.manager.config;

public class Imsi00 {
/*
*** jgroups-tcp.xml
<!--
  ~ JBoss, Home of Professional Open Source
  ~ Copyright 2010 Red Hat Inc. and/or its affiliates and other
  ~ contributors as indicated by the @author tags. All rights reserved.
  ~ See the copyright.txt in the distribution for a full listing of
  ~ individual contributors.
  ~
  ~ This is free software; you can redistribute it and/or modify it
  ~ under the terms of the GNU Lesser General Public License as
  ~ published by the Free Software Foundation; either version 2.1 of
  ~ the License, or (at your option) any later version.
  ~
  ~ This software is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ Lesser General Public License for more details.
  ~
  ~ You should have received a copy of the GNU Lesser General Public
  ~ License along with this software; if not, write to the Free
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  -->
<config xmlns="urn:org:jgroups"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="urn:org:jgroups file:schema/JGroups-2.8.xsd">
   <TCP
        bind_addr="${jgroups.tcp.address:127.0.0.1}"
        bind_port="${jgroups.tcp.port:7800}"
        port_range="30"
        recv_buf_size="20m"
        send_buf_size="640k"
        max_bundle_size="64000"
        enable_diagnostics="false"
        bundler_type="old"

        thread_naming_pattern="pl"

        thread_pool.enabled="true"
        thread_pool.min_threads="2"
        thread_pool.max_threads="30"
        thread_pool.keep_alive_time="60000"
         />

   <!-- Ergonomics, new in JGroups 2.11, are disabled by default in TCPPING until JGRP-1253 is resolved -->
   <!--
   <TCPPING timeout="3000"
            initial_hosts="localhost[7800],localhost[7801]"
            port_range="5"
            num_initial_members="3"
            ergonomics="false"
        />
   -->

   <MPING bind_addr="${jgroups.bind_addr:127.0.0.1}" break_on_coord_rsp="true"
      mcast_addr="${jgroups.mping.mcast_addr:228.2.4.6}"
      mcast_port="${jgroups.mping.mcast_port:43366}"
      ip_ttl="${jgroups.udp.ip_ttl:2}"
      />

   <MERGE3 max_interval="30000" min_interval="10000"/>
   <FD_SOCK/>
   <FD timeout="3000" max_tries="3"/>
   <VERIFY_SUSPECT timeout="1500"/>
   <pbcast.NAKACK2
         use_mcast_xmit="false"
         discard_delivered_msgs="false"/>
   <UNICAST3/>
   <pbcast.STABLE stability_delay="500" desired_avg_gossip="5000" max_bytes="1m"/>
   <pbcast.GMS print_local_addr="false" join_timeout="3000" view_bundling="true"/>
   <UFC max_credits="200k" min_threshold="0.20"/>
   <MFC max_credits="200k" min_threshold="0.20"/>
   <FRAG2 frag_size="60000"/>
   <RSVP timeout="60000" resend_interval="500" ack_on_delivery="false" />
</config>


*** publish

		// TODO Auto-generated method stub
		GlobalConfiguration gc = new GlobalConfigurationBuilder()
				.transport()
				.defaultTransport()
				.clusterName("AP001")
				.addProperty("configurationFile", "jgroups-tcp.xml")
				.machineId("AP001-b")
				.rackId("rack-B")
				.globalJmxStatistics().enable()
				.build()
				;
		Configuration c = new ConfigurationBuilder()
				.clustering()
				.cacheMode(CacheMode.REPL_SYNC).sync()
				.build();
		String cacheName = "myCache";
		EmbeddedCacheManager mgr = new DefaultCacheManager(gc, c);
		mgr.addListener(new ViewListener());
		//mgr.defineConfiguration(cacheName, c);
		Cache<String, String> map = mgr.getCache(cacheName);
		
		int i = 1;
		while(true) {
			try {
				Thread.sleep(2000);
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
			map.put("EQP-001", Integer.toString(i));
			System.out.println("publish EQP-001 >> " + i);
			i++;
		}
		
		
*** consume
		GlobalConfiguration gc = new GlobalConfigurationBuilder()
				.transport()
				.defaultTransport()
				.clusterName("AP001")
				.addProperty("configurationFile", "jgroups-tcp.xml")
				.machineId("AP001-a")
				.rackId("rack-A")
				.globalJmxStatistics().enable()
				.build()
				;
		Configuration c = new ConfigurationBuilder()
				.clustering()
				.cacheMode(CacheMode.REPL_SYNC).sync()
				.build();
		String cacheName = "myCache";
		EmbeddedCacheManager mgr = new DefaultCacheManager(gc, c);
		mgr.addListener(new ViewListener());
		Cache<String, String> map = mgr.getCache(cacheName);
		
		String v = null;
		int i = 1;
		while(true) {
			try {
				Thread.sleep(2000);
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
			v = map.get("EQP-001");
			if( v != null ) {
				System.out.println("Consume EQP-001 >> " + v);
			}
		}
		
*** listener
package test;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

@Listener
public class ViewListener {
	@ViewChanged
	public void viewChanged(ViewChangedEvent ev) {
		System.out.println("#### NEW : " + ev.getNewMembers());
		System.out.println("  ## OLD : " + ev.getOldMembers());
	}
}

 */
}
