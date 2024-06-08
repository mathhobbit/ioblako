package org.ioblako.util;

import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.io.IOException;
import java.io.File;

public class readManifest{

public static void main(String[] argv) throws IOException{
    if(argv.length != 1){
       System.out.println("readManifest needs one argument, path to a jar file.");
       System.exit(1);
     }
   File fl = new File(argv[0]);

    if(!fl.isFile()){
       System.out.println("readManifest needs one argument, path to a jar file. "+
       fl.getPath()+"is not a path to a jar file.");
       System.exit(1);
    }
        JarFile jarFile = new JarFile(fl);
         // Get the manifest
        Manifest manifest = jarFile.getManifest();
         // Close the JAR file
        jarFile.close();
         // Get the manifest main attributes
        Attributes attribs = manifest.getMainAttributes();
         // Get attribute value
        System.out.println(attribs.getValue("Implementation-Version"));
}


}
