package com.example.tesseract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	final static String TAG = "Tesseract";
	private byte[] mContent;


	private static final int PHOTO_CAPTURE = 0x11;// 拍照
	private static final int PHOTO_SELECT = 0x12;// 拍照
	private static final int PHOTO_RESULT = 0x13;// 结果

	private static String LANGUAGE = "eng";
	private static String IMAGE_PATH = "/sdcard/images/";
//	private static String IMG_PATH = getSDPath() + java.io.File.separator
//			+ "ocrtest";

	private static TextView tvResult;
	private static ImageView ivSelected;
	private static ImageView ivTreated;
	private static Button btnCamera;
	private static Button btnSelect;
	private static CheckBox chPreTreat;
	private static RadioGroup radioGroup;
	private static String textResult;
	private static Bitmap bitmapSelected;
	private static Bitmap bitmapTreated;
	private static final int SHOWRESULT = 0x101;
	private static final int SHOWTREATEDIMG = 0x102;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvResult = (TextView) findViewById(R.id.tv_result);
		ivSelected = (ImageView) findViewById(R.id.iv_selected);
		ivTreated = (ImageView) findViewById(R.id.iv_treated);
		btnCamera = (Button) findViewById(R.id.btn_camera);
		btnSelect = (Button) findViewById(R.id.btn_select);
		chPreTreat = (CheckBox) findViewById(R.id.ch_pretreat);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

		btnSelect.setOnClickListener(new selectButtonListener());
		btnCamera.setOnClickListener(new cameraButtonListener());
	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == PHOTO_SELECT) {
				Uri uri = data.getData();
				ivSelected.setImageURI(uri);
			}
			
			if (requestCode == PHOTO_CAPTURE) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				FileOutputStream b = null;
//				File file = new File(IMAGE_PATH);
//				file.mkdirs();// 创建文件夹，名称为myimage

				// 照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。
				String str = null;
				Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
				date = new Date();
				str = format.format(date);
				String fileName = IMAGE_PATH + str + ".jpg";
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (data != null) {
						Bitmap cameraBitmap = (Bitmap) data.getExtras().get(
								"data");
						System.out.println("fdf================="
								+ data.getDataString());
						ivSelected.setImageBitmap(cameraBitmap);

						System.out.println("成功======" + cameraBitmap.getWidth()
								+ cameraBitmap.getHeight());
					}

				}
			}
		}
	
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class selectButtonListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, PHOTO_SELECT);
		}
	}
	
	class cameraButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent, PHOTO_CAPTURE);
		}
	}

}
