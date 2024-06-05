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




/**
 * It types a message into a log-file.
 * <H5>Example </H5>
 * <pre>
 * &lt; move name="log" /&gt;
 * &lt; parameter name="message" value="Hi!"/&gt;
 * &lt;/move&gt;
 * </pre>
 */

public class log extends move{


public void run(){
if(parameters != null){
     if(parameters.containsKey("message")){
          String message  = parameters.get("message");
              Log.log("DEBUG",message,(move)this);
                  currentState.doneWithSuccess(true);
      }
		else {
			Log.log("ERROR","log: missing required parameter ",(move)this);
			currentState.doneWithSuccess(false);
		}
}
else{
	Log.log("ERROR","log: no parameter specified ",(move)this);
			currentState.doneWithSuccess(false);

}
   
}//public void run



}//end of class
