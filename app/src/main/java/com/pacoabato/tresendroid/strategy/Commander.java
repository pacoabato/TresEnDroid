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

import com.pacoabato.tresendroid.Options;
import com.pacoabato.tresendroid.model.Box;


/** Resolves Android movements in the game.*/
public class Commander {
	
	public static Box playTurn(){
		boolean difficultyEasy = Options.isDifficultyEasy();
		boolean difficultyNormal = Options.isDifficultyNormal();
		boolean difficultyImbatible = Options.isDifficultyImbatible();
		
		IStrategy soldier = null;
		
		if(difficultyEasy) {
			soldier = StrategyRandom.getInstance();
		} else if(difficultyNormal) {
			soldier = StrategyDeffensive.getInstance();
		} else if(difficultyImbatible) {
			soldier = StrategyUnbeatable.getInstance();
		}
		
		Box box = soldier.playTurn();
		
		return box;
	}
}
