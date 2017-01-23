package com.intellecteu.hyperledger.transformation;

import com.intellecteu.hyperledger.chaincode.common.Utils;
import com.intellecteu.hyperledger.json.account.Account;
import com.intellecteu.hyperledger.json.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import static com.intellecteu.hyperledger.json.account.Account.AccountType.NOSTRO;
import static com.intellecteu.hyperledger.json.account.Account.AccountType.VOSTRO;
import static com.intellecteu.hyperledger.transformation.MT103Transformer.MT199_TEMPLATE;
import static org.junit.Assert.assertEquals;

public class MT103TransformerTest {

    private static final String BLOCK1 = "F01SPXBUAU1XXXX0000000000";
    private static final String BLOCK2_IN = "I103SPXBUAU2XXXXN";
    private static final String BLOCK2_OUT = "O1001200970103SPXBUAU2AXXX22221234569701031201N";

    private static final String TEST_MT103_BLOCK4 = ":20:PAY/003\r\n" +
            ":23B:CRED\r\n" +
            ":32A:160926USD322,00\r\n" +
            ":33B:USD322,00\r\n" +
            ":50K:/AccountA\r\n" +
            "SENDERS ADDRESS\r\n" +
            "30222 ZURICH\r\n" +
            ":57A:SPXBUAU3\r\n" +
            ":59A:/AccountB\r\n" +
            "SPXBUAU3\r\n" +
            "RECEIVER ADDRESS\r\n" +
            "14456 GENEVA\r\n" +
            ":70:Money transfer using HL codechain\r\n" +
            ":71A:OUR\r\n" +
            ":71G:USD12,00";

    private static final String MT_MESSAGE = "{1:" + BLOCK1 + "}{2:" + BLOCK2_IN + "}{3:{119:STP}}{4:\r\n" + TEST_MT103_BLOCK4 + "\r\n-}";
    private static final String MT_MESSAGE_OUT = "{1:" + BLOCK1 + "}{2:" + BLOCK2_OUT + "}{3:{119:STP}}{4:\r\n" + TEST_MT103_BLOCK4 + "\r\n-}";

    private static final String EXPECTED_MT199 = "{1:F01SPXBUAU2XXXX0000000000}{2:I199SPXBUAU1XXXXN}{4:\r\n"
             + ":20:HL-some-uuid\r\n"
             + ":79:I'm long long comment 0123456789 0123456789\r\n"
             + "0123456789 needs to be wrapped here\r\n"
             + "-}";

    private static final String EXPECTED_MT103 = "{1:F01SPXBUAU2XXXX0000000000}{2:I103SPXBUAU3XXXXN}{3:{119:STP}}{4:\r\n"
             + ":20:PAY/003\r\n"
             + ":23B:CRED\r\n"
             + ":32A:160926USD310,00\r\n"
             + ":33B:USD310,00\r\n"
             + ":50K:/AccountA\r\n"
             + "SENDERS ADDRESS\r\n"
             + "30222 ZURICH\r\n"
             + ":52A:SPXBUAU1\r\n"
             + ":59A:/AccountB\r\n"
             + "SPXBUAU3\r\n"
             + "RECEIVER ADDRESS\r\n"
             + "14456 GENEVA\r\n"
             + ":70:Money transfer using HL codechain\r\n"
             + ":71A:OUR\r\n"
             + "-}";

    private Transaction transaction;

    @Before
    public void init() {
        Transaction.Organization sender = new Transaction.Organization("SPXBUAU1", "AccountA");
        Account.StorageKey senderAccount = new Account.StorageKey("SPXBUAU2", "SPXBUAU1", "USD", NOSTRO);
        Transaction.AccountDetail senderAccountDetail = new Transaction.AccountDetail(senderAccount);

        Transaction.Organization receiver = new Transaction.Organization("SPXBUAU3", "AccountB");
        Account.StorageKey receiverAccount = new Account.StorageKey("SPXBUAU2", "SPXBUAU3", "USD", VOSTRO);
        Transaction.AccountDetail receiverAccountDetail = new Transaction.AccountDetail(receiverAccount);

        //Transaction
        Transaction.TransferringOrganization transferringOrganization = new Transaction.TransferringOrganization(
                "SPXBUAU2", senderAccountDetail, receiverAccountDetail, "12.00", "322.00");
        Transaction.Details details = new Transaction.Details(Utils.base64Encode(MT_MESSAGE));
        transaction = new Transaction(sender, receiver, transferringOrganization, null, null, details);
    }

