# api-project

## Installation Guide
- clone api-test project https://github.com/shiwanthalakmal/api-project.git
- run 'mvn idea:clean idea:idea' command undet root dir
- open project usng InteliJIdea tool

## Execution Guide
- there are 3 runnerble suite levels : (api_smk) smoke suite / (api_reg) reggression suite / (api_all) all suite
- ability to execute any test suite by appending maven command line
ex:    mvn clean test -Papi_smk     (to run smoke test cases)
       mvn clean test -Papi_reg     (to run regression test cases)
       mvn clean test -Papi_all     (to run all test cases)
       
- execution logger file will generate C://log dir with console loggger
- integrated 'report-ng' execution summary report under 'target -> surefire-reports -> html' dir
