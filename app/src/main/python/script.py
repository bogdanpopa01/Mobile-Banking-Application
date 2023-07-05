from datetime import datetime, timedelta
from sklearn.linear_model import LinearRegression
import random
import numpy as np

def prediction(amounts, dates):
    expenses_dates = []
    expenses_amounts = []
    
    # Convert dates to datetime objects
    for i in range(len(dates)):
        expenses_dates.append(datetime.strptime(dates[i], '%Y-%m-%d').date())
        expenses_amounts.append(amounts[i])

    start_date = expenses_dates[0]
    end_date = expenses_dates[len(expenses_dates)-1]

    # Calculate the number of days between start_date and end_date
    num_days = (end_date - start_date).days

    # Iterate over each whole month
    monthly_sums = []
    monthly_dates = []
    for i in range(num_days // 30):  # Assuming each month has 30 days
        month_start = start_date + timedelta(days=i * 30)
        month_end = month_start + timedelta(days=29)

        monthly_dates.append(month_start)

        # Find expenses within the month range
        monthly_expenses = [
            expense_amount
            for expense_date, expense_amount in zip(expenses_dates, expenses_amounts)
            if month_start <= expense_date <= month_end
        ]

        # Calculate the sum of monthly expenses
        monthly_sum = sum(monthly_expenses)
        monthly_sums.append(monthly_sum)

    # Prepare the data for linear regression
    X = [[i] for i in range(len(monthly_sums))]
    y = monthly_sums

    # Create a linear regression model and fit the data
    regression_model = LinearRegression()
    regression_model.fit(X, y)

    # Predict the future monthly sum
    future_month = len(monthly_sums)  # Assuming prediction for the next month
    future_month_sum = regression_model.predict([[future_month]])

    # Add white noise to the predicted value
    noise_std = 0.1 * np.mean(monthly_sums)
    noise = random.gauss(0, noise_std)
    future_month_sum += noise

    # Round the predicted value to two decimal places
    future_month_sum = round(future_month_sum[0], 2)

    return future_month_sum