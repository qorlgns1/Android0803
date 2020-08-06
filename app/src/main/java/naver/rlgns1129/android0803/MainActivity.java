package naver.rlgns1129.android0803;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //View의 참조를 저장할 인스턴스 변수
    TextView resultView;
    Button btn_contact , btn_camera, btn_voice, btn_map, btn_browser , btn_call;
    ImageView resultImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View의 참조를 대입
        resultView = (TextView)findViewById(R.id.resultView);

        btn_contact = (Button)findViewById(R.id.btn_contacts);
        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_voice = (Button)findViewById(R.id.btn_voice);
        btn_map = (Button)findViewById(R.id.btn_map);
        btn_browser = (Button)findViewById(R.id.btn_browser);
        btn_call = (Button)findViewById(R.id.btn_call);

        resultImageView = (ImageView)findViewById(R.id.resultImageView);

        //클릭 이벤트 처리 - 이벤트 라우팅
        //동일 한 객체를 여러번 처리
        View.OnClickListener listener = new View.OnClickListener(){
          @Override
          public void onClick(View view){
              //스위치 안에서 intent를 사용하려면
              //스위치 안에 인스턴스를 계속 만들지말고
              //스위치 바깥에 인텐트 인스턴스를 만들고 사용.
              Intent intent = null;
              switch(view.getId()){
                  case R.id.btn_contacts:
                      //주소록 앱 실행
                      intent = new Intent(Intent.ACTION_PICK);
                      intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                      //여러 개의 Activity를 실행하거나
                      //하위 Activity로부터 데이터를 넘겨받고자 하는 경우에는
                      //아래 메소드를 Activity를 호출
                      //번호는 구분을 하기 위한 것이므로 임의로 배정해도 되지만
                      //중복되면 안됩니다.
                      startActivityForResult(intent, 10);
                      break;
                  case R.id.btn_camera:
                      intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                      startActivityForResult(intent, 20);
                      break;
                  case R.id.btn_browser:
                      intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://seoul.go.kr"));
                      startActivityForResult(intent, 30);
                      break;
                  case R.id.btn_map:
                      //URL & Uri
                      //URL은 인터넷 상에서의 자원의 위치
                      //Uri는 모든 자원의 위치
                      //Uri가 URl보다는 큰 개념
                      //URL을 요청하면 웹 주소를 대입하는 것이고 Uri를 요청하면 웹 주소가 아닐 수 도 있음
                      intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5666, 126.9779"));
                      startActivityForResult(intent, 30);
                      break;
                  case R.id.btn_call:
                      //권한 설정 여부를 확인
                      if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                          //권한이 있는 경우
                          intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-2006-6576"));
                          startActivityForResult(intent, 50);
                      }else{
                          //권한이 없는 경우 - 권한을 다시 요청
                          ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                      }
                      break;
                  case R.id.btn_voice:

                      break;
              }
          }

        };


        //이벤트 핸들러 연결
        btn_contact.setOnClickListener(listener);
        btn_camera.setOnClickListener(listener);
        btn_voice.setOnClickListener(listener);
        btn_map.setOnClickListener(listener);
        btn_browser.setOnClickListener(listener);
        btn_call.setOnClickListener(listener);

        resultImageView.setOnClickListener(listener);


    }

    //startActivityForResult로 하위 Activity를 출력 한 경우
    //하위 Activity가 소멸되면 호출되는 메소드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //상위 클래스의 메소드 호출
        //파괴하는 메소드가 아니면 상위 클래스의 메소드를 먼저 호출하는 것이 일반적입니다.
        super.onActivityResult(requestCode, resultCode, data);


        //여러 개의 Activity를 호출한 경우는 requestCode로 구분
        //만약 이렇게 지정하지 않으면 카메라앱 실행하고 돌아와서 주소록 앱을 실행하면 터져버린다.
        if(requestCode == 20) {
            //카메라는 촬영한 사진을 data라는 이름으로
            //BitMap 타입으로 넘겨줍니다.
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //찾아온 데이터를 이미지뷰에 넘겨주기
            resultImageView.setImageBitmap(bitmap);
        }
    };


}