# Project for CS 5392 @ Texas State University

Write:

1. class framework for a CTL formula representation as a parse tree

2. a corresponding parser that can parse a string with a CTL formula into a parse tree using your class framework

3. a translator of a parse tree into a canonical representation that uses only those CTL operators for which the “Logic in Computer Science” textbook provides algorithms (p. 227: EX, AF, EU)

4. testcases that would take a CTL formula string, parse it, report problems if any and would output a string with a canonical representation of that formula
 
The “Logic in Computer Science” textbook describes the CTL equivalences on p. 216 and in the section about the CTL algorithms pseudo-code starting from p. 225.

The system should produce meaningful warning messages to console if an input string is not a well formed formula in CTL and/or no reasonable output is possible.

The parsing contingencies should be implemented via an exception mechanism.

You should submit your archived (zipped) Java project directory with source files to a TRACS dropbox.


