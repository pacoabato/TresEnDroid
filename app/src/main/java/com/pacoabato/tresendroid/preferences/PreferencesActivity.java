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

package com.pacoabato.tresendroid.preferences;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.pacoabato.tresendroid.Options;
import com.pacoabato.tresendroid.R;
import com.pacoabato.tresendroid.model.Board;

public class PreferencesActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        // GAME MODE:
        
        final Preference gameModePref = findPreference("game_modes_preferences"); // key en el componente correspondiente en preferences.xml
        
        if(Options.isPlayingVsAndroid()) {
        	gameModePref.setSummary(R.string.gamemode_vs_android);
        } else if(Options.isPlayingVsPlayer()) {
        	gameModePref.setSummary(R.string.gamemode_vs_player);
        }
        
        gameModePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean changed = Options.setPlayMode(newValue.toString(), gameModePref);
				if(changed) {
					Board.reset(true);
				}
				return changed;
			}
		});
        
        // GAME TYPE:
        
        final Preference gameTypePref = findPreference("game_types_preferences"); // key en el componente correspondiente en preferences.xml
        
        if(Options.isBlindGameType()) {
        	gameTypePref.setSummary(R.string.gametype_blind);
        } else if(Options.isNormalGameType()) {
        	gameTypePref.setSummary(R.string.gametype_normal);
        }
        
        gameTypePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean changed = Options.setGameType(newValue.toString(), gameTypePref);
				if(changed) {
					Board.reset(true);
				}
				return changed;
			}
		});
        
        // DIFFICULTY:
        
        final Preference difficultyPref = findPreference("difficulty_preferences");
        
        if(Options.isDifficultyEasy()) {
        	difficultyPref.setSummary(R.string.difficulty_easy);
        } else if(Options.isDifficultyNormal()) {
        	difficultyPref.setSummary(R.string.difficulty_normal);
        } else if(Options.isDifficultyImbatible()) {
        	difficultyPref.setSummary(R.string.difficulty_unbeatable);
        }
        
        difficultyPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean changed = Options.setDifficultyLevel(newValue.toString(), difficultyPref);
				return changed;
			}
		});
        
        
        // SOUND:
        
        final Preference soundPref = findPreference("game_sound_preferences");
        
        if(Options.isSoundModePlayOnClick()) {
        	soundPref.setSummary(R.string.sound_option_play_on_click);
        } else if(Options.isSoundModeSilence()) {
        	soundPref.setSummary(R.string.sound_option_silence);
        }
        
        soundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean changed = Options.setSoundMode(newValue.toString(), soundPref);
				return changed;
			}
		});
        
        
        // VIBRATION:
        
        final Preference vibrationPref = findPreference("game_vibration_preferences");
        
        if(Options.isVibrationModeOn()) {
        	vibrationPref.setSummary(R.string.vibration_option_vibrate);
        } else if(Options.isVibrationModeOff()) {
        	vibrationPref.setSummary(R.string.vibration_option_dont_move);
        }
        
        vibrationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean changed = Options.setVibrationMode(newValue.toString(), vibrationPref);
				return changed;
			}
		});
	}
}
