package kr.hs.gimpo.smartmirror;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolMenu;
import org.hyunjun.school.SchoolSchedule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    TextView clock_hm, clock_s, lunch, dinner, schedules, pm10, pm25, comp;
    TimeHandler timeHandler; ScheduleHandler scheduleHandler; WeatherHandler weatherHandler;
    final String SchoolCode = "J100000510"; // 김포고등학교의 학교코드는 'J100000510'이다!
    String time, h, m, s, hm; String lunchToday, dinnerToday, scheduleToday; String pm10data, pm25data, compdata;
    int year, month, day;
    List<SchoolMenu> menu_list; List<SchoolSchedule> schedules_list;
    Thread clock = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                try {
                    long pre = System.currentTimeMillis();
                    time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(System.currentTimeMillis());
                    String ymd_hms[] = time.split("-");
                    day = Integer.parseInt(ymd_hms[2]);
                    h = ymd_hms[3]; m = ymd_hms[4]; s = ymd_hms[5];
                    hm = h + ":" + m;
        
                    Message message = timeHandler.obtainMessage();
                    timeHandler.sendMessage(message);
        
                    Thread.sleep(1000 - (System.currentTimeMillis() - pre));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    Thread news = new Thread(new Runnable() {
        @Override
        public void run() {
        
        }
    });
    Thread schedule = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                School api = new School(School.Type.HIGH, School.Region.GYEONGGI, SchoolCode);
                time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(System.currentTimeMillis());
                String temp[] = time.split("-");
                year = Integer.parseInt(temp[0]); month = Integer.parseInt(temp[1]); day = Integer.parseInt(temp[2]);
                int thisDay = day;
    
                try {
                    menu_list = api.getMonthlyMenu(year, month);
                    schedules_list = api.getMonthlySchedule(year, month);
        
                    lunchToday = menu_list.get(day - 1).lunch;
                    dinnerToday = menu_list.get(day - 1).dinner;
                    scheduleToday = schedules_list.get(day - 1).schedule;
        
                    Message message = scheduleHandler.obtainMessage();
                    scheduleHandler.sendMessage(message);
                    while(thisDay == day) {
                        Thread.sleep(600*1000);
                    }
        
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
    
    Thread weather = new Thread(new Runnable() {
        
        String pm10value, pm10grade, pm25value, pm25grade, compgrade;
        String thisHour;
        
        @Override
        public void run() {
            while(true) {
                time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(System.currentTimeMillis());
                String ymd_hms[] = time.split("-");
                h = ymd_hms[3];
                thisHour = h;
                
                initAirData();
                initWeatherData();
    
                Message message = weatherHandler.obtainMessage();
                weatherHandler.sendMessage(message);
                try {
                    while(thisHour.compareTo(h) == 0) {
                        Thread.sleep(60*1000);
                    }
                    Log.i("MainActivity", "weather : wait for 10 min.");
                    Thread.sleep(10*60*1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                
            }
        }
        
        private void initAirData() {
            try {
                Log.i("MainActivity", "weather : init - air quality");
                Document doc = Jsoup.connect("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?serviceKey="+
                        "uTRaH16OBrv%2BrnhI1l%2BhctIkvNd6DwX%2FxpnCRXHGHLjpRpVqxmQJ7Q4cXR0wucoc%2Bx3v8hg%2BsVZRvhPTzXS1xw%3D%3D"+
                        "&numOfRows=1&pageSize=1&pageNo=1&startPage=1&stationName=%EC%82%AC%EC%9A%B0%EB%8F%99&dataTerm=DAILY&ver=1.3").get();
        
                String resultCode = doc.getElementsByTag("resultCode").text().trim();
                if(resultCode.compareTo("00") == 0) {
                    pm10value = doc.getElementsByTag("pm10Value").text().trim();
                    pm10grade = doc.getElementsByTag("pm10Grade1h").text().trim();
                    pm25value = doc.getElementsByTag("pm25Value").text().trim();
                    pm25grade = doc.getElementsByTag("pm25Grade1h").text().trim();
                    compgrade = doc.getElementsByTag("khaiGrade").text().trim();
            
                    if(pm10grade.compareTo("")!=0) {
                        pm10grade = pm10grade.replace("1", getResources().getString(R.string.best));
                        pm10grade = pm10grade.replace("2", getResources().getString(R.string.good));
                        pm10grade = pm10grade.replace("3", getResources().getString(R.string.bad));
                        pm10grade = pm10grade.replace("4", getResources().getString(R.string.worst));
                    } else pm10grade = "-";
                    if(pm25grade.compareTo("")!=0) {
                
                        pm25grade = pm25grade.replace("1", getResources().getString(R.string.best));
                        pm25grade = pm25grade.replace("2", getResources().getString(R.string.good));
                        pm25grade = pm25grade.replace("3", getResources().getString(R.string.bad));
                        pm25grade = pm25grade.replace("4", getResources().getString(R.string.worst));
                    } else pm25grade = "-";
                    if(compgrade.compareTo("")!=0) {
                        compgrade = compgrade.replace("1", getResources().getString(R.string.best));
                        compgrade = compgrade.replace("2", getResources().getString(R.string.good));
                        compgrade = compgrade.replace("3", getResources().getString(R.string.bad));
                        compgrade = compgrade.replace("4", getResources().getString(R.string.worst));
                    } else compgrade = "-";
            
                    pm10value = pm10value.compareTo("")!=0? pm10value: "-";
                    pm25value = pm25value.compareTo("")!=0? pm25value: "-";
            
                    pm10data = pm10grade + "(" + pm10value + " ㎍/㎥(1H))";
                    pm25data = pm25grade + "(" + pm25value + " ㎍/㎥(1H))";
                    compdata = compgrade;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        private void initWeatherData() {
            try {
                Log.i("MainActivity", "weather : init - weather status");
                Document doc = Jsoup.connect("").get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        clock_hm = findViewById(R.id.clock);
        clock_s = findViewById(R.id.sec);
        
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        schedules = findViewById(R.id.schedule);
        
        pm10 = findViewById(R.id.pm10);
        pm25 = findViewById(R.id.pm25);
        comp = findViewById(R.id.comp);
        
        timeHandler = new TimeHandler();
        scheduleHandler = new ScheduleHandler();
        weatherHandler = new WeatherHandler();
        
        
        initScreen();
        
    }
    
    private void initScreen() {
        
        clock.start();
        schedule.start();
        weather.start();
        
    }
    
    class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message Msg) {
            clock_hm.setText(hm);
            clock_s.setText(s);
        }
    }
    
    class ScheduleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            lunch.setText(lunchToday);
            dinner.setText(dinnerToday);
            schedules.setText(scheduleToday);
        }
    }
    
    class WeatherHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            pm10.setText(pm10data);
            pm25.setText(pm25data);
            comp.setText(compdata);
        }
    }
}
