package com.intellecteu.hyperledger.transformation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MT parser, which doesn't use MT-XML converter and simply relies on RJE format.
 */
public class MTGenericParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MTGenericParser.class);
    private static final String BLOCK_START = "{";
    private static final String BLOCK_END = "}";
    private static final String BLOCK4_START = "{4:";
    private static final String BLOCK4_END = "-}";
    private static final String TAG_END = "\n:";

    public static String getBlock(String mtMessage, int number) {
        try {
            String blockStart = BLOCK_START + number + ":";
            int blockStartIndex = mtMessage.indexOf(blockStart);
            return mtMessage.substring(blockStartIndex + blockStart.length(), mtMessage.indexOf(BLOCK_END, blockStartIndex));
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.warn("IndexOutOfBoundsException occurred while parsing block {} from message: {}", number, mtMessage, ex);
            return null;
        }
    }
    
    public static String getBlock4(String mtMessage) {
        return mtMessage.contains(BLOCK4_START) && mtMessage.contains(BLOCK4_END) ? mtMessage
                .substring(mtMessage.indexOf(BLOCK4_START) + BLOCK4_START.length(), mtMessage.indexOf(BLOCK4_END))
                .trim() : null;
    }

    public static String getSender(String mtMessage) {
        String block1 = getBlock(mtMessage, 1);
        if (block1 != null) {
            try {
                return block1.substring(3, 3 + 8);
            } catch (IndexOutOfBoundsException ex) {
                LOGGER.warn("IndexOutOfBoundsException occurred while getting sender from block1: {}", block1, ex);
                return null;
            }
        }
        return null;
    }

    public static String getReceiver(String mtMessage) {
        String block2 = getBlock(mtMessage, 2);
        if (block2 != null) {
            switch (block2.length()) {
                case 17:
                case 21:
                    return block2.substring(4, 4 + 8);
                case 47:
                    return block2.substring(14, 14 + 8);
                default:
                    LOGGER.warn("Unknown message type from block2: {} length={}", block2, block2.length());
                    return null;

            }
        }
        return null;
    }

    public static String getTag(String block4, String tag) {
        tag = ":" + tag + ":";
        if (block4.contains(tag)) {
            int tagStart = block4.indexOf(tag) + tag.length();
            int tagEnd = block4.indexOf(TAG_END, tagStart);
            tagEnd = tagEnd > 0 ? tagEnd : block4.length();
            return block4.substring(tagStart, tagEnd).trim();
        } else {
            return null;
        }
    }

    public static String getAccount(String block4, String tag) throws ValidationException {
        String account = MTGenericParser.getTag(block4, tag);
        if (StringUtils.isEmpty(account) || account.length() <= 1) {
            throw new ValidationException(tag + " field is missed or has incorrect format.");
        }
        return account.substring(1, account.indexOf("\n")).trim();
    }

    public static String getAmount(String block4, String tag, int prefixLength) throws ValidationException {
        String amountString = MTGenericParser.getTag(block4, tag);
        if (StringUtils.isEmpty(amountString) || amountString.length() <= prefixLength) {
            throw new ValidationException(tag + " field is missed or has incorrect format.");
        }
        return amountString.substring(prefixLength).replace(",", ".");
    }

    public static String getCurrency(String block4, String tag, int prefixLength) throws ValidationException {
        String currAmountString = MTGenericParser.getTag(block4, tag);
        if (StringUtils.isEmpty(currAmountString) || currAmountString.length() <= prefixLength) {
            throw new ValidationException(tag + " field is missed or has incorrect format.");
        }
        return currAmountString.substring(prefixLength, prefixLength + 3);
    }
    
    static String toSwiftCurrency(String currency){
        return currency.replace(".", ",");
    }
}