    @Test
    public void parseInputMT() throws Exception {
        Transaction transaction = MT103Transformer.parseInputMT(MT_MESSAGE);
        assertEquals(this.transaction, transaction);

        transaction = MT103Transformer.parseInputMT(MT_MESSAGE_OUT);
        this.transaction.getDetails().setInputMessage(Utils.base64Encode(MT_MESSAGE_OUT));
        assertEquals(this.transaction, transaction);
    }

    @Test
    public void generateOutputMessage() throws Exception {
        transaction.setTransactionStatus(new Transaction.TransactionStatus(
                Transaction.TransactionStatus.Status.SUCCESS,
                Utils.base64Encode("I'm long long comment 0123456789 0123456789 0123456789 needs to be wrapped here")));
        transaction.setTransactionId("some-uuid");
        transaction.getTransferring().getSenderAccount().setAccountState(new Transaction.AccountState("123.0", "USD"));
        Transaction outputTransaction = MT103Transformer.generateOutputMessage(transaction);
        assertEquals(EXPECTED_MT103, Utils.base64Decode(outputTransaction.getDetails().getOutputMessage()));
        transaction.setTransactionStatus(new Transaction.TransactionStatus(
                Transaction.TransactionStatus.Status.FAILURE,
                Utils.base64Encode("I'm long long comment 0123456789 0123456789 0123456789 needs to be wrapped here")));
        outputTransaction = MT103Transformer.generateOutputMessage(transaction);
        assertEquals(EXPECTED_MT199, Utils.base64Decode(outputTransaction.getDetails().getOutputMessage()));
    }

    @Test
    public void prepareMT103() {
        transaction.setTransactionStatus(new Transaction.TransactionStatus(
                Transaction.TransactionStatus.Status.SUCCESS,
                Utils.base64Encode("I'm long long comment 0123456789 0123456789 0123456789 needs to be wrapped here")));
        transaction.setTransactionId("some-uuid");
        transaction.getTransferring().getSenderAccount().setAccountState(new Transaction.AccountState("123.0", "USD"));

        assertEquals(EXPECTED_MT103, MT103Transformer.prepareMT103(transaction));
    }

    @Test
    public void prepareMT199() {
        transaction.setTransactionStatus(new Transaction.TransactionStatus(
                Transaction.TransactionStatus.Status.FAILURE,
                Utils.base64Encode("I'm long long comment 0123456789 0123456789 0123456789 needs to be wrapped here")));
        transaction.setTransactionId("some-uuid");
        assertEquals(EXPECTED_MT199, MT103Transformer.prepareMT199(transaction, MT199_TEMPLATE));
    }

    @Test
    public void preValidateMT() throws Exception {
        MT103Transformer.preValidateMTMessage(MT_MESSAGE);
        MT103Transformer.preValidateMTMessage(MT_MESSAGE_OUT);
    }

    @Test
    public void getIntermediaryBIC() throws Exception {
        assertEquals("SPXBUAU3", MT103Transformer.getIntermediaryBIC(TEST_MT103_BLOCK4));
    }

    @Test
    public void getCredAccount() throws Exception {
        assertEquals("AccountA", MT103Transformer.getCredAccount(TEST_MT103_BLOCK4));
    }

    @Test
    public void getBenAccount() throws Exception {
        assertEquals("AccountB", MT103Transformer.getBenAccount(TEST_MT103_BLOCK4));
    }

    @Test
    public void getTransferAmount() throws Exception {
        assertEquals("322.00", MT103Transformer.getTransferAmount(TEST_MT103_BLOCK4));
    }

    @Test
    public void getTransferCurrency() throws Exception {
        assertEquals("USD", MT103Transformer.getTransferCurrency(TEST_MT103_BLOCK4));
    }

    @Test
    public void getInstructedAmount() throws Exception {
        assertEquals("322.00", MT103Transformer.getInstructedAmount(TEST_MT103_BLOCK4));
    }

    @Test
    public void getTransferFee() throws Exception {
        assertEquals("12.00", MT103Transformer.getTransferFee(TEST_MT103_BLOCK4));
    }

}