/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.ad;

import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.displayNone;
import static org.diirt.vtype.ValueFactory.timeNow;

import java.util.Arrays;
import java.util.List;

import org.diirt.datasource.formula.FormulaFunction;
import org.diirt.datasource.util.NullUtils;
import org.diirt.util.array.ListNumbers;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.ValueFactory;
/**
 * 
 * @author kunal
 *
 */
public class ADDataTypeMappingFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "adDataEnum";
    }

    @Override
    public String getDescription() {
        return "Map the area detector data to the specified type,"
                + " i.e. [Int8, UInt8, Int16, UInt16, Int32, UInt32, Float32, Float64]";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.asList(VNumberArray.class, VEnum.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("data", "dataType");
    }

    @Override
    public Class<?> getReturnType() {
        return VNumberArray.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        if (NullUtils.containsNull(args)) {
            return null;
        }

        VNumberArray data = (VNumberArray) args.get(0);
        VEnum dataType = (VEnum) args.get(1);
        String type = dataType.getValue();
        switch (type) {
        case "UInt8":
            int[] newUInt8Data = new int[data.getData().size()];
            for (int i = 0; i < data.getData().size(); i++) {
                if (data.getData().getInt(i) < 0) {
                    newUInt8Data[i] = data.getData().getInt(i) + 256;
                } else {
                    newUInt8Data[i] = data.getData().getInt(i);
                }
            }
            return ValueFactory.newVNumberArray(ListNumbers.toListNumber(newUInt8Data), alarmNone(), timeNow(), displayNone());
        case "UInt16":
            int[] newUInt16Data = new int[data.getData().size()];
            for (int i = 0; i < data.getData().size(); i++) {
                if (data.getData().getInt(i) < 0) {
                    newUInt16Data[i] = data.getData().getInt(i) + ((int) Math.pow(2, 16));
                } else {
                    newUInt16Data[i] = data.getData().getInt(i);
                }
            }
            return ValueFactory.newVNumberArray(ListNumbers.toListNumber(newUInt16Data), alarmNone(), timeNow(), displayNone());
        case "UInt32":
            long[] newUInt32Data = new long[data.getData().size()];
            for (int i = 0; i < data.getData().size(); i++) {
                if (data.getData().getInt(i) < 0) {
                    newUInt32Data[i] = data.getData().getInt(i) + ((long) Math.pow(2, 32));
                } else {
                    newUInt32Data[i] = data.getData().getInt(i);
                }
            }
            return ValueFactory.newVNumberArray(ListNumbers.toListNumber(newUInt32Data), alarmNone(), timeNow(), displayNone());
        default:
            break;
        }
        return data;
    }

}
