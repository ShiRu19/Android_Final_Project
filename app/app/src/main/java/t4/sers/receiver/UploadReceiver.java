package t4.sers.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class UploadReceiver extends BroadcastReceiver {

    private final Handler handler;

    public UploadReceiver(Handler handler){
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*String action = intent.getAction();
        System.out.println(action);
        if(action.equals("FirebaseUpload")) {
            String status = intent.getStringExtra("status");
            if(status.equals("OK")) {
                Log.d("FirebaseUploadBroadcast", "OK!");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        context.
                    }
                });
            }
        }*/
    }
}
