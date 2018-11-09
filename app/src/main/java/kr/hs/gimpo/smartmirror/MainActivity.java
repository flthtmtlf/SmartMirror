package kr.hs.gimpo.smartmirror;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolMenu;
import org.hyunjun.school.SchoolSchedule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView clock_date, clock_hm, clock_s,      // Time
            lunch, dinner, schedules,            // School
            pm10, pm25, comp, wt1h, wreh, wetc;  // Weather
    ImageView wicon;
    TimeHandler timeHandler;
    ScheduleHandler scheduleHandler;
    WeatherHandler weatherHandler;
    final String SchoolCode = "J100000510"; // 김포고등학교의 학교코드는 'J100000510'이다!
    String date, time, h, m, s, hm;
    String lunchToday, dinnerToday, scheduleToday;
    String pm10data, pm25data, compdata;
    int year, month, day;
    int weatherIcon;
    String weatherT1H, weatherREH, weatherETC;
    List<SchoolMenu> menu_list;
    List<SchoolSchedule> schedules_list;

    public final static int X_PADDING_ORIGIN = 540;
    public final static int Y_PADDING_ORIGIN = 960;
    public final static int X_PADDING = 80;
    public final static int Y_PADDING = 16;

    Runnable panelActivator = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);

                isPanelActivated = !isPanelActivated;

                panelActivation.interrupt();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

    Thread panelActivation = new Thread(panelActivator);

    Thread clock = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                try {
                    long pre = System.currentTimeMillis();
                    time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                            .format(System.currentTimeMillis());
                    String ymd_hms[] = time.split("-");
                    day = Integer.parseInt(ymd_hms[2]);
                    h = ymd_hms[3]; m = ymd_hms[4]; s = ymd_hms[5];
                    hm = h + ":" + m;
                    date = String.format("%s/%s/%s", ymd_hms[0], ymd_hms[1], ymd_hms[2]);

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

        List<String> NextNewsData = new ArrayList<>();
        String[] newsData;
        ArrayAdapter<String> adapter;
        ListView listView;
        int i;

        @Override
        public void run() {
            try {
                while(true) {
                    time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                            .format(System.currentTimeMillis());
                    String temp[] = time.split("-");
                    year = Integer.parseInt(temp[0]);
                    month = Integer.parseInt(temp[1]);
                    day = Integer.parseInt(temp[2]);
                    int thisDay = day;

                    try {
                        NextNewsData.clear();


                    NextNewsData
                            .add("[단독] 스마트 미러 개발팀 'TechStudio', 팀명을 정한 계기는?");
                    NextNewsData
                            .add("[단독] 김포고등학교 스마트 미러, 10월 31일 개발 완료돼... 대상까지 수상해");
                    NextNewsData
                            .add("[단독] 스마트 미러 개발팀 " +
                                    "\"도와주는 사람 아무도 없어... 팀끼리 고군분투\"");


                        Document doc = Jsoup.connect("http://yonhapnews.co.kr").get();

                        //테스트1
                        Elements titles= doc.select("div.news-con h1.tit-news");

                        System.out.println("-----------------------------------------------------");
                        for(Element e: titles){
                            System.out.println("title: " + e.text());
                            NextNewsData.add(e.text().trim());
                        }

                        //테스트2
                        titles= doc.select("div.news-con h2.tit-news");

                        System.out.println("-----------------------------------------------------");
                        for(Element e: titles){
                            System.out.println("title: " + e.text());
                            NextNewsData.add(e.text().trim());
                        }

                        //테스트3
                        titles= doc.select("li.section02 div.con h2.news-tl");

                        System.out.println("-----------------------------------------------------");
                        for(Element e: titles){
                            System.out.println("title: " + e.text());
                            NextNewsData.add(e.text().trim());
                        }
                        System.out.println("-----------------------------------------------------");


                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    NextNewsData.add(NextNewsData.get(0));
                    newsData = NextNewsData.toArray(new String[NextNewsData.size()]);

                    adapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            R.layout.news_item,
                            newsData
                    );

                    listView = findViewById(R.id.news_list);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                        }
                    });

                    while(thisDay == day) {
                        try {
                            for(i = 0; i < NextNewsData.size() - 1; i++) {
                                int t = 120;
                                listView.smoothScrollToPosition(i);
                                Thread.sleep(t);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView.setSelection(i);
                                    }
                                });
                                Thread.sleep(5000 - t);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setSelection(0);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    newsData = new String[1];
                                    newsData[0] = "에러가 발생했습니다.";
                                    adapter = new ArrayAdapter<>(
                                            getApplicationContext(),
                                            R.layout.news_item,
                                            newsData
                                    );
                                    listView.setAdapter(adapter);
                                    listView.setSelection(0);
                                }
                            });
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    });
    Thread schedule = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                School api = new School(School.Type.HIGH, School.Region.GYEONGGI, SchoolCode);
                time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                        .format(System.currentTimeMillis());
                String temp[] = time.split("-");
                year = Integer.parseInt(temp[0]);
                month = Integer.parseInt(temp[1]);
                day = Integer.parseInt(temp[2]);
                int thisDay = day;

                try {
                    menu_list = api.getMonthlyMenu(year, month);
                    schedules_list = api.getMonthlySchedule(year, month);

                    lunchToday = menu_list.get(day - 1).lunch;
                    dinnerToday = menu_list.get(day - 1).dinner;
                    //scheduleToday = schedules_list.get(day - 1).schedule;
                    StringBuilder sb = new StringBuilder();
                    Calendar oneWeek = Calendar.getInstance();
                    for(int i = 1; i <= 7; i++) {
                        if(schedules_list.get(day - i).schedule.compareTo("등록된 일정이 없습니다.") != 0 && schedules_list.get(day - i).schedule.compareTo("토요휴업일") != 0) {
                            String dd = new SimpleDateFormat("dd", Locale.getDefault()).format(oneWeek.getTime());
                            if(dd.compareTo("01") == 0) {
                                sb.append(new SimpleDateFormat("MM/dd", Locale.getDefault()).format(oneWeek.getTime()));
                            } else {
                                sb.append(dd);
                            }
                            sb.append(" - ");
                            sb.append(schedules_list.get(day - i).schedule);
                            sb.append("\n");
                            oneWeek.add(Calendar.DATE, 1);
                        } else {
                            continue;
                        }
                    }

                    if(sb.toString().compareTo("") == 0) {
                        scheduleToday = "등록된 일정이 없습니다.";
                    } else {
                        scheduleToday = sb.toString();
                    }

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

        String pm10value, pm10grade, pm25value, pm25grade, compvalue, compgrade;
        String thisHour;

        Map<String, String> rawWeatherValue = new HashMap<>();

        boolean isInit = false;
        final String SERVICE_KEY=
                "uTRaH16OBrv%2BrnhI1l%2BhctIkvNd6DwX%2FxpnCRXHGHLj" +
                        "pRpVqxmQJ7Q4cXR0wucoc%2Bx3v8hg%2BsVZRvhPTzXS1xw%3D%3D";

        @Override
        public void run() {
            while(true) {
                time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                        .format(System.currentTimeMillis());
                String ymd_hms[] = time.split("-");
                h = ymd_hms[3];
                thisHour = h;

                if(!isInit) {
                    initAirData();
                    initWeatherData();
                    isInit = true;
                    Message message = weatherHandler.obtainMessage();
                    weatherHandler.sendMessage(message);
                }

                try {
                    while(thisHour.compareTo(h) == 0) {
                        Thread.sleep(60*1000);
                    }
                    Log.i("MainActivity", "weather : wait for 10 min.");
                    Thread.sleep(10*60*1000);
                    initAirData();
                    Message message1 = weatherHandler.obtainMessage();
                    weatherHandler.sendMessage(message1);
                    Log.i("MainActivity", "weather: wait for 30 min.");
                    Thread.sleep(35*60*1000);
                    initWeatherData();
                    Message message2 = weatherHandler.obtainMessage();
                    weatherHandler.sendMessage(message2);
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void initAirData() {
            try {
                Log.i("MainActivity", "weather : init - air quality");
                Document doc = Jsoup
                        .connect("http://openapi.airkorea.or.kr/openapi/services/rest/" +
                                "ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?" +
                                "serviceKey="+ SERVICE_KEY +
                                "&numOfRows=1&pageSize=1&pageNo=1&startPage=1&" +
                                "stationName=%EC%82%AC%EC%9A%B0%EB%8F%99&dataTerm=DAILY&ver=1.3")
                        .get();

                String resultCode = doc.getElementsByTag("resultCode").text().trim();
                if(resultCode.compareTo("00") == 0) {
                    pm10value = doc.getElementsByTag("pm10Value").text().trim();
                    pm10grade = doc.getElementsByTag("pm10Grade1h").text().trim();
                    pm25value = doc.getElementsByTag("pm25Value").text().trim();
                    pm25grade = doc.getElementsByTag("pm25Grade1h").text().trim();
                    compvalue = doc.getElementsByTag("khaiValue").text().trim();
                    compgrade = doc.getElementsByTag("khaiGrade").text().trim();

                    // 숫자로 나오는 등급을 국어로 변환
                    if(pm10grade.compareTo("")!=0) {
                        pm10grade = pm10grade
                                .replace("1", getResources().getString(R.string.best));
                        pm10grade = pm10grade
                                .replace("2", getResources().getString(R.string.good));
                        pm10grade = pm10grade
                                .replace("3", getResources().getString(R.string.bad));
                        pm10grade = pm10grade
                                .replace("4", getResources().getString(R.string.worst));
                    } else pm10grade = "-";
                    if(pm25grade.compareTo("")!=0) {
                        pm25grade = pm25grade
                                .replace("1", getResources().getString(R.string.best));
                        pm25grade = pm25grade
                                .replace("2", getResources().getString(R.string.good));
                        pm25grade = pm25grade
                                .replace("3", getResources().getString(R.string.bad));
                        pm25grade = pm25grade
                                .replace("4", getResources().getString(R.string.worst));
                    } else pm25grade = "-";
                    if(compgrade.compareTo("")!=0) {
                        compgrade = compgrade
                                .replace("1", getResources().getString(R.string.best));
                        compgrade = compgrade
                                .replace("2", getResources().getString(R.string.good));
                        compgrade = compgrade
                                .replace("3", getResources().getString(R.string.bad));
                        compgrade = compgrade
                                .replace("4", getResources().getString(R.string.worst));
                    } else compgrade = "-";

                    // 측정값의 무결성 검증
                    pm10value = pm10value.compareTo("")!=0? pm10value: "-";
                    pm25value = pm25value.compareTo("")!=0? pm25value: "-";
                    compvalue = compvalue.compareTo("")!=0? compvalue: "-";

                    // 표시할 데이터 구성 ( 측정등급 (측정값 + 단위))
                    pm10data = pm10grade + " (" + pm10value + " ㎍/㎥(1H))";
                    pm25data = pm25grade + " (" + pm25value + " ㎍/㎥(1H))";
                    compdata = compgrade + " (" + compvalue + ")";
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        // TODO: 날씨 기능 구현
        private void initWeatherData() {
            try {
                Log.i("MainActivity", "weather : init - weather status");
                Calendar weatherTime = Calendar.getInstance();

                if(Integer.parseInt(
                        new SimpleDateFormat("mm", Locale.getDefault())
                                .format(weatherTime.getTime())) < 40
                        ) {
                        weatherTime.add(Calendar.HOUR, -1);
                }
                String ymd_hms[] =
                        new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                                .format(weatherTime.getTime()).split("-");

                // http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?
                // serviceKey=SERVICE_KEY&
                // base_date=20180929& // 날짜
                // base_time=2100&     // 시간
                // nx=55&              // 김포 지역의 좌표 (x = 55, y = 128)
                // ny=128&
                // numOfRows=10&       // 데이터 개수: 10개 (전부) (이하 수신 데이터 형식 결정)
                // pageSize=10&
                // pageNo=1&
                // startPage=1&
                // _type=xml           // xml 포맷으로 응답

                Document doc = Jsoup.connect(
                        "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/" +
                                "ForecastGrib?" +
                                "serviceKey=" + SERVICE_KEY + "&" +
                                "base_date=" + ymd_hms[0] + ymd_hms[1] + ymd_hms[2] + "&" +
                                "base_time=" + ymd_hms[3] + "00" + "&" +
                                "nx=55&ny=128&" +
                                "numOfRows=10&pageSize=10&pageNo=1&_type=xml"
                ).get();

                String resultCode = doc.getElementsByTag("resultCode").text().trim();

                if(resultCode.compareTo("0000")==0) {
                    Elements rawDatum = doc.select("item");

                    for(Element rawData: rawDatum) {
                        System.out.println(
                                rawData.getElementsByTag("category").text().trim()
                        );
                        System.out.println(
                                rawData.getElementsByTag("obsrValue").text().trim()
                        );
                        rawWeatherValue.put(
                                rawData.getElementsByTag("category").text().trim(),
                                rawData.getElementsByTag("obsrValue").text().trim()
                        );
                    }

                    // T1H : 기온 (°C), RN1 : 1시간 강수량(mm), UUU : 동서바람성분, VVV : 남북바람성분,
                    // REH : 습도(%), PTY : 강수형태 (0: 없음, 1: 비, 2: 비/눈, 3: 눈),
                    // VEC : 풍향(방위각), WSD : 풍속 (m/s)

                    /*
                    * 아이콘 결정 매커니즘
                    * (deprecated - 2018-10-21)
                    * 1. PTY 확인 -> 1 이상이면 상황에 맞게 표기, 0이면 다음.
                    * 2. SKY 확인 -> 상황에 맞게 표기
                    * (revision - 2018-10-21)
                    * PTY 값에 따라 결정
                    * */

                    if(Integer.parseInt(rawWeatherValue.get("PTY")) >= 0) {
                        switch ( Integer.parseInt(rawWeatherValue.get("PTY")) ) {
                            case 0:
                                weatherIcon = 1;
                                break;
                            case 1:
                                weatherIcon = 5;
                                break;
                            case 2:
                                weatherIcon = 6;
                                break;
                            case 3:
                                weatherIcon = 7;
                                break;
                        }
                    }

                    if(Double.parseDouble(rawWeatherValue.get("T1H")) <= -900
                            || Double.parseDouble(rawWeatherValue.get("T1H")) >= 900) {
                        weatherT1H = " - °C";
                    } else {
                        weatherT1H = rawWeatherValue.get("T1H") + " °C";
                    }

                    if(Double.parseDouble(rawWeatherValue.get("REH")) <= -900
                            || Double.parseDouble(rawWeatherValue.get("REH")) >= 900) {
                        weatherREH = " - %";
                    } else {
                        weatherREH = rawWeatherValue.get("REH") + " %";
                    }

                    weatherETC = "";

                    weatherETC += "강수량 ";
                    if(Double.parseDouble(rawWeatherValue.get("RN1")) <= -900
                            || Double.parseDouble(rawWeatherValue.get("RN1")) >= 900) {
                        weatherETC += "- mm / ";
                    } else {
                        weatherETC += rawWeatherValue.get("RN1") + " mm / ";
                    }

                    weatherETC += "풍속 ";
                    if(Double.parseDouble(rawWeatherValue.get("WSD")) <= -900
                            || Double.parseDouble(rawWeatherValue.get("WSD")) >= 900) {
                        weatherETC += "- m/s ";
                    } else {
                        weatherETC += rawWeatherValue.get("WSD") + " m/s ";
                    }

                    if(Double.parseDouble(rawWeatherValue.get("VEC")) <= -900
                            || Double.parseDouble(rawWeatherValue.get("VEC")) >= 900) {
                        weatherETC += "데이터 없음";
                    } else {
                        int vector = (int)
                                ((Double.parseDouble(rawWeatherValue.get("VEC")) / 22.5) + 0.5);
                        switch(vector) {
                            case 0:
                            case 16:
                                weatherETC += "북";
                                break;
                            case 1:
                                weatherETC += "북북동";
                                break;
                            case 2:
                                weatherETC += "북동";
                                break;
                            case 3:
                                weatherETC += "동북동";
                                break;
                            case 4:
                                weatherETC += "동";
                                break;
                            case 5:
                                weatherETC += "동남동";
                                break;
                            case 6:
                                weatherETC += "남동";
                                break;
                            case 7:
                                weatherETC += "남남동";
                                break;
                            case 8:
                                weatherETC += "남";
                                break;
                            case 9:
                                weatherETC += "남남서";
                                break;
                            case 10:
                                weatherETC += "남서";
                                break;
                            case 11:
                                weatherETC += "서남서";
                                break;
                            case 12:
                                weatherETC += "서";
                                break;
                            case 13:
                                weatherETC += "서북서";
                                break;
                            case 14:
                                weatherETC += "북서";
                                break;
                            case 15:
                                weatherETC += "북북서";
                                break;
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                weatherETC = "에러가 발생했습니다.";
                weatherT1H = " - °C";
                weatherREH = " - %";
                Message message = weatherHandler.obtainMessage();
                weatherHandler.sendMessage(message);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        setTheme(R.style.AppTheme_NoActionBar);

        setContentView(R.layout.activity_main);

        clock_date = findViewById(R.id.date);
        clock_hm = findViewById(R.id.clock);
        clock_s = findViewById(R.id.sec);

        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        schedules = findViewById(R.id.schedule);

        pm10 = findViewById(R.id.pm10);
        pm25 = findViewById(R.id.pm25);
        comp = findViewById(R.id.comp);

        wicon = findViewById(R.id.weather_icon);
        wt1h = findViewById(R.id.weather_T1H);
        wreh = findViewById(R.id.weather_REH);
        wetc = findViewById(R.id.weather_etc);

        timeHandler = new TimeHandler();
        scheduleHandler = new ScheduleHandler();
        weatherHandler = new WeatherHandler();

        mainPanel = findViewById(R.id.main_panel);
        weatherPanel = findViewById(R.id.weather_panel);
        mealPanel = findViewById(R.id.meal_panel);
        newsPanel = findViewById(R.id.news_panel);

        initScreen();
        initScreenData();

    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public boolean isPanelActive = false;
    public boolean isPanelActivated = false;
    View mainPanel, weatherPanel, mealPanel, newsPanel;

    private void setPanelOpen() {
        if(!isPanelActive && !isPanelActivated) {
            isPanelActive = true;

            panelActivation = new Thread(panelActivator);
            panelActivation.start();

            mainPanel
                    .animate()
                    .alphaBy(0.5f)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .translationXBy(-convertDpToPixel(X_PADDING_ORIGIN - X_PADDING, getApplicationContext()))
                    .translationYBy(convertDpToPixel(Y_PADDING_ORIGIN - Y_PADDING, getApplicationContext()))
                    .setDuration(2000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mealPanel.animate()
                                    .alpha(1.0f)
                                    .setDuration(500)
                                    .translationXBy(convertDpToPixel(16, getApplicationContext()))
                                    .withLayer();
                            weatherPanel.animate()
                                    .alpha(1.0f)
                                    .setDuration(500)
                                    .translationYBy(-convertDpToPixel(16, getApplicationContext()))
                                    .withLayer();
                        }
                    })
                    .withLayer();
            newsPanel
                    .animate()
                    .translationYBy(-convertDpToPixel(32, getApplicationContext()))
                    .alpha(1.0f)
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            View schedule = findViewById(R.id.schedule);
                            schedule.setVisibility(View.VISIBLE);
                        }
                    })
                    .withLayer();

        }
    }

    private void setPanelClose() {
        if(isPanelActive && isPanelActivated) {
            isPanelActive = false;

            panelActivation = new Thread(panelActivator);
            panelActivation.start();

            mainPanel
                    .animate()
                    .alphaBy(-0.5f)
                    .scaleXBy(1.2f)
                    .scaleYBy(1.2f)
                    .translationXBy(convertDpToPixel(X_PADDING_ORIGIN - X_PADDING, getApplicationContext()))
                    .translationYBy(-convertDpToPixel(Y_PADDING_ORIGIN - Y_PADDING, getApplicationContext()))
                    .setDuration(2000)
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            mealPanel.animate()
                                    .alpha(0.0f)
                                    .setDuration(500)
                                    .translationXBy(-convertDpToPixel(16, getApplicationContext()))
                                    .withLayer();
                            weatherPanel.animate()
                                    .alpha(0.0f)
                                    .setDuration(500)
                                    .translationYBy(convertDpToPixel(16, getApplicationContext()))
                                    .withLayer();
                        }
                    })
                    .withLayer();
            newsPanel
                    .animate()
                    .translationYBy(convertDpToPixel(32, getApplicationContext()))
                    .alpha(0.0f)
                    .setDuration(1000)
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            View schedule = findViewById(R.id.schedule);
                            schedule.setVisibility(View.INVISIBLE);
                        }
                    })
                    .withLayer();
        }
    }

    private void initScreen() {

        mainPanel.animate()
                .alphaBy(-0.5f)
                .scaleXBy(1.2f)
                .scaleYBy(1.2f);
        newsPanel.animate()
                .translationYBy(convertDpToPixel(32, getApplicationContext()));
        weatherPanel.animate()
                .translationYBy(convertDpToPixel(16, getApplicationContext()))
                .alpha(0.0f)
                .withLayer();
        mealPanel.animate()
                .translationXBy(-convertDpToPixel(16, getApplicationContext()))
                .alpha(0.0f)
                .withLayer();

        final Button open = findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanelOpen();
            }
        });

        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanelClose();
            }
        });
    }

    private void initScreenData() {

        clock.start();
        schedule.start();
        weather.start();
        news.start();

    }

    class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message Msg) {
            clock_date.setText(date);
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

            switch(weatherIcon) {
                case 1:
                    wicon.setImageResource(R.drawable.nb01);
                    break;
                case 2:
                    wicon.setImageResource(R.drawable.nb02);
                    break;
                case 3:
                    wicon.setImageResource(R.drawable.nb03);
                    break;
                case 4:
                    wicon.setImageResource(R.drawable.nb04);
                    break;
                case 5:
                    wicon.setImageResource(R.drawable.nb08);
                    break;
                case 6:
                    wicon.setImageResource(R.drawable.nb12);
                    break;
                case 7:
                    wicon.setImageResource(R.drawable.nb11);
                    break;
            }

            wt1h.setText(weatherT1H);
            wreh.setText(weatherREH);
            wetc.setText(weatherETC);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(!isPanelActive && !isPanelActivated) {
            setPanelOpen();
        } else if(isPanelActive && isPanelActivated) {
            setPanelClose();
        }
    }
}
