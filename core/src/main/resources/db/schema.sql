drop table if exists phone2color;
drop table if exists colors;
drop table if exists stocks;
drop table if exists phones;
drop table if exists orders;
drop table if exists orderitems;

create table orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  secureId VARCHAR(50) NOT NULL,
  subtotal FLOAT,
  deliveryPrice FLOAT,
  totalPrice FLOAT,
  firstName VARCHAR(20),
  lastName VARCHAR(20),
  deliveryAddress VARCHAR(50),
  contactPhoneNo VARCHAR(20),
  additionalInformation VARCHAR(200),
  status VARCHAR(255) check (status in ('NEW', 'DELIVERED', 'REJECTED')),
  UNIQUE (secureId)
);

create table colors (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(50),
  UNIQUE (code)
);

create table phones (
  id BIGINT AUTO_INCREMENT primary key,
  brand VARCHAR(50) NOT NULL,
  model VARCHAR(254) NOT NULL,
  price FLOAT,
  displaySizeInches FLOAT,
  weightGr SMALLINT,
  lengthMm FLOAT,
  widthMm FLOAT,
  heightMm FLOAT,
  announced DATETIME,
  deviceType VARCHAR(50),
  os VARCHAR(100),
  displayResolution VARCHAR(50),
  pixelDensity SMALLINT,
  displayTechnology VARCHAR(50),
  backCameraMegapixels FLOAT,
  frontCameraMegapixels FLOAT,
  ramGb FLOAT,
  internalStorageGb FLOAT,
  batteryCapacityMah SMALLINT,
  talkTimeHours FLOAT,
  standByTimeHours FLOAT,
  bluetooth VARCHAR(50),
  positioning VARCHAR(100),
  imageUrl VARCHAR(254),
  description VARCHAR(4096),
  CONSTRAINT UC_phone UNIQUE (brand, model)
);

create table orderitems (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  orderSecureId VARCHAR(50) NOT NULL,
  quantity BIGINT,
  phoneId BIGINT,
  CONSTRAINT FK_orderitems_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_orderitems_orderSecureId FOREIGN KEY (orderSecureId) REFERENCES orders (secureId) ON DELETE CASCADE ON UPDATE CASCADE
);

create table phone2color (
  phoneId BIGINT,
  colorId BIGINT,
  CONSTRAINT FK_phone2color_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_phone2color_colorId FOREIGN KEY (colorId) REFERENCES colors (id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table stocks (
  phoneId BIGINT NOT NULL,
  stock SMALLINT NOT NULL,
  reserved SMALLINT NOT NULL,
  UNIQUE (phoneId),
  CONSTRAINT FK_stocks_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE
);