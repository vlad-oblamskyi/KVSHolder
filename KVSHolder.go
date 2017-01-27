package main

import (
	"errors"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type KVSHolder struct {
}

func (t *KVSHolder) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	return nil, nil
}

func (t *KVSHolder) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	switch function {
	case "put":
		for i := 0; i < len(args); i += 2 {
			stub.PutState(args[i], []byte(args[i + 1]))
		}
		return nil, nil
	default:
		return nil, errors.New("Unsupported operation")
	}
}

func (t *KVSHolder) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	state, _ := stub.GetState(args[0])
	result := string(state)
	return []byte(result), nil
}

func main() {
	err := shim.Start(new(KVSHolder))
	if err != nil {
		fmt.Printf("Error starting chaincode: %s", err)
	}
}