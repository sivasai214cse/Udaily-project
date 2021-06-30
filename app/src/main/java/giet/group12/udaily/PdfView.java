package giet.group12.udaily;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfView extends AppCompatActivity {

    PDFView pdfView;
    int position =-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        pdfView=findViewById(R.id.pdfView);
        position = getIntent().getIntExtra("position",-1);
        viewPdf();

    }
    private void viewPdf()
    {
        pdfView.fromFile(Pviewr.mFiles.get(position)).enableSwipe(true).enableAnnotationRendering(true).scrollHandle(new DefaultScrollHandle(this)).load();


    }
}