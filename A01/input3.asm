    START   200
    MOVER   AREG,   ='5'
    MOVEM   AREG,   X
L1  MOVER   BREG,   ='2'
    MOVER   DREG,   ='5'
    ORIGIN  L1+3
    LTORG
X   DS  1
    END