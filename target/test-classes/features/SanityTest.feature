@SmokeTest
Feature: Send basic request's

  We validate response params

  Scenario: Send request to req/res and validate response
    Given Service is up and running
    When I send request to service
    Then Data is retrieved
