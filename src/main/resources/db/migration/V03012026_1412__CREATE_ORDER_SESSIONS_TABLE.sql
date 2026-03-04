CREATE TABLE order_sessions (
    chat_id    BIGINT PRIMARY KEY,
    step       VARCHAR(50) NOT NULL,
    frac       VARCHAR(50),
    name       VARCHAR(255),
    price      INT,
    qty_user   INT,
    qty_final  INT,
    method     VARCHAR(255),
    address    VARCHAR(500),
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP   NOT NULL DEFAULT now()
);
