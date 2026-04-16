// This file is part of nand2tetris, as taught in The Hebrew University, and
// was written by Aviv Yaish. It is an extension to the specifications given
// [here](https://www.nand2tetris.org) (Shimon Schocken and Noam Nisan, 2017),
// as allowed by the Creative Common Attribution-NonCommercial-ShareAlike 3.0
// Unported [License](https://creativecommons.org/licenses/by-nc-sa/3.0/).

// This program illustrates low-level handling of the screen and keyboard
// devices, as follows.
//
// The program runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.
// 
// Assumptions:
// Your program may blacken and clear the screen's pixels in any spatial/visual
// Order, as long as pressing a key continuously for long enough results in a
// fully blackened screen, and not pressing any key for long enough results in a
// fully cleared screen.
//
// Test Scripts:
// For completeness of testing, test the Fill program both interactively and
// automatically.
// 
// The supplied FillAutomatic.tst script, along with the supplied compare file
// FillAutomatic.cmp, are designed to test the Fill program automatically, as 
// described by the test script documentation.
//
// The supplied Fill.tst script, which comes with no compare file, is designed
// to do two things:
// - Load the Fill.hack program
// - Remind you to select 'no animation', and then test the program
//   interactively by pressing and releasing some keyboard keys

// Put your code here

// R0 holds the color information
@R0
M=0 // White as default

// Initialization label
(INIT)
    // R1 is the running index on the Screen Memory Map
    @SCREEN
    D=A
    @R1
    M=D

// Infinite label that checks the KBD
(INF)
    @KBD
    D=M
    @WHITE // KBD=0 -> no key is pressed -> jump to WHITE
    D;JEQ
    @BLACK // KBD>0 -> some key is pressed -> jump to BLACK
    D;JGT
    @INF // Go back to the beginning of the infinite label
    0;JMP

(WHITE)
    // Set the color to WHITE
    @R0
    M=0
    // Loop over the Screen Memory Map
    @SCREENLOOP
    0;JMP

(BLACK)
    // Set the color to BLACK
    @R0
    M=-1
    // Loop over the Screen Memory Map
    @SCREENLOOP
    0;JMP

(SCREENLOOP)
    // Set: RAM[R1]=color
    @R0 // R0 holds the color info
    D=M
    @R1 // R1 holds the location to color
    A=M
    M=D // That's the command that actually do that: RAM[R1]=color

    // Advance the running index: R1+=1
    @R1
    M=M+1

    // Make sure we're not overstepping the bounds
    // If (KBD-R1<=0) goto INIT, the screen is already full
    @KBD
    D=A
    @R1
    D=D-M // D=KBD-R1, when R1 is the location on the Screen Memory Map
    @INIT // Initialize the location, and then go to check the KBD again
    D;JLE

    @INF // Go to check the KBD again
    0;JMP
