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

package com.pacoabato.tresendroid.model;


import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pacoabato.tresendroid.Options;
import com.pacoabato.tresendroid.R;
import com.pacoabato.tresendroid.TresEnDroid;
import com.pacoabato.tresendroid.strategy.Commander;
import com.pacoabato.tresendroid.view.BoxDrawablesFactory;

/** Represents the game board.*/
public class Board {
	private static Box[][] boxes;
	
	/** true means its the turn of X, false means is the turn of O*/
	private static boolean isXTurn;
	
	private static boolean endedGame;

	/** Counts the number of squares played.*/
	private static int cont;
	
	private static Context context;
	
	/** Means that if it is the turn of the android the player clics will
	 * be ignored.*/
	private static boolean androidIsPlaying;
	
	/** Number of consecutive draw games.*/
	private static int numDraws;

	private static Vibrator vibrator;
	
	private static final int NUM_DRAWS_TO_ADVICE = 10;
	
	private static final long ON_WIN_VIBRATION_TIME = 300;
	private static final long ON_WARGAME_ADVICE_VIBRATION_TIME = 1000;

	private static final long TIME_TO_SLEEP_AFTER_MOVING = 500;
	
	public static void initBoard(Context aContext){
		context = aContext;
        initBoxes();
        isXTurn = true;
        endedGame = false;
        cont = 0;
        androidIsPlaying = false;
        numDraws = 0;
        
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	private static void initBoxes() {
		boxes = new Box[3][3];
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				Box box = new Box();
				boxes[i][j] = box;
			}
		}
	}
	
	public static boolean isFree(int row, int column){
		return boxes[row][column].isFree();
	}
	
	public static Box getBox(int row, int column){
		return boxes[row][column];
	}
	
	private static void endGame() {
		String finalMsg = null;
		int msgDuration = Toast.LENGTH_SHORT;
		long vibrationTime = 0;
		
		if(isTrincarro()) {
			vibrationTime = ON_WIN_VIBRATION_TIME;
			int idMsg = isXTurn ? R.string.o_wins : R.string.x_wins;
			finalMsg = context.getString(idMsg);
			numDraws = 0;
		} else {
			finalMsg = context.getString(R.string.draw_game);
			numDraws++;
			if(numDraws == NUM_DRAWS_TO_ADVICE){
				vibrationTime = ON_WARGAME_ADVICE_VIBRATION_TIME;
				finalMsg = context.getString(R.string.wargame_advice);
				msgDuration = Toast.LENGTH_LONG;
				numDraws = 0;
			}
		}
		Toast.makeText(context, finalMsg, msgDuration).show();
		
		vibrate(vibrationTime);
		endedGame = true;
	}
	
	private static void vibrate(long vibrationTime) {
		if(Options.isVibrationModeOn()) {
			vibrator.vibrate(vibrationTime);
		}
	}
	
	private static void playOn(Box box) {
		cont++;
		
    	if(isXTurn) {
    		box.setX();
    	} else {
    		box.setO();
    	}
    	
        isXTurn = !isXTurn;
        androidIsPlaying = !androidIsPlaying;
	}
	
	public static void setXTurn() {
		isXTurn = true;
	}
	
	public static void setOTurn() {
		isXTurn = false;
	}
	
	private static void androidPlaysItsTurn() {
		try {
			Thread.sleep(TIME_TO_SLEEP_AFTER_MOVING);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
			Log.e("", "", ie);
		}
		
		Box box = Commander.playTurn();
		
		playOn(box);
		
		if(isFinished()){
			((TresEnDroid)context).reloadAdBanner();
        	endGame();
    	}
	}	
	
	private static boolean isTrincarro(){
		// horizontal and vertical rows
		for(int i=0; i<3; i++){
			Box[] horizontales = new Box[3];
			Box[] verticales = new Box[3];
			for(int j=0; j<3; j++){
				horizontales[j] = boxes[i][j];
				verticales[j] = boxes[j][i];
			}
			
			if(isLine(horizontales[0], horizontales[1], horizontales[2]) ||
					isLine(verticales[0], verticales[1], verticales[2])){
				return true;
			}
		}
		
		// check the diagonals
		Box box1 = boxes[0][0];
		Box box2 = boxes[1][1];
		Box box3 = boxes[2][2];
		
		if(isLine(box1, box2, box3)){
			return true;
		}
		
		box1 = boxes[0][2];
		box2 = boxes[1][1];
		box3 = boxes[2][0];
		
		if(isLine(box1, box2, box3)){
			return true;
		}
		
		return false;
	}
	
	private static boolean isLine(Box box1, Box box2, Box box3){
		return box1.isX() && box2.isX() && box3.isX() 
			|| box1.isO() && box2.isO() && box3.isO();
	}
	
	private static boolean isFinished(){
		boolean trincarro = isTrincarro();
		return trincarro || cont == 9;
	}
	
	public static void reset(){
		for(int i=0; i<3; i++){
        	for(int j=0; j<3; j++){
        		Box box = boxes[i][j];
        		box.setFree();
        	}
        }
		
		cont = 0;
		endedGame = false;
		// does not reset isXTurn so the other player now starts
	}
	
	public static void reset(boolean resetTurn) {
		if(resetTurn) {
			isXTurn = true;
			androidIsPlaying = false;
		}
		
		reset();
	}

	public static Box[][] getBoxes() {
		return boxes;
	}
	
	public static boolean isXTurn(){
		return isXTurn;
	}
	
	public static boolean isOTurn() {
		return !isXTurn;
	}
	
	public static void playOn(Box box, final View view) {
		if(getNumTurn() == 0) {
			BoxDrawablesFactory.reorder();
		}
		
    	boolean isPlayingVsAndroid = Options.isPlayingVsAndroid();
    	
    	if(endedGame){
    		reset();
    		view.invalidate();
    		// reset and plays the first turn
    		if(isPlayingVsAndroid && androidIsPlaying){
        		view.post(new Runnable() {
					@Override
					public void run() {
						if(getNumTurn() == 0) {
							BoxDrawablesFactory.reorder();
						}
						androidPlaysItsTurn();
						view.invalidate();
					}
				});
        	}
    		return;
    	}
    	
    	if(isPlayingVsAndroid && androidIsPlaying) {
    		return;
    	}
        
		if(!box.isFree()){
        	return;
        }
        
    	playOn(box);
        
    	view.invalidate();
    	
    	audioAlert();
    	
        if(isFinished()){
        	((TresEnDroid)context).reloadAdBanner();
        	endGame();
        }else if(isPlayingVsAndroid){
        	view.post(new Runnable() {
				@Override
				public void run() {
					androidPlaysItsTurn();
					view.invalidate();
				}
			});
        }
    }
	
    private static void audioAlert(){
    	if(Options.isSoundModeSilence()) {
    		return;
    	}
    	
    	AudioManager mAudioManager = (AudioManager)context
    		.getSystemService(Context.AUDIO_SERVICE);
    	int vol = mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
    	mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, vol, 0);
    	mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, vol);
    }
	
    /**@return the number of the turn being the first 0. It shows
     * the number of moves already done (the first turn there are no
     * moves done).*/
	public static int getNumTurn(){
		return cont;
	}
	
	public static boolean isEndedGame() {
		return endedGame;
	}
}
