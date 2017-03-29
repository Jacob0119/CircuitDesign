package com.example.lee.circuitdesign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.lee.circuitdesign.device.component.Component;
import com.example.lee.circuitdesign.device.component.ComponentFactory;
import com.example.lee.circuitdesign.device.wire.WireFactory;

/**
 * Created by Lee on 2016-11-25.
 */
 class MyCanvas extends View implements Runnable{



    static int Mode;


    int mSelect;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    Rect clipBounds_canvas;
    private float mScaleFactor=1.5f;
    private float focalPointx=0;
    private float focalPointy=0;

    private final float MAXSCALE=2.5f;
    private int preMotion=MotionEvent.ACTION_MASK;



     static WireFactory wires;
    static ComponentFactory components;
    static MakeActivity parent;


    Handler handler;
    public MyCanvas(final Context context, AttributeSet attr){

        super(context,attr);


        Mode=MainResource.START;
        wires=WireFactory.getInstance();
        components=ComponentFactory.getInstance();
        parent=(MakeActivity)context;
        setBackgroundColor(Color.WHITE);

        handler=new Handler();

        mScaleDetector=new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {


            float mLastPointX,mLastPointY;


            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {

                //mLastPointX=detector.getFocusX()/mScaleFactor+clipBounds_canvas.left;
                //mLastPointY=detector.getFocusY()/mScaleFactor+clipBounds_canvas.top;
                mLastPointX=detector.getFocusX();
                mLastPointY=detector.getFocusY();

                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                mScaleFactor *= detector.getScaleFactor();
                mScaleFactor = Math.max(1f, Math.min(mScaleFactor, MAXSCALE));




                final float dx=detector.getFocusX()-mLastPointX;
                final float dy=detector.getFocusY()-mLastPointY;



                   focalPointx -=dx*(MAXSCALE+1-mScaleFactor);
                    focalPointy -=dy*(MAXSCALE+1-mScaleFactor);



                checkFocalPoint();
                    invalidate();

                mLastPointX=detector.getFocusX();
                mLastPointY=detector.getFocusY();

                return true;
            }
        }

        );
        mGestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent e) {
                preMotion=MotionEvent.ACTION_DOWN;
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if(!mScaleDetector.isInProgress()) {
                    preMotion = MotionEvent.ACTION_SCROLL;


                    focalPointx += distanceX * (MAXSCALE + 1 - mScaleFactor);
                    focalPointy += distanceY * (MAXSCALE + 1 - mScaleFactor);


                    checkFocalPoint();


                    invalidate();
                    return true;
                }
                return false;

            }

            @Override
            public void onLongPress(MotionEvent e) {


                if(Mode!=MainResource.CLEAR) {
                    Mode = MainResource.CLEAR;
                    parent.invalidateOptionsMenu();
                  //  parent.add_layout.setVisibility(INVISIBLE);
                }
                }
        });







    }



    private void checkFocalPoint(){
        if (focalPointx < 0 || focalPointx > getWidth()) {

            focalPointx = focalPointx > getWidth() / 2 ? getWidth() : 0;
        }
        if (focalPointy < 0 || focalPointy > getHeight()) {

            focalPointy = focalPointy > getHeight() / 2 ? getHeight() : 0;
        }
    }

    public void onDraw(Canvas canvas){

        canvas.save();
        canvas.drawColor(Color.WHITE);


        canvas.scale(mScaleFactor,mScaleFactor,focalPointx,focalPointy);


        DrawSelectedPoint(canvas);
        DrawWire(canvas);
        DrawComponent(canvas);

        clipBounds_canvas = canvas.getClipBounds();
        canvas.restore();




    }
    public void DrawSelectedPoint(Canvas canvas){
        if(wires.getSelectedPoint()!=null)

        canvas.drawPath(wires.getSelectedPoint(),wires.getSelectedPaint());
    }
    public void DrawComponent(Canvas canvas){


        Resources res=getResources();


        int resizeWidth = 200;
        for(int i=0;i<components.size();i++) {
            Component c=(Component)components.get(i);
            Log.d("Bitmap",c.toString());
            Bitmap original= BitmapFactory.decodeResource(res,c.getImageSource());
            Log.d("Bitmap",original.toString());

            double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
            int targetHeight = (int) (resizeWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
            if (result != original) {
                original.recycle();
            }


            canvas.drawBitmap(result, null,c.getRectF(), null);

            if(c.isSelected()){

                Bitmap checkImage=BitmapFactory.decodeResource(res, R.drawable.checked);
                Bitmap reSize=Bitmap.createScaledBitmap(checkImage,(int)c.getRectF().width()/4,(int)c.getRectF().height()/4,false);
                canvas.drawBitmap(reSize, c.getRectF().left, c.getRectF().top, null);

                canvas.drawRect(c.getRectF(),components.getPaint());
            }


        }
    }
    private void DrawWire(Canvas canvas){




        for(int i=0;i< wires.size();i++){

            canvas.drawPath(wires.getWire(i), wires.getWirePaint());



        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
       switch (event.getAction()&MotionEvent.ACTION_MASK){

           case MotionEvent.ACTION_POINTER_DOWN:


               if(preMotion==MotionEvent.ACTION_DOWN) {
                   System.out.println("Action_point_DOWN");

                   preMotion=MotionEvent.ACTION_POINTER_DOWN;

               }
                   break;



           case MotionEvent.ACTION_UP:{

              if(preMotion==MotionEvent.ACTION_DOWN){


                  float touchX=event.getX()/mScaleFactor+clipBounds_canvas.left;
                float touchY=event.getY()/mScaleFactor+clipBounds_canvas.top;
                final Component temp=components.get(touchX, touchY);
                switch(Mode) {

                    case MainResource.START:
                    case MainResource.WIRE:


                        if (temp == null) {
                            break;
                        }
                        if (temp.getArray().length > 0) {  //인풋 아웃풋 가능 할때

                            this.alertDialog(temp);

                        }
                        break;


                    case MainResource.AND:
                    case MainResource.OR:
                    case MainResource.NOT:
                    case MainResource.CLOCK:
                    case MainResource.HIGH:
                    case MainResource.LOW:
                    case MainResource.OUTPUT:


                        if (temp == null) {
                            components.create(touchX, touchY, Mode);
                            invalidate();
                        } else {

                            this.alertDialog(temp);
                        }
                        break;

                    case MainResource.CLEAR://삭제모드
                        if (wires.select(touchX, touchY) || components.select(touchX, touchY)) {
                            invalidate();
                        }
                        break;
                }
                }
                    }//switch(Mode)
            }//switch(Action)





        return true;

    }

    private void alertDialog(Component c){
        final Component temp=c;
        new AlertDialog.Builder(getContext()).setTitle("선택")
                .setSingleChoiceItems(temp.getArray(), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mSelect=i;





                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String pointName=temp.getArray()[mSelect];//input 값
                wires.setValus(temp,pointName);

                //Toast.makeText(getContext(), "selected com:" + temp.getArray()[mSelect], Toast.LENGTH_LONG).show();
                mSelect=0;
                invalidate();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int i){
                mSelect=0;
            }
        }).show();

    }
    @Override
    public void run() {
            this.invalidate();
    }

    public void clearAll(){
        components.deleteAll();
        wires.deleteAll();
        invalidate();
    }
    public void clear(){
        components.delete();
        wires.delete();
        invalidate();
    }

    public void clearCancle(){

        MyCanvas.Mode=MainResource.START;
        components.reset();
        wires.reset();
        invalidate();

    }
    public void wireCreate(){
        if (wires.create()) {
            Snackbar.make(this,"와이어 생성 완료",Snackbar.LENGTH_SHORT).show();
            //Toast.makeText(getContext(), "와이어 생성 완료!", Toast.LENGTH_SHORT).show();

        } else {
            Snackbar.make(this,"Input,output 모두 선택해주세요",Snackbar.LENGTH_SHORT).show();
           // Toast.makeText(getContext(), "Input,output 모두 선택해주세요", Toast.LENGTH_SHORT).show();
        }
        invalidate();
    }


    public void execute(){

        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                while(MainResource.IsExecuted){

                    components.start();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        t.setDaemon(true);
        t.start();

    }



}

