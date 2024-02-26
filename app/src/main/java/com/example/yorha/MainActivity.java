package com.example.yorha;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView portrait;
    TextView descT;
    TextView nameT;
    Button btnAceptar;
    Button btnCambiar;
    public int personaje = 0;
    MediaPlayer musica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musica = MediaPlayer.create(this,R.raw.peaceful_sleep);
        musica.start();

        portrait = findViewById(R.id.imageView);
        descT = findViewById(R.id.textViewDesc);
        nameT = findViewById(R.id.textName);
        btnAceptar = findViewById(R.id.buttonA);
        btnCambiar = findViewById(R.id.buttonC);

        String[] descriptions = getResources().getStringArray(R.array.desc);
        descT.setText(descriptions[personaje]);
        btnAceptar.setBackgroundColor(getResources().getColor(R.color.black));
        btnCambiar.setBackgroundColor(getResources().getColor(R.color.black));

        portraitAnimation();


        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personaje == 0) {
                    personaje = 1;
                    portrait.setImageResource(R.drawable.portrait_9s);
                    portraitAnimation();
                    descT.setText(descriptions[personaje]);
                    nameT.setText("9S");
                } else {
                    personaje = 0;
                    portrait.setImageResource(R.drawable.portrait_2b);
                    portraitAnimation();
                    descT.setText(descriptions[personaje]);
                    nameT.setText("2B");
                }
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musica.release();
                Intent juego = new Intent(getApplication(),GameActivity.class);
                juego.putExtra("Personaje",personaje);
                startActivity(juego);
            }
        });
    }

    private void portraitAnimation() {
        AnimatorSet animator = new AnimatorSet();
        ObjectAnimator fadein = ObjectAnimator.ofFloat(portrait,"alpha",0f,1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(portrait,"scaleX",0.75f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(portrait,"scaleY",0.75f,1f);
        fadein.setDuration(1000);
        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        animator.play(fadein).with(scaleX).with(scaleY);
        animator.start();
    }
}