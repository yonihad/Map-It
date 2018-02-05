package movie.yoni.mapexample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movie.yoni.mapexample.db.DBHandlerFavorites;

/**
 * Created by Tsur on 23/07/2017.
 */

public class NearMeRecyclerView extends RecyclerView.Adapter<NearMeRecyclerView.ViewHolder> {

    private ArrayList<Cities> list;
    private Activity activity;

    private OnNearByAdapterItemClicked callback;
    private OnNearByAdapterItemClicked callbackList;

    private DBHandlerFavorites dbHandlerFavorites;
    private Cities citiesToFavorites;

    public NearMeRecyclerView(Activity activity, ArrayList<Cities> list, List_Fragment fragment) {
        this.list = list;
        this.activity = activity;
        callback = (OnNearByAdapterItemClicked) activity;
        callbackList = fragment;
        dbHandlerFavorites = new DBHandlerFavorites(activity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_cities, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextView.setText(list.get(position).getName());
        holder.tv_distance.setText("");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getLocation() != null) {
                    callback.onNearByAdapterItemClicked(position, list);
                } else {
                    callbackList.onNearByAdapterItemTypeClick(list.get(position).getName());
                }

            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Choose it");
                builder.setMessage("Do you want to save in Favorites or Share it ?");
                builder.setPositiveButton("Favorites", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {


                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setTitle("Favorites");
                        builder1.setMessage("Do you want to save it in Favorites ?");
                        builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                citiesToFavorites = list.get(position);
                                dbHandlerFavorites.addToFavorites(citiesToFavorites);
                                dialogInterface.cancel();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        builder1.show();

                    }
                }).setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "I want to show you amazing place that call " + list.get(position).getName());
                        sendIntent.setType("text/plain");
                        activity.startActivity(sendIntent);
                    }
                });
                builder.show();

                return true;
            }
        });


        if (activity instanceof MainActivity) {

            MainActivity main = (MainActivity) activity;

            float[] results = new float[1];
            //String.format(java.util.Locale.US,"%.2f", results);
            //DecimalFormat decimalFormat = new DecimalFormat("#.##");
            //float twoDigitsF = Float.valueOf(decimalFormat.format(results));


            list.get(position);

            if (list.get(position).getLocation() != null) {
                Location.distanceBetween(main.latitude, main.longitude,
                        list.get(position).getLatitude(), list.get(position).getLongitude(),
                        results);
                float res = (results[0] / 1000);


                if (main.isKM) {

                    holder.tv_distance.setText(String.format("%.2f", res) + " KM");
                } else {
                    holder.tv_distance.setText(String.format("%.2f", (res / 1.62)) + " Miles");
                }

            }


        }


        Picasso.with(activity).load(list.get(position).getIcon()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnNearByAdapterItemClicked {
        void onNearByAdapterItemClicked(int pos, ArrayList<Cities> cities);

        void onNearByAdapterItemTypeClick(String type);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView img;
        public TextView tv_distance;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.info_text);
            img = v.findViewById(R.id.imageView);
            tv_distance = v.findViewById(R.id.tv_distance);
            cardView = v.findViewById(R.id.card_view);

        }
    }


}
