package tech.sunboshi.app.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;

public class HTTPLogService extends Service {
    public static final int Port = 10086;
    public static final int MaxLog = 2000;
    public static final String TAG = "HTTPLogService";
    public static final List<String> LOGS = new ArrayList<String>();

    private HTTPServer server;

    public HTTPLogService() {
    }

    public static void log(String log) {
        // TODO: 2017/9/12 注意多线程安全
        Date nowTime=new Date();
        System.out.println(nowTime);
        SimpleDateFormat time=new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
        log = time.format(nowTime) + log;

        synchronized (LOGS) {
            if (LOGS.size() > MaxLog) {
                LOGS.clear();
            }
            LOGS.add(log);
        }
        Log.i(TAG, log);
    }

    public static void logErr(String log) {
        log("[ERR] " + log);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new HTTPServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    class HTTPServer extends NanoHTTPD {
        public HTTPServer() throws IOException {
            super(Port);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.i(TAG, "HTTPServer: Runing at :" + Integer.toString(Port));
        }

        @Override
        public Response serve(IHTTPSession session) {
            String allLog = TextUtils.join("<br/>\n", LOGS);
            return newFixedLengthResponse(allLog);
        }
    }
}
