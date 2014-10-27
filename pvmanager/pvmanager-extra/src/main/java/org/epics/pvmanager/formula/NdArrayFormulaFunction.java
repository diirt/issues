/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.List;
import org.epics.pvmanager.util.NullUtils;
import org.diirt.vtype.ArrayDimensionDisplay;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.table.VTableFactory;

/**
 *
 * @author carcassi
 */
class NdArrayFormulaFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return true;
    }

    @Override
    public String getName() {
        return "ndArray";
    }

    @Override
    public String getDescription() {
        return "Creates an nd array with the given data and dimension display information";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>>asList(VNumberArray.class, ArrayDimensionDisplay.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("data", "dimDisplay");
    }

    @Override
    public Class<?> getReturnType() {
        return VNumberArray.class;
    }

    @Override
    public Object calculate(final List<Object> args) {
        if (NullUtils.containsNull(args)) {
            return null;
        }
        
        VNumberArray data = (VNumberArray) args.get(0);
        ArrayDimensionDisplay[] displays = new ArrayDimensionDisplay[args.size() - 1];
        for (int i = 0; i < displays.length; i++) {
            displays[i] = (ArrayDimensionDisplay) args.get(i + 1);
        }
        
        return ValueFactory.ndArray(data, displays);
    }
    
}
