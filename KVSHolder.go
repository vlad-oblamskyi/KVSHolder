package main

import (
	"errors"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/op/go-logging"
	"strings"
)

var myLogger = logging.MustGetLogger("kvs_holder")

type KVSHolder struct {
}

func (t *KVSHolder) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	myLogger.Info("Inited...")
	return nil, nil
}

func (t *KVSHolder) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	switch function {
	case "put":
		for i := 0; i < len(args); i += 2 {
			myLogger.Debugf("Put tuple: [{%s}, {%s}]", args[i], args[i + 1])
			stub.PutState(args[i], []byte(args[i + 1]))
		}
		return nil, nil
	default:
		myLogger.Errorf("Unsupported operation %s", function)
		return nil, errors.New("Unsupported operation")
	}
}

func (t *KVSHolder) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	state, _ := stub.GetState(args[0])
	result := string(state)
	myLogger.Infof("Query result [{%s}] for {%s}", result, strings.Join(args, ","));
	return []byte(result), nil
}

func main() {
	err := shim.Start(new(KVSHolder))
	if err != nil {
		fmt.Printf("Error starting chaincode: %s", err)
	}
}