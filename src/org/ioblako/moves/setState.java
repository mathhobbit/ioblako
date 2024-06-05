/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: setState.java,v 1.2 2003/12/15 15:57:01 nikitis Exp $
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

import java.util.Enumeration;


/**
 * It can have any number of parameters.
 * It puts those parameters into the current State. 
 * <H5>Example </H5>
 * <pre>
 * &lt; move name="setState" /&gt;
 * &lt; parameter name="Name1" value="Value1"/&gt;
 * &lt; parameter name="Name2" value="Value2"/&gt;
 * &lt; parameter name="Name_yet_another_one" value="Value_and_one_more"/&gt;
 * &lt;/move&gt;
 * </pre>
 */

public class setState extends move{


public void run(){
if(parameters == null){
	Log.log("ERROR","setState: no parameter specified ",(move)this);
			currentState.doneWithSuccess(false);
         return;
}
  String key=null;
  for(Enumeration<String> en = parameters.keys();en.hasMoreElements();){
      key = en.nextElement(); 
	Log.log("DEBUG","setState: key="+key,(move)this);
      
      currentState.put(key,parameters.get(key));
   }   
			currentState.doneWithSuccess(true);

   
}//public void run



}//end of class
