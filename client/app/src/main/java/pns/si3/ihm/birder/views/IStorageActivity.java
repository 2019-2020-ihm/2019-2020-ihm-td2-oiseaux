package pns.si3.ihm.birder.views;

import android.graphics.Bitmap;

public interface IStorageActivity {
    int REQUEST_MEDIA_READ = 100;
    int REQUEST_MEDIA_WRITE = 200;

    void onPictureLoad(Bitmap bitmap);
    Bitmap getPictureToSave();
}
