package m.tribes.test.app.view.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import m.tribes.test.app.R;
import m.tribes.test.app.core.presenter.IPresenter;
import m.tribes.test.app.core.interactor.HomeInteractor;
import m.tribes.test.app.core.presenter.HomePresenter;

public class HomeFragment extends Fragment implements IPresenter {
  Unbinder unbinder;
  private HomePresenter presenter;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.progressBar)
  ProgressBar spinner;

  public HomeFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new HomePresenter(this, new HomeInteractor());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, rootView);

    spinner = rootView.findViewById(R.id.progressBar);
    recyclerView = rootView.findViewById(R.id.recyclerView);

    showProgress(spinner);
    initViews();
    hideProgress(spinner);

    return rootView;
  }

  @Override
  public void showProgress(ProgressBar spinner) {
    spinner.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress(ProgressBar spinner) {
    spinner.setVisibility(View.GONE);
  }

  @Override
  public void showMessage(String message) {
    Toasty.warning(getContext(), message, Toast.LENGTH_SHORT).show();

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    presenter.onDestroy();
  }

  private void initViews() {
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(gridLayoutManager);
    presenter.returnPlacemarksList(getContext(), recyclerView);
  }
}
