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

package com.pacoabato.tresendroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Options {
	public static Context context;
	
	// estos valores corresponden a los values de arrays.xml
	public static final String PLAYMODE_VS_PLAYER = "vsplayer";
	public static final String PLAYMODE_VS_ANDROID = "vsandroid";
	
	public static final String PLAYTYPE_BLIND = "blind";
	public static final String PLAYTYPE_NORMAL = "normal";

	public static final String DIFFICULTY_EASY = "muyfacil";
	public static final String DIFFICULTY_NORMAL = "normal";
	public static final String DIFFICULTY_IMBATIBLE = "imbatible";
	
	public static final String SOUND_PLAY_ON_CLICK = "playonclick";
	public static final String SOUND_KEEP_SILENCE = "keepsilence";
	
	public static final String VIBRATION_ON  = "vibrate";
	public static final String VIBRATION_DONT_MOVE = "dontmove";
	
	public static void setContext(Context aContext){
		context = aContext;
		
		// TODO poner las constantes diréctamente desde arrays.xml
//		String[] gameModes = context.getResources().getStringArray(R.array.game_modes_values);
	}
	
	private static String getSelectedPlayMode() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedOption = prefs.getString("game_modes_preferences", PLAYMODE_VS_ANDROID);
		return selectedOption;
	}
	
	public static boolean isPlayingVsPlayer() {
		String selectedPlayMode = getSelectedPlayMode();
		return selectedPlayMode.equals(PLAYMODE_VS_PLAYER);
	}
	
	public static boolean isPlayingVsAndroid() {
		String selectedPlayMode = getSelectedPlayMode();
		return selectedPlayMode.equals(PLAYMODE_VS_ANDROID);
	}
	
	/** Devuelve true si se ha cambiado el modo, false si el modo indicado
	 * (mode) no es válido o si el modo es el mismo que ya estaba.
	 * @param preference */
	public static boolean setPlayMode(String mode, Preference preference) {
		boolean changed = false;
		boolean newModeVsAndroid = mode.equals(PLAYMODE_VS_ANDROID);
		boolean newModevsPlayer = mode.equals(PLAYMODE_VS_PLAYER);
		if(newModeVsAndroid && isPlayingVsPlayer()) {
			changed = true;
			preference.setSummary(R.string.gamemode_vs_android);
		}else if(newModevsPlayer && isPlayingVsAndroid()) {
			changed = true;
			preference.setSummary(R.string.gamemode_vs_player);
		} else if(! (newModeVsAndroid || newModevsPlayer)){
			Toast.makeText(
					context,
					context.getString(R.string.gamemode_not_valid),
					Toast.LENGTH_SHORT)
					.show();
		}
		
		return changed;
	}
	
	private static String getDifficultyLevel() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedOption = prefs.getString("difficulty_preferences", DIFFICULTY_NORMAL);
		return selectedOption;
	}
	
	/** Devuelve true si se ha cambiado el nivel, false si el nivel indicado
	 * (level) no es válido o si el nivel es el mismo que ya estaba.
	 * @param preference */
	public static boolean setDifficultyLevel(String level, Preference preference) {
		boolean changed = false;
		boolean newDifficultyEasy = level.equals(DIFFICULTY_EASY);
		boolean newDifficultyNormal = level.equals(DIFFICULTY_NORMAL);
		boolean newDifficultyImbatible = level.equals(DIFFICULTY_IMBATIBLE);
		if(newDifficultyEasy && !isDifficultyEasy()) {
			changed = true;
			preference.setSummary(R.string.difficulty_easy);
		} else if(newDifficultyNormal && !isDifficultyNormal()) {
			changed = true;
			preference.setSummary(R.string.difficulty_normal);
		} else if(newDifficultyImbatible && !isDifficultyImbatible()) {
			changed = true;
			preference.setSummary(R.string.difficulty_unbeatable);
		} else if(! (newDifficultyEasy || newDifficultyNormal || newDifficultyImbatible)){
			Toast.makeText(
					context,
					context.getString(R.string.difficulty_level_not_valid),
					Toast.LENGTH_SHORT)
				.show();
		}
		
		return changed;
	}
	
	public static boolean isDifficultyEasy(){
		return getDifficultyLevel().equals(DIFFICULTY_EASY);
	}
	
	public static boolean isDifficultyNormal(){
		return getDifficultyLevel().equals(DIFFICULTY_NORMAL);
	}
	
	public static boolean isDifficultyImbatible(){
		return getDifficultyLevel().equals(DIFFICULTY_IMBATIBLE);
	}
	
	/** Devuelve true si se ha cambiado el valor, false si el valor indicado
	 * (sndMode) no es v�lido o si es el mismo que ya estaba.
	 * @param preference */
	public static boolean setSoundMode(String newSoundMode, Preference preference) {
		boolean changed = false;
		boolean newSndModeSilence = newSoundMode.equals(SOUND_KEEP_SILENCE);
		boolean newSndPlayOnClick = newSoundMode.equals(SOUND_PLAY_ON_CLICK);
		if(newSndModeSilence && !isSoundModeSilence()) {
			changed = true;
			preference.setSummary(R.string.sound_option_silence);
		} else if(newSndPlayOnClick && !isSoundModePlayOnClick()) {
			changed = true;
			preference.setSummary(R.string.sound_option_play_on_click);
		} else if(! (newSndModeSilence || newSndPlayOnClick)){
			Toast.makeText(
					context,
					context.getString(R.string.sound_mode_not_valid),
					Toast.LENGTH_SHORT)
					.show();
		}
		
		return changed;
	}
	
	private static String getSoundMode() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedOption = prefs.getString("game_sound_preferences", SOUND_PLAY_ON_CLICK);
		return selectedOption;
	}
	
	public static boolean isSoundModeSilence() {
		return getSoundMode().equals(SOUND_KEEP_SILENCE);
	}
	
	public static boolean isSoundModePlayOnClick() {
		return getSoundMode().equals(SOUND_PLAY_ON_CLICK);
	}
	
	/** Devuelve true si se ha cambiado el valor, false si el valor indicado
	 * (vibrationMode) no es v�lido o si es el mismo que ya estaba.
	 * @param preference */
	public static boolean setVibrationMode(String newVibrationMode, Preference preference) {
		boolean changed = false;
		boolean newVibrationModeOff = newVibrationMode.equals(VIBRATION_DONT_MOVE);
		boolean newVibrationModeOn = newVibrationMode.equals(VIBRATION_ON);
		if(newVibrationModeOff && !isVibrationModeOff()) {
			changed = true;
			preference.setSummary(R.string.vibration_option_dont_move);
		} else if(newVibrationModeOn && !isVibrationModeOn()) {
			changed = true;
			preference.setSummary(R.string.vibration_option_vibrate);
		} else if(! (newVibrationModeOff || newVibrationModeOn)){
			Toast.makeText(
					context,
					context.getString(R.string.vibration_mode_not_valid),
					Toast.LENGTH_SHORT)
				.show();
		}
		
		return changed;
	}
	
	private static String getVibrationMode() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedOption = prefs.getString("game_vibration_preferences", VIBRATION_ON);
		return selectedOption;
	}
	
	public static boolean isVibrationModeOn() {
		return getVibrationMode().equals(VIBRATION_ON);
	}
	
	public static boolean isVibrationModeOff() {
		return getVibrationMode().equals(VIBRATION_DONT_MOVE);
	}

	public static boolean isBlindGameType() {
		return getSelectedPlayType().equals(PLAYTYPE_BLIND);
	}
	
	public static boolean isNormalGameType() {
		return getSelectedPlayType().equals(PLAYTYPE_NORMAL);
	}
	
	private static String getSelectedPlayType() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedOption = prefs.getString("game_types_preferences", PLAYTYPE_NORMAL);
		return selectedOption;
	}
	
	/** Devuelve true si se ha cambiado el tipo, false si el tipo indicado
	 * (type) no es v�lido o si el tipo es el mismo que ya estaba.
	 * @param preference */
	public static boolean setGameType(String mode, Preference preference) {
		boolean changed = false;
		boolean newTypeBlind = mode.equals(PLAYTYPE_BLIND);
		boolean newTypeNormal = mode.equals(PLAYTYPE_NORMAL);
		if(newTypeBlind && isNormalGameType()) {
			changed = true;
			preference.setSummary(R.string.gametype_blind);
		}else if(newTypeNormal && isBlindGameType()) {
			changed = true;
			preference.setSummary(R.string.gametype_normal);
		} else if(! (newTypeBlind || newTypeNormal)){
			Toast.makeText(
					context,
					context.getString(R.string.gametype_not_valid),
					Toast.LENGTH_SHORT)
				.show();
		}
		
		return changed;
	}
}