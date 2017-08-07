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

public class StrategyUnbeatable implements IStrategy {
	private static StrategyUnbeatable INSTANCE;
	
	private StrategyUnbeatable(){
		// nothing to do, this is to hide the constructor
	}
	
	public static StrategyUnbeatable getInstance(){
		if(INSTANCE == null) {
			INSTANCE = new StrategyUnbeatable();
		}
		
		return INSTANCE;
	}
	
	@Override
	public Box playTurn() {
		int numTurn = Board.getNumTurn();
		
		if(numTurn == 0) {
			// como es el primer turno todas las esquinas están libres
			return StrategyUtils.getRandomCorner();
		}
		
		if(numTurn == 2) {
			Box oppositeCorner = StrategyUtils.playSelfOppositeCorner();
			if(oppositeCorner != null) {
				return oppositeCorner;
			}
		}
		
		if(numTurn == 3) {
			boolean hasTwoOppositeCorners = StrategyUtils.playerHasTwoOppositeCorners(true);
			if(hasTwoOppositeCorners) {
				Box freeSide = StrategyUtils.getRandomFreeSide();
				if(freeSide != null) {
					return freeSide;
				}
			}
		}
		
		// try to win
		Box winingMove = StrategyUtils.getWiningBox();
		if(winingMove != null) {
			return winingMove;
		}
		
		// try to block the opponent
		Box target = StrategyUtils.getThirdInLine(Board.isOTurn());
		if(target != null) {
			return target;
		}
		
		// try to fork (create an opportunity to win in two boxes)
		Box boxToMakeFork = StrategyUtils.getBoxToMakeFork(false);
		if(boxToMakeFork != null) {
			return boxToMakeFork;
		}
		
		// block opponent's fork
		Box boxToBlockFork = StrategyUtils.getBoxToMakeFork(true);
		if(boxToBlockFork != null) {
			return boxToBlockFork;
		}
		
		// play centre
		boolean isFreeCenter = Board.isFree(1, 1);
		if(isFreeCenter) {
			return Board.getBox(1, 1);
		}
		
		// if the opponent is in the corner play the opposite corner
		Box oppositeCorner = StrategyUtils.playOppositeCorner();
		if(oppositeCorner != null) {
			return oppositeCorner;
		}
		
		// play an empty corner
		Box freeCorner = StrategyUtils.findFreeCorner();
		if(freeCorner != null) {
			return freeCorner;
		}
		
		// play an empty side
		Box freeSide = StrategyUtils.getRandomFreeSide();
		if(freeSide != null) {
			return freeSide;
		}
		
		return null;
	}
}