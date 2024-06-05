/*
*Copyright (C) Sergey Nikitin, sergey@indexoffice.com, nikitin@asu.edu
*$Id: clearState.java,v 1.1.1.1 2003/01/23 04:54:31 nikitis Exp $
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
 * It clears the current state of the project.
 * <H5>Example</H5>
 * <pre>
 * &lt;move name="clearState"/&gt;
 * </pre>
 */


public class clearState extends move{


public void run(){
	 currentState.clear();
         currentState.doneWithSuccess(true);
}//public void run



}//end of class
