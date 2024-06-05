/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: loopMlist.java,v 1.2 2003/12/15 15:57:01 nikitis Exp $
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
import org.ioblako.core.State;


import java.io.File;
import java.util.Date;


/**
 * <H5>DESCRIPTION:</H5>
 *  It starts execution of a given mlist in the loop.
 *  The system waits until the mlist is finishedi.
 * Then it waits designated period of time 
 * (defined by the variable \"period\", default is 1000 miliseconds).
 * After that it starts invocation of the next iteration of the mlist.
 * If \"number_of_iteration\" is not defined, then it is assumed to be equalt
 * to infinity.
 *
 * <hr>
 * <H5> MANDATORY PARAMETERS:</H5>
 *  mlist
 *
 *
 * <hr>
 * <H5>EXAMPLE:</H5>
 * <pre>
 * &lt;move name="loopMlist" &gt;
 *       &lt;parameter name="mlist" value="/tmp/mlist.xml" /&gt;
 *       &lt;parameter name="period" value="3000" /&gt;
 *       &lt;parameter name="number_of_iterations" value="5" /&gt;
 *       &lt;parameter name="timeInterval" value="5000" /&gt;
 * &lt;/move&gt;
 * </pre>
 *
 * <hr>
 * @see <a href="startDaemon.html">startDaemon</a>
 * @see <a href="addMlist.html">addMlist</a>
 */



public class loopMlist extends move{


public void run(){

if(parameters == null){
Log.log("loopMlist:ERROR","parameters are not defined",(move)this);
        currentState.doneWithSuccess(false);
return;

}



if(!parameters.containsKey("mlist")){

Log.log("loopMlist:ERROR","loopMlist should have parameter mlist",(move)this);
        currentState.doneWithSuccess(false);
return;
}//if(!parameters.containsKey())


if(!(new File(parameters.get("mlist"))).exists()){

Log.log("loopMlist: ERROR","mlist file does not exists",(move)this);
          currentState.doneWithSuccess(false);
return;
}//if(!parameters.containsKey())

          int period=1000;
       if(parameters.containsKey("period"))
             try{
                period = Integer.parseInt(parameters.get("period")); 
              }
            catch(Exception ex){
              Log.log("loopMlist: ERROR",ex.toString(),(move)this);
                 period=1000;
               }

         int number_of_iterations =-1;

          if(parameters.containsKey("number_of_iterations"))
             try{
                number_of_iterations = Integer.parseInt(parameters.get("number_of_iterations"));
             }
              catch(Exception ex){
                      Log.log("loopMlist: ERROR",ex.toString(),(move)this);
                   number_of_iterations =-1;
               } 
          int timeInterval=-1;
          if(parameters.containsKey("timeInterval"))
             try{
                timeInterval = Integer.parseInt(parameters.get("timeInterval"));
             }
              catch(Exception ex){
                      Log.log("loopMlist: ERROR",ex.toString(),(move)this);
                   timeInterval =-1;
               } 

    long start=0, current=0; 
   if(timeInterval>0){
            start=(new Date()).getTime();
    }
  

runMst mn = null;
     while(this.isAlive()){ 
        if(timeInterval>0){
                current = (new Date()).getTime();
                if(current - start > timeInterval)
                   break;
         }
        if(number_of_iterations == 0)
             break;
    try{
    mn = new runMst(parameters);
     }
    catch(Exception exx){
        Log.log("ERROR","loopMlist: "+exx.toString(),(move)this);
        currentState.doneWithSuccess(false);
        return;
     }
    mn.setState(currentState);
    if(myThreadGroup != null){
       if(myName != null){
        mn.setThreadGroup(new ThreadGroup(myThreadGroup,myName+".loopMlist"));
        mn.setName(myName+".loopMlist"); 
        }//if
       else{
           mn.setThreadGroup(new ThreadGroup(myThreadGroup,"loopMlist"));
           mn.setName("loopMlist");
        }//else
       }//if
      else{
              if(myName != null)
                    mn.setName(myName+".loopMlist");
              else
                    mn.setName("loopMlist");
       }//else

      mn.setParent((move)this);
      try{
        mn.start();
       }
      catch(Exception ex){
              Log.log("ERROR","loopMlist:"+ex.toString(),(move)this);
                currentState.doneWithSuccess(false);
                 return;
         }

        try{
                   currentState.isItSuccess();
	      
          if(currentState.containsKey(parameters.get("mlist"))){
	             State mlistState = (State)currentState.get(parameters.get("mlist"));

		  if(!mlistState.isItSuccess()){
                        currentState.doneWithSuccess(false);
			return; 
		  }
	   }
                  this.sleep(period);
               }
             catch(InterruptedException ie){
              Log.log("loopMlist: ERROR",ie.toString(),(move)this);
                   currentState.doneWithSuccess(false);
                   return;
                 }
             if(number_of_iterations>0)
                   number_of_iterations--;
              mn.setState(null);
              currentState.SetIsItDone(false);
	  }//while

                   currentState.doneWithSuccess(true);
                   return;

}//public void run()




}//public class rename extends move{
