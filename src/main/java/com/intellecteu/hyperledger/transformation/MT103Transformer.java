package com.intellecteu.hyperledger.transformation;


import java.math.BigDecimal;

import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.intellecteu.hyperledger.chaincode.common.Utils;
import com.intellecteu.hyperledger.json.account.Account;
import com.intellecteu.hyperledger.json.transaction.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.intellecteu.hyperledger.json.account.Account.AccountType.NOSTRO;
import static com.intellecteu.hyperledger.json.account.Account.AccountType.VOSTRO;
import static com.intellecteu.hyperledger.transformation.MTGenericParser.getReceiver;
import static com.intellecteu.hyperledger.transformation.MTGenericParser.toSwiftCurrency;

/**
 * MT 103 parser - it relies on specific RJE format.
 */
public class MT103Transformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MT103Transformer.class);

    private static final int MAX_FREE_FORMAT_FIELD_LENGTH = 50;
    static final String MT199_TEMPLATE = "{1:F01[[SENDER]]XXXX0000000000}{2:I199[[RECEIVER]]XXXXN}{4:\r\n"
                                                 + ":20:HL-[[TX-ID]]\r\n"
                                                 + ":79:[[COMMENT]]\r\n"
                                                 + "-}";

    /**
     * Parses input MT message and based on
     *
     * @param mtMessage input mt message
     * @return pre-filled transaction object
     */
    public static Transaction parseInputMT(String mtMessage) {
        Transaction transaction = new Transaction();
        try {
            preValidateMTMessage(mtMessage);
            String block4 = MTGenericParser.getBlock4(mtMessage);
            // This is not a misprint intermediary of the transaction is a receiver of the message.
            String intermediaryBIC = getReceiver(mtMessage);

            String amount = getTransferAmount(block4);
            String currency = getTransferCurrency(block4);
            String fee = getTransferFee(block4);

            //Sender
            String senderBIC = MTGenericParser.getSender(mtMessage);
            String senderAcc = getCredAccount(block4);
            Transaction.Organization sender = new Transaction.Organization(senderBIC, senderAcc);
            Account.StorageKey senderAccount = new Account.StorageKey(intermediaryBIC, senderBIC, currency, NOSTRO);
            Transaction.AccountDetail senderAccountDetail = new Transaction.AccountDetail(senderAccount);

            //Receiver
            // This is not a misprint final receiver of the transaction is the intermediary of the message.
            String receiverBIC = getIntermediaryBIC(block4);
            String receiverAcc = getBenAccount(block4);
            Transaction.Organization receiver = new Transaction.Organization(receiverBIC, receiverAcc);
            Account.StorageKey receiverAccount = new Account.StorageKey(intermediaryBIC, receiverBIC, currency, VOSTRO);
            Transaction.AccountDetail receiverAccountDetail = new Transaction.AccountDetail(receiverAccount);

            //Transaction
            Transaction.TransferringOrganization transferringOrganization = new Transaction.TransferringOrganization(intermediaryBIC, senderAccountDetail,
                                                                                                                     receiverAccountDetail, fee, amount);
            Transaction.Details details = new Transaction.Details(Utils.base64Encode(mtMessage));

            transaction.setSender(sender);
            transaction.setReceiver(receiver);
            transaction.setTransferring(transferringOrganization);
            transaction.setDetails(details);
        } catch (Exception ex) {
            LOGGER.error("Unhandled exception occurred while parsing input MT {}", mtMessage, ex);
            transaction.setTransactionStatus(new Transaction.TransactionStatus(Transaction.TransactionStatus.Status.FAILURE,
                    Utils.base64Encode(ex.getMessage())));
        }
        return transaction;
    }

    /**
     * Parses {@link Transaction.TransactionStatus} from input transaction
     * and based on the info this status creates and populates output message
     *
     * @param transaction validated transaction
     * @return transaction object with updated {@link Transaction.Details#getOutputMessage()}
     */
    public static Transaction generateOutputMessage(Transaction transaction) {
        Preconditions.checkNotNull("Transaction status object can't be null", transaction.getTransactionStatus());
        Preconditions.checkNotNull("Transaction status can't be null", transaction.getTransactionStatus().getStatus());
        Preconditions.checkNotNull("Transaction ID can't be null", transaction.getTransactionId());
        String outputMessage;
        switch (transaction.getTransactionStatus().getStatus()) {
            case SUCCESS:
                outputMessage = prepareMT103(transaction);
                break;
            default:
                outputMessage = prepareMT199(transaction, MT199_TEMPLATE);
        }
        transaction.getDetails().setOutputMessage(Utils.base64Encode(outputMessage));
        return transaction;
    }

    public static String prepareMT103(Transaction transaction) {
        String outputMessage = Utils.base64Decode(transaction.getDetails().getInputMessage());
        String sender = transaction.getSender().getBic();
        String receiver = transaction.getReceiver().getBic();
        String intermediaryBIC = transaction.getTransferring().getBic();
        String currency = transaction.getTransferring().getSenderAccount().getAccountKey().getCurrency();
        String amount = transaction.getTransferring().getAmount();
        String fee = transaction.getTransferring().getFee();
        BigDecimal newAmount = (new BigDecimal(amount)).subtract(new BigDecimal(fee));
        return outputMessage
                .replace(intermediaryBIC, receiver)
                .replace(sender, intermediaryBIC)
                .replace(":57A:" + receiver, ":52A:" + sender)
                .replace(toSwiftCurrency(amount), toSwiftCurrency(newAmount.toString()))
                .replace(":71G:" + currency + toSwiftCurrency(fee) + "\r\n", "");
    }

    public static String prepareMT199(Transaction transaction, String template) {
        Preconditions.checkNotNull("Transaction ID can't be null", transaction.getTransactionId());
        String sender = transaction.getTransferring().getBic();
        String receiver = transaction.getSender().getBic();
        String transactionId = transaction.getTransactionId();
        String comment = transaction.getTransactionStatus().getComment();
        comment = comment != null ? WordUtils.wrap(Utils.base64Decode(comment), MAX_FREE_FORMAT_FIELD_LENGTH, "\r\n", true) : "";
        return template
                .replaceAll("\\[\\[SENDER\\]\\]", sender)
                .replaceAll("\\[\\[RECEIVER\\]\\]", receiver)
                .replaceAll("\\[\\[TX-ID\\]\\]", transactionId)
                .replaceAll("\\[\\[COMMENT\\]\\]", comment);
    }

    public static void preValidateMTMessage(String mtMessage) throws ValidationException {
        if (StringUtils.isEmpty(mtMessage)) {
            throw new ValidationException("MT message is empty");
        }
        if (StringUtils.isEmpty(MTGenericParser.getBlock4(mtMessage))) {
            throw new ValidationException("MT message has no block 4");
        }
    }

    public static String getIntermediaryBIC(String block4) throws ValidationException {
        return MTGenericParser.getTag(block4, "57A");
    }

    public static String getCredAccount(String block4) throws ValidationException {
        return MTGenericParser.getAccount(block4, "50K");
    }

    public static String getBenAccount(String block4) throws ValidationException {
        return MTGenericParser.getAccount(block4, "59A");
    }

    public static String getTransferAmount(String block4) throws ValidationException {
        return MTGenericParser.getAmount(block4, "32A", 9);
    }

    public static String getTransferCurrency(String block4) throws ValidationException {
        return MTGenericParser.getCurrency(block4, "32A", 6);
    }

    public static String getInstructedAmount(String block4) throws ValidationException {
        return MTGenericParser.getAmount(block4, "33B", 3);
    }

    public static String getTransferFee(String block4) throws ValidationException {
        return MTGenericParser.getAmount(block4, "71G", 3);
    }
    
    

}
