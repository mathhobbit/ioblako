/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: log.java,v 1.2 2003/12/15 15:57:01 nikitis Exp $
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
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * It types a message into a log-file.
 * <H5>Example </H5>
 * <pre>
 * &lt; move name="org.ghnu6.moves.dumpMyPID" /&gt;
 * &lt; parameter name="file" value="path 2 file were PID will be written"/&gt;
 * &lt;/move&gt;
 * </pre>
 */

public class dumpMyPID extends move{


public void run(){
     if(parameters.containsKey("file"))
               try{
          File pidFile = new File(parameters.get("file"));
             String[] cmd={"bash","-c","echo $$ > "+pidFile.getPath()};
                          Process myProcess = Runtime.getRuntime().exec(cmd); 
                         BufferedReader stdout = new BufferedReader( new InputStreamReader( myProcess.getInputStream() ) ) ; 
                         String line = stdout.readLine() ;  
                            while ( line != null ){
                                     Log.log("DEBUG","org.ioblako.moves.dumpMyPID: "+line,(move)this);
                                     line = stdout.readLine() ; 
                             }
                           stdout.close();
                            stdout=null;
                         myProcess = null;
                  currentState.doneWithSuccess(true);
                  return;
           }
         catch(java.io.IOException ex){
                                     Log.log("ERROR","org.ghnu6.moves.dumpMyPID: "+
                                                          ex.toString(),(move)this);
			currentState.doneWithSuccess(false);
                        return;
              }
                        Log.log("ERROR","org.ghnu6.moves.dumpMyPID: parameter \"file\" is missing!",(move)this);
                                  
			currentState.doneWithSuccess(false);

   
}//public void run



}//end of class
