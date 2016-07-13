package info.devexchanges.qrcodegenerator;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editText;
    private ImageView imageView;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmapResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.QR_Image);
        editText = (EditText) findViewById(R.id.edt_value);
        Button btnStart = (Button) findViewById(R.id.start);
        final Button btnSave = (Button) findViewById(R.id.save);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().length() > 0) {

                    //calculating bitmap dimension
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(editText.getText().toString().trim(), null, QRGContents.Type.TEXT, smallerDimension);
                    try {
                        bitmapResult = qrgEncoder.encodeAsBitmap();
                        imageView.setImageBitmap(bitmapResult);
                        btnSave.setVisibility(View.VISIBLE);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                    editText.setError("Enter some text");
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;
                try {
                    String savePath = Environment.getExternalStorageDirectory() + "/QRCode/";
                    save = QRGSaver.save(savePath, "WebSite QR code", bitmapResult, QRGContents.ImageType.IMAGE_JPEG);
                    result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}