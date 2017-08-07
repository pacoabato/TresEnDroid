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

public class StrategyUtils {
	
	/** Searches for a row or column in which there are two boxes occupied with
	 * an X (if searchX is true) or an O (if searchX is false) and
	 * the third box is free. Returns such the free box.*/
	public static Box getThirdInLine(boolean searchX) {
		Box[][] boxes = Board.getBoxes();
		Box boxToPlay = null;
		
		// search the rows and columns:
		
		for(int i=0; i<3; i++) {
			Box horiz0 = boxes[i][0];
			Box horiz1 = boxes[i][1];
			Box horiz2 = boxes[i][2];
			
			Box vert0 = boxes[0][i];
			Box vert1 = boxes[1][i];
			Box vert2 = boxes[2][i];
			
			
			boxToPlay = getFreeBoxInRow(horiz0, horiz1, horiz2, searchX);
			if(boxToPlay == null) {
				boxToPlay = getFreeBoxInRow(vert0, vert1, vert2, searchX);
			}
			
			if(boxToPlay != null) {
				return boxToPlay;
			}
		}
		
		// search the diagonals:
		
		Box b0 = boxes[0][0];
		Box b1 = boxes[1][1];
		Box b2 = boxes[2][2];
		
		boxToPlay = getFreeBoxInRow(b0, b1, b2, searchX);
		
		if(boxToPlay != null){
			return boxToPlay;
		}
		
		b0 = boxes[0][2];
		b1 = boxes[1][1];
		b2 = boxes[2][0];
		
		boxToPlay = getFreeBoxInRow(b0, b1, b2, searchX);
		
		return boxToPlay;
	}
	
	/** Si en la fila representada por las tres Box hay
	 * dos ocupadas por un mismo jugador (X si searchForX es true, O en
	 * caso contrario) y una libre, devuelve la libre. En caso
	 * contrario devuelve null.*/
	public static Box getFreeBoxInRow(Box b0, Box b1, Box b2, boolean searchForX) {
		int cont = 0;
		Box[] boxArray = { b0, b1, b2 };
		Box freeBox = null;
		if(searchForX){
			for(int i=0; i<3; i++) {
				Box box = boxArray[i];
				if(box.isX()) {
					cont++;
				} else if(box.isFree()) {
					freeBox = box;
				}
			}
		} else {
			for(int i=0; i<3; i++) {
				Box box = boxArray[i];
				if(box.isO()) {
					cont++;
				} else if(box.isFree()) {
					freeBox = box;
				}
			}
		}
		
		if(cont == 2) {
			return freeBox;
		}
		
		return null;
	}
	
	/**If a free box gives the victory, returns it; in other case
	 * returns null.*/
	public static Box getWiningBox() {
		Box boxToPlay = getThirdInLine(Board.isXTurn());
		
		return boxToPlay;
	}
	
	/** If the X player has occupied a corner and the opposite
	 * corner is free, returns such the free opposite corner.*/
	public static Box playOppositeCorner(){
		Box corner = Board.getBox(0, 0);
		Box oppositeCorner = Board.getBox(2, 2);
		if(corner.isX() && oppositeCorner.isFree()) {
			return oppositeCorner;
		}
		
		if(oppositeCorner.isX() && corner.isFree()) {
			return corner;
		}
		
		corner = Board.getBox(0, 2);
		oppositeCorner = Board.getBox(2, 0);
		
		if(corner.isX() && oppositeCorner.isFree()) {
			return oppositeCorner;
		}
		
		if(oppositeCorner.isX() && corner.isFree()) {
			return corner;
		}
		
		return null;
	}

	/** If the android player has occupied a corner and the opposite
	 * corner is free, returns such the free opposite corner.*/
	public static Box playSelfOppositeCorner(){
		Box corner = Board.getBox(0, 0);
		Box oppositeCorner = Board.getBox(2, 2);
		if(corner.isO() && oppositeCorner.isFree()) {
			return oppositeCorner;
		}
		
		if(oppositeCorner.isO() && corner.isFree()) {
			return corner;
		}
		
		corner = Board.getBox(0, 2);
		oppositeCorner = Board.getBox(2, 0);
		
		if(corner.isO() && oppositeCorner.isFree()) {
			return oppositeCorner;
		}
		
		if(oppositeCorner.isO() && corner.isFree()) {
			return corner;
		}
		
		return null;
	}

