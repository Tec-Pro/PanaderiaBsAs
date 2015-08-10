/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import javax.swing.filechooser.*;

/**
 *
 * @author xen
 */

/* ImageFilter.java is used by FileChooserDemo2.java. */
public class ImageFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = ImageExtensions.getExtension(f);
        if (extension != null) {
            return extension.equals(ImageExtensions.jpeg)
                    || extension.equals(ImageExtensions.jpg)
                    || extension.equals(ImageExtensions.gif)
                    || extension.equals(ImageExtensions.png);
        }

        return false;
    }

    //The description of this filter
    @Override
    public String getDescription() {
        return "*.jpeg, *.jpg, *.png, *.gif";
    }

}
