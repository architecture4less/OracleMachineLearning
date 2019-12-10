<!--
  AUTH: Jared O'Toole
  DATE: Sun Sep 15th 2019
  PROJ: OracleMachineLearning
  FILE: README.md
  
  The project's README file.
-->

# OracleMachineLearning

This is my personal project submission for professor Vanselow's _COP3003: Object Oriented Programming_ class at Florida Gulf Coast University for the 2019 fall semester. I will be completing the [Artificial Intelligence with Machine Learning in Java](https://academy.oracle.com/en/solutions-curriculum-full.html#machinelearn) Oracle iLearning course.

---
- [X] [Week 0](#week-0)
- [X] [Week 1](#week-1)
- [X] [Week 2](#week-2)
- [X] [Week 3](#week-3)
- [X] [Week 4, 5, 6](#week-4-5-6)
- [X] [Week 7](#week-7)
- [X] [Week 8, 9, 10](#week-8-9-10)
- [ ] [Week 11](#week-11)
---

## Week 0
> **Sunday, Sep 15th, 2019**

- Section 0 - Course resources
  > A course description, overview, curriculum map, and additional resources.


## Week 1
> **Sunday, Sep 22nd, 2019**

- Section 1 - Introduction
  > This week covered basic vocabulary and concepts related to AI and ML.

- 1-1 Course Overview
  - [X] Understand the nature of the course
  - [X] Understand the delivery mechanism

- 1-2 Introduction to AI
  - [X] Define artificial intelligence
    > AI is the ability for a machine to exhibit human-like intelligence. This can be evaluated through a Turing test. The appearance of intelligence can be due to the performance of an algorithm with immense processing power rather than the mimicking of human behavior.
  - [X] Define machine learning
    > The Stanford University definition of Machine Learning is "The science of getting computers to act without being explicitly programmed". ML is a potential approach to building an AI. ML algorithms are focused on making predictions on data sets.
  - [x] Give examples of using artificial intelligence
    > AI and ML are used today in facial recognition, speech recognition, image categorization, targeted advertising, content suggestion, etc.
  - [x] Define data exhaust
    > Data exhaust refers to the data that we create as we go about our day, which can potentially be collected.
  
- 1-3 Data and Information
  - [X] Define data
    > Data are the raw, unprocessed facts about the world.
  - [X] Define information
    > Information is the useful and contextualized presentation of data.  
  - [X] Differentiate between data and information
  
- 1-4 Categorizing Data
  - [X] Define supervised learning
    > A labeled set of inputs (independent data) and expected outputs (dependent data) are given to the computer to learn from. 
  - [X] Define unsupervised learning
    > The set of training data is unstructured, meaning it has not been labelled and the relationships of the data are not known. This can be the result of extremely large data sets.
  - [x] Define classification
    > Input data is defined in terms of discrete values or 'class labels'.
  - [x] Define regression
    > Input data is defined over a continuous range, using real numbers.
  - [x] Define structured and unstructured data
    > Structured data is highly organized and tabular, with little complexity. Examples are numbers, dates, and names. Unstructured data can be complex, including things such as emails, objects in a photograph, etc.


## Week 2
> **Sunday, Sep 29th, 2019**

- Section 2 – Machine Learning
  > This week examined the forces behind ML's adoption and its implementation.

- 2-1 Why Now?
  - [X] State the reasons behind the growth in AI
    > Modern, technology-rich life creates bountiful data exhaust that can be turned into useful information. Companies are interested in this information to make better decisions within their market. Machine learning takes advantage of modern processing power and resources such as the cloud to provide companies with algorithms that allow them to analyze data to make rapid and strategic decisions.
  - [X] Understand the growth in processing power
    > The growth in processing power can be summarized by Moore's Law, which states that the number of transistors on an integrated circuit doubles every two years, due to innovation and falling costs. This phenomena was first written about by Gordon Moore in 1965, and has proved accurate in prior decades. In the 2010's, however, the rate began to decelerate, down to every two-or-three years. Some experts believe Moore's Law could fail completely by 2025.  

- 2-2 Machine Learning Workflow
  - [X] Understand the use of models within machine learning
    > A project that utilizes ML for its solution uses a workflow model (such as CRISP-DM) to stick to a defined processes and keep a specific goal in mind.
  - [X] Understand the CRISP-DM Model
    > 1. Business Understanding (what questions need to be answered?) 2. Data Understanding (where will the data originate?) 3. Data Preparation (what data is required and in what format?) 4. Modelling (what ML algorithm will be used?) 5. Evaluation (how accurate were the results?) 6. Deployment (push out the solution)

- [X] Complete Section 1 & 2 Quiz
  ```
  Score: 14 out of 15
  Percentage Scored: 93.3%
  Mastery Score: 70%
  ```
  Question|Points Available|Points Awarded
  ---|---|--- 
  [**Section 1**]()|**11**|**10**
  [The Turing test is]()|1|1
  [Data exhaust allows systems t...]()|1|1
  [Which of the following are ex...]()|1|1
  [Information can be as simple ...]()|1|1
  [The date and time of a credit...]()|1|1
  [Information is data in contex...]()|1|1
  [Rainfall recorded as 2,3,0,3,...]()|1|1
  [Regression of data is when]()|1|1
  [You watch a baseball game wit...]()|1|0
  [Data that can be shown in tab...]()|1|1
  [Data shown as a regression ca...]()|1|1
  [**Section 2**]()|**4**|**4**
  [As personal computers and dev...]()|1|1
  [Using a methodology such as C...]()|1|1
  [Which one of the following is...]()|1|1
  [In the CRISP model, how would...]()|1|1


## Week 3
> **Sunday, Oct 6th, 2019**

- Section 3 – Trees and Recursion

  - 3-1 Binary Trees
    - [X] Understand a node
      > A node is a point of data in a tree that can have references to other nodes. These are called child nodes. Every node also has a parent, except for the root node. Nodes that have the same parent are siblings. Nodes that do not have children are leaf nodes.
    - [X] Understand a binary tree
      > A binary tree is a data structure in which each node has a maximum of two children: the left and right node. Trees can be used for decision-making algorithms. Binary trees can also be used for efficient search algorithms.  
    - [X] Create a Node class
      > [Node.java](src/me/jwotoole9141/oracleml/s3l1/Node.java)
    
  - 3-2 Recursion
    - [X] Define recursion
      > Recursion is an approach to problem solving by having a function call itself. 
    - [X] Understand recursive methods
      > A recursive algorithm must have a way to stop recursing and finally produce a result. This is achieved by defining separate BASE and RECURSION cases. The base case returns a result _without_ making another recursive call.  
        [Recursion.java](src/me/jwotoole9141/oracleml/s3l2/Recursion.java)  
        [Output of Recursion.main()](media/output_s3l2_recursion.png)
    - [X] State the advantages and disadvantages of recursion
      > A recursive algorithm can make code simpler and more readable. However, a recursive solution requires significantly more memory than an iterative solution. Recursive functions carry the risk of a stack overflow if the recursion gets too deep.
    
  - 3-3 Tree Traversal
    - [X] Describe tree traversal
      > It is common to want to traverse through a tree's nodes in order to search for something or to gather and print the tree's data. Recursive algorithms simplify the navigation of a tree.
    - [X] Define pre-order traversal
      > In pre-order traversal, the node's data is gathered before traversing its left and right nodes (if they exist).
    - [X] Define post-order traversal
      > In post-order traversal, the node's data is gathered after traversing its left and right nodes (if they exist).
    - [X] Define in-order traversal
      > In in-order traversal, the node's left child is traversed (if it exists), then the node's data is gathered, and then the node's right child is traversed (if it exists).
    - [X] Create methods for BTree
      > [Task 1](src/me/jwotoole9141/oracleml/s3l3t1)  
        [Output of Task 1](media/output_s3l3t1_node_driver.png)  
        [Task 2](src/me/jwotoole9141/oracleml/s3l3t2)  
        [Output of Task 2](media/output_s3l3t2_tree_driver.png)  


## Week 4, 5, 6
> **Sunday, Oct 13th, 2019**  
> **Sunday, Oct 20th, 2019**  
> **Sunday, Oct 27th, 2019**  

- Section 3 – Trees and Recursion
    
  - 3-4 Yes/No Game
    - [X] Describe the use of decision trees
      > Decision trees are useful for building a branching network of sequential questions and answers. A few examples are a technical troubleshooting guide or a medical diagnosis.
    - [X] Create a yes/no game
      > [GuessingGame](src/me/jwotoole9141/oracleml/s3l4)  
        [Demo of GuessingGame](media/guessing_game_demo.gif)
    - [X] State the problems of creating a manual decision tree
      > The main problem with creating a manual decision tree is that an answer might take a large number of questions to reach. But, if the tree were to be re-ordered with a good decision tree algorithm, it may take significantly fewer questions. Manually determining the most significant question to ask first for a sample of data is difficult.

## Week 7
> **Sunday, Nov 3rd, 2019**

- Section 4 – Entropy and the ID3 Algorithm

  - 4-1 Decision Tree Algorithms
    - [X] State a number of decision tree algorithms
      > Some examples of decision tree algorithms are:  
        ~ Classification And Regression Tree (CART)  
        ~ Iterative Dichotomiser 3 (ID3)  
        ~ Chi-Squared Automatic Interaction Detection (CHAID)  
        ~ Decision Stump  
        ~ Conditional Decision Trees
    - [X] Identify the ID3 algorithm
      > The ID3 algorithm is an algorithm to create an efficient decision tree. Each node branches on the attribute whose results produce the greatest variance.
    
  - 4-2 Information Entropy
    - [X] Define information entropy
      > Information entropy is the seeming randomness in a data set, from which information can be extracted. A data set which has no randonmess, in which all of the results can be known ahead of time, has no entropy.  
    - [X] Understand variance
      > Variance is a mathematical measure of how spread out a data set is. The information entropy of a data set is measured by its variance. 
    - [X] Calculate information entropy
      > `info_entropy(D)` is calculated using the formula:  
        ```sum([-px * log(px) / log(2) for px in D])```  
        with the data set `D` and probability of success `px`. 
    - [X] Understand information entropy
      > Information entropy allows us to evaluate the most efficient order of questions to ask to make an accurate prediction about a data set.
    
  - 4-3 ID3 Worked Example
    - [X] Calculate entropy
      > `entropy(S)` is calculated using the formula:  
        ``` (-px * log(px) / log(2)) + (-pk * log(pk) / log(2)) ```  
        with the system `S`, probability of success `px`, and probability of failure `pk`. 
    - [X] Calculate gain
      > `gain(S, A)` is calculated using the formula:  
        ``` entropy(S) - sum((len(O)/len(S)) * entropy(O) for O in A) ```  
        with the system `S`, attribute `A`, and outcome of an attribute `O`. 
    - [X] Manually work through the ID3 algorithm
      > [ID3 Algorithm Manually Worked Through PDF](media/id3_algorithm_worked.pdf)

## Week 8, 9, 10
> **Sunday, Nov 10th, 2019**  
> **Sunday, Nov 17th, 2019**  
> **Sunday, Nov 24th, 2019**  

- Section 4 – Entropy and the ID3 Algorithm

  - 4-4 Create an ID3 Tree
    - [X] Understand non binary tree structure
    - [X] Create a non-binary tree structure
      > [ID3 Algorithm Implementation](src/me/jwotoole9141/oracleml/s4l4)  
        [Output of Table Tests](media/output_s4l4_table_driver.png)  
        [Output of Tree Tests](media/output_s4l4_tree_driver.png)  
        [Output of ID3 Tests](media/output_s4l4_id3_driver.png)  

## Week 11
> **Sunday, Dec 1st, 2019**  

- [ ] Complete Section 3 & 4 Quiz
- [ ] Complete AiML Final Exam
