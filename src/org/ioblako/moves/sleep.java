/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: sleep.java,v 1.2 2003/12/15 15:57:01 nikitis Exp $
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




/**
 * It makes the project sleeping for a specified number of milliseconds.
 * <H5>Example </H5>
 * <pre>
 * &lt; move name="sleep" /&gt;
 * &lt; parameter name="milliseconds" value="10000"/&gt;
 * &lt;/move&gt;
 * </pre>
 */

public class sleep extends move{


public void run(){
if(parameters != null){
     if(parameters.containsKey("milliseconds")){
       move prnt = getParent();
       boolean running=true;
       if(prnt != null)
             running = prnt.isAlive(); 
     
       if(this.isAlive() && running)
        try
         {
          sleep(Integer.parseInt(parameters.get("milliseconds")));
        }
       catch(Exception e){
           Log.log("DEBUG",e.toString(),(move)this);
       }
                  currentState.doneWithSuccess(true);
      }
		else {
			Log.log("ERROR","org.ioblako.moves.sleep: missing required parameter",(move)this);
			currentState.doneWithSuccess(false);
		}
}
else{
	Log.log("ERROR:","no parameter specified ",(move)this);
			currentState.doneWithSuccess(false);

}
   
}//public void run



}//end of class
