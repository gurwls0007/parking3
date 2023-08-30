package com.example.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button button_location1, button_location2;      // 위치를 나타내는 버튼 2개 정의
    static int receive_module_state1 = 0 ;
    static int receive_module_state2 = 0;
    static int mod_1;
    static int mod_2;
    private DatabaseReference database;
    private Handler handler = new Handler();
    private Runnable runnable;   // 반복문 정의
    private final int DELAY = 0; // 딜레이 시간 (0초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_location1 = findViewById(R.id.btn_1);  // 버튼 연결
        button_location2 = findViewById(R.id.btn_2);
        startRepeatedTask();                          // 반복문 실행
    }
    private void startRepeatedTask() {
        runnable = new Runnable() {
            @Override
            public void run() {
                database = FirebaseDatabase.getInstance().getReference();  // 파이어베이스에서 데이터 참조
                database.child("state_1").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer state_1 = dataSnapshot.getValue(Integer.class); // state 값을 파이어베이스 서버에서 가져옴
                        receive_module_state1 = state_1;                        // if문에 사용할 상태변수에 대입
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {      // 애러 발생시 처리할 방식
                    }
                });

                database.child("state_2").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer state_2 = dataSnapshot.getValue(Integer.class);
                        receive_module_state2 = state_2;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                database.child("mod_1").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {      // mod 값을 파이어베이스 서버에서 가져옴
                        Integer mod1 = dataSnapshot.getValue(Integer.class);
                        mod_1 = mod1;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                database.child("mod_2").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer mod2 = dataSnapshot.getValue(Integer.class);
                        mod_2 = mod2;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                if (receive_module_state1 == 0) {          // 받아온 값이 0이면 회색버튼
                    button_location1.setBackgroundColor(Color.parseColor("#D3D3D3"));
                } else if (receive_module_state1 == 1) {   // 받아온 값이 1이면 1번 모듈 활성화
                    if (mod_1 == 0) {                        // 1번 모듈이 0또는 1일때 각각의 버튼 색상 반환
                        button_location1.setBackgroundColor(Color.GREEN);
                    } else if (mod_1 == 1) {
                        button_location1.setBackgroundColor(Color.RED);
                    }
                } else if (receive_module_state1 == 2) {   // 받아온 값이 2이면 2번 모듈 활성화
                    if (mod_2 == 0) {
                        button_location1.setBackgroundColor(Color.GREEN);
                    } else if (mod_2 == 1) {
                        button_location1.setBackgroundColor(Color.RED);
                    }
                }
                if (receive_module_state2 == 0) {
                    button_location2.setBackgroundColor(Color.parseColor("#D3D3D3"));
                } else if (receive_module_state2 == 1) {
                    if (mod_1 == 0) {
                        button_location2.setBackgroundColor(Color.GREEN);
                    } else if (mod_1 == 1) {
                        button_location2.setBackgroundColor(Color.RED);
                    }
                } else if (receive_module_state2 == 2) {
                    if (mod_2 == 0) {
                        button_location2.setBackgroundColor(Color.GREEN);
                    } else if (mod_2 == 1) {
                        button_location2.setBackgroundColor(Color.RED);
                    }
                }
                button_location1.setOnClickListener(new View.OnClickListener() {  // 1번위치 버튼 클릭시
                    public void onClick(View view) {
                        showCustomAlertDialog1();  // 팝업창을 생성

                    }
                });
                button_location2.setOnClickListener(new View.OnClickListener() {  // 2번위치 버튼 클릭시
                    public void onClick(View view) {
                        showCustomAlertDialog2();  // 팝업창을 생성

                    }
                });

                handler.postDelayed(this, DELAY);  // 다음 반복을 위한 딜레이 후 실행
            }
        };

        handler.postDelayed(runnable, DELAY);  // 딜레이 후 첫 반복 실행
    }

    private void showCustomAlertDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("주차허용시간\n토, 일, 공휴일\n06:00 - 22:00");

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                AlertDialog alertDialog = (AlertDialog) dialog;
                if (alertDialog != null) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);  // 팝업창 색상
                    WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                    layoutParams.dimAmount = 0f; // 팝업창 밝기
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    alertDialog.getWindow().setAttributes(layoutParams);

                    TextView messageTextView = alertDialog.findViewById(android.R.id.message);
                    if (messageTextView != null) {
                        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40); // 글자 크기
                        messageTextView.setGravity(Gravity.CENTER); // 글자 가운데 정렬
                    }
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 팝업창 닫음
            }
        });

        alertDialog.show();
    }
    private void showCustomAlertDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("주차허용시간\n토, 일, 공휴일\n10:00 - 17:00");

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                AlertDialog alertDialog = (AlertDialog) dialog;
                if (alertDialog != null) {
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                    alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);  // 팝업창 색상
                    WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                    layoutParams.dimAmount = 0f; // 팝업창 밝기
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    alertDialog.getWindow().setAttributes(layoutParams);

                    TextView messageTextView = alertDialog.findViewById(android.R.id.message);
                    if (messageTextView != null) {
                        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40); // 글자 크기
                        messageTextView.setGravity(Gravity.CENTER); // 글자 가운데 정렬
                    }
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 팝업창 닫음
            }
        });

        alertDialog.show();
    }
}