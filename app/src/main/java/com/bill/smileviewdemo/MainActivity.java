package com.bill.smileviewdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Bill
 * <p>
 * 参考：https://github.com/zenglingchao/SmileView.git 思路特别好，手动撸一遍
 */

public class MainActivity extends AppCompatActivity implements Animator.AnimatorListener {

    private ImageView disLikeImage;
    private ImageView likeImage;
    private View likeLayout;
    private View disLikeLayout;

    private ValueAnimator animatorBack; //背景拉伸动画
    private AnimationDrawable animLike, animDis; //笑脸帧动画
    private int like = 50;
    private int disLike = 100; //点赞数,差评数
    private int type = 0; //选择执行帧动画的笑脸 //0 笑脸 1 哭脸
    private boolean isClose = false; //判断收起动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        likeLayout = findViewById(R.id.ll_like);
        disLikeLayout = findViewById(R.id.ll_dis_like);
        disLikeImage = (ImageView) findViewById(R.id.iv_dis_like);
        disLikeImage.setImageResource(R.drawable.dislike_anim_list);
        animDis = (AnimationDrawable) disLikeImage.getDrawable();
        likeImage = (ImageView) findViewById(R.id.iv_like);
        likeImage.setImageResource(R.drawable.like_anim_list);
        animLike = (AnimationDrawable) likeImage.getDrawable();
        disLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1; //设置动画对象
                disLikeLayout.setBackgroundResource(R.drawable.smile_yellow_bg);
                likeLayout.setBackgroundResource(R.drawable.smile_white_bg);
                likeImage.setImageResource(R.drawable.like_anim_list);
                animLike = (AnimationDrawable) likeImage.getDrawable();
                animBack(); //拉伸背景
            }
        });
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                likeLayout.setBackgroundResource(R.drawable.smile_yellow_bg);
                disLikeLayout.setBackgroundResource(R.drawable.smile_white_bg);
                disLikeImage.setImageResource(R.drawable.dislike_anim_list);
                animDis = (AnimationDrawable) disLikeImage.getDrawable();
                animBack();
            }
        });

    }


    //背景伸展动画
    private void animBack() {
        final int max = Math.max(like * 4, disLike * 4);
        animatorBack = ValueAnimator.ofInt(5, max);
        animatorBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int margin = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) likeImage.getLayoutParams();
                params.bottomMargin = margin;
                if (margin <= like * 4) {
                    likeImage.setLayoutParams(params);
                }
                if (margin <= disLike * 4) {
                    disLikeImage.setLayoutParams(params);
                }
            }
        });
        isClose = false;
        animatorBack.addListener(this);
        animatorBack.setDuration(500);
        animatorBack.start();
    }

    //背景收回动画
    private void setBackUp() {
        final int max = Math.max(like * 4, disLike * 4);
        animatorBack = ValueAnimator.ofInt(max, 5);
        animatorBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int margin = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) likeImage.getLayoutParams();
                params.bottomMargin = margin;

                if (margin <= like * 4) {
                    likeImage.setLayoutParams(params);
                }
                if (margin <= disLike * 4) {
                    disLikeImage.setLayoutParams(params);
                }
            }
        });
        animatorBack.addListener(this);
        animatorBack.setDuration(500);
        animatorBack.start();
    }

    private void objectY(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -10.0f, 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(1500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setBackUp(); //执行回弹动画
            }
        });
    }

    private void objectX(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -10.0f, 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(1500);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setBackUp(); //执行回弹动画
            }
        });
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //关闭时不执行帧动画
        if (isClose) {
            return;
        }
        isClose = true;

        if (type == 0) {
            animLike.start();
            objectY(likeImage);
        } else {
            animDis.start();
            objectX(disLikeImage);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
