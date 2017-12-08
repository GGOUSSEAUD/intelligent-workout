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
    private Bitmap menum;
    private Bitmap implay;
    private Bitmap implaypressed;
    private Bitmap imsettings;
    private Bitmap imsettingspressed;
    private Bitmap interin;
    private Bitmap interout;

    private int ximplay;
    private int ximsettings;
    private int xinterin;

    private int yimplay;
    private int yimsettings;
    private int yinterin;

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

    boolean lock_inter;
    boolean lock_play;
    boolean lock_settings;

    private IMyEventListener mEventListener;

    private Resources mRes;
    private Context mContext;

    public boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;


    public IntelligentWorkoutMenu(Context context, AttributeSet attrs) {
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
        Bitmap tmenum;
        Bitmap imtplay;
        Bitmap imtplaypressed;
        Bitmap imtsettings;
        Bitmap imtsettingspressed;
        Bitmap tinterin;
        Bitmap tinterout;

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

        tmenum              = BitmapFactory.decodeResource(mRes, R.drawable.menum);
        imtplay             = BitmapFactory.decodeResource(mRes, R.drawable.play);
        imtplaypressed      = BitmapFactory.decodeResource(mRes, R.drawable.playpressed);
        imtsettings         = BitmapFactory.decodeResource(mRes, R.drawable.settings);
        imtsettingspressed  = BitmapFactory.decodeResource(mRes, R.drawable.settingspressed);
        tinterin            = BitmapFactory.decodeResource(mRes, R.drawable.interin);
        tinterout           = BitmapFactory.decodeResource(mRes, R.drawable.interout);
        menum               = Bitmap.createScaledBitmap(tmenum, (int)screenX, (int)screenY, true);
        implay              = Bitmap.createScaledBitmap(imtplay, carteTileWidth, carteTileHeight, true);
        implaypressed       = Bitmap.createScaledBitmap(imtplaypressed, carteTileWidth, carteTileHeight, true);
        imsettings          = Bitmap.createScaledBitmap(imtsettings, carteTileWidth, carteTileHeight, true);
        imsettingspressed   = Bitmap.createScaledBitmap(imtsettingspressed, carteTileWidth, carteTileHeight, true);
        interin             = Bitmap.createScaledBitmap(tinterin, carteTileWidth, carteTileHeight, true);
        interout            = Bitmap.createScaledBitmap(tinterout, carteTileWidth, carteTileHeight, true);

        ximplay = (int)(screenX/6);
        ximsettings = ximplay + carteTileWidth + (int)screenX/10;
        xinterin = ximsettings + carteTileWidth + (int)screenX/12;

        yimplay = (int)(screenY - screenY/carteHeight);
        yimsettings = (int)(screenY - screenY/carteHeight);
        yinterin = (int)(screenY - screenY/carteHeight);

        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);

        lock_inter = false;
        lock_play = false;
        lock_settings = false;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    private void paintmenum(Canvas canvas)
    {
        canvas.drawBitmap(menum, 0, 0, null);
        if(!lock_play)
            canvas.drawBitmap(implay, ximplay, yimplay, null);
        else
            canvas.drawBitmap(implaypressed, ximplay, yimplay, null);
        if(!lock_settings)
            canvas.drawBitmap(imsettings, ximsettings, yimsettings, null);
        else
            canvas.drawBitmap(imsettingspressed, ximsettings, yimsettings, null);
        if(!lock_inter)
            canvas.drawBitmap(interin, xinterin, yinterin, null);
        else
            canvas.drawBitmap(interout, xinterin, yinterin, null);
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

    public void surfaceDestroyed(SurfaceHolder arg0)
    {
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
        public void onEventStart();
        public void onEventSettings();
        public void onEventAbout();
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
                //touch sur play
                if(x > ximplay && x < ximplay + carteTileWidth && y > yimplay && y < yimplay + carteTileHeight)
                    lock_play = true;
                //touch sur settings
                if(x > ximsettings && x < ximsettings + carteTileWidth && y > yimsettings && y < yimsettings + carteTileHeight)
                    lock_settings = true;
                //touch sur a propos
                if(x > xinterin && x < xinterin + carteTileWidth && y > yinterin && y < yinterin + carteTileHeight)
                    lock_inter = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //out of play
                if(x < ximplay || x > ximplay || y < yimplay || y > yimplay)
                    lock_play = false;
                //out of settings
                if(x < ximsettings || x > ximsettings || y < yimsettings || y > yimsettings)
                    lock_settings = false;
                //out of a propos
                if(x < xinterin || x > xinterin || y < yinterin || y > yinterin)
                    lock_inter = false;
                //touch again sur play
                if(x > ximplay && x < ximplay + carteTileWidth && y > yimplay && y < yimplay + carteTileHeight)
                    lock_play = true;
                //touch again sur settings
                if(x > ximsettings && x < ximsettings + carteTileWidth && y > yimsettings && y < yimsettings + carteTileHeight)
                    lock_settings = true;
                //touch sur a propos
                if(x > xinterin && x < xinterin + carteTileWidth && y > yinterin && y < yinterin + carteTileHeight)
                    lock_inter = true;
                break;
            case MotionEvent.ACTION_UP:
                //touch up sur play
                if (lock_play)
                {
                    lock_play = false;
                    if (mEventListener != null)
                    {
                        mEventListener.onEventStart();
                    }
                }
                //touch up sur settings
                if (lock_settings)
                {
                    lock_settings = false;
                    mEventListener.onEventSettings();
                }
                //touch sur settings
                if (lock_inter)
                {
                    lock_inter = false;
                    mEventListener.onEventAbout();
                }
                break;
        }
        return true;
    }
}
