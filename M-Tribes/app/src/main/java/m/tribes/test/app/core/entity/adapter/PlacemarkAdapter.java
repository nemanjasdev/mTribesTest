package m.tribes.test.app.core.entity.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import m.tribes.test.app.R;
import m.tribes.test.app.core.entity.Placemark;
import m.tribes.test.app.core.entity.Placemarks;

import static m.tribes.test.app.view.map.Constants.CarState.*;


public class PlacemarkAdapter extends RecyclerView.Adapter<PlacemarkAdapter.ViewHolder> {
  public List<Placemark> placemarks = new ArrayList<>();
  public Context context;

  public PlacemarkAdapter(Context applicationContext, Placemarks flowerArrayList) {
    this.context = applicationContext;
    this.placemarks.addAll(flowerArrayList.item);
  }

  @Override
  public PlacemarkAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_map, viewGroup, false);
    return new ViewHolder(view);
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  @Override
  public void onBindViewHolder(PlacemarkAdapter.ViewHolder viewHolder, int i) {
    viewHolder.name.setText(placemarks.get(i).getName());
    viewHolder.address.setText(placemarks.get(i).getAddress());
    viewHolder.fuel.setText(String.valueOf(placemarks.get(i).getFuel()));

    if (placemarks.get(i).getInterior().equals(GOOD.toString())) {
      viewHolder.imageView.setImageResource(R.drawable.car_green);
    } else if (placemarks.get(i).getInterior().equals(UNACCEPTABLE.toString())) {
      viewHolder.imageView.setImageResource(R.drawable.car_red);
    }

    viewHolder.interior.setText(placemarks.get(i).getInterior());
    viewHolder.engine.setText(placemarks.get(i).getEngineType());
  }

  @Override
  public int getItemCount() {
    return placemarks.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title)
    TextView name;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.interior)
    TextView interior;

    @BindView(R.id.exterior)
    TextView exterior;

    @BindView(R.id.fuel)
    TextView fuel;

    @BindView(R.id.engine)
    TextView engine;

    @BindView(R.id.cover)
    ImageView imageView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);

      itemView.setOnClickListener(_view -> {
        int pos = getAdapterPosition();
        if (pos != RecyclerView.NO_POSITION) {
          Placemark clickedDataItem = placemarks.get(pos);
          showMessage(_view, clickedDataItem);
        }
      });
    }
  }

  private void showMessage(View view, Placemark clickedDataItem) {
    Toasty.success(view.getContext(), clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
  }
}