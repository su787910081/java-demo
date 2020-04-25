package test.com.suyh;

import com.alibaba.fastjson.JSON;
import com.suyh.Kafka03Application;
import com.suyh.constant.KafkaConstant;
import com.suyh.entity.WmsCkOmsShipmentMO;
import com.suyh.utils.KafkaUtil;
import com.suyh.utils.MQEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.suyh.constant.KafkaConstant.EVENT_WMS_SHIPMENT_OUT;
import static com.suyh.constant.KafkaConstant.TOPIC_PREFIX;


/**
 * @author 苏雲弘
 * @Description:
 * @date 2020-04-24 18:34
 */
@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = Kafka03Application.class)
public class KafkaTest {

    @Resource
    private KafkaUtil kafkaUtil;

    @Test
    public void testPushKafka() {
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return;
//        }

        WmsCkOmsShipmentMO data = makeKafkaDataByWmsOut();
        MQEvent<WmsCkOmsShipmentMO> mqEvent = new MQEvent<>(
                UUID.randomUUID().toString(), EVENT_WMS_SHIPMENT_OUT, data);
        kafkaUtil.kafkaProducerSend(
                TOPIC_PREFIX + KafkaConstant.TOPIC_WMS_ORDER,
                JSON.toJSONString(mqEvent));

        System.out.println("KafkaTest send data.");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private WmsCkOmsShipmentMO makeKafkaDataByWmsOut() {

        WmsCkOmsShipmentMO shipment = new WmsCkOmsShipmentMO();
        shipment.setOrderNo("suyh-OrderNo");
        shipment.setOrderType("suyh-OrderType");
        shipment.setPriority("urgent");


        return shipment;
    }
}
