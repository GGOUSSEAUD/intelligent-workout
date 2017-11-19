package com.projects.p8.intelligent_workout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class IntelligentWorkout extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // Declaration des images
    private Bitmap 		redblock;
    private Bitmap 		blueblock;
    private Bitmap 		greenblock;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;


    // constante modelisant les differentes types de cases
    static final int    CST_redblock     = 0;
    static final int    CST_blueblock   = 1;
    static final int    CST_greenblock     = 2;

    static final int carteHeight = 5;
    static final int carteWidth = 5;

    // tableau de reference du terrain
    int [][] ref    = {
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock}
    };

    SurfaceHolder holder;
    Paint paint;

    public IntelligentWorkout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext	= context;
        mRes 		= mContext.getResources();
        redblock 		= BitmapFactory.decodeResource(mRes, R.drawable.redblock);
        blueblock 		= BitmapFactory.decodeResource(mRes, R.drawable.blueblock);
        greenblock 		= BitmapFactory.decodeResource(mRes, R.drawable.greenblock);

        // initialisation des parmametres du jeu
        //initparameters();

        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    // chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel(int lvl)
    {
        switch (lvl)
        {
            case 0:
            {
                for (int i=0; i< carteHeight; i++)
                {
                    for (int j=0; j< carteWidth; j++)
                    {
                        carte[j][i]= ref[j][i];
                    }
                }
                break;
            }
            case 1:
            {
                break;
            }
            case 2:
            {
                break;
            }
            default:
            {
                break;
            }
        }
    }

    // initialisation du jeu
    public void initparameters()
    {
        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);
        carte           = new int[carteHeight][carteWidth];
    }

    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas)
    {
        for (int i=0; i< carteHeight; i++)
        {
            for (int j=0; j< carteWidth; j++)
            {
                switch (carte[i][j])
                {
                    case CST_redblock:
                        canvas.drawBitmap(redblock, j, i, null);
                        break;
                    case CST_blueblock:
                        canvas.drawBitmap(blueblock, j, i, null);
                        break;
                    case CST_greenblock:
                        canvas.drawBitmap(greenblock, j, i, null);
                        break;
                }
            }
        }
    }

    // permet d'identifier si la partie est gagnee (tous les diamants à leur place)
    private boolean isWon()
    {
        return true;
    }

    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau)
    private void nDraw(Canvas canvas)
    {
        canvas.drawRGB(44,44,44);
        paintcarte(canvas);
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
    }

    public void surfaceCreated(SurfaceHolder arg0)
    {
        Log.i("-> FCT <-", "surfaceCreated");
    }


    public void surfaceDestroyed(SurfaceHolder arg0)
    {
        Log.i("-> FCT <-", "surfaceDestroyed");
    }

    public void run()
    {
        Canvas c = null;
        while (true)
        {
            try
            {
                try
                {
                    c = holder.lockCanvas(null);
                    if(c != null)
                        nDraw(c);
                }
                finally
                {
                    if (c != null)
                    {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }
            catch(Exception e)
            {
                Log.e("-> RUN <-", e.getMessage());
            }
        }
    }

    // verification que nous sommes dans le tableau
    private boolean IsOut(int x, int y)
    {
        if ((x < 0) || (x > carteWidth- 1))
        {
            return true;
        }
        if ((y < 0) || (y > carteHeight- 1))
        {
            return true;
        }
        return false;
    }

    //controle de la valeur d'une cellule
    private boolean IsCell(int x, int y, int mask)
    {
        if (carte[y][x] == mask)
        {
            return true;
        }
        return false;
    }

    // fonction permettant de recuperer les retours clavier
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        Log.i("-> FCT <-", "onKeyUp: "+ keyCode);

        if (keyCode == KeyEvent.KEYCODE_0)
        {
            initparameters();
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
        {
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        {
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
        }
        return true;
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event)
    {
        Log.i("-> FCT <-", "onTouchEvent: X"+ event.getX());
        Log.i("-> FCT <-", "onTouchEvent: Y"+ event.getY());

        if (event.getY()<50)
        {
            onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null);
        }
        else if (event.getY()>getHeight()-50)
        {
            if (event.getX()>getWidth()-50)
            {
                onKeyDown(KeyEvent.KEYCODE_0, null);
            }
            else
            {
                onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null);
            }
        }
        else if (event.getX()<50)
        {
            onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
        }
        else if (event.getX()>getWidth()-50)
        {
            onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }
        return super.onTouchEvent(event);
    }
}
