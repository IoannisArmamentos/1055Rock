package com.desertovercrowded.rock1055;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class DataSingleton {

    private static DataSingleton instance;

    private static ProducerList producerList;

    private DataSingleton() {

    }

    public static DataSingleton getInstance() {
        if (instance == null) {
            instance = new DataSingleton();
        }

        return instance;
    }

    public static ProducerList getProducerList() {
        return producerList;
    }

    public static void setProducerList(ProducerList producerList) {
        DataSingleton.producerList = producerList;
    }

    public void LoadDataFromJson(Context context) {

        Gson gson = new Gson();

        try {
            InputStream stream = context.getResources().openRawResource(R.raw.data);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes, 0, bytes.length);

             producerList = gson.fromJson(new String(bytes), ProducerList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
