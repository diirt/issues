/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.epics.pvmanager.WriteBuffer;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.loc.ChannelHandler;

/**
 *
 * @author carcassi
 */
class WritePlanner {
    
    private Map<String, ChannelHandler> channels = new HashMap<String, ChannelHandler>();
    private Map<String, Object> values = new HashMap<String, Object>();
    private Map<String, Set<String>> preceding = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> succeeding = new HashMap<String, Set<String>>();
    private Set<String> leafs = new HashSet<String>();
    
    void addChannel(ChannelHandler channel, Object value, Collection<String> precedingChannels) {
        channels.put(channel.getChannelName(), channel);
        values.put(channel.getChannelName(), value);
        preceding.put(channel.getChannelName(), new HashSet<String>(precedingChannels));
        for (String precedingChannel : precedingChannels) {
            Set<String> succeedingChannels = succeeding.get(channel.getChannelName());
            if (succeedingChannels ==  null) {
                succeedingChannels = new HashSet<String>();
                succeeding.put(precedingChannel, succeedingChannels);
            }
            succeedingChannels.add(channel.getChannelName());
        }
        if (precedingChannels.isEmpty()) {
            leafs.add(channel.getChannelName());
        }
    }
    
    void removeChannel(String channelName) {
        
    }

    Map<ChannelHandler, Object> nextChannels() {
        Map<ChannelHandler, Object> nextChannels = new HashMap<ChannelHandler, Object>();
        for (String channelName : leafs) {
            nextChannels.put(channels.get(channelName), values.get(channelName));
        }
        leafs.clear();
        return nextChannels;
    }
    
}
