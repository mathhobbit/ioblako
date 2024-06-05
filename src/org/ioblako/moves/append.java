/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: append.java,v 1.1.1.1 2003/01/23 04:54:31 nikitis Exp $
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * <H5>DESCRIPTION:</H5>
 * It appends one file to another. 
 *
 * <hr>
 * <H5> MANDATORY PARAMETERS:</H5>
 *  part, result
 *
 * <hr>
 *
 * <hr>
 * <H5>EXAMPLE:</H5>
 * <pre>
 * &lt;move name="org.ioblako.moves.append" skipFail="true"&gt;
 *       &lt;parameter name="part" value="/tmp/appendIt" /&gt;
 *       &lt;parameter name="result" value="/tmp/writeIthere" /&gt;
 * &lt;/move&gt;
 * </pre>
 *
 * <hr>
 * @see <a href="append.html">append.java</a>
 */



public class append extends move{


public void run(){

    try{
        sleep(60);
     }
    catch(InterruptedException e){}
String destFile=null;
if(parameters != null)
     if(parameters.containsKey("part")&&
        parameters.containsKey("result")){
          
          String srcFile1 = parameters.get("part");

          destFile = parameters.get("result");
      File src1= new File (srcFile1);


             try{



                FileInputStream  in   = new FileInputStream(src1);
                FileOutputStream out  = new FileOutputStream(destFile,true);

                  int buf;
                 while((buf=in.read())!=-1)
                        out.write(buf);

                 out.close();
                 in.close();
             
Log.log("APPEND: SUCCESS",srcFile1 +" is appended to "+destFile,(move)this);
  currentState.doneWithSuccess(true);
  return;
               }
             catch(IOException ioex){
Log.log("APPEND: ERROR",ioex.toString(),(move)this);
              currentState.doneWithSuccess(false);
	      return;
              } 







     }
    else{
Log.log("APPEND ERROR","parameter Hashtable for \"append\" is wrong",(move)this);
              currentState.doneWithSuccess(false);
	       return;
       }//else
   
}//public void run



}//end of class
