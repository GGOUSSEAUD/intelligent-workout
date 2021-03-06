package com.projects.p8.intelligent_workout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.System.exit;

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
    private Bitmap      menuioff;
    private Bitmap      tmenuioff;

    private Bitmap tbackground;
    private Bitmap background;
    private Bitmap imtplay;
    private Bitmap implay;
    private Bitmap imtplaypressed;
    private Bitmap implaypressed;
    private Bitmap tretry;
    private Bitmap retry;

    //for transition to win
    private Paint alphaPaint = new Paint();
    private int outAlpha = 255;
    private int inAlpha = 0;
    private int nbCoup = 0;
    private double timer = 0.0;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    int[][] carteprev;

    int [][] rand_ref;
    int [][] rand_ref_prev;

    //tableau pour les niveaux success
    int [] leveldone;

    int nblevel = 11;
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

    private int xretry;
    private int yretry;
    private int xmenui;
    private int ymenui;
    private int xecartmenui;
    private int yecartmenui;
    private int xplay;
    private int yplay;
    private int iconWinWidth;
    private int iconWinHeight;

    private int xmovestext;
    private int ymovestext;
    private int xtimertext;
    private int ytimertext;

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
    boolean lock_menu;
    boolean lock_retry;
    boolean lock_continue;

    // valeur courante du touchEvent
    static double touchX;
    static double touchY;
    static double touchDebutX;
    static double touchDebutY;

    private boolean boolwin = false;

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
            },
            {       //Level 3
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_greenblock, CST_blueblock, CST_blueblock},
                    {CST_greenblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_greenblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_redblock, CST_blueblock}
            },
            {       //Level 4
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_redblock, CST_blueblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_redblock, CST_blueblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock}
            },
            {       //Level 5
                    {CST_greenblock, CST_redblock, CST_blueblock, CST_blueblock, CST_greenblock},
                    {CST_redblock, CST_greenblock, CST_greenblock, CST_greenblock, CST_redblock},
                    {CST_greenblock, CST_blueblock, CST_blueblock, CST_greenblock, CST_redblock},
                    {CST_blueblock, CST_greenblock, CST_blueblock, CST_greenblock, CST_redblock},
                    {CST_blueblock, CST_redblock, CST_redblock, CST_blueblock, CST_redblock}
            },
            {       //Level 6
                    {CST_redblock, CST_redblock, CST_blueblock, CST_redblock, CST_redblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_blueblock, CST_redblock}
            },
            {       //Level 7
                    {CST_redblock, CST_greenblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_greenblock, CST_redblock, CST_greenblock, CST_blueblock, CST_redblock},
                    {CST_blueblock, CST_redblock, CST_greenblock, CST_blueblock, CST_greenblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_greenblock},
                    {CST_blueblock, CST_redblock, CST_redblock, CST_blueblock, CST_blueblock}
            },
            {       //Level 8
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_redblock},
                    {CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock, CST_redblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_blueblock}
            },
            {       //Level 9
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_blueblock},
                    {CST_blueblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_blueblock, CST_redblock, CST_blueblock, CST_greenblock, CST_blueblock}
            },
            {       //Level 10
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_greenblock, CST_greenblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_greenblock, CST_greenblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_greenblock, CST_greenblock, CST_redblock, CST_redblock, CST_redblock}
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
            },
            {       //Level 3
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_blueblock, CST_greenblock, CST_blueblock, CST_greenblock, CST_blueblock},
                    {CST_blueblock, CST_blueblock, CST_redblock, CST_blueblock, CST_blueblock},
                    {CST_blueblock, CST_greenblock, CST_blueblock, CST_greenblock, CST_blueblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock}
            },
            {       //Level 4
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock}
            },
            {       //Level 5
                    {CST_blueblock, CST_greenblock, CST_redblock, CST_blueblock, CST_greenblock},
                    {CST_greenblock, CST_redblock, CST_blueblock, CST_greenblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_greenblock, CST_redblock, CST_blueblock},
                    {CST_blueblock, CST_greenblock, CST_redblock, CST_blueblock, CST_greenblock},
                    {CST_greenblock, CST_redblock, CST_blueblock, CST_greenblock, CST_redblock}
            },
            {       //Level 6
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_redblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock}
            },
            {       //Level 7
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_greenblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_greenblock, CST_greenblock, CST_greenblock, CST_redblock},
                    {CST_redblock, CST_greenblock, CST_blueblock, CST_greenblock, CST_redblock}
            },
            {       //Level 8
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock}
            },
            {       //Level 9
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_greenblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_blueblock, CST_blueblock, CST_blueblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock}
            },
            {       //Level 10
                    {CST_greenblock, CST_redblock, CST_redblock, CST_redblock, CST_greenblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_redblock, CST_greenblock, CST_redblock, CST_greenblock, CST_redblock},
                    {CST_redblock, CST_redblock, CST_redblock, CST_redblock, CST_redblock},
                    {CST_greenblock, CST_redblock, CST_redblock, CST_redblock, CST_greenblock}
            }
    };

    public     boolean in = true;
    private     Thread  cv_thread;
    SurfaceHolder holder;

    Paint paint;
    private int textSize = 0;

    CountDownTimer CDTimer;

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
    private void loadlevel(int lvl, boolean isRand)
    {
        if(!isRand)
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    carte[i][j] = ref[lvl][i][j];
                    carteprev[i][j] = refprev[lvl][i][j];
                }
            }
            else{
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    carte[i][j] = rand_ref[i][j];
                    carteprev[i][j] = rand_ref_prev[i][j];
                }
            }
        }

    }


    public void setLvl (int val)
    {
        lvl = val;
    }

    // initialisation du jeu
    public void initparameters()
    {
        final long totalSeconds = 100000;
        long intervalSeconds = 1;

        CDTimer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            public void onTick(long millisUntilFinished) {
                timer = (totalSeconds * 1000 - millisUntilFinished) / 1000;
            }

            public void onFinish() {
                Log.d( "done!", "Time's up!");
            }

        };

        carte           = new int[carteHeight][carteWidth];
        carteprev       = new int[carteHeight][carteWidth];

        mEventListener.getLevelDone();
        //for (int i = 0; i < nblevel; i++) leveldone[i] = 0;

        mEventListener.onLvlInstancePressed();
        if(lvl == nblevel )
        {
            generateLevels(5,5,0.5f,0.3f,0.2f);
            loadlevel(lvl,true);
        }
        else
        {
            loadlevel(lvl,false);
        }


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

        iconWinWidth = cartePrevLeftAnchor/2;
        iconWinHeight = (int) (cartePrevTileHeight*1.5);
        xretry = cartePrevLeftAnchor;
        yretry = (int)screenY-carteTopAnchor-carteTopAnchor/5;
        xplay = cartePrevLeftAnchor + 2*(cartePrevLeftAnchor/2 + cartePrevLeftAnchor/5);
        yplay = (int)screenY-carteTopAnchor-carteTopAnchor/5;

        //image pour le main board
        tredblock       = BitmapFactory.decodeResource(mRes, R.drawable.redblock);
        tblueblock      = BitmapFactory.decodeResource(mRes, R.drawable.blueblock);
        tgreenblock     = BitmapFactory.decodeResource(mRes, R.drawable.greenblock);
        redblock        = Bitmap.createScaledBitmap(tredblock, carteTileWidth, carteTileHeight, true);
        blueblock       = Bitmap.createScaledBitmap(tblueblock, carteTileWidth, carteTileHeight, true);
        greenblock 		  = Bitmap.createScaledBitmap(tgreenblock, carteTileWidth, carteTileHeight, true);

        //image pour la preview
        tredblockprev   = BitmapFactory.decodeResource(mRes, R.drawable.redblock);
        tblueblockprev  = BitmapFactory.decodeResource(mRes, R.drawable.blueblock);
        tgreenblockprev = BitmapFactory.decodeResource(mRes, R.drawable.greenblock);
        redblockprev    = Bitmap.createScaledBitmap(tredblockprev, cartePrevTileWidth, cartePrevTileHeight, true);
        blueblockprev 	= Bitmap.createScaledBitmap(tblueblockprev, cartePrevTileWidth, cartePrevTileHeight, true);
        greenblockprev 	= Bitmap.createScaledBitmap(tgreenblockprev, cartePrevTileWidth, cartePrevTileHeight, true);

        tmenui          = BitmapFactory.decodeResource(mRes, R.drawable.menui);
        menui           = Bitmap.createScaledBitmap(tmenui, cartePrevLeftAnchor/2, (int) (cartePrevTileHeight*1.5), true);
        tmenuioff       = BitmapFactory.decodeResource(mRes, R.drawable.menuioff);
        menuioff        = Bitmap.createScaledBitmap(tmenuioff, cartePrevLeftAnchor/2, (int) (cartePrevTileHeight*1.5), true);
        tmenum          = BitmapFactory.decodeResource(mRes, R.drawable.menum);
        menum           = Bitmap.createScaledBitmap(tmenum, (int)screenX, (int)screenY, true);

        tbackground         = BitmapFactory.decodeResource(mRes, R.drawable.win);
        background          = Bitmap.createScaledBitmap(tbackground, (int)screenX, (int)screenY, true);
        tretry              = BitmapFactory.decodeResource(mRes, R.drawable.retry);
        retry               = Bitmap.createScaledBitmap(tretry, iconWinWidth, iconWinHeight, true);
        imtplay             = BitmapFactory.decodeResource(mRes, R.drawable.play);
        imtplaypressed      = BitmapFactory.decodeResource(mRes, R.drawable.playpressed);
        implay              = Bitmap.createScaledBitmap(imtplay, iconWinWidth, iconWinHeight, true);
        implaypressed       = Bitmap.createScaledBitmap(imtplaypressed, iconWinWidth, iconWinHeight, true);

        xmenui      = cartePrevLeftAnchor/5;
        ymenui      = cartePrevTopAnchor;
        xecartmenui = cartePrevLeftAnchor/2;
        yecartmenui = (int) (cartePrevTileHeight*1.5);

        textSize    = (int) screenY/30;

        xmovestext  = cartePrevLeftAnchor + carteWidth*cartePrevTileWidth + cartePrevLeftAnchor/5;
        ymovestext  = cartePrevTopAnchor + textSize;
        xtimertext  = cartePrevLeftAnchor + carteWidth*cartePrevTileWidth + cartePrevLeftAnchor/4;
        ytimertext  = cartePrevTopAnchor + textSize*5;

        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(Color.YELLOW);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setTextSize(textSize);


        lock_rowy = false;
        lock_rowx = false;
        lock_row = false;
        lock_menu = false;
        lock_retry = false;
        lock_continue = false;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            CDTimer.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas, Paint alpha)
    {
        for (int i=0; i< carteHeight; i++)
        {
            for (int j=0; j< carteWidth; j++)
            {
                switch (carte[i][j])
                {
                    case CST_redblock:
                        canvas.drawBitmap(redblock, carteLeftAnchor+ j*carteTileWidth, carteTopAnchor+ i*carteTileHeight, alpha);
                        break;
                    case CST_blueblock:
                        canvas.drawBitmap(blueblock, carteLeftAnchor+ j*carteTileWidth, carteTopAnchor+ i*carteTileHeight, alpha);
                        break;
                    case CST_greenblock:
                        canvas.drawBitmap(greenblock, carteLeftAnchor+ j*carteTileWidth, carteTopAnchor+ i*carteTileHeight, alpha);
                        break;
                }
            }
        }
    }

    private void paintpreview(Canvas canvas, Paint alpha)
    {
        for (int i=0; i< carteHeight; i++)
        {
            for (int j=0; j< carteWidth; j++)
            {
                switch (carteprev[i][j])
                {
                    case CST_redblock:
                        canvas.drawBitmap(redblockprev, cartePrevLeftAnchor+ j*cartePrevTileWidth, cartePrevTopAnchor+ i*cartePrevTileHeight, alpha);
                        break;
                    case CST_blueblock:
                        canvas.drawBitmap(blueblockprev, cartePrevLeftAnchor+ j*cartePrevTileWidth, cartePrevTopAnchor+ i*cartePrevTileHeight, alpha);
                        break;
                    case CST_greenblock:
                        canvas.drawBitmap(greenblockprev, cartePrevLeftAnchor+ j*cartePrevTileWidth, cartePrevTopAnchor+ i*cartePrevTileHeight, alpha);
                        break;
                }
            }
        }
    }

    private void paintmenui(Canvas canvas, Paint alpha)
    {
        if(lock_menu)
            canvas.drawBitmap(menuioff, xmenui, ymenui, alpha);
        else
            canvas.drawBitmap(menui, xmenui, ymenui, alpha);
    }

    private void painttimerandcoup(Canvas canvas, Paint alpha)
    {
        canvas.drawText("Moves", xmovestext, ymovestext, alpha);
        canvas.drawText("" + nbCoup, xmovestext, ymovestext + 2*textSize, alpha);
        canvas.drawText("Timer", xtimertext, ytimertext, alpha);
        canvas.drawText("" + timer, xtimertext, ytimertext + 2*textSize, alpha);
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
        mEventListener.soundHappening();
        nbCoup++;
    }

    private boolean isWon()
    {
        for (int i=0; i < carteHeight; i++)
        {
            for (int j=0; j < carteWidth; j++)
            {
                if (carte[i][j] != carteprev[i][j])
                {
                    //Log.e("-FCT-", "pas win lvl=" + lvl + " : carte["+ i + "][" +j+"] =" + carte[i][j] + ", carteprev["+ i + "][" +j+"] = " + refprev[i][j]);
                    return false;
                }
            }
        }
        leveldone[lvl] = 1;
        Log.e("Intelligent-workout","isWon done");
        return true;
    }

    public void transition_to_win (Canvas canvas)
    {
        CDTimer.cancel();
        Log.e("Intelligent-workout","Transition to win");
        if (outAlpha > 40)
        {
            paintcarte(canvas, alphaPaint);
            paintpreview(canvas, alphaPaint);
            paintmenui(canvas, alphaPaint);
            outAlpha-=40;
            alphaPaint.setAlpha(outAlpha);
        }
        else if (inAlpha < 215)
        {
            inAlpha+=40;
            alphaPaint.setAlpha(inAlpha);
            canvas.drawBitmap(background, 0, 0, alphaPaint);
        }
        else
        {
            paint.setColor(Color.BLACK);

            //icone du menu change de place
            xecartmenui = iconWinWidth;
            yecartmenui = iconWinHeight;
            xmenui = cartePrevLeftAnchor + cartePrevLeftAnchor/2 + cartePrevLeftAnchor/5;
            ymenui = (int)screenY-carteTopAnchor-carteTopAnchor/5;

            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawText("Number of move : " + nbCoup, (int)screenX/2 - (int)screenX/4, (int)screenY/2 - textSize, paint);
            canvas.drawText("Times until end : " + timer, (int)screenX/2 - (int)screenX/4, (int)screenY/2 + textSize, paint);
            canvas.drawBitmap(retry, xretry, yretry, null);
            paintmenui(canvas, null);
            canvas.drawBitmap(implay, xplay, yplay, null);
        }
        mEventListener.onLevelWon();
    }

    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau)
    private void nDraw(Canvas canvas)
    {
        //((TextView)(((Inlevel)getContext()).findViewById(R.id.textView))).setText("test");
        canvas.drawRGB(44,44,44);
        if(isWon())
        {
            boolwin = true;
            transition_to_win(canvas);
        }
        else
        {
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
            }
            paintcarte(canvas, null);
            paintpreview(canvas, null);
            paintmenui(canvas, null);
            painttimerandcoup(canvas, paint);
        }
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        in = true;
        Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0)
    {
        in = true;
        Log.i("-> FCT <-", "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder arg0)
    {
        in = false;
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

    public interface IMyEventListener
    {
        public void onMenuPressed();
        public void onRetryPressed();
        public void onContinuePressed();
        public void onLvlInstancePressed();
        public void onLevelWon();
        public void soundHappening();
        public void getLevelDone();
    }

    public void setEventListener(IntelligentWorkout.IMyEventListener mEventListener)
    {
        this.mEventListener = mEventListener;
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event)
    {
        double x = event.getX();
        double y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //touch on menuicon
                if(x > xmenui && x < xmenui + xecartmenui
                        && y > ymenui && y < ymenui + yecartmenui)
                {
                    lock_menu = true;
                }
                else if(boolwin && (x > xretry && x < xretry + iconWinWidth
                        && y > yretry && y < yretry + iconWinHeight))
                {
                    lock_retry = true;
                }
                else if(boolwin && (x > xplay && x < xplay + iconWinWidth
                        && y > yplay && y < yplay + iconWinHeight))
                {
                    lock_continue = true;
                }
                // Touch on main board screen
                else if (event.getY() > carteTopAnchor)
                {
                    Tuple tuple = indice_carte(x, y);
                    iX = tuple.getIx();
                    iY = tuple.getIy();
                    touchDebutX = x;
                    touchDebutY = y;
                    lock_row = true;
                    //Log.i("-> FCT <-", "indice_carte: [" + iX + "," + iY + "]");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //touch on menuicon
                if(x > xmenui && x < xmenui + xecartmenui
                        && y > ymenui && y < ymenui + yecartmenui && !lock_row)
                {
                    lock_menu = true;
                }
                //touch on menuicon
                if((x < xmenui || x > xmenui + xecartmenui
                        || y < ymenui || y > ymenui + yecartmenui) && !lock_row)
                {
                    lock_menu = false;
                }

                touchX = x - touchDebutX;
                touchY = y - touchDebutY;

                //Log.i("-> FCT <-", "val[x, y] = [" + touchX + "," + touchY + "]");
                if(Math.abs(touchX) > Math.abs(touchY) && !boolwin)
                {
                    lock_rowx = true;
                    lock_rowy = false;
                }
                else if(!boolwin)
                {
                    lock_rowy = true;
                    lock_rowx = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(lock_menu)
                {
                    lock_menu = false;
                    mEventListener.onMenuPressed();
                }
                if(boolwin)
                {
                    if(lock_menu)
                    {
                        lock_menu = false;
                        mEventListener.onMenuPressed();
                    }
                    if(lock_retry)
                    {
                        mEventListener.onRetryPressed();
                    }
                    if(lock_continue)
                    {
                        mEventListener.onContinuePressed();
                    }
                }
                lock_row = false;
                lock_rowx = false;
                lock_rowy = false;
                Log.i("TAG", "touched up");
                break;
        }
        return true;
    }

    /*
    sizex : Taille en x du tableau de niveau
    sizey : Taille en y du tableau de niveau
    number_of_levels : Nombre de niveau souhaité
    percent_... : pourcentage d'apparition des couleurs dans les niveaux (entre 0 et 1)
    */
    public void generateLevels(int sizex, int sizey,
                               float percent_blue,float percent_red,float percent_green) {
        //Évite pour un pourcentage d'être plus grand que 100%
        if ((percent_blue + percent_red + percent_green) != 1.0) {
            Log.i("ERROR",
                    "ERR_BLOCK_PERCENTAGE(Percentage must cumulate to be equal to 1");
            exit(0);
        }
        //long seed = 1; // Seed pour l'aléatoire
        Random rand_gen = new Random(/*seed*/); // Aléatoire servant pour la sélection d'une couleur
        float whois; // Variable contenant ce dernier pourcentage

        //Liste d'indice aléatoire pour Y (évite les doublons)
        ArrayList<Integer> Rand_verticeY= new ArrayList<Integer>(sizey);
        for(int number = 0 ; number < sizey ; number++)
            Rand_verticeY.add(number);
        //Liste d'indice aléatoire pour X (évite les doublons)
        ArrayList<Integer> Rand_verticeX= new ArrayList<Integer>(sizex);
        for(int number = 0 ; number < sizey ; number++)
            Rand_verticeX.add(number);
        //Tableau contenant les niveaux aléatoire
        rand_ref = new int[sizey][sizex];
        //Tableau contenant les preview des niveaux aléatoire
        rand_ref_prev = new int[sizey][sizex];
        Collections.shuffle(Rand_verticeX);//Mélange les X
        Collections.shuffle(Rand_verticeY);//Mélange les Y
            for (int line = 0; line < sizey; line++) {
                for (int col = 0; col < sizex; col++) {
                    whois = rand_gen.nextFloat(); //Test d'un pourcentage (entre 0 et 1)
                    //Je suis bleu!
                    if (whois <= percent_blue) {
                        rand_ref[line][col] = CST_blueblock;
                        rand_ref_prev[Rand_verticeY.get(line)][Rand_verticeX.get(col)] = CST_blueblock;
                        //Je suis rouge!
                    } else if (whois > percent_blue && whois <= (percent_blue + percent_red)) {
                        rand_ref[line][col] = CST_redblock;
                        rand_ref_prev[Rand_verticeY.get(line)][Rand_verticeX.get(col)] = CST_redblock;
                        //Je suis vert!
                    } else if (whois > (percent_blue + percent_red)) {
                        rand_ref[line][col] = CST_greenblock;
                        rand_ref_prev[Rand_verticeY.get(line)][Rand_verticeX.get(col)] = CST_greenblock;
                    }
                }
            }
        }
    }

