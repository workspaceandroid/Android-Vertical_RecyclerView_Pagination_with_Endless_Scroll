package hemath.com.pagination.Adapters;

import android.content.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

import com.android.volley.toolbox.*;

import java.util.*;

import hemath.com.pagination.*;
import hemath.com.pagination.Models.*;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder>
{
    private Context mContext;
    private ArrayList<TemplateModel> models;
    private ImageLoader mImageLoader;


    public TemplateAdapter(Context mContext,ArrayList<TemplateModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_template, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,int i)
    {
        final TemplateModel templateModel = models.get(i);

        viewHolder.text.setText(templateModel.getTitle());
        mImageLoader = CustomVolleyRequest.getInstance(mContext)
                .getImageLoader();
        mImageLoader.get(models.get(i).getImageurl(), mImageLoader.getImageListener( viewHolder.image,
                R.drawable.ic_pic, android.R.drawable.ic_dialog_alert));

        viewHolder.image.setImageUrl(models.get(i).getImageurl(), mImageLoader);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView image;
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.NetText);
            image = itemView.findViewById(R.id.NetImage);
        }
    }

}
