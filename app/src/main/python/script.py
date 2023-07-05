from datetime import datetime, timedelta

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

    # Iterate over each whole week
    weekly_sums = []
    for i in range(num_days // 7):
        week_start = start_date + timedelta(days=i * 7)
        week_end = week_start + timedelta(days=6)

        # Find expenses within the week range
        weekly_expenses = [
            expense_amount
            for expense_date, expense_amount in zip(expenses_dates, expenses_amounts)
            if week_start <= expense_date <= week_end
        ]

        # Calculate the sum of weekly expenses
        weekly_sum = sum(weekly_expenses)
        weekly_sums.append(weekly_sum)



