/*
 * Copyright 2008-2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.Executor;
import org.epics.pvmanager.util.TimeDuration;

/**
 *
 * @author carcassi
 */
class CommonConfiguration {

    Executor notificationExecutor;
    DataSource source;
    TimeDuration timeout;

    /**
     * Defines which DataSource should be used to read the data.
     *
     * @param dataSource a connection manager
     * @return this
     */
    public CommonConfiguration from(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can't be null");
        }
        source = dataSource;
        return this;
    }

    /**
     * Defines on which thread the PVManager should notify the client.
     *
     * @param onThread the thread on which to notify
     * @return this
     */
    public CommonConfiguration notifyOn(Executor onThread) {
        if (this.notificationExecutor == null) {
            this.notificationExecutor = onThread;
        } else {
            throw new IllegalStateException("Already set what thread to notify");
        }
        return this;
    }
    
    public CommonConfiguration timeout(TimeDuration timeout) {
        if (this.timeout != null)
            throw new IllegalStateException("Timeout already set");
        this.timeout = timeout;
        return this;
    }

    void checkDataSourceAndThreadSwitch() {
        // Get defaults
        if (source == null) {
            source = PVManager.getDefaultDataSource();
        }
        if (notificationExecutor == null) {
            notificationExecutor = PVManager.getDefaultNotificationExecutor();
        }

        // Check that a data source has been specified
        if (source == null) {
            throw new IllegalStateException("You need to specify a source either "
                    + "using PVManager.setDefaultDataSource or by using "
                    + "read(...).from(dataSource).");
        }

        // Check that thread switch has been specified
        if (notificationExecutor == null) {
            throw new IllegalStateException("You need to specify a thread either "
                    + "using PVManager.setDefaultThreadSwitch or by using "
                    + "read(...).andNotify(threadSwitch).");
        }
    }
}
