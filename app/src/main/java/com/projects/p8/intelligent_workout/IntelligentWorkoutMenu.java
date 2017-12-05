package com.projects.p8.intelligent_workout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class IntelligentWorkoutMenu extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private Bitmap tmenum;
    private Bitmap menum;
    private Bitmap imtplay;
    private Bitmap implay;
    private Bitmap imtplaypressed;
    private Bitmap implaypressed;
    private Bitmap imtsettings;
    private Bitmap imsettings;
    private Bitmap imtsettingspressed;
    private Bitmap imsettingspressed;

    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte
    int        cartePrevTopAnchor;
    int        cartePrevLeftAnchor;
    int        leftLevelAnchor;
    int        topLevelAnchor;

    static double screenX;
    static double screenY;

    // taille de la carte
    static final int    carteWidth          = 5;
    static final int    carteHeight         = 5;
    static int          carteTileWidth      = 0;
    static int          carteTileHeight     = 0;
    static int          cartePrevTileWidth  = 0;
    static int          cartePrevTileHeight = 0;
    static int          lockTileWidth       = 0;
    static int          lockTileHeight      = 0;

    // indice_carte courant du touchEvent
    static int iX = -1;
    static int iY = -1;
    boolean lock_row;
    boolean lock_rowx;
    boolean lock_rowy;
    boolean bmenu;
    boolean bmenulevel;
    boolean bplaypressed;

    // valeur courante du touchEvent
    static double touchX;
    static double touchY;
    static double touchDebutX;
    static double touchDebutY;

    private IMyEventListener mEventListener;

    private Resources mRes;
    private Context mContext;

    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;


    public IntelligentWorkoutMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        cv_thread = new Thread(this);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext = context;
        mRes = mContext.getResources();

        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    public void initparameters()
    {
        screenX = getWidth();
        screenY = getHeight();

        carteTopAnchor = (int)screenY / 3;
        carteLeftAnchor = 0;
        carteTileHeight = (getHeight()-carteTopAnchor) / 5 ;
        carteTileWidth  = getWidth() / 5;

        leftLevelAnchor = (int) screenX/20;
        topLevelAnchor = (int) screenY/20;

        lockTileWidth = (int)(screenX - 5* leftLevelAnchor)/4;
        lockTileHeight = (int)(screenY - 6* topLevelAnchor)/5;

        cartePrevTopAnchor = (int) screenY/20;
        cartePrevLeftAnchor = (int) screenX/4;
        cartePrevTileWidth = ((int) (screenX) - 2*cartePrevLeftAnchor) / carteWidth;
        cartePrevTileHeight = (carteTopAnchor - 2*cartePrevTopAnchor) / carteHeight;

        tmenum      = BitmapFactory.decodeResource(mRes, R.drawable.menum);
        menum       = Bitmap.createScaledBitmap(tmenum, (int)screenX, (int)screenY, true);
        imtplay         = BitmapFactory.decodeResource(mRes, R.drawable.play);
        imtplaypressed  = BitmapFactory.decodeResource(mRes, R.drawable.playpressed);
        imtsettings  = BitmapFactory.decodeResource(mRes, R.drawable.settings);
        imtsettingspressed  = BitmapFactory.decodeResource(mRes, R.drawable.settingspressed);
        implay          = Bitmap.createScaledBitmap(imtplay, carteTileWidth, carteTileHeight, true);
        implaypressed   = Bitmap.createScaledBitmap(imtplaypressed, carteTileWidth, carteTileHeight, true);
        imsettings   = Bitmap.createScaledBitmap(imtsettings, carteTileWidth, carteTileHeight, true);
        imsettingspressed   = Bitmap.createScaledBitmap(imtsettingspressed, carteTileWidth, carteTileHeight, true);

        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    private void paintmenum(Canvas canvas)
    {
        canvas.drawBitmap(menum, 0, 0, null);
        if(!lock_rowx)
            canvas.drawBitmap(implay, (int)(screenX/4), (int)(screenY - screenY/carteHeight), null);
        else
            canvas.drawBitmap(implaypressed, (int)(screenX/4), (int)(screenY - screenY/carteHeight), null);
        if(!lock_rowy)
            canvas.drawBitmap(imsettings, (int)(screenX - carteTileWidth - screenX/4), (int)(screenY - screenY/carteHeight), null);
        else
            canvas.drawBitmap(imsettingspressed, (int)(screenX - carteTileWidth - screenX/4), (int)(screenY - screenY/carteHeight), null);
    }

    private void nDraw(Canvas canvas)
    {
        canvas.drawRGB(44, 44, 44);
        paintmenum(canvas);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
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
        public void onEventAccured();
    }

    public void setEventListener(IMyEventListener mEventListener)
    {
        this.mEventListener = mEventListener;
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (mEventListener != null)
        {
            mEventListener.onEventAccured();
        }

        double x = event.getX();
        double y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //touch sur play
                if(x > (screenX/4) && x < (screenX/4 + carteTileWidth)
                        && y > screenY - screenY/carteHeight && y < screenY - screenY/carteHeight + carteTileHeight)
                    lock_rowx = true;
                //touch sur settings
                if(x > (screenX - carteTileWidth - screenX/4) && x < (screenX - screenX/4)
                        && y > screenY - screenY/carteHeight && y < screenY - screenY/carteHeight + carteTileHeight)
                    lock_rowy = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //out of play
                if(x < (screenX/4) || x > (screenX/4 + carteTileWidth)
                        || y < screenY - screenY/carteHeight || y > screenY - screenY/carteHeight + carteTileHeight) {
                    lock_rowx = false;
                }
                //out of settings
                if(x < (screenX - carteTileWidth - screenX/4) || x > (screenX - screenX/4)
                        || y < screenY - screenY/carteHeight || y > screenY - screenY/carteHeight + carteTileHeight)
                    lock_rowy = false;
                break;
            case MotionEvent.ACTION_UP:
                //touch sur play
                if (lock_rowx)
                {
                    bmenu = false;
                    lock_rowx = false;
                    bmenulevel = true;
                }
                //touch sur settings
                if (lock_rowy)
                {
                    initparameters();//reset lvl
                    lock_rowy = false;
                }

                break;
        }
        return true;
    }
}
