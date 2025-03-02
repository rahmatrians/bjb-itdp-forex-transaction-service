
DROP TABLE IF EXISTS user_profiles CASCADE;
DROP TABLE IF EXISTS user_authentications CASCADE;
DROP TABLE IF EXISTS currencies CASCADE;
DROP TABLE IF EXISTS user_accounts CASCADE;
DROP TABLE IF EXISTS exchange_rates CASCADE;
DROP TABLE IF EXISTS transaction_histories CASCADE;

CREATE TABLE user_profiles (
  id SERIAL PRIMARY KEY,
  fullname VARCHAR NOT NULL,
  gender VARCHAR NOT NULL,
  place_of_birth VARCHAR NOT NULL,
  date_of_birth DATE NOT NULL,
  address VARCHAR NOT NULL,
  province VARCHAR NOT NULL,
  city VARCHAR NOT NULL,
  district VARCHAR NOT NULL,
  subdistrict VARCHAR NOT NULL,
  postal_code VARCHAR NOT NULL,
  identity_type VARCHAR NOT NULL,
  identity_number VARCHAR NOT NULL,
  phone_number VARCHAR NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);


CREATE TABLE user_authentications (
  id SERIAL PRIMARY KEY,
  user_profile_id INT REFERENCES user_profiles(id),
  email VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  pin VARCHAR NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE currencies (
  id SERIAL PRIMARY KEY,
  code VARCHAR UNIQUE NOT NULL,
  description VARCHAR NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE user_accounts (
  id SERIAL PRIMARY KEY,
  user_profile_id INT REFERENCES user_profiles(id),
  currency_code VARCHAR REFERENCES currencies(code),
  account_number VARCHAR NOT NULL,
  balance DECIMAL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE exchange_rates (
  id SERIAL PRIMARY KEY,
  from_currency_code VARCHAR REFERENCES currencies(code),
  to_currency_code VARCHAR REFERENCES currencies(code),
  exchange_rate FLOAT NOT NULL,
  rate_date DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE transaction_histories (
  id SERIAL PRIMARY KEY,
  from_user_account_id INT REFERENCES user_accounts(id),
  dest_user_account_id INT REFERENCES user_accounts(id),
  transaction_date TIMESTAMP,
  from_trans_amount DECIMAL DEFAULT 0.00,
  dest_trans_amount DECIMAL DEFAULT 0.00,
  from_currency VARCHAR REFERENCES currencies(code),
  dest_currency VARCHAR REFERENCES currencies(code),
  exchange_rate FLOAT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);
