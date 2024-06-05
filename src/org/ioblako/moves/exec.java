/*
*Copyright (C) Sergey Nikitin, Dan Ciccarelli, sergey@indexoffice.com, nikitin@asu.edu
*$Id: exec.java,v 1.3 2003/01/30 05:05:10 nikitis Exp $
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
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * <H5>DESCRIPTION:</H5>
 * It executes a given script
 *
 * <hr>
 * <H5> MANDATORY PARAMETERS:</H5>
 *  script
 *
 * <hr>
 * <H5> OPTIONAL PARAMETERS:</H5>
 *  one can add arbirary finite number of parameters that are the same as 
 *  the appropriate variables in the "script". 
 *
 * <hr>
 * <H5>EXAMPLE:</H5>
 * <pre>
 * &lt;move name="exec" skipFail="true"&gt;
 *       &lt;parameter name="script" value="/usr/local/test.sh" /&gt;
 *       &lt;parameter name="hello" value="Hello world!" /&gt;
 * &lt;/move&gt;
 * </pre>
 *<BR> and the corresponding script "test.sh" is
 *<BR>
 *<pre>
 * #!/bin/sh
 * echo $hello
 *</pre>
 *
 */



public class exec extends move{


public String[]  props;


public void run(){

    try{
        sleep(60);
     }
    catch(InterruptedException e){}
    if(parameters != null){
      if(parameters.containsKey("script")){
        Enumeration<String> keys = parameters.keys();
        String key;
        props = new String[parameters.size()];
        int k = 0;
        while(keys.hasMoreElements()){
          key = keys.nextElement();
          props[k] = key+"="+parameters.get(key);
          k++;
        }
	String[] Script = parameters.get("script").split(" ");
Log.log("EXEC:DEBUG","Starting EXEC with script "+Script,(move)this);

	Process pr = Execute(Script);
if(pr == null){
         Log.log("EXEC:ERROR","exec move failed!",(move)this);
         currentState.doneWithSuccess(false);
         return;
}

	
	BufferedReader inError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
	BufferedReader inInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	try{
              String erBuf;
	      if(inError!=null){
               while((erBuf=inError.readLine())!=null)
                             Log.log("EXEC:ERROR",erBuf,(move)this);

                 inError.close();
	      }
              if(inInput!=null){
               while((erBuf=inInput.readLine())!=null)
                             Log.log("EXEC:OUTPUT",erBuf,(move)this);

                 inInput.close();
	      }



             if(pr.exitValue() == 0){ 
                       currentState.doneWithSuccess(true);
		       return;
              }
            else{
                   currentState.doneWithSuccess(false);
		     return;
              }//else
	} catch(Exception e) {
Log.log("EXEC:ERROR",e.toString(),(move)this);
                   currentState.doneWithSuccess(false);
		   return;
	}
      } else {
Log.log("EXEC:ERROR","Missing Required Parameters",(move)this);
                   currentState.doneWithSuccess(false);
		   return;
      } // end if parameters contain
    } else {
           Log.log("EXEC:ERROR","Missing All Parameters",(move)this);
                   currentState.doneWithSuccess(false);
		   return;
    }
} // end run()


	public Process Execute(String[] Script){

			try {
			  Process pr = Runtime.getRuntime().exec(Script,props);
                           if(pr!=null)
                               pr.waitFor();
                    Log.log("EXEC:DEBUG","Done waiting.",(move)this);
			  return pr;
		        } catch(Exception e) {
                    Log.log("EXEC:DEBUG",e.toString(),(move)this);
                          return null;
		        }
	}



} // end class
