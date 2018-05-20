package me.phum.pocketigl;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity {

    private float[] lastLongTouchXY = new float[2];
    private ConstraintLayout constraintLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;
        constraintLayout = (ConstraintLayout) findViewById(R.id.mainMap);

        TouchImageView mainMap = (TouchImageView) findViewById(R.id.mainMapImg);
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
        mainMap.setOnLongClickListener(longClickListener);
        mainMap.setOnTouchListener(onLongTouch);
    }
}
