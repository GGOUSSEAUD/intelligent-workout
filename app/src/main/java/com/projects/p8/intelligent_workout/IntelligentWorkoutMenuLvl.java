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

public class IntelligentWorkoutMenuLvl extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private IMyEventListener mEventListener;

    private Bitmap      tmenum;
    private Bitmap      menum;
    private Bitmap      tlock;
    private Bitmap      lock;
    private Bitmap      tlockrand;
    private Bitmap      lockrand;
    private Bitmap      tsucces;
    private Bitmap      succes;

    static double screenX;
    static double screenY;

    private Resources mRes;
    private Context mContext;

    public boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;

    // constante modelisant les differentes types de cases
    static final int    CST_redblock    = 0;
    static final int    CST_blueblock   = 1;
    static final int    CST_greenblock  = 2;

    int nblevel = 11;
    int lvl = 0;
    int [] leveldone;

    // ancres pour pouvoir centrer la carte du jeu
    int        leftLevelAnchor;
    int        topLevelAnchor;

    //taille de la carte
    static int          lockTileWidth = 0;
    static int          lockTileHeight = 0;

    public IntelligentWorkoutMenuLvl(Context context, AttributeSet attrs) {
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
        screenX         = getWidth();
        screenY         = getHeight();

        leftLevelAnchor = (int) screenX/20;
        topLevelAnchor  = (int) screenY/20;

        lockTileWidth   = (int)(screenX - 5* leftLevelAnchor)/4;
        lockTileHeight  = (int)(screenY - 6* topLevelAnchor)/5;

        tmenum          = BitmapFactory.decodeResource(mRes, R.drawable.about);
        menum           = Bitmap.createScaledBitmap(tmenum, (int)screenX, (int)screenY, true);
        tlock           = BitmapFactory.decodeResource(mRes, R.drawable.lock);
        tlockrand       = BitmapFactory.decodeResource(mRes, R.drawable.lockrand);
        tsucces         = BitmapFactory.decodeResource(mRes, R.drawable.succes);
        lock            = Bitmap.createScaledBitmap(tlock, lockTileWidth, lockTileHeight, true);
        lockrand        = Bitmap.createScaledBitmap(tlockrand, lockTileWidth, lockTileHeight, true);
        succes          = Bitmap.createScaledBitmap(tsucces, lockTileWidth, lockTileHeight, true);

        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);

        leveldone       = new int[nblevel];
        mEventListener.getLvldone();
        //for (int i = 0; i < nblevel; i++) leveldone[i] = 0;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    public int indice_lvl(double x, double y)
    {
        int i;
        int nb = -1;
        for (i = 0; i < y; i += topLevelAnchor + lockTileHeight) { nb++; }
        nb *= 4;
        for (i = 0; i < x; i += leftLevelAnchor + lockTileWidth) { nb++; }
        return nb-1;
    }

    private void paintlvl(Canvas canvas)
    {
        int nbimagedraw = 0;
        int i = 0, j = 0;
        canvas.drawBitmap(menum, 0, 0, null);

        //affiche jusqu'à 20 niveaux par page
        while (nbimagedraw < nblevel)
        {
            for (i = 0; i < 4; i++)
            {
                if (leveldone[nbimagedraw] == 0)
                    canvas.drawBitmap(lock, leftLevelAnchor + i * lockTileWidth + i * leftLevelAnchor, j * lockTileHeight + topLevelAnchor + j*topLevelAnchor, null);
                else
                    canvas.drawBitmap(succes, leftLevelAnchor + i * lockTileWidth + i * leftLevelAnchor, j * lockTileHeight + topLevelAnchor + j*topLevelAnchor, null);
                nbimagedraw++;
                if(nbimagedraw == nblevel)
                    break;
            }
            j++;
        }
        if(i >= 4)
        {
            i = 0;
        }
        else
        {
            j--;
            i++;
        }
        canvas.drawBitmap(lockrand, leftLevelAnchor + i * lockTileWidth + i * leftLevelAnchor, j * lockTileHeight + topLevelAnchor + j*topLevelAnchor, null);
    }

    private void nDraw(Canvas canvas)
    {
        canvas.drawRGB(44, 44, 44);

        paintlvl(canvas);
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
        public void onEventAccured();
        public void getLvldone();
    }

    public void setEventListener(IntelligentWorkoutMenuLvl.IMyEventListener mEventListener)
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
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Log.i("INDICE LVL", "IM " + indice_lvl(x, y));
                lvl = indice_lvl(x, y);
                if (lvl < nblevel)
                {
                    if (mEventListener != null)
                    {
                        mEventListener.onEventAccured();
                    }
                }
                if (lvl == nblevel)
                {
                    if (mEventListener != null)
                    {
                        mEventListener.onEventAccured();
                    }
                }
                break;
        }
        return true;
    }
}
