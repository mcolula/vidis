/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util;

import java.nio.DoubleBuffer;

import javax.vecmath.Tuple4d;
import javax.vecmath.Vector4d;

import org.apache.log4j.Logger;

public class Matrix4d {
	private static Logger logger = Logger.getLogger(Matrix4d.class);

	private DoubleBuffer data;
	
	public Matrix4d( DoubleBuffer b ){
		data = b;
	}
	
	public String toString() {
		String ret = "( ";
		for ( double d : data.array() ) {
			ret += " " + d;
		}
		ret += " )";
		return ret;
	}
	
	public double get(int x, int y) {
		return data.array()[4*y+x];
	}
	
	public Vector4d mul( Tuple4d in ) {
		Vector4d ret = new Vector4d();
		ret.x = get(0,0) * in.x + get(1,0) * in.y + get(2,0) * in.z + get(3,0) * in.w;
		ret.y = get(0,1) * in.x + get(1,1) * in.y + get(2,1) * in.z + get(3,1) * in.w;
		ret.z = get(0,2) * in.x + get(1,2) * in.y + get(2,2) * in.z + get(3,2) * in.w;
		ret.w = get(0,3) * in.x + get(1,3) * in.y + get(2,3) * in.z + get(3,3) * in.w;
		return ret;
	}
}
