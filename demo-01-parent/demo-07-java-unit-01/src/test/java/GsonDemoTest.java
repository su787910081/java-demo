import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GsonDemoTest {

    @Test
    public void test01() {
        Gson gson = new Gson();
        int i = gson.fromJson("100", int.class);
        Assert.assertEquals("整数转换错误", 100, i);

        double d = gson.fromJson("99.99", double.class);
        // 双精度浮点数需要给定一个误差值
        Assert.assertEquals("浮点数转换错误", 99.99, d, 0.01);

        boolean b = gson.fromJson("true", boolean.class);
        Assert.assertTrue("boolean 类型转换错误", b);

        String str = gson.fromJson("string", String.class);
        Assert.assertEquals("字符串类型转换错误", "string", str);
    }

    @Test
    public void test02() {
        Gson gson = new Gson();
        String jsonNumber = gson.toJson(100);
        Assert.assertEquals("100", jsonNumber);

        String jsonBoolean = gson.toJson(false);
        Assert.assertEquals("false", jsonBoolean);
    }

    @Test
    public void test03() {
        User user = new User();
        user.setName("zhangsan");
        user.setAge(18);
        user.setEmailAddress("address@163.com");

        // 将user 对象序列化为一个字符串
        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);
        System.out.println("jsonUser string: " + jsonUser);

        // 反序列化
        User userJson = gson.fromJson(jsonUser, User.class);
        System.out.println("userJson: " + userJson);
    }

    @Test
    public void test04() {
        User user1 = new User();
        user1.setName("zhangsan_01");
        user1.setAge(18);
        user1.setEmailAddress("zhangsan_01@163.com");

        User user2 = new User();
        user2.setName("zhangsan_02");
        user2.setAge(20);
        user2.setEmailAddress("zhangsan_02@163.com");

        User user3 = new User();
        user3.setName("zhangsan_03");
        user3.setAge(22);
        user3.setEmailAddress("zhangsan_03@163.com");

        List<User> users = Arrays.asList(user1, user2, user3);
        Gson gson = new Gson();
        String jsonUsers = gson.toJson(users);
        System.out.println("List<User>: " + jsonUsers);
    }

    @Test
    public void test05() {
        User user = new User();
        user.setName("zhangsan");
        user.setAge(18);
        user.setEmailAddress("address@163.com");
        user.setToday(new Date());

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String userGson = gson.toJson(user);
        System.out.println("Date format result: " + userGson);

        User userObject = gson.fromJson(userGson, User.class);
        System.out.println("Date format result: " + userObject);

    }
}

class User {
    // 指定一个序列化与反序列化的key
    @SerializedName("userName")
    private String name;

    // 可以同时指定多个，这多个都解析到这个字段。
    @SerializedName(value = "userAge", alternate = {"age", "peopleAge"})
    private int age;

    // 该字段不参与序列化与反序列化
    @Expose(serialize = false, deserialize = false)
    private String emailAddress;

    private Date today;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", emailAddress='" + emailAddress + '\'' +
                ", today=" + today +
                '}';
    }
}