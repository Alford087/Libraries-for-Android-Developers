package com.cn.activityresult.demo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class PicCutDemoActivity extends Activity implements OnClickListener {
	public void b(){
		BundleContextFactory.getInstance();
	}

 private ImageButton ib = null;
 private ImageView iv = null;
 private Button bt = null;
 private String tp = null;

 /** Called when the activity is first created. */
 @SuppressLint("ParserError")

 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.contact_pic);
 System.out.println("getParent:"+this.getParent());
 // ��ʼ��  
 init();

 
 }



 /**
 * ��ʼ������ʵ��
 */
 private void init() {
 bt = (Button) findViewById(R.id.bt);
 ib = (ImageButton) findViewById(R.id.ib);
 iv = (ImageView) findViewById(R.id.iv);

 bt.setOnClickListener(this);
 ib.setOnClickListener(this);
 iv.setOnClickListener(this);
 
 }


 /**
 * �ؼ�����¼�ʵ�� ��Ϊ�������ʲ�ͬ�ؼ��ı���ͼ�ü���ôʵ�֣� �Ҿ�������ط����������ؼ���ֻΪ���Լ���¼ѧϰ ��Ҿ���û�õĿ���������
 */
 public void onClick(View v) {
 switch (v.getId()) {
 case R.id.bt:
 ShowPickDialog();
 break;
 case R.id.ib:
 ShowPickDialog();
 break;
 case R.id.iv:
 ShowPickDialog();
 break;


 default:
 break;
 }
 }


 /**
 * ѡ����ʾ�Ի���
 */
 private void ShowPickDialog() {
 new AlertDialog.Builder(this)
 .setTitle("����ͷ��...")
 .setNegativeButton("���", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int which) {
 dialog.dismiss();
 /**
 * �տ�ʼ�����Լ�Ҳ��֪��ACTION_PICK�Ǹ���ģ�����ֱ�ӿ�IntentԴ�룬
 * ���Է�������ܶණ����Intent�Ǹ���ǿ��Ķ��������һ����ϸ�Ķ���
 */
 Intent intent = new Intent(Intent.ACTION_PICK, null);


 /**
 * ������仰����������ʽд��һ����Ч���������
 * intent.setData(MediaStore.Images
 * .Media.EXTERNAL_CONTENT_URI);
 * intent.setType(""image/*");������������
 * ���������Ҫ�����ϴ�����������ͼƬ����ʱ����ֱ��д��
 * ��"image/jpeg �� image/png�ȵ�����"
 */
 intent.setDataAndType(
 MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
 "image/*");
 startActivityForResult(intent, 1);
 }
 })
 .setPositiveButton("����", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int whichButton) {
 dialog.dismiss();
 /**
 * ������仹�������ӣ����ÿ������չ��ܣ�����Ϊʲô�п������գ���ҿ��Բο����¹ٷ�
 * �ĵ���you_sdk_path/docs/guide/topics/media/camera.html
 * �Ҹտ���ʱ����Ϊ̫�������濴����ʵ�Ǵ�ģ�����������õ�̫���ˣ����Դ�Ҳ�Ҫ��Ϊ
 * �ٷ��ĵ�̫���˾Ͳ����ˣ���ʵ�Ǵ��
 */
 Intent intent = new Intent(
 MediaStore.ACTION_IMAGE_CAPTURE);
 // �������ָ������������պ����Ƭ�洢��·��
 intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
 .fromFile(new File(Environment
 .getExternalStorageDirectory(),
 "superspace.jpg")));
 startActivityForResult(intent, 2);
 }
 }).show();
 }


 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 switch (requestCode) {
 // �����ֱ�Ӵ�����ȡ
 case 1:
 startPhotoZoom(data.getData());
 break;
 // ����ǵ����������ʱ
 case 2:
 File temp = new File(Environment.getExternalStorageDirectory()
 + "/superspace.jpg");
 startPhotoZoom(Uri.fromFile(temp));
 break;
 // ȡ�òü����ͼƬ
 case 3:
 /**
 * �ǿ��жϴ��һ��Ҫ��֤���������֤�Ļ��� �ڼ���֮��������ֲ����⣬Ҫ���²ü�������
 * ��ǰ����ʱ���ᱨNullException��ֻ ������ط����£���ҿ��Ը��ݲ�ͬ����ں��ʵ� �ط����жϴ����������
 *&nbsp;
 */
 if (data != null) {
 setPicToView(data);
 }
 break;
 default:
 break;


 }
 super.onActivityResult(requestCode, resultCode, data);
 }


 /**
 * �ü�ͼƬ����ʵ��
 *&nbsp;
 * @param uri
 */


 public void startPhotoZoom(Uri uri) {
 /*
 * �����������Intent��ACTION����ô֪���ģ���ҿ��Կ����Լ�·���µ�������ҳ
 * yourself_sdk_path/docs/reference/android/content/Intent.html
 * ֱ��������Ctrl+F�ѣ�CROP ��֮ǰû��ϸ��������ʵ��׿ϵͳ���Ѿ����Դ�ͼƬ�ü�����, ��ֱ�ӵ����ؿ��
 */
 Intent intent = new Intent("com.android.camera.action.CROP");
 intent.setDataAndType(uri, "image/*");
 // �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
 intent.putExtra("crop", "true");
 // aspectX aspectY �ǿ�ߵı���
 intent.putExtra("aspectX", 1);
 intent.putExtra("aspectY", 1);
 // outputX outputY �ǲü�ͼƬ���
 intent.putExtra("outputX", 80);
 intent.putExtra("outputY", 80);
 intent.putExtra("return-data", true);
 startActivityForResult(intent, 3);
 }


 /**
 * ����ü�֮���ͼƬ����
 *&nbsp;
 * @param picdata
 */
 private void setPicToView(Intent picdata) {
 Bundle extras = picdata.getExtras();
 if (extras != null) {
 Bitmap photo = extras.getParcelable("data");
 Drawable drawable = new BitmapDrawable(photo);


 /**
 * ����ע�͵ķ����ǽ��ü�֮���ͼƬ��Base64Coder���ַ���ʽ�� ������������QQͷ���ϴ����õķ������������
 */
 ByteArrayOutputStream stream = new ByteArrayOutputStream();
 photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); 
 byte[] b= stream.toByteArray(); 
 
 
 /*
 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
 * = stream.toByteArray(); // ��ͼƬ�����ַ�����ʽ�洢����
 *&nbsp;
 * tp = new String(Base64Coder.encodeLines(b));
 * ����ط���ҿ���д�¸��������ϴ�ͼƬ��ʵ�֣�ֱ�Ӱ�tpֱ���ϴ��Ϳ����ˣ� ����������ķ����Ƿ������Ǳߵ����ˣ����
 *&nbsp;
 * ������ص��ķ����������ݻ�����Base64Coder����ʽ�Ļ������������·�ʽת�� Ϊ���ǿ����õ�ͼƬ���;�OK��...���
 * Bitmap dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable
 * = new BitmapDrawable(dBitmap);
 */
 ib.setBackgroundDrawable(drawable);
 iv.setBackgroundDrawable(drawable);
 }
 }
}