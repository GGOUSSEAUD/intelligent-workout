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

public class IntelligentWorkout extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private IntelligentWorkout.IMyEventListener mEventListener;

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
    private Bitmap      tmenui;
    private Bitmap      menui;
    private Bitmap      tmenum;
    private Bitmap      menum;
    private Bitmap      tlock;
    private Bitmap      lock;
    private Bitmap      tsucces;
    private Bitmap      succes;


    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    int[][] carteprev;

    //tableau pour les niveaux success
    int [] leveldone;

    int nblevel = 3;
    int lvl = 0;
    // constante modelisant les differentes types de cases
    static final int    CST_redblock    = 0;
    static final int    CST_blueblock   = 1;
    static final int    CST_greenblock  = 2;
    static final int    CST_RIGHT    = 1;
    static final int    CST_LEFT   = 2;
    static final int    CST_UP  = 3;
    static final int    CST_DOWN  = 4;

    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte
    int        cartePrevTopAnchor;
    int        cartePrevLeftAnchor;
    int        leftLevelAnchor;
    int        topLevelAnchor;

    static double screenX;
    static double screenY;

    // taille de la carte
    static final int    carteWidth    = 5;
    static final int    carteHeight   = 5;
    static int          carteTileWidth = 0;
    static int          carteTileHeight = 0;
    static int          cartePrevTileWidth = 0;
    static int          cartePrevTileHeight = 0;
    static int          lockTileWidth = 0;
    static int          lockTileHeight = 0;

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

    // tableau de reference du terrain
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
    private void loadlevel(int lvl) {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                carte[i][j] = ref[lvl][i][j];
                carteprev[i][j] = refprev[lvl][i][j];
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

        leftLevelAnchor = (int) screenX/20;
        topLevelAnchor = (int) screenY/20;

        lockTileWidth = (int)(screenX - 5* leftLevelAnchor)/4;
        lockTileHeight = (int)(screenY - 6* topLevelAnchor)/5;

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

        tmenui      = BitmapFactory.decodeResource(mRes, R.drawable.menui);
        menui       = Bitmap.createScaledBitmap(tmenui, cartePrevLeftAnchor/2, (int) (cartePrevTileHeight*1.5), true);
        tmenum      = BitmapFactory.decodeResource(mRes, R.drawable.menum);
        menum       = Bitmap.createScaledBitmap(tmenum, (int)screenX, (int)screenY, true);


        tlock         = BitmapFactory.decodeResource(mRes, R.drawable.lock);
        tsucces         = BitmapFactory.decodeResource(mRes, R.drawable.succes);
        lock   = Bitmap.createScaledBitmap(tlock, lockTileWidth, lockTileHeight, true);
        succes   = Bitmap.createScaledBitmap(tsucces, lockTileWidth, lockTileHeight, true);

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
        leveldone       = new int[nblevel];

        for (int i = 0; i < nblevel; i++) leveldone[i] = 0;

        loadlevel(lvl);

        lock_rowy = false;
        lock_rowx = false;
        lock_row = false;
        bmenu = true; //init au bmenu
        bmenulevel = false; //menu lvl

        bplaypressed = false; //appuye sur bouton play

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

    private void paintmenui(Canvas canvas)
    {
        canvas.drawBitmap(menui, cartePrevLeftAnchor/5, cartePrevTopAnchor, null);
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

    private void decaleCarte (int ind, int num)
    {
        int tmp, i, j;
        switch (num)
        {
            case CST_RIGHT:
                //decale a droite
                tmp = carte[ind][carteWidth-1];
                for (j = carteWidth-1; j > 0; --j)
                {
                    carte[ind][j] = carte[ind][j-1];
                }
                carte[ind][0] = tmp;
                break;
            case CST_LEFT:
                //decale a gauche
                tmp = carte[ind][0];
                for (j = 0; j < carteWidth-1; ++j)
                {
                    carte[ind][j] = carte[ind][j+1];
                }
                carte[ind][carteWidth-1] = tmp;
                break;
            case CST_UP:
                //decale en haut
                tmp = carte[0][ind];
                for (i = 0; i < carteHeight-1; ++i)
                {
                    carte[i][ind] = carte[i+1][ind];
                }
                carte[carteHeight-1][ind] = tmp;
                break;
            case CST_DOWN:
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
        for (int i=0; i < carteHeight; i++)
        {
            for (int j=0; j < carteWidth; j++)
            {
                if (carte[i][j] != carteprev[i][j])
                {
                    Log.e("-FCT-", "pas win lvl=" + lvl + " : carte["+ i + "][" +j+"] =" + carte[i][j] + ", carteprev["+ i + "][" +j+"] = " + refprev[i][j]);
                    return false;
                }
            }
        }
        leveldone[lvl] = 1;
        return true;
    }

    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau)
    private void nDraw(Canvas canvas)
    {
        canvas.drawRGB(44,44,44);

        if(bmenulevel)
        {
            paintlvl(canvas);
        }
        else
        {
            if(isWon())
            {
                lvl++;
                bmenulevel = true;
            }
            if(lock_row)
            {
                if(lock_rowx)
                {
                    if(Math.abs(touchX) > carteTileWidth/2 && touchX > 0)
                    {
                        decaleCarte(iX, CST_RIGHT);
                        touchDebutX = touchDebutX + touchX;
                    }
                    else if(Math.abs(touchX) > carteTileWidth/2 && touchX < 0)
                    {
                        decaleCarte(iX, CST_LEFT);
                        touchDebutX = touchDebutX + touchX;
                    }
                }
                else if(lock_rowy)
                {
                    if(Math.abs(touchY) > carteTileHeight/2 && touchY < 0)
                    {
                        decaleCarte(iY, CST_UP);
                        touchDebutY = touchDebutY + touchY;
                    }
                    else if(Math.abs(touchY) > carteTileHeight/2 && touchY > 0)
                    {
                        decaleCarte(iY, CST_DOWN);
                        touchDebutY = touchDebutY + touchY;
                    }
                }
                paintcarte(canvas);
                paintpreview(canvas);
                paintmenui(canvas);
            }
            else
            {
                paintcarte(canvas);
                paintpreview(canvas);
                paintmenui(canvas);
            }
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
            while( !cv_thread.interrupted() ) {
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

    public Tuple indice_carte(double x, double y)
    {
        int i, j;
        int xi = -1, yj = -1;
        for (i = 0; i < x; i += carteTileWidth) { xi++; }
        for (j = carteTopAnchor; j < y; j += carteTileHeight) { yj++; }
        Tuple o = new Tuple(yj, xi);
        return o;
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

    public interface IMyEventListener
    {
        public void onEventAccured();
    }

    public void setEventListener(IntelligentWorkout.IMyEventListener mEventListener)
    {
        this.mEventListener = mEventListener;
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event)
    {
        if (mEventListener != null)
        {
            mEventListener.onEventAccured();
        }

        double x = event.getX();
        double y = event.getY();

        if(bmenu) //while menu
        {

        }
        else if(bmenulevel)
        {

        }
        else //while in level
        {
            if(x > cartePrevLeftAnchor/5 && x < cartePrevLeftAnchor/5 + cartePrevLeftAnchor/2
                    && y > cartePrevTopAnchor && y < cartePrevTopAnchor + (int) (cartePrevTileHeight*1.5))
            {
                bmenu = true;
            }

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    Log.i("TAG", "touched down");
                    // Touch on main board screen
                    if (event.getY() > carteTopAnchor)
                    {
                        Tuple tuple = indice_carte(x, y);
                        iX = tuple.getIx();
                        iY = tuple.getIy();
                        touchDebutX = x;
                        touchDebutY = y;
                        lock_row = true;
                        Log.i("-> FCT <-", "indice_carte: [" + iX + "," + iY + "]");
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
        }
        return true;
    }
    //((AppCompatActivity) getContext()).finish(); // end activity
}
