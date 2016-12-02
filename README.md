# pocketmathtest

This repository has scala code with play plug-in for the mentioned Api problem. I deployed the code in heroku and following endpoints gives result for the corresponding queries.

1> Find all traders from Singapore and sort them by name
    https://pocketmathtestapp.herokuapp.com/SingaporeTraders
2> Find the transaction with the highest value
    https://pocketmathtestapp.herokuapp.com/HighestTransaction
3> Find all transactions in the year 2016 and sort them by value (high to small)
    https://pocketmathtestapp.herokuapp.com/2016transactions
4> Find the average of transactions' values from the traders living in Beijing
    https://pocketmathtestapp.herokuapp.com/BeijingAverageTransactions
    
Code Structure:

  >pocketmathtest
    >models
      There are two different models: Traders & Transactions
    >views
      View is how to show the data. Nothing as of now.
    >controllers
      It controls the interaction between different models and views.
    >main 
      It has four different methods for four different use cases
    >conf
      app configurations and api configurations
  Remaining files are play and heroku templates.
      
