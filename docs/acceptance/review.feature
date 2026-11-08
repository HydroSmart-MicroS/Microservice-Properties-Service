# Acceptance specification. Automated evidence: tests/integration/broker-flow.mjs
@US11 @TS09
Feature: List units visible to the current account

  Scenario: Authorized or healthy request
    Given a tenant with an active plan is assigned to a unit
    When the client requests GET /api/v1/units
    Then the response status is 200
    And the array includes the assigned unit

  Scenario: Boundary or access control
    Given the review environment is available
    When an authenticated account without an active plan sends the request through Gateway
    Then the response status is 402
    And the business route is blocked
