# Result [![Code Coverage](https://img.shields.io/codecov/c/github/pablisco/result/master.svg)](https://codecov.io/github/pablisco/result?branch=master) [![Travis branch](https://img.shields.io/travis/pablisco/result/master.svg)](https://travis-ci.org/pablisco/result)


- Result
- ???
- Profit

This simple library let's you handle results from any source in a fluid manner. Instead of returnning an exception for 
instance the user of your API receives a meaningful object that can be checked for either success or failure.

## Installation

Using Gradle:

`compile 'com.pablisco.result:result-core:<current-version>'`

More information about the library (i.e. version) maven repo can be found here: https://bintray.com/pablisco/com.pablisco.result

## Simple Usage

To create a success result use:

`Result<String, Error> result = Result.success("success value");`

to create a failure result use:

`Result<String, Error> result = Result.failure(new Error());`

## Functional syntax

`Result` provides de means to link multiple calls that produce instances of `Result`. There is two transformation 
functions defined. One for success and another for failure.

Example of how to chain two or more actions that may provide a result:

``` java
Result<Character, String> result = Result.<String, String>success(SUCCESS_VALUE)
    .map(new Result.Function<String, Integer>() {
        @Override
        public Integer apply(String success) {
            return INTEGER_VALUE;
        }
    })
    .map(new Result.Function<Integer, Character>() {
        @Override
        public Character apply(Integer success) {
            return CHAR_VALUE;
        }
    });
```

If we are interested on capturing an error we can transform it into a success or another failure like this:

``` java

Result<String, Character> result = Result.<String, String>failure(FAILURE_VALUE)
    .bimap(
        new Result.Function<String, String>() {
            @Override
            public String apply(String success) {
                return SUCCESS_VALUE;
            }
        },
        new Result.Function<String, Character>() {
            @Override
            public Character apply(String failure) {
                return CHAR_VALUE;
            }
        }
    );
```

More examples can be found in the tests source set

## Notes

Examples on other JVM languages like Scala, kotlin or groovy will follow soon. Stay tuned.
