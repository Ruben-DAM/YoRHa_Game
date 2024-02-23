package com.example.yorha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView portrait;
    TextView descT;
    TextView nameT;
    public int personaje = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        portrait = findViewById(R.id.imageView);
        descT = findViewById(R.id.textViewDesc);
        nameT = findViewById(R.id.textName);
        Button btnAceptar = findViewById(R.id.buttonA);
        Button btnCambiar = findViewById(R.id.buttonC);

        String[] descriptions = getResources().getStringArray(R.array.desc);
        descT.setText(descriptions[personaje]);
        btnAceptar.setBackgroundColor(getResources().getColor(R.color.black));
        btnCambiar.setBackgroundColor(getResources().getColor(R.color.black));

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personaje == 0) {
                    personaje = 1;
                    portrait.setImageResource(R.drawable.portrait_9s);
                    descT.setText(descriptions[personaje]);
                    nameT.setText("9S");
                } else {
                    personaje = 0;
                    portrait.setImageResource(R.drawable.portrait_2b);
                    descT.setText(descriptions[personaje]);
                    nameT.setText("2B");
                }
            }
        });
    }
}