	/** Search for a Box so the X player (if searchForX is true) or
	 * the O player (if searchForX is false) can make a fork moving
	 * onto it. If no Box let's the player make a fork, then
	 * returns null.*/
	public static Box getBoxToMakeFork(boolean searchForX) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				Box box = Board.getBox(i, j);
				if(box.isFree()) {
					
					if(searchForX) {
						box.setX(true);
					} else {
						box.setO(true);
					}
					
					boolean isFork = StrategyUtils.isFork(searchForX);
					box.setFree();
					if(isFork){
						return box;
					}
				}
			}
		}
		
		return null;
	}

	public static Box getRandomCorner(){
		Random random = new Random();
		int value = random.nextInt(4);
		Box randomCorner = null;
		
		switch(value){
		case 0:
			randomCorner = Board.getBox(0, 0);
			break;
		case 1:
			randomCorner = Board.getBox(0, 2);
			break;
		case 2:
			randomCorner = Board.getBox(2, 0);
			break;
		case 3:
			randomCorner = Board.getBox(2, 2);
			break;
		}
		return randomCorner;
	}

	/** Return a random free side (if any).*/
	public static Box getRandomFreeSide() {
		List<Box> freeSides = new ArrayList<Box>();
		
		Box[][] boxes = Board.getBoxes();
		
		Box[] sides = {
				boxes[0][1],
				boxes[1][0],
				boxes[1][2],
				boxes[2][1]
		};
		
		for (Box sidebox : sides) {
			if(sidebox.isFree()) {
				freeSides.add(sidebox);
			}
		}
		
		if(freeSides.isEmpty()) {
			return null;
		}
		
		Random random = new Random();
		int index = random.nextInt(freeSides.size());
		
		return freeSides.get(index);
	}

	/** Checks if there is a fork for the X player (if searchForX is true)
	 * or the O player (if searchForX is false).
	 * A fork is when the given player has two winning boxes.*/
	public static boolean isFork(boolean searchForX) {
		int numWinningRows = 0;
		
		Box[][] boxes = Board.getBoxes();
		Box box = null;
		
		// search the rows and columns:
		
		for(int i=0; i<3; i++) {
			Box horiz0 = boxes[i][0];
			Box horiz1 = boxes[i][1];
			Box horiz2 = boxes[i][2];
			
			Box vert0 = boxes[0][i];
			Box vert1 = boxes[1][i];
			Box vert2 = boxes[2][i];
			
			
			box = getFreeBoxInRow(horiz0, horiz1, horiz2, searchForX);
			if(box != null) {
				numWinningRows++;
			}
			
			box = getFreeBoxInRow(vert0, vert1, vert2, searchForX);
			if(box != null) {
				numWinningRows++;
			}
		}
		
		// search the diagonals:
		
		Box b0 = boxes[0][0];
		Box b1 = boxes[1][1];
		Box b2 = boxes[2][2];
		
		box = getFreeBoxInRow(b0, b1, b2, searchForX);
		
		if(box != null){
			numWinningRows++;
		}
		
		b0 = boxes[0][2];
		b1 = boxes[1][1];
		b2 = boxes[2][0];
		
		box = getFreeBoxInRow(b0, b1, b2, searchForX);
		
		if(box != null) {
			numWinningRows++;
		}
		
		return numWinningRows > 1;
	}

	public static boolean playerHasTwoOppositeCorners(boolean playerX) {
		boolean has2Opposite = false;
		
		Box[][] boxes = Board.getBoxes();
		
		if(playerX) {
			has2Opposite = boxes[0][0].isX() && boxes[2][2].isX() ||
				boxes[0][2].isX() && boxes[2][0].isX();
		} else {
			has2Opposite = boxes[0][0].isO() && boxes[2][2].isO() ||
				boxes[0][2].isO() && boxes[2][0].isO();
		}
		
		return has2Opposite;
	}

	public static Box findFreeCorner() {
		Box[][] boxes = Board.getBoxes();
		
		if(Board.isFree(0, 0)) {
			return boxes[0][0];
		} else if(Board.isFree(0, 2)) {
			return boxes[0][2];
		} else if(Board.isFree(2, 0)) {
			return boxes[2][0];
		} else if(Board.isFree(2, 2)) {
			return boxes[2][2];
		}
		
		return null;
	}
}
