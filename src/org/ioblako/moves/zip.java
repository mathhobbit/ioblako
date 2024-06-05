/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: zip.java,v 1.4 2003/12/15 15:57:01 nikitis Exp $
*
*This program is free software; you can redistribute it and/or
*modify it under the terms of the GNU General Public License
*as published by the Free Software Foundation; either version 2
*of the License, or (at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program; if not, write to the Free Software
*Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package org.ioblako.moves;


import org.ioblako.core.move;
import org.ioblako.core.Log;
import org.ioblako.core.Time;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Date;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.text.DateFormat;


/**
 * <H5>DESCRIPTION:</H5>
 * It zips file "file" and puts it into "archive" directory attaching a date stamp to the file name.  
 *
 * <hr>
 * <H5> MANDATORY PARAMETERS:</H5>
 *  file, archive
 *
 * <hr>
 * <H5> OPTIONAL PARAMETERS:</H5>
 * none 
 *
 * <hr>
 * <H5>EXAMPLE:</H5>
 * <pre>
 * &lt;move name="zip" /&gt;
 *       &lt;parameter name="file" value="/usr/local/ioblako/projects/&project;/inproc/&project;_ack.dat" /&gt;
 *       &lt;parameter name="archive" value="/usr/local/ioblako/projects/&project;/archive" /&gt;
 * &lt;/move&gt;
 * </pre>
 *
 * <hr>
 * 
 */


public class zip extends move{


public void run(){



if(parameters != null)
     if(parameters.containsKey("file")&&parameters.containsKey("archive")){
          
          String srcFile  = parameters.get("file");
          String destFile = parameters.get("archive");
      File src= new File (srcFile);
      File dest= new File (destFile); 



        if(!src.exists()){
                Log.log("ARCHIVE ERROR",srcFile+" does not exist",(move)this);
                currentState.doneWithSuccess(false);
                   return;
         }//if(!src.exists()){

         if(src.isDirectory()){
                Log.log("ARCHIVE ERROR",srcFile+" is not a file",(move)this);
                currentState.doneWithSuccess(false);
                   return; 
            }  


         if(!dest.isDirectory()){
              Log.log("ARCHIVE ERROR",destFile+" should be a valid directory",(move)this);
                currentState.doneWithSuccess(false);
                   return;
         }     


 




                        Locale currentLocale = Locale.of(Time.language,Time.country);
                        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);     
                 
                    


            
              try{

             int i=0;

              String  archName=destFile+File.separator+src.getName()+
                                      "_"+((dateFormatter.format(new Date())).replace(' ','_')).replace('/','_')+"_"+i+".zip";

         File wArch = new File(archName);
                          

             while(wArch.exists()){
                  i++;
               archName=destFile+File.separator+src.getName()+
                                      "_"+((dateFormatter.format(new Date())).replace(' ','_')).replace('/','_')+"_"+i+".zip";
                   wArch = new File(archName);
               }
          

                    FileOutputStream archiveZip = new FileOutputStream(archName);
                    ZipOutputStream zout = new ZipOutputStream(archiveZip);
               
                       
                      ZipEntry ze=null;
                      PrintWriter os=null;

                      ze = new ZipEntry(src.getName());         
                      zout.putNextEntry(ze);

                  //BufferedReader in = new BufferedReader(new FileReader(srcFile));
                  FileInputStream in = new FileInputStream(src);

                          byte[] buf;
                             while(in.available()!=0){
                                   buf=new byte[in.available()];
                                   in.read(buf);
                                   zout.write(buf);
                              }

                      
                      
                   in.close();
                   zout.closeEntry();
                   zout.close();
                   archiveZip.close();                       
               }
            catch(IOException ex){ System.out.println(ex.toString());}

 
Log.log("ARCHIVE SUCCESS",srcFile +" is added to "+destFile,(move)this);
                currentState.doneWithSuccess(true);







     }
    else{
Log.log("ARCHIVE ERROR","parameter Hashtable for \"archive\" is not defined",(move)this);
                currentState.doneWithSuccess(false);
       }//else
   
}//public void run



}//end of class
