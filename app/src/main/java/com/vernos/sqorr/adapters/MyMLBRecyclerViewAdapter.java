package com.vernos.sqorr.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.vernos.sqorr.R;
import com.vernos.sqorr.fragments.PromosFragment;
import com.vernos.sqorr.pojos.DummyContent;
import com.vernos.sqorr.utilities.PathParser;
import com.vernos.sqorr.views.MatchupScreen;

import java.util.ArrayList;
import java.util.List;

import static com.vernos.sqorr.utilities.Utilities.convertTParellelogram;
import static com.vernos.sqorr.utilities.Utilities.resizePath;


public class MyMLBRecyclerViewAdapter extends RecyclerView.Adapter<MyMLBRecyclerViewAdapter.ViewHolder> {



    private final List<DummyContent.DummyItem> mValues
            ;
    List<Object> recyclerViewItems = new ArrayList<>();

//    private final List<DummyContent.DummyItem> mValues;
    private final PromosFragment.OnListFragmentInteractionListener mListener;
    static FragmentActivity activity_t;

    public MyMLBRecyclerViewAdapter(List<DummyContent.DummyItem> items,
                                    PromosFragment.OnListFragmentInteractionListener listener, FragmentActivity activity) {
        mValues = items;
        mListener = listener;
        this.activity_t = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mlb, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Bitmap first_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                R.drawable.adam_scott);
        final Bitmap second_bitmap = BitmapFactory.decodeResource(activity_t.getResources(),
                R.drawable.branden_grace);


     //   holder.player1Img.setImageBitmap(convertTParellelogram(first_bitmap, "xxx"));
       // holder.player2Img.setImageBitmap(convertTParellelogram(second_bitmap, "pare"));


        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
//                    Toast.makeText(activity_t, position+"Kalyan", Toast.LENGTH_LONG).show();
                    Intent matchup = new Intent(activity_t, MatchupScreen.class);
                    activity_t.startActivity(matchup);
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        // public final TextView mIdView;
        // public final TextView mContentView;
        final ImageView player1Img,player2Img ;

        public DummyContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            //mContentView = (TextView) view.findViewById(R.id.content);
             player1Img = (ImageView) view.findViewById(R.id.player1Img);
             player2Img = (ImageView) view.findViewById(R.id.player2Img);
        }

        @Override
        public String toString() {
            return super.toString() + " '" /*+ mContentView.getText()*/ + "'";
        }
    }

    private static Path getParellelogramPath(Bitmap src, String type) {
        Path path = null;
        if (type.equals("pare")) {
            path = resizePath(PathParser.createPathFromPathData(activity_t.getString(R.string.pare)),
                    src.getWidth(), src.getHeight());
        } else {
            path = resizePath(PathParser.createPathFromPathData(activity_t.getString(R.string.square)),
                    src.getWidth(), src.getHeight());
        }

        return path;
    }

}
