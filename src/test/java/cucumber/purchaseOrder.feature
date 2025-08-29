@Purchase
Feature: Purchase the order from E-commerce website
Background:
Given I landed on login page
  @Positive
  Scenario Outline: Positive test of submitting the order
    Given user logs in with username <name> and password <password>
    When user adds product <product> to the cart
    And proceeds to checkout <product> and submit the order
   
    Then the order should be placed successfully with confirmation message "THANKYOU FOR THE ORDER."

    Examples:
      | name        | password   | product       | 
      | bardepkk@gmail.com | Star1234| Pant   |
      | pranaybn@gmail.com | Abcd1234@|ADIDAS ORIGINAL |
