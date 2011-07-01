/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.Map;
import java.util.Collections;
import org.epics.pvmanager.loc.ChannelHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class WritePlannerTest {
    
    public WritePlannerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Mock ChannelHandler channel1;
    @Mock ChannelHandler channel2;
    @Mock ChannelHandler channel3;
    @Mock ChannelHandler channel4;

    @Test
    public void noDependencies() {
        when(channel1.getChannelName()).thenReturn("channel1");
        when(channel2.getChannelName()).thenReturn("channel2");
        when(channel3.getChannelName()).thenReturn("channel3");
        
        WritePlanner planner = new WritePlanner();
        planner.addChannel(channel1, 6.28, Collections.<String>emptySet());
        planner.addChannel(channel2, 3.14, Collections.<String>emptySet());
        planner.addChannel(channel3, 1.57, Collections.<String>emptySet());
        
        Map<ChannelHandler, Object> nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(3));
        assertThat(nextChannels.get(channel1), equalTo((Object) 6.28));
        assertThat(nextChannels.get(channel2), equalTo((Object) 3.14));
        assertThat(nextChannels.get(channel3), equalTo((Object) 1.57));
        
        nextChannels = planner.nextChannels();
        assertThat(nextChannels.size(), equalTo(0));
    }
}
