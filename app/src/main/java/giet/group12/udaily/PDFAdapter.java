package giet.group12.udaily;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.MyViewHolder> {



    private Context mContext;
    private ArrayList<File> mFiles;
    String[] items;


    public PDFAdapter(Context mContext, ArrayList<File> mFiles,String[] items) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.items=items;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        android.view.View view = LayoutInflater.from(mContext).inflate(R.layout.pdf_item,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.file_name.setText(items[position]);

        holder.pdf_item.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent (mContext,PdfView.class);

                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {

        return mFiles.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView file_name;
        ImageView img_icon;
        RelativeLayout pdf_item;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            file_name=itemView.findViewById(R.id.pdf_file_name);
            img_icon=itemView.findViewById(R.id.img_pdf);
            pdf_item=itemView.findViewById(R.id.pdf_item);
        }
    }

}


