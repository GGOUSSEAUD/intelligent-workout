package com.projects.p8.intelligent_workout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class IntelligentWorkoutAbout extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private Bitmap background;
    private Bitmap menui;
    private Bitmap menuioff;

    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte
    int        cartePrevTopAnchor;
    int        cartePrevLeftAnchor;
    int        leftLevelAnchor;
    int        topLevelAnchor;

    static double screenX;
    static double screenY;

    private int ximmenui;
    private int yimmenui;

    // taille de la carte
    static final int    carteWidth          = 5;
    static final int    carteHeight         = 5;
    static int          carteTileWidth      = 0;
    static int          carteTileHeight     = 0;
    static int          cartePrevTileWidth  = 0;
    static int          cartePrevTileHeight = 0;
    static int          lockTileWidth       = 0;
    static int          lockTileHeight      = 0;

    private int textSize = 0;

    boolean lock_menu;

    private IMyEventListener mEventListener;

    private Resources mRes;
    private Context mContext;

    public boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;


    public IntelligentWorkoutAbout(Context context, AttributeSet attrs) {
        super(context, attrs);

        cv_thread   = new Thread(this);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder      = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext    = context;
        mRes        = mContext.getResources();

        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    public void initparameters()
    {
        Bitmap tbackground;
        Bitmap tmenui;
        Bitmap tmenuioff;

        screenX             = getWidth();
        screenY             = getHeight();

        carteTopAnchor      = (int)screenY / 3;
        carteLeftAnchor     = 0;
        carteTileHeight     = (getHeight()-carteTopAnchor) / 5 ;
        carteTileWidth      = getWidth() / 5;

        leftLevelAnchor     = (int) screenX/20;
        topLevelAnchor      = (int) screenY/20;

        lockTileWidth       = (int)(screenX - 5* leftLevelAnchor)/4;
        lockTileHeight      = (int)(screenY - 6* topLevelAnchor)/5;

        cartePrevTopAnchor  = (int) screenY/20;
        cartePrevLeftAnchor = (int) screenX/4;
        cartePrevTileWidth  = ((int) (screenX) - 2*cartePrevLeftAnchor) / carteWidth;
        cartePrevTileHeight = (carteTopAnchor - 2*cartePrevTopAnchor) / carteHeight;

        tbackground         = BitmapFactory.decodeResource(mRes, R.drawable.about);
        tmenui              = BitmapFactory.decodeResource(mRes, R.drawable.menui);
        tmenuioff           = BitmapFactory.decodeResource(mRes, R.drawable.menuioff);
        background          = Bitmap.createScaledBitmap(tbackground, (int)screenX, (int)screenY, true);
        menui               = Bitmap.createScaledBitmap(tmenui, carteTileWidth, carteTileHeight, true);
        menuioff            = Bitmap.createScaledBitmap(tmenuioff, carteTileWidth, carteTileHeight, true);

        ximmenui = cartePrevLeftAnchor/5;
        yimmenui = cartePrevTopAnchor;

        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(Color.GREEN);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        textSize = (int) screenY/18;
        paint.setTextSize(textSize);

        lock_menu = false;
        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    private void paintmenum(Canvas canvas)
    {
        canvas.drawBitmap(background, 0, 0, null);
        if(!lock_menu)
            canvas.drawBitmap(menui, ximmenui, yimmenui, null);
        else
            canvas.drawBitmap(menuioff, ximmenui, yimmenui, null);
        canvas.drawText("GOUSSEAUD GAËTAN", textSize, (int)(screenY-screenY/3) - 2*textSize, paint);
        canvas.drawText("13402498", textSize, (int)(screenY-screenY/3) - textSize, paint);
        canvas.drawText("LE JEUNE VINCENT", textSize, (int)(screenY-screenY/3) + textSize, paint);
        canvas.drawText("14505788", textSize, (int)(screenY-screenY/3) + 2*textSize, paint);
    }

    private void nDraw(Canvas canvas)
    {
        canvas.drawRGB(44, 44, 44);
        paintmenum(canvas);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        in = true;
        Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        in = true;
        Log.i("-> FCT <-", "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        in = false;
        Log.i("-> FCT <-", "surfaceDestroyed");
    }

    public void run() {
        while (!cv_thread.interrupted()) {
            Canvas c = null;
            while (in) {
                try {
                    cv_thread.sleep(40);
                    try {
                        c = holder.lockCanvas(null);
                        if (c != null)
                            nDraw(c);
                    } finally {
                        if (c != null) {
                            holder.unlockCanvasAndPost(c);
                        }
                    }
                } catch (Exception e) {
                    Log.e("-> RUN <-", e.getMessage());
                }
            }
        }
    }

    public interface IMyEventListener
    {
        public void onMenuPressed();
    }

    public void setEventListener(IMyEventListener mEventListener)
    {
        this.mEventListener = mEventListener;
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        double x = event.getX();
        double y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //touch on menuicon
                if(x > ximmenui && x < ximmenui + carteTileWidth
                        && y > yimmenui && y < yimmenui + carteTileHeight)
                {
                    lock_menu = true;
                }
            case MotionEvent.ACTION_MOVE:
                //touch on menuicon
                if(x > ximmenui && x < ximmenui + carteTileWidth
                        && y > yimmenui && y < yimmenui + carteTileHeight)
                {
                    lock_menu = true;
                }
                //touch on menuicon
                if((x < ximmenui || x > ximmenui + carteTileWidth
                        || y < yimmenui || y > yimmenui + carteTileHeight))
                {
                    lock_menu = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(lock_menu)
                {
                    lock_menu = false;
                    mEventListener.onMenuPressed();
                }
                break;
        }
        return true;
    }
}
