package t4.sers.receiver;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import t4.sers.R;
import t4.sers.util.ConfirmCourse;

public class WarningReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "社交距離警示";
    public static List<String> courseCode = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        triggerNotification(context);
    }

    public void deliverWarningNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .setContentTitle("偵測到暴露通知")
                .setContentText("請查看 APP 通知。")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void deliverPassedNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_gpp_good_24)
                .setContentTitle("北科社交距離")
                .setContentText("尚無接觸風險")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void triggerNotification(Context context){
        ExecutorService mExecutor = Executors.newSingleThreadExecutor();
        Runnable runnable = () -> {
            try{
                List<ConfirmCourse> list = new ArrayList<>();
                String confirmCaseURL = context.getString(R.string.confirm_course_URL);
                Connection.Response response = Jsoup.connect(confirmCaseURL).ignoreContentType(true).method(Connection.Method.GET).execute();
                String body = response.body();
                JsonObject responseData = JsonParser.parseString(body).getAsJsonObject();
                JsonObject dateObject = responseData.get("data").getAsJsonObject();
                for(String key : dateObject.keySet()){
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
                    JsonObject dateIsolateObject = dateObject.get(key).getAsJsonObject();
                    for(String code : dateIsolateObject.keySet()){
                        Date date = dateFormat.parse(key);
                        DateTime dateTime = new DateTime(date);
                        JsonObject courseObject = dateIsolateObject.get(code).getAsJsonObject();
                        int courseWeekDay = courseObject.get("courseIsolateDate").getAsInt();
                        dateTime = dateTime.minusDays(Math.abs(dateTime.getDayOfWeek() - courseWeekDay));
                        DateTime isolateFinish = dateTime.plusDays(2);
                        list.add(new ConfirmCourse(code, "", "", dateTime.withZone(DateTimeZone.forID("Asia/Taipei")), isolateFinish.withZone(DateTimeZone.forID("Asia/Taipei"))));
                    }
                }
                SharedPreferences sharedPreferences = context.getSharedPreferences("warningLiftTime", MODE_PRIVATE);
                boolean trigger = false;
                for(ConfirmCourse confirmCourse : list){
                    String code = confirmCourse.getCode();
                    DateTime dateTime = confirmCourse.getRiskDurationStart();
                    DateTime isolateTime = confirmCourse.getRiskDurationEnd();
                    DateTime liftTime = new DateTime(sharedPreferences.getLong("liftTime", 0L));
                    System.out.println(isolateTime + " " + liftTime);
                    if(courseCode.contains(code) && isolateTime.isAfter(liftTime)){
                        deliverWarningNotification(context);
                        trigger = true;
                    }
                }
                if(!trigger){
                    deliverPassedNotification(context);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        mExecutor.execute(runnable);
    }
}