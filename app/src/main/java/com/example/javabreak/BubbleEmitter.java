package com.example.javabreak;

import android.os.Handler;
import android.view.Window;

import org.firezenk.bubbleemitter.BubbleEmitterView;

import java.util.Random;

public class BubbleEmitter {
    MainActivity mainActivity;
    BubbleEmitterView bubbleEmitterObject, bubbleEmitterObjectUp, BubbleEmitterObjectRight, BubbleEmitterObjectLeft;

    Random random = new Random();
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            emitBubbles();
            handler.postDelayed(this,1);
        }
    };

    private void emitBubbles() {
        bubbleEmitterObject= mainActivity.findViewById(R.id.bubbleEmitter);
        bubbleEmitterObjectUp = mainActivity.findViewById(R.id.bubbleEmitterUp);
        BubbleEmitterObjectLeft= mainActivity.findViewById(R.id.bubbleEmitterLeft);
        BubbleEmitterObjectRight = mainActivity.findViewById(R.id.bubbleEmitterRight);
        bubbleEmitterObjectUp.canExplode(false);
        bubbleEmitterObject.canExplode(false);
        BubbleEmitterObjectLeft.canExplode(false);
        BubbleEmitterObjectRight.canExplode(false);
        bubbleEmitterObjectUp.setColorResources(R.color.grey, R.color.grey,R.color.grey);
        bubbleEmitterObject.setColorResources(R.color.grey, R.color.grey,R.color.grey);
        BubbleEmitterObjectLeft.setColorResources(R.color.grey, R.color.grey,R.color.grey);
        BubbleEmitterObjectRight.setColorResources(R.color.grey, R.color.grey,R.color.grey);


        bubbleEmitterObjectUp.emitBubble(random.nextInt(60));
        bubbleEmitterObject.emitBubble(random.nextInt(60));
        BubbleEmitterObjectLeft.emitBubble(random.nextInt(60));
        BubbleEmitterObjectRight.emitBubble(random.nextInt(60));
    }
}
