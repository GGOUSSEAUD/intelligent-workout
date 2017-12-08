package com.projects.p8.intelligent_workout;

import android.content.Context;
import android.content.SharedPreferences;
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

public class IntelligentWorkoutSettings extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private Bitmap imbackground;
    private Bitmap immusic;
    private Bitmap imsound;
    private Bitmap immusicoff;
    private Bitmap imsoundoff;
    private Bitmap immenui;
    private Bitmap immenuioff;

    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte
    int        cartePrevTopAnchor;
    int        cartePrevLeftAnchor;
    int        leftLevelAnchor;
    int        topLevelAnchor;

    static double screenX;
    static double screenY;

    private int ximmusic;
    private int ximsound;
    private int ximmenui;

    private int yimmusic;
    private int yimsound;
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

    boolean lock_sound;
    boolean lock_music;
    boolean lock_menu;

    private IMyEventListener mEventListener;

    private Resources mRes;
    private Context mContext;

    SharedPreferences sharedpref;

    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;


    public IntelligentWorkoutSettings(Context context, AttributeSet attrs) {
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
        Bitmap timbackground;
        Bitmap timmusic;
        Bitmap timmusicoff;
        Bitmap timsound;
        Bitmap timsoundoff;
        Bitmap timmenui;
        Bitmap timmenuioff;

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

        timbackground       = BitmapFactory.decodeResource(mRes, R.drawable.fondsettings);
        timmusic            = BitmapFactory.decodeResource(mRes, R.drawable.music);
        timmusicoff         = BitmapFactory.decodeResource(mRes, R.drawable.musicoff);
        timsound            = BitmapFactory.decodeResource(mRes, R.drawable.sound);
        timsoundoff         = BitmapFactory.decodeResource(mRes, R.drawable.soundoff);
        timmenui            = BitmapFactory.decodeResource(mRes, R.drawable.menui);
        timmenuioff         = BitmapFactory.decodeResource(mRes, R.drawable.menuioff);

        imbackground        = Bitmap.createScaledBitmap(timbackground, (int)screenX, (int)screenY, true);
        immusic             = Bitmap.createScaledBitmap(timmusic, carteTileWidth, carteTileHeight, true);
        immusicoff          = Bitmap.createScaledBitmap(timmusicoff, carteTileWidth, carteTileHeight, true);
        imsound             = Bitmap.createScaledBitmap(timsound, carteTileWidth, carteTileHeight, true);
        imsoundoff          = Bitmap.createScaledBitmap(timsoundoff, carteTileWidth, carteTileHeight, true);
        immenui             = Bitmap.createScaledBitmap(timmenui, carteTileWidth, carteTileHeight, true);
        immenuioff          = Bitmap.createScaledBitmap(timmenuioff, carteTileWidth, carteTileHeight, true);

        ximmusic = (int)screenX/4;
        ximsound = ximmusic + carteTileWidth + ximmusic/2;
        ximmenui = cartePrevLeftAnchor/5;

        yimmusic = (int)(screenY/2 - screenY/20);
        yimsound = yimmusic;
        yimmenui = cartePrevTopAnchor;

        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);

        //lock_sound = false;
        //lock_music = false;

        lock_menu = false;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    private void paintsettings(Canvas canvas)
    {
        canvas.drawBitmap(imbackground, 0, 0, null);
        if(!lock_menu)
            canvas.drawBitmap(immenui, ximmenui, yimmenui, null);
        else
            canvas.drawBitmap(immenuioff, ximmenui, yimmenui, null);
        if(!lock_sound)
            canvas.drawBitmap(imsound, ximsound, yimsound, null);
        else
            canvas.drawBitmap(imsoundoff, ximsound, yimsound, null);

        if(!lock_music)
            canvas.drawBitmap(immusic, ximmusic, yimmusic, null);
        else
            canvas.drawBitmap(immusicoff, ximmusic, yimmusic, null);

    }

    private void nDraw(Canvas canvas)
    {
        canvas.drawRGB(44, 44, 44);
        paintsettings(canvas);
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
        public void onSoundPressed();
        public void onMusicPressed();
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
                //touch on sound
                if(x > ximsound && x < ximsound + carteTileWidth
                        && y > yimsound && y < yimsound + carteTileHeight)
                {
                    lock_sound = !lock_sound;
                    mEventListener.onSoundPressed();
                }
                //touch on music
                if(x > ximmusic && x < ximmusic + carteTileWidth
                        && y > yimmusic && y < yimmusic + carteTileHeight)
                {
                    lock_music = !lock_music;
                    mEventListener.onMusicPressed();
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
                if(lock_sound)

                    ;
                if(lock_music)

                    ;
                break;
        }
        return true;
    }
}
