Example of the input MT:
```
{1:F01SPXBUAU1XXXX0000000000}{2:I103SPXBUAU2XXXXN}{3:{119:STP}}{4:
:20:PAY/003
:23B:CRED
:32A:160926USD322,00
:33B:USD322,00
:50K:/AccountA
SENDERS ADDRESS
30222 ZURICH
:57A:SPXBUAU3
:59A:/AccountB
SPXBUAU3
RECEIVER ADDRESS
14456 GENEVA
:70:Money transfer using HL codechain
:71A:OUR
:71G:USD12,00
-}
```
Output MT for this message:
```
{1:F01SPXBUAU2XXXX0000000000}{2:I103SPXBUAU3XXXXN}{3:{119:STP}}{4:
:20:PAY/003
:23B:CRED
:32A:160926USD310,00
:33B:USD32,
:50K:/AccountA
SENDERS ADDRESS
30222 ZURICH
:52A:SPXBUAU1
:59A:/AccountB
SPXBUAU3
RECEIVER ADDRESS
14456 GENEVA
:70:Money transfer using HL codechain
:71A:OUR
-}
```