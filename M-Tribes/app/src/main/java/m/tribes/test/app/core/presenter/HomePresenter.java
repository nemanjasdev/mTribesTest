package m.tribes.test.app.core.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import m.tribes.test.app.core.interactor.HomeInteractor;
import m.tribes.test.app.core.interactor.MapInteractor;

public class HomePresenter {

  private IPresenter presenter;
  public HomeInteractor homeInteractor;

  public HomePresenter(IPresenter presenter, HomeInteractor homeInteractor) {
    this.presenter = presenter;
    this.homeInteractor = homeInteractor;
  }

  public void returnPlacemarksList(Context context, RecyclerView recyclerView) {
    homeInteractor.findListItems(context, recyclerView);
  }

  public void onDestroy() {
    presenter = null;
  }
}
