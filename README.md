# jlox walking tree interpreter implementation


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
./jlox src/main/resources/program1.jlox
```
or 
```
./jlox src/main/resources/fibonacci.jlox
```
