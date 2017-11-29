package com.projects.p8.intelligent_workout;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

    private Resources mRes;
    private Context mContext;

    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;

    // constante modelisant les differentes types de cases
    static final int    CST_redblock    = 0;
    static final int    CST_blueblock   = 1;
    static final int    CST_greenblock  = 2;


    int nblevel = 3;
    int lvl = 0;
    int [] leveldone;
    // tableau modelisant la carte du jeu
    int[][] carte;
    int[][] carteprev;


    private Bitmap succes;
    private Bitmap lock;
    private Bitmap menum;

    boolean bmenulevel;

    // ancres pour pouvoir centrer la carte du jeu
    int        leftLevelAnchor;
    int        topLevelAnchor;

    //taille de la carte
    static int          lockTileWidth = 0;
    static int          lockTileHeight = 0;
    static final int    carteWidth    = 5;
    static final int    carteHeight   = 5;

    int [][][] ref    = {
            {       //Level 0
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock}
            },
            {       //Level 1
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock}
            },
            {       //Level 2
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_greenblock, CST_blueblock}
            }
    };

    int [][][] refprev    = {
            {
                    //Level 0
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock}
            },
            {       //Level 1
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock}
            },
            {       //Level 2
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_redblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_redblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock}
            }
    };


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

    public void initparameters() {
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

    // chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel(int lvl) {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                carte[i][j] = ref[lvl][i][j];
                carteprev[i][j] = refprev[lvl][i][j];
            }
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
        int j = 0;
        canvas.drawBitmap(menum, 0, 0, null);

        //affiche jusqu'à 20 niveaux par page
        while (nbimagedraw < nblevel) {
            for (int i = 0; i < 4; i++) {
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
    }

    private void nDraw(Canvas canvas) {

        canvas.drawRGB(44, 44, 44);

        paintlvl(canvas);
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

    public void setEventListener(IntelligentWorkoutMenuLvl.IMyEventListener mEventListener)
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
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //Log.i("TAG", "" + indice_lvl(x, y));
                lvl = indice_lvl(x, y);
                if (lvl < nblevel) {
                    bmenulevel = false;
                    loadlevel(lvl);
                }
                break;
        }

        return true;
    }
}
