package com.service.eurekaclient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.service.eurekaclient.model.ImgCutInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ImgCutInfoTest {
    public static void main(String[] args) {
        File file = new File("D:/cutInfo.json");
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String str = "";
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.toString();
        Gson gson = new Gson();
        ImgCutInfo info = gson.fromJson(sb.toString(), ImgCutInfo.class);

        System.out.println(info.getPaperTemplateList());
    }
}
