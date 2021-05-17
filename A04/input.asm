MACRO
INCR	&X,&Y,&REG=AREG
MOVER	&REG,&X
ADD	&REG,&Y
MOVEM	&REG,&X
MEND
MACRO
SWAP	&P,&Q,&R
MOVER	&Q,&P
ADD	&P,&R
MOVEM	&R,&Q
MEND
MACRO
DECR	&A,&REG1=BREG,&REG2=CREG
MOVER	&REG1,&A
SUB	&REG1,&REG2
MOVEM	&REG1,&A
MEND
START	100
READ	N1
READ	N2
INCR	N1,N2,REG=CREG
DECR	N1,N2
STOP
N1	DS	1
N2	DS	1
END