package m.tribes.test.app.view.map;

public abstract class Constants {
  public static final int GPS_PERMISSION_REQUEST_CODE = 1001;
  public static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  public enum CarState {
    GOOD {
      @Override
      public String toString() {
        return "GOOD";
      }
    },
    UNACCEPTABLE {
      @Override
      public String toString() {
        return "UNACCEPTABLE";
      }
    },
    DISTANCE {
      @Override
      public String toString() {
        return "Distance from you (km): ";
      }
    }
  }

  public enum ErrorMessage {
    ERROR {
      @Override
      public String toString() {
        return "ERROR";
      }
    },
    FAILURE {
      @Override
      public String toString() {
        return "FAILURE";
      }
    },
    GPS {
      @Override
      public String toString() {
        return "Please turn on GPS";
      }
    }
  }
}
