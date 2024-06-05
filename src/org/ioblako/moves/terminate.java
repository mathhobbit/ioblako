/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: terminate.java,v 1.2 2003/12/15 15:57:01 nikitis Exp $
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




/**
 * It terminates the execution of the current mlist.
 * <H5>Example </H5>
 * <pre>
 * &lt; move name="terminate" /&gt;
 * </pre>
 */

public class terminate extends move{


public void run(){
       move prnt = this.getParent();
       if(prnt != null)
             prnt.setAlive(false); 
     
       this.setAlive(false);
                  currentState.doneWithSuccess(true);
   
}//public void run



}//end of class
