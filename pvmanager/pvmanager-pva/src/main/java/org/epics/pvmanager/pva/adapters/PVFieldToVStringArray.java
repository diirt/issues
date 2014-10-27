/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.pva.adapters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.epics.pvdata.pv.StringArrayData;
import org.epics.pvdata.pv.PVStringArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTypeToString;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListInt;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVStringArray extends AlarmTimeDisplayExtractor implements VStringArray {

	private final ListInt size;
	private final List<String> array;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVStringArray(PVStructure pvField, String fieldName, boolean disconnected) {
		super(pvField, disconnected);
		
		PVStringArray valueField =
			(PVStringArray)pvField.getScalarArrayField(fieldName, ScalarType.pvString);
		if (valueField != null)
		{
			StringArrayData data = new StringArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.size = new ArrayInt(data.data.length);
			this.array = Collections.unmodifiableList(Arrays.asList(data.data));
		}
		else
		{
			this.size = null;
			this.array = null;
		}
	}

	public PVFieldToVStringArray(PVStructure pvField, boolean disconnected) {
		this(pvField, "value", disconnected);
	}
	
	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public ListInt getSizes() {
		return size;
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VStringArray#getArray()
	 */
	@Override
	public List<String> getData() {
		return array;
	}
    
    @Override
    public String toString() {
        return VTypeToString.toString(this);
    }

}
