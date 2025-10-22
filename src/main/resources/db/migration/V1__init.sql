-- ===============================
-- INITIAL DATABASE SCHEMA
-- For Ozatea Tea App
-- ===============================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===============================
-- USERS
-- ===============================
CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email           VARCHAR(255) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    name            VARCHAR(100) NOT NULL,
    avatar_url      TEXT,
    badge_level     VARCHAR(50) DEFAULT 'Newbie',
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

-- ===============================
-- PRODUCTS
-- ===============================
CREATE TABLE products (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    base_price      NUMERIC(12, 2) NOT NULL DEFAULT 0,
    image_url       TEXT,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW(),
    active          BOOLEAN DEFAULT TRUE
);

-- ===============================
-- PRODUCT VARIANT ATTRIBUTES
-- (e.g. "Weight", "Color", "Quality")
-- ===============================
CREATE TABLE product_variant_attributes (
    id              BIGSERIAL PRIMARY KEY,
    product_id      BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL
);

-- ===============================
-- PRODUCT VARIANT OPTIONS
-- (e.g. "100g", "200g", "Blue", "High Quality")
-- ===============================
CREATE TABLE product_variant_options (
    id              BIGSERIAL PRIMARY KEY,
    attribute_id    BIGINT NOT NULL REFERENCES product_variant_attributes(id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL,
    price_modifier  NUMERIC(15,2) DEFAULT 0
);

-- ===============================
-- PRODUCT SKUs
-- (Each combination of options that forms a unique product)
-- ===============================
-- ===============================
-- PRODUCT SKUs
-- (Each combination of options that forms a unique product)
-- ===============================
CREATE TABLE product_skus (
    id              BIGSERIAL PRIMARY KEY,
    product_id      BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    sku_code        VARCHAR(100) UNIQUE NOT NULL,
    stock_quantity  INT DEFAULT 0,
    final_price     NUMERIC(15,2) DEFAULT 0,
    active          BOOLEAN DEFAULT TRUE
);

-- ===============================
-- PRODUCT SKU OPTION RELATION
-- (Maps which variant options are used by each SKU)
-- ===============================
CREATE TABLE product_sku_options (
    sku_id          BIGINT NOT NULL REFERENCES product_skus(id) ON DELETE CASCADE,
    option_id       BIGINT NOT NULL REFERENCES product_variant_options(id) ON DELETE CASCADE,
    PRIMARY KEY (sku_id, option_id)
);

-- ===============================
-- TRIGGER FUNCTION: Update SKU Final Price
-- ===============================
CREATE OR REPLACE FUNCTION update_sku_final_price()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE product_skus s
    SET final_price = (
        SELECT p.base_price + COALESCE(SUM(o.price_modifier), 0)
        FROM products p
        LEFT JOIN product_sku_options so ON so.sku_id = s.id
        LEFT JOIN product_variant_options o ON o.id = so.option_id
        WHERE p.id = s.product_id
        GROUP BY p.base_price
    )
    WHERE s.id = NEW.id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger after insert/update of sku options or product price
CREATE TRIGGER trg_update_sku_price_on_sku
AFTER INSERT OR UPDATE ON product_skus
FOR EACH ROW EXECUTE FUNCTION update_sku_final_price();

CREATE TRIGGER trg_update_sku_price_on_option
AFTER INSERT OR UPDATE OR DELETE ON product_sku_options
FOR EACH ROW
EXECUTE FUNCTION update_sku_final_price();

-- ===============================
-- CART
-- ===============================
CREATE TABLE carts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE cart_items (
    id              BIGSERIAL PRIMARY KEY,
    cart_id         BIGINT NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
    sku_id          BIGINT NOT NULL REFERENCES product_skus(id),
    quantity        INT NOT NULL DEFAULT 1
);

-- ===============================
-- ORDERS
-- ===============================
CREATE TABLE orders (
    id              BIGSERIAL PRIMARY KEY,
    user_id         UUID NOT NULL REFERENCES users(id),
    total_amount    NUMERIC(15,2) NOT NULL,
    status          VARCHAR(50) DEFAULT 'PENDING',
    created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE order_items (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    sku_id          BIGINT NOT NULL REFERENCES product_skus(id),
    quantity        INT NOT NULL,
    unit_price      NUMERIC(15,2) NOT NULL
);

-- ===============================
-- POSTS (Community)
-- ===============================
CREATE TABLE posts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content         TEXT,
    media_url       TEXT,
    media_type      VARCHAR(20), -- image / video
    created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE post_comments (
    id              BIGSERIAL PRIMARY KEY,
    post_id         BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id         UUID NOT NULL REFERENCES users(id),
    content         TEXT NOT NULL,
    created_at      TIMESTAMP DEFAULT NOW()
);

CREATE TABLE post_likes (
    post_id         BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id         UUID NOT NULL REFERENCES users(id),
    PRIMARY KEY (post_id, user_id)
);

-- ===============================
-- PRODUCT RATINGS
-- ===============================
CREATE TABLE product_ratings (
    id              BIGSERIAL PRIMARY KEY,
    product_id      BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    user_id         UUID NOT NULL REFERENCES users(id),
    rating          SMALLINT CHECK (rating BETWEEN 1 AND 5),
    comment         TEXT,
    created_at      TIMESTAMP DEFAULT NOW()
);

-- ===============================
-- PROMO & COUPONS
-- ===============================
CREATE TABLE promos (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    discount_type   VARCHAR(20) NOT NULL, -- PERCENTAGE or FIXED
    discount_value  NUMERIC(15,2) NOT NULL,
    active          BOOLEAN DEFAULT TRUE,
    valid_from      TIMESTAMP,
    valid_until     TIMESTAMP
);

CREATE TABLE coupons (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(50) UNIQUE NOT NULL,
    description     TEXT,
    discount_type   VARCHAR(20) NOT NULL,
    discount_value  NUMERIC(15,2) NOT NULL,
    active          BOOLEAN DEFAULT TRUE,
    valid_from      TIMESTAMP,
    valid_until     TIMESTAMP
);

CREATE TABLE event_publication (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listener_id     VARCHAR(255) NOT NULL,
    event_type      VARCHAR(255) NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP DEFAULT NOW(),
    completion_date  TIMESTAMP
);

