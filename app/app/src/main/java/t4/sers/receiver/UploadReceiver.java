package t4.sers.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UploadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("FirebaseUpload")) {
            String status = intent.getStringExtra("status");
            /* 得到上傳結果之後... */
        }
    }
}
