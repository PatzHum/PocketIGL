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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import me.phum.pocketigl.components.HotspotButton;

public class MapActivity extends AppCompatActivity {

    private float[] lastLongTouchXY = new float[2];
    private FrameLayout constraintLayout;
    private Context context;
    private ArrayList<Pair<Float, Float>> drawBuffer = new ArrayList<>();

    Bitmap bitmap = Bitmap.createBitmap(5000, 5000, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    String sessionId;

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            HotspotButton b = new HotspotButton(context);
            b.setAvailableActions(HotspotButton.Companion.getHOTSPOTS_DEFAULT());
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = (int) e.getX() - 20;
            layoutParams.topMargin = (int) e.getY() - 20;
            constraintLayout.addView(b, layoutParams);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionId = getIntent().getStringExtra("SESSION_ID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;
        constraintLayout = (FrameLayout) findViewById(R.id.mainMap);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        bitmap.setWidth(displayMetrics.widthPixels);
        bitmap.setHeight(displayMetrics.heightPixels);
        canvas.setBitmap(bitmap);

        canvas.translate(displayMetrics.widthPixels*0.008f, displayMetrics.heightPixels*0.008f);

        final ImageView mainMap = (ImageView) findViewById(R.id.mainMapImg);
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
                //float x = lastLongTouchXY[0];
                //float y = lastLongTouchXY[1];
/*

                ImageView playerNode = new ImageView(context);
*/
/*                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playerNode.getLayoutParams();
                layoutParams.setMargins();*//*

                playerNode.setMaxHeight(20);
                playerNode.setMaxWidth(20);
                playerNode.setImageResource(R.drawable.terrorist_face);
                playerNode.setX(x);
                playerNode.setY(y);

                constraintLayout.addView(playerNode);
                Toast.makeText(getApplicationContext(), "The image view has been added to" + x + " " + y, Toast.LENGTH_SHORT).show();
*/

                Toast.makeText(context, "Test 1", Toast.LENGTH_SHORT).show();
                HotspotButton hotspotButton = new HotspotButton(context);
                //constraintLayout.addView(hotspotButton);
                Toast.makeText(context, "The hotspot button has been added", Toast.LENGTH_SHORT).show();

                return true;
            }
        };





        View.OnTouchListener onTouchListener = new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    Pair<Float, Float> p = debounceDrawBuffer(motionEvent.getX(), motionEvent.getY(), displayMetrics.widthPixels, displayMetrics.heightPixels);

                    if(p != null) {
                        ImageView  i = findViewById(R.id.imageView);
                        i.setImageBitmap(bitmap);
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.RED);
                        canvas.drawCircle(p.first*displayMetrics.widthPixels, p.second*displayMetrics.heightPixels, 4, paint);
                    }
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference("pocketigl").child("sessions").child(sessionId).child("canvas").push();
                    sessionsRef.setValue(drawBuffer);
                    drawBuffer.clear();
                }

                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        };

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pocketigl").child("sessions").child(sessionId).child("canvas");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot u: dataSnapshot.getChildren()) {
                    for(DataSnapshot p: u.getChildren()) {
                        float first = Float.valueOf(String.valueOf(p.child("first").getValue()));
                        float second = Float.valueOf(String.valueOf(p.child("second").getValue()));
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.RED);
                        canvas.drawCircle(first*displayMetrics.widthPixels, second*displayMetrics.heightPixels, 4, paint);
                    }
                }
                findViewById(R.id.imageView).invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //todo: not necessary
            }
        });

        mainMap.setOnLongClickListener(longClickListener);
        //mainMap.setOnTouchListener(onLongTouch);
        mainMap.setOnTouchListener(onTouchListener);

    }

    Pair<Float, Float> debounceDrawBuffer(float newX, float newY, float width, float height) {
        if(drawBuffer.isEmpty()) {
            drawBuffer.add(new Pair(newX/width, newY/height));
        } else {
            float lastX = drawBuffer.get(drawBuffer.size() - 1).first * width;
            float lastY = drawBuffer.get(drawBuffer.size() - 1).second * height;
            float d = (float)Math.sqrt(Math.pow((newX - lastX), 2) + Math.pow((newY - lastY), 2));

            if(d > 3) {
                drawBuffer.add(new Pair(newX/width, newY/height));
                //Log.d("x,y: ", newX + " " + newY);

                return new Pair(newX/width, newY/height);
            }
        }

        return null;
    }
}
