package com.myApps.applications;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.pdf.PdfRenderer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.util.BitSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public PdfRenderer renderer;
 private int CP,max=0;
  TextView showText;
   Button select,Min,Max;
    ImageView Filer;
    
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle("PDF View");
        initialize();
       String[] permission={android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
	  
	  if(ContextCompat.checkSelfPermission(MainActivity.this,permission[0])!=PackageManager.PERMISSION_GRANTED){
		  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
		   ActivityCompat.requestPermissions(this,permission,100);
    	  }
	   }
	  
       
	   
	}
	
	public void openIntent(){
		Intent u= new Intent(Intent.ACTION_OPEN_DOCUMENT);
		 u.addCategory(Intent.CATEGORY_OPENABLE);
		 u.setType("application/pdf");
		   startActivityForResult(u,200);
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
   	if(arg0==200&& arg1==RESULT_OK){
		try{
			Uri l= arg2.getData();
			ParcelFileDescriptor f=getContentResolver().openFileDescriptor(l,"r");
			 renderer= new PdfRenderer(f);
			  max=renderer.getPageCount();
			   CP=0;
			    showPage(CP);
		}catch(Exception e){
			
		}
	   }
	   	   
	  }
	  
	  void showPage(int p){
		  if(renderer!=null){
			  PdfRenderer.Page m= renderer.openPage(p);
			   Bitmap b= Bitmap.createBitmap(
			   getResources().getDisplayMetrics().densityDpi*m.getWidth()/72
				,getResources().getDisplayMetrics().densityDpi*m.getHeight()/72,
				Bitmap.Config.ARGB_8888);
			     
			    m.render(b,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
			      
				 Filer.setImageBitmap(b);
				 m.close();
				  showText.setText(p+1+"/"+max);
		  }
	  }
	  
	  
	  void initialize(){
		  showText=findViewById(R.id.txt);
		   Filer=findViewById(R.id.Show);
		   select=findViewById(R.id.Select);
		    Min=findViewById(R.id.min);
			 Max=findViewById(R.id.Max);
			 select.setOnClickListener(this);
			  Min.setOnClickListener(this);
			   Max.setOnClickListener(this);
	  }

	@Override
	public void onClick(View arg0) {
        switch(arg0.getId()){
			case R.id.Select:
			openIntent();
			break;
			case R.id.Max:
			 if(CP<(max-1)){
				 CP++;
				  showPage(CP);
			 }
			 break;
			 case R.id.min:
			  if(CP>0){
				  CP--;
				   showPage(CP);
			  }
			
		}

	}
	
	
	  
}