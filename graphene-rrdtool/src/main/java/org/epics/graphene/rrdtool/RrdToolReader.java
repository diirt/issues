/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene.rrdtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class RrdToolReader {
    
    public TimeSeries readFile(String filename, String cf, Timestamp start, Timestamp end) {
        List<String> args = new ArrayList<>();
        args.add("rrdtool");
        args.add("fetch");
        args.add(filename);
        args.add(cf);
        if (start != null) {
            args.add("-s");
            args.add(Long.toString(start.getSec()));
        }
        if (end != null) {
            args.add("-e");
            args.add(Long.toString(end.getSec()));
        }
        InputStream input = null;
        Process process = null;
        try {
            process = new ProcessBuilder(args.toArray(new String[args.size()])).start();
            input = process.getInputStream();
            RrdToolOutputParser parser = new RrdToolOutputParser();
            TimeSeries data = parser.parse(new BufferedReader(new InputStreamReader(input)));
            return data;
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't read RRD data", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(RrdToolReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (process != null) {
                process.destroy();
            }
        }
    }
}
