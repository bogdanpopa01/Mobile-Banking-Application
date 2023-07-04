from datetime import datetime

def prediction(amounts, dates):
    return type(datetime.strptime(dates[0], '%Y-%m-%d').date())