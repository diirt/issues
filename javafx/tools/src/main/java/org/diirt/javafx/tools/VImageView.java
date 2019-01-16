/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.epics.util.array.ListNumber;
import org.epics.vtype.VImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

public final class VImageView extends BorderPane {

    private ImageView imageView;

    public VImageView() {
        imageView = new ImageView();
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());
        setCenter(imageView);
        setMinSize( 0 , 0 );
    }

    public void setVImage(VImage image) {
        if ( image != null ) {

            //we must create a new WritableImage every time. If the WritableImage
            //is larger than the image we want to display, then SwingFXUtils.toFXImage()
            //simply draws our smaller image onto the larger WritableImage.
            //This is bad because then we have a random gray gap on the right side.
            //Thus, we fix this by creating a new WritableImage of the correct
            //size every time and having SwingFXUtils.toFXImage() draw on that.
            WritableImage newImage = new WritableImage( image.getWidth() , image.getHeight() );
            
            BufferedImage vImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            ListNumber data = image.getData();
            for (int i = 0; i < data.size(); i++) {
                ((DataBufferByte) vImage.getRaster().getDataBuffer()).getData()[i] = data.getByte(i);
            }
            newImage = SwingFXUtils.toFXImage(vImage, newImage );
            imageView.setImage(newImage);
        }
        else {
            imageView.setImage( null );
        }
    }

}
