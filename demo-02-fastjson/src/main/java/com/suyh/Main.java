package com.suyh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {
    static {
        System.setProperty("log4j.configurationFile", "conf/log4j2.xml");
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        readJsonArray();
        readJson();
    }

    public static void readJsonArray() throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource("conf/menu_array.json");
        String filePath = resource.getPath();
        File file = new File(filePath);
        String jsonString = FileUtils.readFileToString(file);

        JSONArray array = JSON.parseArray(jsonString);
        List<Menu> menus = JSON.parseArray(jsonString, Menu.class);
        
        String strMenus = JSON.toJSONString(menus);
        logger.info(menus.toString());
    }

    public static void readJson() throws IOException {
        String filePath = Main.class.getClassLoader().getResource("conf/menu_single.json").getPath();
        File file = new File(filePath);
        String jsonString = FileUtils.readFileToString(file);

        JSONObject obj = JSONObject.parseObject(jsonString);
        Menu menu = JSON.parseObject(jsonString, Menu.class);

        logger.info(menu.toString());
        String strMenu = JSON.toJSONString(menu);
        logger.info(strMenu);
    }
}