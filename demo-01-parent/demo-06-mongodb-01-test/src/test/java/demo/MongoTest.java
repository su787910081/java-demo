package demo;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoTest {
    private static MongoDatabase mongoDb;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> c;

    // 连接到mongo 数据库
    private static void init() {
        MongoClientOptions.Builder mongoOptions = MongoClientOptions.builder();
        mongoOptions.connectTimeout(5000);
        mongoOptions.connectionsPerHost(10);
        mongoOptions.minConnectionsPerHost(2);
        mongoOptions.socketTimeout(30000);
        mongoOptions.threadsAllowedToBlockForConnectionMultiplier(
                Runtime.getRuntime().availableProcessors() + 1);
        mongoOptions.readPreference(ReadPreference.primary());

        try {
            ArrayList<ServerAddress> servers = new ArrayList<>();
            ServerAddress server = new ServerAddress("192.168.252.91", 27018);
            servers.add(server);
            CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                    MongoClient.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(
                            PojoCodecProvider.builder().automatic(true).build()));
            mongoOptions.codecRegistry(codecRegistry);
            MongoCredential credential = MongoCredential.createCredential(
                    "mongo_user", "mongo_dbname", "mongo_pwd".toCharArray());
            MongoClient mongo = new MongoClient(servers, credential, mongoOptions.build());
            mongoDb = mongo.getDatabase("mongo_dbname");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 连接到指定的集合
    private static MongoCollection<Document> connColl(String name) {
        MongoCollection<Document> c = mongoDb.getCollection(name);
        c.withWriteConcern(WriteConcern.MAJORITY);
        return c;
    }

    @BeforeClass
    public static void beforeClass() {
        collection = connColl("test");
        c = collection;
    }

    @Before
    public void before() {

    }

    @Test
    public void test01() {
        Document document = new Document();
        document.put("hello", "world");

        collection.deleteMany(Filters.eq("hello", "world"));

        long countBefore = collection.countDocuments();
        collection.insertOne(document);
        long countAfter = collection.countDocuments();
        Assert.assertEquals(countBefore + 1, countAfter);
        Document d = collection.findOneAndDelete(document);
        Assert.assertEquals(document, d);
        Assert.assertEquals(countBefore, collection.countDocuments());
    }

    /**
     * 数据查询，包含其中一个元素
     *
     * @throws Exception
     */
    @Test
    public void testQueryArrayElement() throws Exception {
        Document document = new Document();
        document.put("arr", Arrays.asList("a", "b", "c"));
        document.put("arr2", Arrays.asList("t1", "t2", "t3"));

        // 插入测试数据之前先删除原先存在的数据
        Bson eqDel = Filters.eq("arr", "a");
        collection.deleteMany(eqDel);
        collection.insertOne(document);

        FindIterable<Document> documents = null;
        Document first = null;

        Bson eqArr = Filters.eq("arr", "a");
        documents = collection.find(eqArr);
        first = documents.first();

        Bson eqArr2 = Filters.eq("arr2", "t1");
        documents = collection.find(eqArr2);
        first = documents.first();
        Assert.assertNotNull(first);

        List<String> arr2 = first.getList("arr2", String.class);
        Assert.assertEquals(3, arr2.size());
    }

    /**
     * 数组查询，包含其中的部分或者全部元素
     *
     * @throws Exception
     */
    @Test
    public void testManyQuery() throws Exception {
        // 测试数据 key-value
        String fruitsKey = "fruits";
        List<String> fruitValues = Arrays.asList("apple", "banana", "orange");

        // 删除后插入一条测试数据
        Document document = new Document();
        document.put(fruitsKey, fruitValues);

        Bson eqAll = Filters.all(fruitsKey, fruitValues);
        collection.deleteMany(eqAll);

        collection.insertOne(document);

        // 查询包含每一个数组元素
//        Filters.all(fruitsKey, "apple", "banana");
        FindIterable<Document> documentsAll = collection.find(eqAll);

        Document first = documentsAll.first();
        Assert.assertNotNull(first);
        List<String> fruits = first.getList(fruitsKey, String.class);
        Assert.assertNotNull(fruitsKey);
        Assert.assertEquals(fruitValues.size(), fruits.size());
    }

    /**
     * 向Document 的数组中追加元素
     * $push: 元素可以重复
     * $addToSet: 元素不可以重复
     * 将Document 数据中的元素删除
     * $pop: 从两端删除，$key {1: 从尾部删除，-1: 从头部删除}
     * $pull: 删除一个指定元素(与$addToSet相对应)
     */
    @Test
    public void testArrayAdd() {
        String fruitsKey = "fruits";
        String dogsKey = "dogs";

        Bson filtersId = Filters.eq("_id", "testArrayAdd");
        c.deleteMany(filtersId);
        Document first = c.find(filtersId).first();
        Assert.assertNull("未能正确删除测试数据", first);

        // 插入一条测试数据
        Document doc = new Document("_id", "testArrayAdd")
                .append(fruitsKey, Arrays.asList("apple", "banana"))
                // "labrador" "akita"
                .append(dogsKey, Arrays.asList("teddy", "husky"));
        c.insertOne(doc);
        first = c.find(filtersId).first();
        Assert.assertNotNull("插入数据出错", first);
        List<String> fruitsList = first.getList(fruitsKey, String.class);
        Assert.assertNotNull("集合中数据错误", fruitsList);

        UpdateResult result = null;

        // 通过$push 向数组中追加一条数组元素
        Bson docPushFruits = Updates.push(fruitsKey, "apple");
        Bson docPushDogs = Updates.push(dogsKey, "labrador");
        // 合并两个条件
        Bson docPushCombine = Updates.combine(docPushFruits, docPushDogs);
        result = c.updateOne(filtersId, docPushCombine);
        Assert.assertEquals("匹配数量错误", 1, result.getMatchedCount());
        Assert.assertEquals("更新数据错误", 1, result.getModifiedCount());
        result = null;

        // 期望，原来数组中的元素不变，同时会多出一个元素
        // 即使多出来的这个元素在都让狗日数组中已经存在的
        MongoCursor<Document> cursor = null;

        // "apple" "banana", "apple"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, fruitsKey, 3);
        // "teddy", "husky", "labrador"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, dogsKey, 3);

        // 从尾部将这个添加的元素删除，使用$pop
        Bson popFruits = Updates.popLast(fruitsKey);
        Bson popDogs = Updates.popLast(dogsKey);
        Bson docPop = Updates.combine(popFruits, popDogs);
        result = c.updateOne(filtersId, docPop);
        Assert.assertEquals("匹配数量错误", 1, result.getMatchedCount());
        Assert.assertEquals("更新数据错误", 1, result.getModifiedCount());
        result = null;

        // "applie", "banana"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, fruitsKey, 2);
        // "teddy", "husky"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, dogsKey, 2);

        // 使用$addToSet 添加一个已民存在的元素
        Bson addApple = Updates.addToSet(fruitsKey, "apple");
        Bson addTeddy = Updates.addToSet(dogsKey, "teddy");
        Bson addExists = Updates.combine(addApple, addTeddy);
        result = c.updateOne(filtersId, addExists);
        // 没有插入成功，这里返回的结果是未匹配和无王维新记录
        Assert.assertEquals("匹配数量错误", 0, result.getMatchedCount());
        Assert.assertEquals("不应该有记录更新", 0, result.getModifiedCount());
        result = null;

        // "apple", "banana"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, fruitsKey, 2);
        // “teddy", "husky"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, dogsKey, 2);

        // 使用$addToSet 添加一个不存在的元素
        Bson addOrange = Updates.addToSet(fruitsKey, "orange");
        Bson addLabrador = Updates.addToSet(dogsKey, "labrador");
        Bson adds = Updates.combine(addOrange, addLabrador);
        result = c.updateOne(filtersId, adds);

        Assert.assertEquals("匹配数量错误", 1, result.getMatchedCount());
        Assert.assertEquals("更新数据错误", 1, result.getModifiedCount());
        result = null;

        // "apple", "banana", "orange"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, fruitsKey, 3);
        // “teddy", "husky", "labrador"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, dogsKey, 3);

        // 使用$addToSet 添加多个元素，重复数据不会添加
        Bson setId = Updates.set("name", "tempName");
        Bson addFruits = Updates.addEachToSet(fruitsKey, Arrays.asList(
                "apple", "banana", "plum", "pear", "grape"));
        Bson addDogs = Updates.addEachToSet(dogsKey, Arrays.asList(
                "teddy", "husky", "labrador", "akita"));
        adds = Updates.combine(setId, addFruits, addDogs);
        result = c.updateOne(filtersId, adds);
        Assert.assertEquals("匹配数量错误", 1, result.getMatchedCount());
        Assert.assertEquals("更新数据错误", 1, result.getModifiedCount());
        result = null;

        cursor = c.find(filtersId).iterator();
        // "apple", "banana", "orange", "plum", "pear", "grape"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, fruitsKey, 6);
        // “teddy", "husky", "labrador", "akita"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, dogsKey, 4);

        // 使用$pull 从数组中删除一个指定元素
        result = c.updateOne(filtersId,
                Updates.pull(fruitsKey, "orange"));
        Assert.assertEquals("匹配数量错误", 1, result.getModifiedCount());
        Assert.assertEquals("更新数据错误", 1, result.getModifiedCount());
        result = null;

        // "apple", "banana", "orange", "plum", "pear", "grape"
        cursor = c.find(filtersId).iterator();
        updateResult(cursor, fruitsKey, 5);
    }

    /**
     * 相同的代码提取出来
     * 更新之后的结果断言
     *
     * @param cursor         更新之后从数据库中查询 的结果
     * @param key            数组元素的key
     * @param expectedRecord 期望数组元素个数
     */
    private void updateResult(MongoCursor<Document> cursor, String key, int expectedRecord) {
        try {
            int count = 0;
            while (cursor.hasNext()) {
                ++count;
                Assert.assertEquals("有且仅有一条数据", 1, count);
                Document docTemp = cursor.next();
                List<String> fruitsTemp = docTemp.getList(key, String.class);
                Assert.assertNotNull("未找到这个元素数据", fruitsTemp);
                Assert.assertEquals("数组元素数量错误", expectedRecord, fruitsTemp.size());
            }
            Assert.assertNotEquals("有且仅有一条数据", 0, count);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        } finally {
            cursor.close();
        }
    }

    // 测试重复插入唯一索引的数据记录
    @Test
    public void testDuplicateKey() {
        MongoCollection<Document> coll = c;
        final String keyValue = "testDuplicateKey";

        coll.deleteOne(Filters.eq("_id", keyValue));
        coll.insertOne(new Document("_id", keyValue));

        try {
            coll.insertOne(new Document("_id", keyValue).append("fieldOther", "value"));
            Assert.assertTrue("不允许重复插入相同key", false);
        } catch (MongoWriteConcernException e) {
            // 插入相同key 将捕获重复key 异常
            ErrorCategory error = ErrorCategory.fromErrorCode(e.getCode());
            Assert.assertEquals(error, ErrorCategory.DUPLICATE_KEY);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    /**
     * 仅更新指定字段的值
     * 特别地：如果要同时更新多个字段需要使用combine
     */
    @Test
    public void testUpdateDocument() {
        String id = "testUpdateDocument";
        MongoCollection<Document> coll = c;
        coll.deleteOne(Filters.eq("_id", id));

        Document doc = new Document("_id", id)
                .append("fieldName1", "value1")
                .append("fieldName2", "value2")
                .append("fieldName3", "value3");

        coll.insertOne(doc);

        // 更新多个字段；fieldName1 fieldName2
        UpdateResult result = coll.updateOne(Filters.eq("_id", id),
                Updates.combine(Updates.set("fieldName1", "valueOther1"),
                        Updates.set("fieldName2", "valueNew2")));

        Assert.assertEquals("更新匹配的文档数错误",
                1L, result.getMatchedCount());
        Assert.assertEquals("更新修改的文档数错误",
                1L, result.getModifiedCount());
        Assert.assertTrue("获取一个值，该值指示修改的计数是否可用",
                result.isModifiedCountAvailable());

        Document first = coll.find(Filters.eq("_id", id)).first();

        Assert.assertNotNull(first);

        String fieldName1 = first.getString("fieldName1");
        String fieldName2 = first.getString("fieldName2");
        String fieldName3 = first.getString("fieldName3");

        Assert.assertNotNull(fieldName1);
        Assert.assertNotNull(fieldName2);
        Assert.assertNotNull(fieldName3);
        Assert.assertEquals("valueOther1", fieldName1);
        Assert.assertEquals("valueNew2", fieldName2);
        Assert.assertEquals("value3", fieldName3);

        // 更新一个字段
    }

    // 替换，整个文档完全替换
    @Test
    public void testReplaceDocument() {
        String id = "testReplaceDocument";
        MongoCollection<Document> coll = c;
        coll.deleteOne(Filters.eq("_id", id));

        Document docSource = new Document("_id", id)
                .append("fieldName1", "value1")
                .append("fieldName2", "value2");

        coll.insertOne(docSource);

        Document docReplace = new Document("_id", id)
                .append("field", "nothing");
        UpdateResult result = coll.replaceOne(Filters.eq("_id", id), docReplace);

        Assert.assertEquals("替换匹配的文档数错误",
                1L, result.getMatchedCount());
        Assert.assertEquals("替换修改的文档数错误",
                1L, result.getModifiedCount());
        Assert.assertTrue("获取一个值，该值指示修改的计数是否可用",
                result.isModifiedCountAvailable());

        Document first = coll.find(Filters.eq("_id", id)).first();

        Assert.assertNotNull(first);

        String fieldName1 = first.getString("fieldName1");
        String fieldName2 = first.getString("fieldName2");
        String fieldName3 = first.getString("fieldName3");
        String field = first.getString("field");

        Assert.assertNull(fieldName1);
        Assert.assertNull(fieldName2);
        Assert.assertNull(fieldName3);
        Assert.assertNotNull(field);
        Assert.assertEquals("nothing", field);
    }
}















