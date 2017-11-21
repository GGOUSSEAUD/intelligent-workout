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
    private Bitmap      tredblock;
    private Bitmap 		tblueblock;
    private Bitmap 		tgreenblock;
    private Bitmap      redblock;
    private Bitmap 		blueblock;
    private Bitmap 		greenblock;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;


    // constante modelisant les differentes types de cases
    static final int    CST_redblock    = 0;
    static final int    CST_blueblock   = 1;
    static final int    CST_greenblock  = 2;

    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte

    // taille de la carte
    static final int    carteWidth    = 5;
    static final int    carteHeight   = 5;
    static int    carteTileWidth = 0;
    static int    carteTileHeight = 0;

    // tableau de reference du terrain
    int [][] ref    = {
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock}
    };

    private     boolean in = true;
    private     Thread  cv_thread;
    SurfaceHolder holder;

    Paint paint;

    public IntelligentWorkout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        cv_thread   = new Thread(this);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext	    = context;
        mRes 		    = mContext.getResources();

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
        carteTopAnchor = getHeight() / 3;
        carteLeftAnchor = 0; // (getWidth()- carteWidth*carteTileSize)/2;
        carteTileHeight = (getHeight()-carteTopAnchor) / 5 ;
        carteTileWidth  = getWidth() / 5;

        tredblock       = BitmapFactory.decodeResource(mRes, R.drawable.redblock);
        tblueblock      = BitmapFactory.decodeResource(mRes, R.drawable.blueblock);
        tgreenblock     = BitmapFactory.decodeResource(mRes, R.drawable.greenblock);
        redblock        = Bitmap.createScaledBitmap(tredblock, carteTileWidth, carteTileHeight, true);
        blueblock 		= Bitmap.createScaledBitmap(tblueblock, carteTileWidth, carteTileHeight, true);
        greenblock 		= Bitmap.createScaledBitmap(tgreenblock, carteTileWidth, carteTileHeight, true);

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

        loadlevel(0);

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
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
                        canvas.drawBitmap(redblock, carteLeftAnchor+ j*carteTileWidth, carteTopAnchor+ i*carteTileHeight, null);
                        break;
                    case CST_blueblock:
                        canvas.drawBitmap(blueblock, carteLeftAnchor+ j*carteTileWidth, carteTopAnchor+ i*carteTileHeight, null);
                        break;
                    case CST_greenblock:
                        canvas.drawBitmap(greenblock, carteLeftAnchor+ j*carteTileWidth, carteTopAnchor+ i*carteTileHeight, null);
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
        initparameters();
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
        while (in)
        {
            try
            {
                cv_thread.sleep(40);
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

    public String indice (double x, double y)
    {
        int i, j;
        int xi = -1, yj = -1;
        for (i = 0; i < x; i += carteTileWidth) { xi++; }
        for (j = carteTopAnchor; j < y; j += carteTileHeight) { yj++; }

        return "[" + yj + "," + xi + "]";
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event)
    {
        Log.i("-> FCT <-", "onTouchEvent: X"+ event.getX());
        Log.i("-> FCT <-", "onTouchEvent: Y"+ event.getY());
        if (event.getY() > carteTopAnchor)
            Log.i("-> FCT <-", "Indice: "+ indice(event.getX(), event.getY()));


        return super.onTouchEvent(event);
    }
}
