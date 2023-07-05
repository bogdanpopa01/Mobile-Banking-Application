from datetime import datetime, timedelta
from sklearn.linear_model import LinearRegression
import random
import numpy as np

def prediction(amounts, dates):
    expenses_dates = []
    expenses_amounts = []
    
    for i in range(len(dates)):
        expenses_dates.append(datetime.strptime(dates[i], '%Y-%m-%d').date())
        expenses_amounts.append(amounts[i])

    start_date = expenses_dates[0]
    end_date = expenses_dates[len(expenses_dates)-1]

    num_days = (end_date - start_date).days

    monthly_sums = []
    for i in range(num_days // 30):  
        month_start = start_date + timedelta(days=i * 30)
        month_end = month_start + timedelta(days=29)

        monthly_expenses = [
            expense_amount
            for expense_date, expense_amount in zip(expenses_dates, expenses_amounts)
            if month_start <= expense_date <= month_end
        ]

        monthly_sum = sum(monthly_expenses)
        monthly_sums.append(monthly_sum)

    noise_std = 0.035 * np.mean(monthly_sums)
    noise = random.gauss(0, noise_std)

    # data preparation
    X = [[i] for i in range(len(monthly_sums))]
    y = monthly_sums

    regression_model = LinearRegression()
    regression_model.fit(X, y)

    future_month = len(monthly_sums)  
    future_month_sum = regression_model.predict([[future_month]])

    future_month_sum += noise

    future_month_sum = round(future_month_sum[0], 2)

    return future_month_sum