package m.tribes.test.app.core.presenter;

import android.widget.ProgressBar;

public interface IPresenter {

  void showProgress(ProgressBar spinner);

  void hideProgress(ProgressBar spinner);

  void showMessage(String message);
}

