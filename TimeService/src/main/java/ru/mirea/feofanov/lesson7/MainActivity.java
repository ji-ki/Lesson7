package ru.mirea.feofanov.lesson7;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;

import java.util.Date;

import ru.mirea.feofanov.lesson7.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = MainActivity.class.getSimpleName();
    private final String host = "time.nist.gov"; // или time-a.nist.gov
    private final int port = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            String dateTimeView="";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); // игнорируем первую строку
                timeResult = reader.readLine(); // считываем вторую строку
                //В результате выполнения кода вы получите преобразованную дату и время в формате yy-MM-dd HH:mm:ss. Обратите внимание, что указанная строка содержит также другую информацию, которая не будет использоваться при преобразовании в дату и время.
                Log.d(TAG, timeResult);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return timeResult;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.textView.setText(result);
            String dateTimeView="";
            String dateTimeSubstring = result.substring(6, 22);
            String pattern = "yy-MM-dd HH:mm:ss";

            // Создание объекта SimpleDateFormat с указанным шаблоном
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                // Преобразование строки в объект Date
                Date dateTime = dateFormat.parse(dateTimeSubstring);
                // Вывод преобразованной даты и времени
                dateTimeView=new SimpleDateFormat().format(dateTime);
                System.out.println(new SimpleDateFormat().format(dateTime));
            } catch (ParseException e) {
                System.out.println("Ошибка при преобразовании строки в дату и время.");
                e.printStackTrace();
            }
            binding.textView2.setText(dateTimeView);
        }
    }
}