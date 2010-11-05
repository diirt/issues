/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.replay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author carcassi
 */
class Parser {

    static XmlValues parse(URI url) {
        // If relative, resolve it in the current directory
        if (!url.isAbsolute()) {
            File current = new File(".");
            url = current.toURI().resolve(url);
        }
        
        InputStream in = null;
        try {
            JAXBContext jaxbCtx = JAXBContext.newInstance(XmlValues.class);
            Unmarshaller reader = jaxbCtx.createUnmarshaller();
            return (XmlValues) reader.unmarshal(url.toURL());
        } catch (Exception ex) {
            throw new RuntimeException("Can't parse file", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    //
                }
            }
        }
    }

}
