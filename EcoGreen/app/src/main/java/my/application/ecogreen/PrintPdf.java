package my.application.ecogreen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import my.application.ecogreen.datas.PdfItem;

public class PrintPdf extends Activity {
    String price, selectedDate, address, requestDate;
    ArrayList<PdfItem> mList;
    int pageWidth = 1200;
    Date dateObj;
    DateFormat dateFormat;

    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.print_pdf);

        Intent intent = getIntent();
        price = intent.getStringExtra("priceTotal");
        selectedDate = intent.getStringExtra("selectedDate");
        address = intent.getStringExtra("addressFinal");
        requestDate = intent.getStringExtra("requestDate");
        mList = (ArrayList<PdfItem>) intent.getSerializableExtra("mList");

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    public void onClickDialog(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                createPDF();
                break;
            case R.id.btn_cancel:
                this.finish();
                break;
        }
        finish();
    }

    private void createPDF() {
        EditText emailAddr = findViewById(R.id.eamil_addr);
        dateObj = new Date();

        if (price.length() == 0 || selectedDate.length() == 0 || mList.size() == 0) {
            Toast.makeText(PrintPdf.this, "값이 전달되지 않음!", Toast.LENGTH_SHORT).show();
        } else {
            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();
            Paint titlePaint = new Paint();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2000, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(70);
            canvas.drawText("대형폐기물 배출 신고필증", pageWidth / 2, 180, titlePaint);
            canvas.drawLine(pageWidth / 2 - 350, 200, pageWidth / 2 + 350, 200, paint);

            paint.setTextSize(20f);
            paint.setTextAlign(Paint.Align.LEFT);
            dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            canvas.drawText("No." + dateFormat.format(dateObj), 20, 40, paint);

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(30f);

            int index = address.indexOf("구");
            canvas.drawText("이 대형생활 폐기물은 " + address.substring(0, index + 1) + " 폐기물 관리 조례에 의거 신고된 물품입니다.", pageWidth / 2, 310, titlePaint);

            titlePaint.setTextSize(28f);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            titlePaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("※ 유의사항", 55, 420, titlePaint);
            canvas.drawText("1. 대형생활폐기물을 배출시에는 품목별 수수료에 해당하는 신고필증을 구입 부착하여 배출하셔야", 55, 460, titlePaint);
            canvas.drawText("수거합니다.", 80, 500, titlePaint);
            canvas.drawText("2. 대형생활폐기물 환불 신청은 동주민센터 방문접수인 경우 당일 취소분에 한하여 바로 환불이", 55, 540, titlePaint);
            canvas.drawText("가능하나 신청일이 지난 경우는 신청 접수일로 부터 9일 이내에 환불 요청하셔야 환불이 가능합니다.", 80, 580, titlePaint);

            paint.setTextSize(25f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.drawRect(20, 650, pageWidth - 20, 810, paint);
            canvas.drawLine(20, 730, pageWidth - 20, 730, paint);

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("전화번호", 40, 700, paint);
            canvas.drawText("주소", 40, 780, paint);
            canvas.drawLine(250, 650, 250, 810, paint);

            String phoneNum = auth.getCurrentUser().getPhoneNumber();
            phoneNum = "0" + phoneNum.substring(3);
            canvas.drawText(phoneNum, 300, 700, paint);

            canvas.drawText(address, 300, 780, paint);

            paint.setTextSize(30f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.drawRect(20, 850, pageWidth - 20, 930, paint);

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("번호", 40, 900, paint);
            canvas.drawText("품목", 160, 900, paint);
            canvas.drawText("규격", 405, 900, paint);
            canvas.drawText("개당", 750, 900, paint);
            canvas.drawText("수량", 900, 900, paint);
            canvas.drawText("금액", 1050, 900, paint);

            canvas.drawLine(140, 860, 140, 910, paint);
            canvas.drawLine(385, 860, 385, 910, paint);
            canvas.drawLine(730, 860, 730, 910, paint);
            canvas.drawLine(880, 860, 880, 910, paint);
            canvas.drawLine(1030, 860, 1030, 910, paint);

            //리스트 표
            String standard = null;
            int i;
            for (i = 0; i < mList.size(); i++) {
                long totalEach = 0;

                canvas.drawText((i + 1) + ".", 40, 1020 + 100 * i, paint);
                canvas.drawText(mList.get(i).getItem(), 160, 1020 + 100 * i, paint);
                standard = mList.get(i).getStandard();
                if (standard == null) {
                    standard = " ";
                }
                canvas.drawText(standard, 405, 1020 + 100 * i, paint);
                canvas.drawText(mList.get(i).getLevy_amt().toString(), 750, 1020 + 100 * i, paint);
                canvas.drawText(mList.get(i).getCount().toString(), 900, 1020 + 100 * i, paint);
                totalEach = mList.get(i).getCount() * mList.get(i).getLevy_amt();
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.valueOf(totalEach), pageWidth - 40, 1020 + 100 * i, paint);
                paint.setTextAlign(Paint.Align.LEFT);
            }

            canvas.drawLine(680, 1020 + 100 * i, pageWidth - 20, 1020 + 100 * i, paint);
            canvas.drawText("수수료 총액", 700, 1070 + 100 * i, paint);
            canvas.drawText(":", 900, 1070 + 100 * i, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(price, pageWidth - 40, 1070 + 100 * i, paint);

            paint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            //canvas.drawText("배출예정일 : "+selectedDate, pageWidth/2, 1270+100*i,paint);
            canvas.drawText("배출예정일 : " + selectedDate, pageWidth / 2, 1800, paint);

            pdfDocument.finishPage(page);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/Baro/" + "report_" + dateFormat.format(dateObj) + ".pdf");
            if (!file.isDirectory()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(this, Environment.DIRECTORY_DOCUMENTS + "/Baro/에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = emailAddr.getText().toString();
            if(str.length() != 0){
                final Uri fileUri = FileProvider.getUriForFile(this, "my.application.ecogreen.provider", file);

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                // email setting 배열로 해놔서 복수 발송 가능
                String[] address = {str};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT,"신고필증 발급");
                email.putExtra(Intent.EXTRA_TEXT,"발급신청한 신고필증 pdf 파일 보내드립니다.\n");
                email.putExtra(Intent.EXTRA_STREAM, fileUri);

                startActivity(email);
            }

            pdfDocument.close();
        }

    }
}