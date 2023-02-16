package com.points.spesqlitemanager.spesqlite.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class JsonUtil {
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //未找到给个默认数据库配置,请基于如下配置在assets中新建dbupdate.json：
            // 1.dbName可以随意改，但是新建完后就不能改动
            // 2.dbVersion建议从0开始
            // 3.dbconfig表为固定表，不能少
            // 4.新建在后面直接新增
            /*
            {
  "dbName": "lcoaldb",
  "dbVersion": 1 ,
  "dbTables": [
    {
      "tableName":"dbconfig",
      "columns":[
        {
          "key": "dbversion",
          "keyType": "TEXT"
        },
        {
          "key": "dbname",
          "keyType": "TEXT"
        },
        {
          "key": "dbtables",
          "keyType": "TEXT"
        }
      ]
    }
  ]
}
             */
            return "{\n" +
                    "  \"dbName\": \"temp\",\n" +
                    "  \"dbVersion\": 1 ,\n" +
                    "  \"dbTables\": [\n" +
                    "    {\n" +
                    "      \"tableName\":\"dbconfig\",\n" +
                    "      \"columns\":[\n" +
                    "        {\n" +
                    "          \"key\": \"dbversion\",\n" +
                    "          \"keyType\": \"TEXT\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"key\": \"dbname\",\n" +
                    "          \"keyType\": \"TEXT\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"key\": \"dbtables\",\n" +
                    "          \"keyType\": \"TEXT\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        }
        return stringBuilder.toString();
    }
}
