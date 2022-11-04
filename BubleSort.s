-- R1 is boolean flag for work done
-- R2 is current index
-- R3 is is unsorted bound
-- R4, R5, and R6 are work registers
-- R7 is current loop limit
Begin Assembly
addi R1, R0, 1
addi R3, R0, 15
LABEL StartSort
addi R2, R0, 4000
addi R7, R0, 4
mul R7, R7, R3
addi R7, R7, 4000
LABEL Bubble
lw R4, 0(R2)
lw R5, 4(R2)
sub R6, R4, R5
BGTZ R6, NeedsSort
LABEL ret
addi R2, R2, 4
bne R2, R7, Bubble
bne R1, R0, Done
addi R3, R3, -1
bne R3, R0, StartSort
LABEL Done
halt
LABEL NeedsSort
add R1, R0, R0
sw R4, 4(R2)
sw R5, 0(R2)
j ret
End Assembly
Begin Data 4000 16
8
0
9
10
11
12
2
6
7
4
13
14
15
3
1
5
End Data