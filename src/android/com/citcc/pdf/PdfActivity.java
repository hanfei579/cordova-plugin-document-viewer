package com.citcc.pdf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView; 

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public class PdfActivity extends Activity {


  private static final String ASSETS = "file:///android_asset/";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
	setContentView(getApplication().getResources().getIdentifier("activity_pdf", "layout", getApplication().getPackageName())); 
    PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
    try {
      Intent intent = getIntent();
      String fileUrl = intent.getStringExtra("FileUrl");
      if (fileUrl.startsWith(ASSETS)) {
        pdfView.fromAsset(fileUrl).load();
      } else {
        Uri uri = Uri.parse(fileUrl);
        pdfView.fromUri(uri).load();
      }
    } catch (Exception ex) {
      Intent i = new Intent();
      i.putExtra("Error", ex.getMessage());

      setResult(100, i);
      this.finish();
    }


  }
}
