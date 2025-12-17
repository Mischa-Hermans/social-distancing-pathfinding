# Socially Distant Paths – Implementation

This project contains a Java implementation of the Socially Distant Paths algorithm.

## Overview

The entry point of the program is the class `Algorithm`, located in the package
`social_distancing`. This class is responsible for loading an input instance,
invoking the solver, and printing the resulting paths and running time.

The core algorithm is implemented in the package `social_distancing.bfs`:
- `Solver` performs the alternating breadth-first search on the product state space.
- `Forbidden` precomputes all vertex pairs that violate the distance constraint.

The package `social_distancing.data` contains simple data structures for
representing input instances, intermediate states, and output paths.

## Running the program

The program reads an instance from input and prints the result to standard output.
Example input files are provided in the `testcases` directory.

To run the program, open the source code in a Java IDE and execute the
`Algorithm` class, selecting one of the test case files as input.
