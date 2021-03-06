package id.ac.tazkia.payment.bnisyariah.ecollection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.tazkia.payment.bnisyariah.ecollection.dto.VaPayment;
import id.ac.tazkia.payment.bnisyariah.ecollection.entity.AccountType;
import id.ac.tazkia.payment.bnisyariah.ecollection.entity.RequestType;
import id.ac.tazkia.payment.bnisyariah.ecollection.entity.VirtualAccountRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaListenerServiceTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerServiceTests.class);

    @Value("${kafka.topic.va.request}") private String topic;
    @Value("${bni.client-id}") private String clientId;
    @Value("${bni.client-prefix}") private String clientPrefix;
    @Value("${bni.bank-id}") private String bankId;

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testSendVaRequests() throws Exception {
        VirtualAccountRequest vaRequest = VirtualAccountRequest
                .builder()
                .requestTime(LocalDateTime.now())
                .accountNumber(clientPrefix+clientId+"08123456789012")
                .invoiceNumber("01234567890")
                .requestType(RequestType.CREATE)
                .accountType(AccountType.CLOSED)
                .amount(BigDecimal.valueOf(100000.00))
                .description("Tagihan Test")
                .email("endy@tazkia.ac.id")
                .expireDate(LocalDate.now().plusMonths(1))
                .name("Endy Muhardin")
                .phone("081234567890")
                .build();

        String vaJson = objectMapper.writeValueAsString(vaRequest);
        LOGGER.debug("VA Request JSON : {}", vaJson);

        //kafkaTemplate.send(topic, vaJson);
        LOGGER.debug("Va Request Sent");
    }

    @Test
    public void testSendPaymentNotification() throws Exception {
        VaPayment vaPayment = new VaPayment();
        vaPayment.setBankId(bankId);
        vaPayment.setInvoiceNumber("2018032312000276");
        vaPayment.setAccountNumber("8311121519076000");
        vaPayment.setAmount(new BigDecimal("1000000"));
        vaPayment.setCumulativeAmount(new BigDecimal("1000000"));
        vaPayment.setPaymentTime(LocalDateTime.of(2018,4,5,9,38,8));
        vaPayment.setReference("729973");
        String jsonData = objectMapper.writeValueAsString(vaPayment);
        System.out.println("JSON Data :"+jsonData);
    }
}
