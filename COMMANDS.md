# Useful commands

Here are a list of useful commands to run in CMD or Shell to automate the software lifecycle.

## Build
`mvn clean install`

## Test
`mvn test`

## Generate code coverage report
`mvn jacoco:report`
Then, if you want to visualize the report :
`call scripts/code_coverage_windows`

## Linter
`mvn checkstyle:check`

## Deploy
