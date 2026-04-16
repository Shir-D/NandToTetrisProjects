// This file is part of nand2tetris, as taught in The Hebrew University, and
// was written by Aviv Yaish. It is an extension to the specifications given
// [here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
// as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
// Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).

// The program should swap between the max. and min. elements of an array.
// Assumptions:
// - The array's start address is stored in R14, and R15 contains its length
// - Each array value x is between -16384 < x < 16384
// - The address in R14 is at least >= 2048
// - R14 + R15 <= 16383
//
// Requirements:
// - Changing R14, R15 is not allowed.

// Put your code here

// Initialize R0,R1,R2,R3,R4,j
(INIT)
    // R0 holds the location (the j value) of the current max element in the array
    // Initialized to be the first element
    @R0
    M=0
    // R1 holds the location (the j value) of the current min element in the array
    // Initialized to be the first element
    @R1
    M=0

    // R2 and R3 holds the actual value of the current min and max (correspondingly)
    // R4 holds the current element value
    // All three are initialized to be the first element value
    @R14
    A=M // goto RAM[R14]
    D=M
    @R2
    M=D // R2=arr[0]
    @R3
    M=D // R3=arr[0]
    @R4
    M=D // R4=arr[0]

    // j is the index running upon the array
    @j
    M=-1

(LOOP)
    // j+=1
    @j
    M=M+1

    // If (j > array.length) goto SWAP
    @j
    D=M
    @R15
    D=D-M // D=j-(array.length)
    @SWAP
    D;JGE // if j-(array.length) >= 0 it means that j >= array.length then go to SWAP (we've covered all the array)

    // Update R4 to hold current value
    @j
    D=M
    @R14
    A=D+M // A=j+(array.base_address)
    D=M // D=arr[j]
    @R4
    M=D // R4=arr[j] = current element value

    // If (current_max < current_val) goto MAX
    @R3
    D=M
    @R4
    D=D-M // D = current_max - current_val
    @MAX
    D;JLT

    // If (current_val < current_min) goto MIN
    @R2
    D=M
    @R4
    D=M-D // D = current_val - current_min
    @MIN
    D;JLT

    // Loop over
    @LOOP
    0;JMP

// Insert current j into R0 and current max value into R3
(MAX)
    @j
    D=M
    @R0
    M=D // R0=j

    @R14
    A=D+M // D is still equal to j, then A=j+(array.base_address)
    D=M // Now D holds current element (new max) value
    @R3
    M=D // Now R3 holds the current max value

    // Then go back to the loop
    @LOOP
    0;JMP

// Insert current j into R1 and current min value into R2
(MIN)
    @j
    D=M
    @R1
    M=D // R1=j

    @R14
    A=D+M // D is still equal to j, then A=j+(array.base_address)
    D=M // Now D holds current element (new min) value
    @R2
    M=D // Now R2 holds the current min value

    // Then go back to the loop
    @LOOP
    0;JMP

// Swap places between the max element and the min element in the array
(SWAP)

    // Step 1: put the full address of the max into R0 (and not only the address that is in relation to the array)
    @R0
    D=M
    @R14
    D=D+M // D=j+(the array's start address)
    @R0
    M=D // Now R0 holds the full address of max

    // Step 2: put the full address of the min into R1 (and not only the address that is in relation to the array)
    @R1
    D=M
    @R14
    D=D+M // D=j+(the array's start address)
    @R1
    M=D // Now R1 holds the full address of min

    // Step 3: put the min value (held in R2) into the max's place (RAM[R0] <- R2)
    @R2
    D=M // D = min value
    @R0
    A=M // goto RAM[R0]
    M=D // RAM[R0]=R2

    // Step 4: put the max value (held in R3) into the min's place (RAM[R1] <- R3)
    @R3
    D=M // D = max value
    @R1
    A=M // goto RAM[R1]
    M=D // RAM[R1]=R3

(END)
    @END
    0;JMP

