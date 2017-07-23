package jp.app.oomae.hisaki.asynktask_sample;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hisaki on 2017/07/23.
 */

public class AsyncNetworkTask extends AsyncTask<String,Integer,String>{

    private TextView textView;

    //結果を反映させるTextviewを取得
    public AsyncNetworkTask(Context context){
        super();
        MainActivity activity = (MainActivity)context;
        textView = (TextView)activity.findViewById(R.id.txtresult);
    }

    //非同期でHTTP GETリクエストを送信
    @Override
    protected String doInBackground(String... params){
        publishProgress(30);//進捗の通知
        //ダミーで時間のかかる処理を実行
        SystemClock.sleep(3000);
        publishProgress(60);//進捗の通知
        StringBuilder builder = new StringBuilder();
        try {
            //指定されたアドレスにアクセス
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            //レスポンスを順に読み込み
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(),"Shift-JIS"));
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        publishProgress(100);
        return builder.toString();
    }

    //進捗状況をログに出力
    @Override
    protected void onProgressUpdate(Integer... values){
        Log.d("url",values[0].toString());
    }

    //非同期を終了した後Textviewに反映
    @Override
    protected void onPostExecute(String result){
        textView.setText(result);
    }

    //非同期処理をキャンセルしたときtextviewにキャンセルを反映
    @Override
    protected void onCancelled(){
        textView.setText("キャンセルしました。");
    }
}
