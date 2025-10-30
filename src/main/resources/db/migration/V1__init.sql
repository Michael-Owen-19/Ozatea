-- ===========================================================
-- OZATEA DATABASE MIGRATION (NEW SCHEMA)
-- ===========================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===========================================================
-- USER
-- ===========================================================
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    provider VARCHAR(50) NOT NULL DEFAULT 'LOCAL',
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    name VARCHAR(100) NOT NULL,
    avatar_url TEXT,
    badge_level VARCHAR(50) DEFAULT 'Newbie',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    UNIQUE (email, provider)
);

-- ===========================================================
-- REFRESH TOKENS
-- ===========================================================
CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- CATEGORY
-- ===========================================================
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_id BIGINT REFERENCES category(id) ON DELETE SET NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- PRODUCT
-- ===========================================================
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    slug VARCHAR(255) UNIQUE NOT NULL,
    category_id INT REFERENCES category(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

CREATE TABLE media (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    alt_text VARCHAR(255),
    filepath TEXT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    media_type VARCHAR(50) NOT NULL,
    size BIGINT NOT NULL,
    storage_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    created_by UUID,
    updated_at TIMESTAMP DEFAULT NOW(),
    updated_by UUID,
    deleted_at TIMESTAMP,
    deleted_by UUID
);

-- ===========================================================
-- PRODUCT MEDIA (images/videos)
-- ===========================================================
CREATE TABLE product_media (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product(id) ON DELETE CASCADE,
    is_thumbnail BOOLEAN DEFAULT FALSE,
    media_id BIGINT REFERENCES media(id) ON DELETE SET NULL,
    order_index INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- PROPERTY
-- ===========================================================
CREATE TABLE property (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- PROPERTY ATTRIBUTE (values for each property)
-- ===========================================================
CREATE TABLE property_attribute (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT REFERENCES property(id) ON DELETE CASCADE,
    value VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- SKU
-- ===========================================================
CREATE TABLE sku (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) UNIQUE NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    quantity INT NOT NULL,
    product_id BIGINT REFERENCES product(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- SKU PROPERTY (link SKU ↔ property attribute)
-- ===========================================================
CREATE TABLE sku_property (
    id BIGSERIAL PRIMARY KEY,
    sku_id BIGINT REFERENCES sku(id) ON DELETE CASCADE,
    property_attribute_id BIGINT REFERENCES property_attribute(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- DISCOUNT
-- ===========================================================
CREATE TABLE discount (
    id BIGSERIAL PRIMARY KEY,
    discount_type VARCHAR(10) CHECK (discount_type IN ('fixed', 'percentage')),
    value DECIMAL(15, 2) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    product_id BIGINT REFERENCES product(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- REVIEW
-- ===========================================================
CREATE TABLE review (
    id BIGSERIAL PRIMARY KEY,
    rate INT CHECK (rate BETWEEN 1 AND 5),
    text TEXT NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    sku_id BIGINT REFERENCES sku(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- EVENT PUBLICATION (for outbox pattern)
-- ===========================================================
CREATE TABLE event_publication (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listener_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP DEFAULT NOW(),
    completion_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- CART (User’s active shopping cart)
-- ===========================================================
CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- CART ITEM (Products inside a user’s cart)
-- ===========================================================
CREATE TABLE cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES cart(id) ON DELETE CASCADE,
    sku_id BIGINT REFERENCES sku(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(15, 2) NOT NULL, -- snapshot of SKU price at the time added
    discount DECIMAL(15, 2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- ORDER (checkout result)
-- ===========================================================
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) CHECK (status IN (
        'pending', 'paid', 'shipped', 'delivered', 'cancelled', 'refunded'
    )) DEFAULT 'pending',
    total_amount DECIMAL(15, 2) NOT NULL,
    discount_total DECIMAL(15, 2) DEFAULT 0,
    shipping_fee DECIMAL(15, 2) DEFAULT 0,
    payment_method VARCHAR(100),
    shipping_address TEXT,
    note TEXT,
    ordered_at TIMESTAMP DEFAULT NOW(),
    paid_at TIMESTAMP,
    shipped_at TIMESTAMP,
    delivered_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    refunded_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- ORDER ITEM (Snapshot of SKUs purchased)
-- ===========================================================
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    sku_id BIGINT REFERENCES sku(id) ON DELETE SET NULL,
    product_name VARCHAR(255) NOT NULL, -- snapshot
    sku_code VARCHAR(255),
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(15, 2) NOT NULL,
    discount DECIMAL(15, 2) DEFAULT 0,
    subtotal DECIMAL(15, 2) NOT NULL, -- (price - discount) * quantity
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- POST (User-generated posts or reviews)
-- ===========================================================
CREATE TABLE post (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255),
    content TEXT NOT NULL,
    image_url TEXT,
    likes_count INT DEFAULT 0,
    comments_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- POST COMMENT
-- ===========================================================
CREATE TABLE post_comment (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES post(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

-- ===========================================================
-- POST LIKE
-- ===========================================================
CREATE TABLE post_like (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES post(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE (post_id, user_id), -- prevent duplicate likes
    created_by UUID,
    updated_by UUID,
    deleted_by UUID
);

