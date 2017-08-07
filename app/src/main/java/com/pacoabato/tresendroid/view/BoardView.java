package com.pacoabato.tresendroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.pacoabato.tresendroid.Options;
import com.pacoabato.tresendroid.model.Board;
import com.pacoabato.tresendroid.model.Box;

public class BoardView extends View {
	private int viewWidth = -1;
	private int viewHeight = -1;
	
	private int left = -1;
	private int right = -1;
	private int top = -1;
	private int bottom = -1;
	
	/** Distance in pixel between two vertically-consecutive boxes.*/
	private static final int VERTICAL_GAP = 10;
	/** Distance in pixel between two horizontally-consecutive boxes.*/
	private static final int HORIZONTAL_GAP = 10;
	
	/** Distance in pixel between the top and bottom of the board and the 
	 * upper and lowest position of the view.*/
	private static int VERTICAL_MARGIN = 20;
	/** Distance in pixel between the left and right of the board and the 
	 * left-most and right-most position of the view.*/
	private static int HORIZONAL_MARGIN = 20;
	
	public BoardView(Context context) {
		super(context);
		setBackgroundColor(0xffffffff);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// hay que recalcular cada vez por si se cambia la orientación del móvil
		// (en realidad ya no se permite el cambio de orientación, así que se
		// podría calcular sólo al principio)
		viewWidth = getWidth();
		viewHeight = getHeight();
		
		// TODO si la pantalla es demasiado grande ¿definir un rectángulo menor que
		// el total de la pantalla para el tablero?: hacerlo estableciendo nuevos
		// valores para los márgenes
		
		left = HORIZONAL_MARGIN;
		top = VERTICAL_MARGIN;
		right = viewWidth - HORIZONAL_MARGIN;
		bottom = viewHeight - VERTICAL_MARGIN;
		
		int boxWidth = calcBoxWidth();
		int boxHeight = calcBoxHeight();
		
		drawGrid(canvas);
		
		Box[][] boxes = Board.getBoxes();
		
		int row = top;
		int column = left;
		
		for(Box[] boxRow : boxes) {
			for(Box box : boxRow) {
				BitmapDrawable drawable = null;
				if(!box.isFree()) {
					boolean hideBox = Options.isBlindGameType() && !Board.isEndedGame();
					if(hideBox) {
						drawable = BoxDrawablesFactory.createOXBoxDrawable();
					} else {
						drawable = box.getDrawable();
					}
					
					drawable.setBounds(column, row, column+boxWidth, row+boxHeight);
					drawable.draw(canvas);
				}
				column += boxWidth + HORIZONTAL_GAP;
			}
			row += boxHeight + VERTICAL_GAP;
			column = left;
		}
	}
	
	private void drawGrid(Canvas canvas) {
		Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        
        int boxHeight = calcBoxHeight();
        int boxWidth = calcBoxWidth();
        
		canvas.drawLine(
				left,
				top+boxHeight+(VERTICAL_GAP/2),
				right,
				top+boxHeight+(VERTICAL_GAP/2),
				paint);
		canvas.drawLine(
				left,
				(float)(top+2*boxHeight+1.5*VERTICAL_GAP),
				right,
				(float)(top+2*boxHeight+1.5*VERTICAL_GAP),
				paint);
		
		canvas.drawLine(
				left+boxWidth+(HORIZONTAL_GAP/2),
				bottom,
				left+boxWidth+(HORIZONTAL_GAP/2),
				top,
				paint);
		canvas.drawLine(
				(float)(left+2*boxWidth+(1.5*HORIZONTAL_GAP)),
				bottom,
				(float)(left+2*boxWidth+(1.5*HORIZONTAL_GAP)),
				top,
				paint);		
	}

	private int calcBoxWidth() {
		int boxWidth = (right - left - (2*HORIZONTAL_GAP)) / 3;
		return boxWidth;
	}
	
	private int calcBoxHeight() {
		int boxHeight = (bottom - top - (2*VERTICAL_GAP)) / 3;
		return boxHeight;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		
		if(action != MotionEvent.ACTION_UP) {
			return true;
		}
		
		if(viewWidth < 0 || viewHeight < 0) {
			return true;
		}
		
		float x = event.getX();
		float y = event.getY();
		
		boolean outOfBoard = x < left || x > right 
			|| y < top || y > bottom;
		
		if(outOfBoard) {
			return true;
		}
		
		int boxWidth = calcBoxWidth();
		int boxHeight = calcBoxHeight();
		
		int column = -1;
		
		boolean firstColumn = x >= left && x <= left+boxWidth;
		boolean secondColumn = x >= left+boxWidth+HORIZONTAL_GAP && x <= left+2*boxWidth+HORIZONTAL_GAP;
		boolean thirdColumn = x >= left+2*boxWidth+2*HORIZONTAL_GAP && x <= left+3*boxWidth+2*HORIZONTAL_GAP;
		
		if(firstColumn) {
			column = 0;
		} else if(secondColumn) {
			column = 1;
		} else if(thirdColumn) {
			column = 2;
		}
		
		int row = -1;
		
		boolean firstRow = y >= top && y <= top+boxHeight;
		boolean secondRow = y >= top+boxHeight+VERTICAL_GAP && y <= top+2*boxHeight+VERTICAL_GAP;
		boolean thirdRow = y >= top+2*boxHeight+2*VERTICAL_GAP && y <= top+3*boxHeight+2*VERTICAL_GAP;
		
		if(firstRow) {
			row = 0;
		} else if(secondRow) {
			row = 1;
		} else if(thirdRow) {
			row = 2;
		}
		
		if(row == -1 || column == -1) {
			// habrá pulsado en la zona del grid
			return true;
		}
		
		Box box = Board.getBox(row, column);
		
		Board.playOn(box, this);
		
		return true;
	}
}
