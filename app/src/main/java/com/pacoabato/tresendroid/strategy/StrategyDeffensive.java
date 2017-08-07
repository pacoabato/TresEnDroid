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

import com.pacoabato.tresendroid.model.Board;
import com.pacoabato.tresendroid.model.Box;

public class StrategyDeffensive implements IStrategy {
	private static StrategyDeffensive INSTANCE;
	
	private StrategyDeffensive(){
		// nothing to do, this is to hide the constructor
	}
	
	public static StrategyDeffensive getInstance(){
		if(INSTANCE == null) {
			INSTANCE = new StrategyDeffensive();
		}
		
		return INSTANCE;
	}
	
	@Override
	public Box playTurn() {
		Box winingMove = StrategyUtils.getWiningBox();
		
		if(winingMove != null) {
			return winingMove;
		}
		
		Box target = StrategyUtils.getThirdInLine(Board.isOTurn());
		
		if(target != null) {
			return target;
		}
		
		if(Board.isFree(1, 1)){
			return Board.getBox(1, 1);
		}
		
		IStrategy random = StrategyRandom.getInstance();
		target = random.playTurn();
		
		return target;
	}
}
