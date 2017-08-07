/*  This file is part of Tres en Droid (Tic tac Droid).

    Tres en Droid (Tic tac Droid) is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tres en Droid (Tic tac Droid) is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tres en Droid (Tic tac Droid). If not, see <http://www.gnu.org/licenses/>.
*/

package com.pacoabato.tresendroid.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pacoabato.tresendroid.model.Board;
import com.pacoabato.tresendroid.model.Box;

public class StrategyRandom implements IStrategy {
	private static StrategyRandom INSTANCE;
	
	private StrategyRandom(){
		// nothing to do, this is to hide the constructor
	}
	
	public static StrategyRandom getInstance(){
		if(INSTANCE == null) {
			INSTANCE = new StrategyRandom();
		}
		
		return INSTANCE;
	}
	
	@Override
	public Box playTurn() {
		Box winingMove = StrategyUtils.getWiningBox();
		
		if(winingMove != null) {
			return winingMove;
		}
		
		List<Box> freeBoxes = new ArrayList<Box>();
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(Board.isFree(i, j)){
					freeBoxes.add(Board.getBox(i, j));
				}
			}
		}
		
		int index = new Random().nextInt(freeBoxes.size());
		
		return freeBoxes.get(index);
	}
}
