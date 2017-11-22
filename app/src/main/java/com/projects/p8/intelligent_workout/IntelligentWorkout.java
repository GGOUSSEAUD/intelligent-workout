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
    private Bitmap      tredblockprev;
    private Bitmap 		tblueblockprev;
    private Bitmap 		tgreenblockprev;
    private Bitmap      redblockprev;
    private Bitmap 		blueblockprev;
    private Bitmap 		greenblockprev;
    private Bitmap 		tmenu;
    private Bitmap 		menu;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    int[][] carteprev;

    int lvl = 0;
    // constante modelisant les differentes types de cases
    static final int    CST_redblock    = 0;
    static final int    CST_blueblock   = 1;
    static final int    CST_greenblock  = 2;

    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte
    int        cartePrevTopAnchor;
    int        cartePrevLeftAnchor;


    static double screenX;
    static double screenY;

    // taille de la carte
    static final int    carteWidth    = 5;
    static final int    carteHeight   = 5;
    static int          carteTileWidth = 0;
    static int          carteTileHeight = 0;
    static int          cartePrevTileWidth = 0;
    static int          cartePrevTileHeight = 0;

    // indice courant du touchEvent
    static int iX = -1;
    static int iY = -1;
    boolean lock_row;
    boolean lock_rowx;
    boolean lock_rowy;

    // valeur courante du touchEvent
    static double touchX;
    static double touchY;
    static double touchDebutX;
    static double touchDebutY;

    // tableau de reference du terrain
    int [][] ref    = {
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock}
    };

    int [][] refprev    = {
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock}
    };

    int [][] ref2    = {
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock}
    };

    int [][] refprev2    = {
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock},
            {CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_blueblock}
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
                        carteprev[j][i]= refprev[j][i];
                    }
                }
                break;
            }
            case 1:
            {
                for (int i=0; i< carteHeight; i++)
                {
                    for (int j=0; j< carteWidth; j++)
                    {
                        carte[j][i]= ref2[j][i];
                        carteprev[j][i]= refprev2[j][i];
                    }
                }
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
        screenX = getWidth();
        screenY = getHeight();

        carteTopAnchor = (int)screenY / 3;
        carteLeftAnchor = 0;
        carteTileHeight = (getHeight()-carteTopAnchor) / 5 ;
        carteTileWidth  = getWidth() / 5;

        cartePrevTopAnchor = (int) screenY/20;
        cartePrevLeftAnchor = (int) screenX/4;
        cartePrevTileWidth = ((int) (screenX) - 2*cartePrevLeftAnchor) / carteWidth;
        cartePrevTileHeight = (carteTopAnchor - 2*cartePrevTopAnchor) / carteHeight;

        //image pour le main board
        tredblock       = BitmapFactory.decodeResource(mRes, R.drawable.redblock);
        tblueblock      = BitmapFactory.decodeResource(mRes, R.drawable.blueblock);
        tgreenblock     = BitmapFactory.decodeResource(mRes, R.drawable.greenblock);
        redblock        = Bitmap.createScaledBitmap(tredblock, carteTileWidth, carteTileHeight, true);
        blueblock 		= Bitmap.createScaledBitmap(tblueblock, carteTileWidth, carteTileHeight, true);
        greenblock 		= Bitmap.createScaledBitmap(tgreenblock, carteTileWidth, carteTileHeight, true);

        //image pour la preview
        tredblockprev   = BitmapFactory.decodeResource(mRes, R.drawable.redblock);
        tblueblockprev  = BitmapFactory.decodeResource(mRes, R.drawable.blueblock);
        tgreenblockprev = BitmapFactory.decodeResource(mRes, R.drawable.greenblock);
        redblockprev    = Bitmap.createScaledBitmap(tredblockprev, cartePrevTileWidth, cartePrevTileHeight, true);
        blueblockprev 	= Bitmap.createScaledBitmap(tblueblockprev, cartePrevTileWidth, cartePrevTileHeight, true);
        greenblockprev 	= Bitmap.createScaledBitmap(tgreenblockprev, cartePrevTileWidth, cartePrevTileHeight, true);

        tmenu   = BitmapFactory.decodeResource(mRes, R.drawable.menu);
        menu    = Bitmap.createScaledBitmap(tmenu, cartePrevLeftAnchor/2, (int) (cartePrevTileHeight*1.5), true);

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
        carteprev       = new int[carteHeight][carteWidth];
        loadlevel(lvl);

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

    private void paintpreview(Canvas canvas)
    {
        for (int i=0; i< carteHeight; i++)
        {
            for (int j=0; j< carteWidth; j++)
            {
                switch (carteprev[i][j])
                {
                    case CST_redblock:
                        canvas.drawBitmap(redblockprev, cartePrevLeftAnchor+ j*cartePrevTileWidth, cartePrevTopAnchor+ i*cartePrevTileHeight, null);
                        break;
                    case CST_blueblock:
                        canvas.drawBitmap(blueblockprev, cartePrevLeftAnchor+ j*cartePrevTileWidth, cartePrevTopAnchor+ i*cartePrevTileHeight, null);
                        break;
                    case CST_greenblock:
                        canvas.drawBitmap(greenblockprev, cartePrevLeftAnchor+ j*cartePrevTileWidth, cartePrevTopAnchor+ i*cartePrevTileHeight, null);
                        break;
                }
            }
        }
    }

    private void paintmenu(Canvas canvas)
    {
        canvas.drawBitmap(menu, cartePrevLeftAnchor/5, cartePrevTopAnchor, null);
    }

    private void decaleCarte (int ind, int num)
    {
        int tmp, i, j;
        switch (num)
        {
            case 1:
                //decale a droite
                tmp = carte[ind][carteWidth-1];
                for (j = carteWidth-1; j > 0; --j)
                {
                    carte[ind][j] = carte[ind][j-1];
                }
                carte[ind][0] = tmp;
                break;
            case 2:
                //decale a gauche
                tmp = carte[ind][0];
                for (j = 0; j < carteWidth-1; ++j)
                {
                    carte[ind][j] = carte[ind][j+1];
                }
                carte[ind][carteWidth-1] = tmp;
                break;
            case 3:
                //decale en haut
                tmp = carte[0][ind];
                for (i = 0; i < carteHeight-1; ++i)
                {
                    carte[i][ind] = carte[i+1][ind];
                }
                carte[carteHeight-1][ind] = tmp;
                break;
            case 4:
                //decale en bas
                tmp = carte[carteHeight-1][ind];
                for (i = carteHeight-1; i > 0; --i)
                {
                    carte[i][ind] = carte[i-1][ind];
                }
                carte[0][ind] = tmp;
                break;
        }
    }

    // permet d'identifier si la partie est gagnee (tous les diamants à leur place)
    private boolean isWon()
    {
        for (int i=0; i< carteHeight; i++)
        {
            for (int j=0; j< carteWidth; j++)
            {
                if (carte[j][i] != refprev[j][i])
                    return false;
            }
        }
        return true;
    }

    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau)
    private void nDraw(Canvas canvas)
    {
        if(isWon())
        {
            lvl++;
            loadlevel(lvl);
        }
        if(lock_row)
        {
            canvas.drawRGB(44,44,44);

            if(lock_rowx)
            {
                if(Math.abs(touchX) > carteTileWidth/2 && touchX > 0)
                {
                    decaleCarte(iX, 1);
                    touchDebutX = touchDebutX + touchX;
                }
                else if(Math.abs(touchX) > carteTileWidth/2 && touchX < 0)
                {
                    decaleCarte(iX, 2);
                    touchDebutX = touchDebutX + touchX;
                }
            }
            else if(lock_rowy)
            {
                if(Math.abs(touchY) > carteTileHeight/2 && touchY < 0)
                {
                    decaleCarte(iY, 3);
                    touchDebutY = touchDebutY + touchY;
                }
                else if(Math.abs(touchY) > carteTileHeight/2 && touchY > 0)
                {
                    decaleCarte(iY, 4);
                    touchDebutY = touchDebutY + touchY;
                }
            }
            paintcarte(canvas);
            paintpreview(canvas);
            paintmenu(canvas);
        }
        else
        {
            canvas.drawRGB(44,44,44);
            paintcarte(canvas);
            paintpreview(canvas);
            paintmenu(canvas);
        }
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

    public Tuple indice (double x, double y)
    {
        int i, j;
        int xi = -1, yj = -1;
        for (i = 0; i < x; i += carteTileWidth) { xi++; }
        for (j = carteTopAnchor; j < y; j += carteTileHeight) { yj++; }
        Tuple o = new Tuple(yj, xi);
        return o;
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event)
    {
        double x = event.getX();
        double y = event.getY();

        //Log.i("-> FCT <-", "onTouchEvent: X"+ x);
        //Log.i("-> FCT <-", "onTouchEvent: Y"+ y);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG", "touched down");
                // Touch on main board screen
                if (event.getY() > carteTopAnchor)
                {
                    Tuple tuple = indice(x, y);
                    iX = tuple.getIx();
                    iY = tuple.getIy();
                    touchDebutX = x;
                    touchDebutY = y;
                    lock_row = true;
                    Log.i("-> FCT <-", "indice: [" + iX + "," + iY + "]");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                touchX = x - touchDebutX;
                touchY = y - touchDebutY;
                Log.i("-> FCT <-", "val[x, y] = [" + touchX + "," + touchY + "]");
                if(Math.abs(touchX) > Math.abs(touchY))
                {
                    lock_rowx = true;
                    lock_rowy = false;
                }
                else
                {
                    lock_rowy = true;
                    lock_rowx = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                lock_row = false;
                lock_rowx = false;
                lock_rowy = false;
                Log.i("TAG", "touched up");
                break;
        }
        return true;
    }
}
