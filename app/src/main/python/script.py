import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import LabelEncoder, OneHotEncoder

def prediction(transaction_amounts, transaction_types):
    transaction_amounts = np.array(transaction_amounts)

    label_encoder = LabelEncoder()
    transaction_types_encoded = label_encoder.fit_transform(transaction_types)

    onehot_encoder = OneHotEncoder(sparse=False, handle_unknown='ignore')
    transaction_types_encoded = onehot_encoder.fit_transform(transaction_types_encoded.reshape(-1, 1))

    X = np.column_stack((transaction_amounts, transaction_types_encoded))

    model = LinearRegression()
    model.fit(X, transaction_amounts.ravel())

    future_expenses_sum = np.sum(transaction_amounts)
    prediction = model.predict([[future_expenses_sum] + [0] * len(transaction_types_encoded[0])])

    return float(prediction)
