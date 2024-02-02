# Bob's Bagels - Object-oriented Programming

![](./assets/bagels.jpg)

## Learning Objectives
- Design a domain from user stories
- Use test driven development to build an application
- Use object-oriented concepts to manage the complexity of the project

## Set up instructions
- Fork this repository and clone the forked version to your machine
- Open the root directory of the project in IntelliJ
- Your classes and tests should go in the usual places

## Exercise Requirements

- You'll be extending this exercise over multiple days as we introduce new OOP concepts
- Revisit the domain model you created for the previous Bob's Bagels exercise. You'll need to make changes to it as this exercise evolves, so copy it into this exercise to leave the original intact.
- Create class diagrams from your domain model and include screenshots of them.
- You **must** design a domain model before you begin building. Add your model as either a `.md` file or a screenshot
- You **must** use the Red Green Refactor approach to write your code. To demonstrate this, `git commit` after writing your test and commit again after writing the source code to pass it
- This exercise **must** use multiple classes that interact with each other. You might start with one, but by the end of the unit you should have a few more. As you learn new OOP concepts, apply them to this exercise to improve your solution over time.

## User Stories

```
1.
As a member of the public,
So I can order a bagel before work,
I'd like to add a specific type of bagel to my basket.
```

```
2.
As a member of the public,
So I can change my order,
I'd like to remove a bagel from my basket.
```

```
3.
As a member of the public,
So that I can not overfill my small bagel basket
I'd like to know when my basket is full when I try adding an item beyond my basket capacity.
```

```
4.
As a Bob's Bagels manager,
So that I can expand my business,
I’d like to change the capacity of baskets.
```

```
5.
As a member of the public
So that I can maintain my sanity
I'd like to know if I try to remove an item that doesn't exist in my basket.
```

```
6.
As a customer,
So I know how much money I need,
I'd like to know the total cost of items in my basket.
```

```
7.
As a customer,
So I know what the damage will be,
I'd like to know the cost of a bagel before I add it to my basket.
```

```
8.
As a customer,
So I can shake things up a bit,
I'd like to be able to choose fillings for my bagel.
```

```
9.
As a customer,
So I don't over-spend,
I'd like to know the cost of each filling before I add it to my bagel order.
```

```
10.
As the manager,
So we don't get any weird requests,
I want customers to only be able to order things that we stock in our inventory.
```

## Bob's Inventory

| SKU  | Price | Name    | Variant       |
|------|-------|---------|---------------|
| BGLO | 0.49  | Bagel   | Onion         |
| BGLP | 0.39  | Bagel   | Plain         |
| BGLE | 0.49  | Bagel   | Everything    |
| BGLS | 0.49  | Bagel   | Sesame        |
| COFB | 0.99  | Coffee  | Black         |
| COFW | 1.19  | Coffee  | White         |
| COFC | 1.29  | Coffee  | Capuccino     |
| COFL | 1.29  | Coffee  | Latte         |
| FILB | 0.12  | Filling | Bacon         |
| FILE | 0.12  | Filling | Egg           |
| FILC | 0.12  | Filling | Cheese        |
| FILX | 0.12  | Filling | Cream Cheese  |
| FILS | 0.12  | Filling | Smoked Salmon |
| FILH | 0.12  | Filling | Ham           |

## Extensions

You should only work on these after completing the core criteria above.

- [Extension 1: Discounts](./EXTENSION1.md)
- [Extension 2: Receipts](./EXTENSION2.md)
- [Extension 3: Discount Receipts](./EXTENSION3.md)
- [Extension 4: SMS](./EXTENSION4.md)


![](./assets/run-a-test.PNG)

## Test Output

When you run a test, it's either going to pass or fail. When it fails, you'll be presented with a big red stream of text. This is called a stack trace and, though intimidating, does contain some useful information.

One of the core skills of a developer is debugging stack traces like this. The stack trace details in which classes & files the failure happened, and gives you a line number at the end. Most of the lines in the stack trace are irrelevant most of the time, you want to try and identify the files that you're actually working with.

In the sample screenshot below, we've tried to complete the first step of the exercise but provided an invalid value. Then we run the test associated with it and we see a big red stack trace, a test failure.

At the top, we see `expected: <32> but was: <33>`. This means the test expected the value to be 32, but the value the student provided was 33. We can see this in the code snippets at the top of the screenshot.

In the stack trace itself, we see this line: `at app//com.booleanuk.core.BasketTest.shouldBeAged32(ExerciseTest.java:20)`. This is helpful! This tells us the exact line in the ExerciseTest.java file (line 20) where the failure happened, as well as the method name (shouldBeAged32), helping us to identify where the issue began. This is the kind of thing you need to look for; a relevant file name, method name, class name and line number to give you a good starting point for debugging.

![](./assets/test-failure.PNG)