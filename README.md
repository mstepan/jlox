# JLox walking tree interpreter implementation

jlox is dynamically typed language that supports functional style of programming with lambdas, closures, higher-order functions 
as well as OOP programming paradigms using classes. Language doesn't use any compact bytecode representation and just creates
AST directly from source code. A dedicated tree-walking interpreter used to execute the AST nodes. 

## Build

Requires `java 15` or later for build and execution.

```
./mvnw clean install
```

## Run interpreter in REPL mode
Build interpreter first, then execute:
```
./jlox
```
Enjoy experimenting with jlox interpreter.

## Execute simple script

Build interpreter first and just run: 
```
./jlox <script-file>
```
As a quick example, just run 
```
./jlox examples/fibonacci.jlox

./jlox examples/factorial.jlox
```
