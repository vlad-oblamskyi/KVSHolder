package com.intellecteu.hyperledger.transformation;

import org.junit.Assert;
import org.junit.Test;


public class MTGenericParserTest {
    
    private static final String BLOCK1 = "F01SPXBUAU1XXXX0000000000";
    private static final String BLOCK2_IN = "I103SPXBUAU2XXXXN";
    private static final String BLOCK2_OUT = "O1001200970103SPXBUAU2AXXX22221234569701031201N";
    
    private static final String TEST_MT103_BLOCK4 = ":20:PAY/003\n" +
            ":23B:CRED\n" +
            ":32A:160926USD322,00\n" +
            ":33B:USD322,00\n" +
            ":50K:/AccountA\n" +
            "SENDERS ADDRESS\n" +
            "30222 ZURICH\n" +
            ":57A:SPXBUAU3\n" +
            ":59A:/AccountB\n" +
            "RECEIVER ADDRESS\n" +
            "14456 GENEVA\n" +
            ":70:Money transfer using HL codechain\n" +
            ":71A:OUR\n" +
            ":71G:USD12,00";
    private static final String MT_MESSAGE = "{1:" + BLOCK1 + "}{2:" + BLOCK2_IN + "}{3:{119:STP}}{4:\n" + TEST_MT103_BLOCK4 + "\n-}";
    private static final String MT_MESSAGE_OUT = "{1:" + BLOCK1 + "}{2:" + BLOCK2_OUT + "}{3:{119:STP}}{4:\n" + TEST_MT103_BLOCK4 + "\n-}";
    
    @Test
    public void getBlock() {
        Assert.assertEquals(BLOCK1, MTGenericParser.getBlock(MT_MESSAGE, 1));
        Assert.assertEquals(BLOCK2_IN, MTGenericParser.getBlock(MT_MESSAGE, 2));
        Assert.assertNull(MTGenericParser.getBlock("Non MT message", 2));
    }

    @Test
    public void getBlock4() throws Exception {
        Assert.assertEquals(TEST_MT103_BLOCK4, MTGenericParser.getBlock4(MT_MESSAGE));
    }    
    
    @Test
    public void getReceiver() throws Exception {
        Assert.assertEquals("SPXBUAU2", MTGenericParser.getReceiver(MT_MESSAGE));
        Assert.assertEquals("SPXBUAU2", MTGenericParser.getReceiver(MT_MESSAGE_OUT));
    }

    @Test
    public void getSender() throws Exception {
        Assert.assertEquals("SPXBUAU1", MTGenericParser.getSender(MT_MESSAGE));
        
    }
    

    @Test
    public void getTag() throws Exception {
        Assert.assertEquals("/AccountA\nSENDERS ADDRESS\n30222 ZURICH", MTGenericParser.getTag(TEST_MT103_BLOCK4, "50K"));
        Assert.assertEquals("SPXBUAU3", MTGenericParser.getTag(TEST_MT103_BLOCK4, "57A"));
        Assert.assertEquals("USD12,00", MTGenericParser.getTag(TEST_MT103_BLOCK4, "71G"));
        Assert.assertNull(MTGenericParser.getTag(TEST_MT103_BLOCK4, "70F"));
    }

    @Test
    public void getAccount() throws Exception {
        Assert.assertEquals("AccountA", MTGenericParser.getAccount(TEST_MT103_BLOCK4, "50K"));
        Assert.assertEquals("AccountB", MTGenericParser.getAccount(TEST_MT103_BLOCK4, "59A"));
    }

    @Test
    public void getAmount() throws Exception {
        Assert.assertEquals("322.00", MTGenericParser.getAmount(TEST_MT103_BLOCK4, "33B", 3));
        Assert.assertEquals("322.00", MTGenericParser.getAmount(TEST_MT103_BLOCK4, "32A", 9));
        Assert.assertEquals("12.00", MTGenericParser.getAmount(TEST_MT103_BLOCK4, "71G", 3));
    }    
    
    @Test
    public void getCurrency() throws Exception {
        Assert.assertEquals("USD", MTGenericParser.getCurrency(TEST_MT103_BLOCK4, "33B", 0));
        Assert.assertEquals("USD", MTGenericParser.getCurrency(TEST_MT103_BLOCK4, "32A", 6));
        Assert.assertEquals("USD", MTGenericParser.getCurrency(TEST_MT103_BLOCK4, "71G", 0));
    }
}