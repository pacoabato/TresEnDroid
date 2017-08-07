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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pacoabato.tresendroid.model.Board;
import com.pacoabato.tresendroid.preferences.PreferencesActivity;
import com.pacoabato.tresendroid.view.BoardView;
import com.pacoabato.tresendroid.view.BoxDrawablesFactory;

/** Main activity for Tres en Droid (Tic tac Droid) application.
 * @author Paco Abato - pacoabato@gmail.com
 * */
public class TresEnDroid extends Activity {
	
	private static final String DONT_SHOW_AGAIN_SHARED_PREFERENCES = "DONT_SHOW_AGAIN_SHARED_PREFERENCES";
	private static final String OPTION_SHOW_ADVICE_DIALOG = "option_show_advice_dialog";
	private View boardView;
//	private AdView adView;

	private static final String APP_VERSION = "1.3.0";
	private static final String AUTHOR_NAME = "Paco Abato";
	private static final String AUTHOR_EMAIL = "pacoabato@gmail.com";
	private static final String LICENSE = "GNU/GPL";
	
	private static final String BREAK_LINE_CHAR = "\n";
	private static final String BLANK_SPACE = " ";
	
	private static final String DOWNLOAD_URL = "http://sourceforge.net/projects/tresendroid/files/";
	
	private long lastAdReload = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Options.setContext(this);

        Board.initBoard(this);
        BoxDrawablesFactory.setContext(this);

//		// el ID de adUnit es para un solo banner, si se quisiese poner otro
//		// banner o poner publi en otro sitio de la app habría que obtener otro ID

        boardView = new BoardView(this);

		LinearLayout mainView = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.activity_main,
				null);
		mainView.addView(boardView);
		mainView.setOrientation(LinearLayout.VERTICAL);
		setContentView(mainView);
        
        reloadAdBanner();
        
        showAdviceDialog();
    }
    
    private void showAdviceDialog() {
    	SharedPreferences preferences = getSharedPreferences(
    			DONT_SHOW_AGAIN_SHARED_PREFERENCES,
    			MODE_PRIVATE);
    	
    	final boolean[] show = {true};
    	show[0] = preferences.getBoolean(OPTION_SHOW_ADVICE_DIALOG, true);
    	
    	if (!show[0]) {
    		return;
    	}
    	
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		
		CharSequence title = getText(R.string.dialog_advice_title);
		
		CharSequence msg = getText(R.string.dialog_advice_message);
		
		CharSequence buttonText = getText(R.string.dialog_advice_button_close);
		DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setButton(buttonText, onClickListener);
		
		final Editor editor = preferences.edit();
		
		View checkBoxView = View.inflate(this, R.layout.dont_show_checkbox, null);
		CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.dont_show_checkbox);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	editor.putBoolean(OPTION_SHOW_ADVICE_DIALOG, !isChecked);
				editor.commit();
		    }
		});
		checkBox.setText(getText(R.string.dialog_advice_button_dont_show));
		dialog.setView(checkBoxView);
		dialog.show();
	}

	/** Reloads the ad banner if at least one minute has elapsed since the
     * last reload.*/
    public void reloadAdBanner(){
		AdView mAdView = (AdView) findViewById(R.id.adView);

		// TODO quitar mi ID al publicar el código (está en strings.xml)

		// TODO quitar las siguientes líneas (ids test) para publicar el código
		AdRequest request = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
				.addTestDevice("x")
				.build();

        long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis - lastAdReload >= 60000) {
			mAdView.loadAd(request);
        	lastAdReload = currentTimeMillis;
        }
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_options_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean itemProcessed = true;
		switch (item.getItemId()) {
		case R.id.menu_preferences:
			showPreferencesScreen();
			break;
		case R.id.menu_restart:
			Board.reset();
			boardView.invalidate();
			break;
		case R.id.menu_about:
			showAboutScreen();
			break;
		default:
			itemProcessed = super.onOptionsItemSelected(item);
			break;
		}
		
		return itemProcessed;
	}

	private void showPreferencesScreen() {
		Intent i = new Intent(this, PreferencesActivity.class);
		startActivity(i);
	}
	
	private void showAboutScreen() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		
		dialog.setTitle(getText(R.string.dialog_about_title));
		
		String msg = "";
		
		msg += getText(R.string.dialog_about_author_line);
		msg += BLANK_SPACE + AUTHOR_NAME;
		msg += BREAK_LINE_CHAR;
		msg += getText(R.string.dialog_about_email_line);
		msg += BLANK_SPACE + AUTHOR_EMAIL;
		msg += BREAK_LINE_CHAR;
		msg += getText(R.string.dialog_about_version_line);
		msg += BLANK_SPACE + APP_VERSION;
		msg += BREAK_LINE_CHAR;
		msg += getText(R.string.dialog_about_license_line);
		msg += BLANK_SPACE + LICENSE;
		
		dialog.setMessage(msg);
		
		dialog.setButton(
				getText(R.string.dialog_about_button_close),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		
		dialog.setButton2(
				getText(R.string.dialog_about_button_download_source),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Uri uri = Uri.parse(DOWNLOAD_URL);
						Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(launchBrowser);
					}
				});
		
		dialog.show();
	}
	
	@Override
	  public void onDestroy() {
//	    adView.destroy();
	    super.onDestroy();
	  }
}