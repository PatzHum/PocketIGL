package me.phum.pocketigl;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private float[] lastLongTouchXY = new float[2];
    private ConstraintLayout constraintLayout;
    private Context context;
    private ArrayList<Pair<Float, Float>> drawBuffer = new ArrayList<>();

    Bitmap bitmap = Bitmap.createBitmap(5000, 5000, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;
        constraintLayout = (ConstraintLayout) findViewById(R.id.mainMap);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        bitmap.setWidth(displayMetrics.widthPixels);
        bitmap.setHeight(displayMetrics.heightPixels);
        canvas.setBitmap(bitmap);

        canvas.translate(displayMetrics.widthPixels*0.008f, displayMetrics.heightPixels*0.008f);

        final TouchImageView mainMap = (TouchImageView) findViewById(R.id.mainMapImg);
        mainMap.setImageResource(R.drawable.de_mirage);

        View.OnTouchListener onLongTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
                    lastLongTouchXY[0] = motionEvent.getX();
                    lastLongTouchXY[1] = motionEvent.getY();
                    Log.d("asdasd", "The x coord is: " + lastLongTouchXY[0]);
                    Log.d("asdasd", "The y coord is: " + lastLongTouchXY[1]);
                }
                return false;
            }
        };

        View.OnLongClickListener longClickListener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                float x = lastLongTouchXY[0];
                float y = lastLongTouchXY[1];

                ImageView playerNode = new ImageView(context);
/*                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playerNode.getLayoutParams();
                layoutParams.setMargins();*/
                playerNode.setMaxHeight(20);
                playerNode.setMaxWidth(20);
                playerNode.setImageResource(R.drawable.terrorist_face);
                playerNode.setX(x);
                playerNode.setY(y);

                constraintLayout.addView(playerNode);
                Toast.makeText(getApplicationContext(), "The image view has been added to" + x + " " + y, Toast.LENGTH_SHORT).show();

                return true;
            }
        };

        View.OnTouchListener onTouchListener = new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Pair<Float, Float> p = debounceDrawBuffer(motionEvent.getX(), motionEvent.getY());

                    if(p != null) {
                        ImageView  i = findViewById(R.id.imageView);
                        i.setImageBitmap(bitmap);
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.RED);
                        canvas.drawCircle(p.first, p.second, 4, paint);
                    }
                }
                return true;
            }
        };

        //mainMap.setOnLongClickListener(longClickListener);
        //mainMap.setOnTouchListener(onLongTouch);
        mainMap.setOnTouchListener(onTouchListener);


    }

    Pair<Float, Float> debounceDrawBuffer(float newX, float newY) {
        if(drawBuffer.isEmpty()) {
            drawBuffer.add(new Pair(newX, newY));
        } else {
            float lastX = drawBuffer.get(drawBuffer.size() - 1).first;
            float lastY = drawBuffer.get(drawBuffer.size() - 1).second;
            float d = (float)Math.sqrt(Math.pow((newX - lastX), 2) + Math.pow((newY - lastY), 2));

            if(d > 3) {
                drawBuffer.add(new Pair(newX, newY));
                Log.d("x,y: ", newX + " " + newY);

                return new Pair(newX, newY);
            }
        }

        return null;
    }
}
