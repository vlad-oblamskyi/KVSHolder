package com.intellecteu.hyperledger.chaincode.common;

import com.google.protobuf.ByteString;
import org.hyperledger.java.shim.ChaincodeBase;
import org.hyperledger.java.shim.ChaincodeStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.intellecteu.hyperledger.chaincode.common.Utils.getJsonStringFromChaincodeResponse;

/**
 * Class to work with KVS storage:
 * Using this chaincode other chaincodes should access KVS to share common variables.
 */
public class KVSHolder extends ChaincodeBase {
    public static final String KVS_HANDLER_KEY = "KVS_HANDLER_KEY";
    public static final String KVS_PUT_FUNCTION = "put";
    private static final Logger LOGGER = LoggerFactory.getLogger(KVSHolder.class);

    public static void main(String[] args) throws Exception {
        new KVSHolder().start(args);
    }

    @Override
    public String run(ChaincodeStub stub, String function, String[] args) {
        switch (function) {
            case "init":
                // NOOP - just to deploy
                break;
            case KVS_PUT_FUNCTION:
                for (int i = 0; i < args.length; i += 2) {
                    LOGGER.debug("Put tuple: [{}, {}]", args[i], args[i + 1]);
                    stub.putState(args[i], args[i + 1]);
                }
                break;
            case "del":
                throw new UnsupportedOperationException("Not sure that we have to support " + function + " operation");
            default:
                throw new UnsupportedOperationException(function + " is not implemented yet");
        }
        return null;
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        String result = stub.getState(args[0]);
        LOGGER.info("Query result [{}] for {}", result, Arrays.toString(args));
        return result;
    }

    @Override
    public String getChaincodeID() {
        return "KVSHolder";
    }

    public static String put(ChaincodeStub stub, String mapChainCodeId, String key, String value) {
        LOGGER.info("Persist in KVS with the key:{}  value:{}", key, value);
        List<ByteString> putArgs = Arrays.asList(key, value)
                .stream()
                .map(ByteString::copyFromUtf8)
                .collect(Collectors.toList());

        return stub.invokeChaincode(mapChainCodeId, "put", putArgs);

    }

    public static String get(ChaincodeStub stub, String mapChainCodeId, String key) {
        if (key == null) {
            LOGGER.error("Key is null");
            return null;
        }
        LOGGER.debug("Select value by the key: {}", key);
        String storedJsonValue = stub.queryChaincode(mapChainCodeId,
                "function", Collections.singletonList(ByteString.copyFromUtf8(key)));

        if (storedJsonValue == null || storedJsonValue.isEmpty()) {
            LOGGER.warn("Nothing was found by the key:{}", key);
            return null;
        }

        storedJsonValue = getJsonStringFromChaincodeResponse(storedJsonValue);

        return storedJsonValue.isEmpty() ? null : storedJsonValue;
    }
}
