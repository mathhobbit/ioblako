/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: startDaemon.java,v 1.2 2003/12/15 15:57:01 nikitis Exp $
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
import org.ioblako.core.runMst;


import java.io.File;



/**
 * <H5>DESCRIPTION:</H5>
 *  It starts execution of a given mlist.
 *  The system does not wait until the mlist is finished and
 *  proceedes with the next move.
 *
 * <hr>
 * <H5> MANDATORY PARAMETERS:</H5>
 *  mlist
 *
 *
 * <hr>
 * <H5>EXAMPLE:</H5>
 * <pre>
 * &lt;move name="startDaemon" &gt;
 *       &lt;parameter name="mlist" value="/tmp/mlist.mst" /&gt;
 * &lt;/move&gt;
 * </pre>
 *
 * <hr>
 * @see <a href="assMlist.html">startDaemon</a>
 */



public class startDaemon extends move {


public void run(){

if(parameters == null){
Log.log("ERROR","startDaemon: parameters are not defined",(move)this);
        currentState.doneWithSuccess(false);
return;

}



if(!parameters.containsKey("mlist")){

Log.log("ERROR","startDaemon should have parameter mlist",(move)this);
        currentState.doneWithSuccess(false);
return;
}//if(!parameters.containsKey())


if(!(new File(parameters.get("mlist"))).exists()){

Log.log("ERROR","startDaemon: mlist file does not exists",(move)this);
          currentState.doneWithSuccess(false);
return;
}//if(!parameters.containsKey())





    try{
        sleep(60);
     }
    catch(InterruptedException e){}
     runMst mn = null;
  try{
    mn = new runMst(parameters);
   }
  catch(Exception exx){
         Log.log("ERROR","startDaemon: "+exx.toString(),(move)this);
          currentState.doneWithSuccess(false);
          return;
   }

    if(myThreadGroup != null){
       if(myName != null){
        mn.setThreadGroup(new ThreadGroup(myThreadGroup,myName+".startDaemon"));
        mn.setName(myName+".startDaemon");
        }//if
       else{
           mn.setThreadGroup(new ThreadGroup(myThreadGroup,"startDaemon"));
           mn.setName("startDaemon");
        }//else
       }//if
      else{
              if(myName != null)
                    mn.setName(myName+".startDaemon");
              else
                    mn.setName("startDaemon");
       }//else
       
         
    //mn.setInstance(mn);
    mn.setParent((move)this);
    try{
           mn.start();
     }
    catch(Exception ex){
                Log.log("ERROR","startDaemon:"+ ex.toString(),(move)this);
                 currentState.doneWithSuccess(false);
                 return;
     }

    currentState.doneWithSuccess(true);
    try{
      mn.join();
    }
    catch(InterruptedException e){}
                return;

}//public void run(){




}//public class rename extends move{
