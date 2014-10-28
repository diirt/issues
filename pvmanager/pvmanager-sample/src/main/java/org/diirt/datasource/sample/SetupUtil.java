/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import org.diirt.datasource.CompositeDataSource;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.ca.JCADataSourceBuilder;
import org.diirt.datasource.file.FileDataSource;
import org.diirt.datasource.loc.LocalDataSource;
import org.diirt.datasource.formula.ArrayFunctionSet;
import org.diirt.datasource.formula.FormulaRegistry;
import org.diirt.datasource.formula.MathFunctionSet;
import org.diirt.datasource.formula.NumberOperatorFunctionSet;
import org.diirt.datasource.formula.StringFunctionSet;
import org.diirt.datasource.formula.TableFunctionSet;
import org.diirt.datasource.sim.SimulationDataSource;
import org.diirt.datasource.util.Executors;
import org.diirt.datasource.sys.SystemDataSource;

/**
 *
 * @author carcassi
 */
public class SetupUtil {
    public static void defaultCASetup() {
        CompositeDataSource dataSource = new CompositeDataSource();
        dataSource.putDataSource("sim", SimulationDataSource.simulatedData());
        System.setProperty("com.cosylab.epics.caj.CAJContext.max_array_bytes", "10000000");
        dataSource.putDataSource("ca", new JCADataSourceBuilder().build());
        dataSource.putDataSource("loc", new LocalDataSource());
        dataSource.putDataSource("sys", new SystemDataSource());
        dataSource.putDataSource("file", new FileDataSource());
        dataSource.setDefaultDataSource("ca");
        PVManager.setDefaultDataSource(dataSource);
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new ArrayFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new MathFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new NumberOperatorFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new TableFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new StringFunctionSet());
    }
    public static void defaultCASetupForSwing() {
        PVManager.setDefaultNotificationExecutor(Executors.swingEDT());
        defaultCASetup();
    }
}